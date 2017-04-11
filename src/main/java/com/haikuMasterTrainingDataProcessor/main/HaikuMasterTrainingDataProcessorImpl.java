package com.haikuMasterTrainingDataProcessor.main;

import com.haikuMasterTrainingDataProcessor.factory.TokenTagDataMultiListFactory;
import com.haikuMasterTrainingDataProcessor.factory.Word2VecMatchTrainingRowFactory;
import com.haikuMasterTrainingDataProcessor.preprocessor.TrainingRawDataPreprocessor;
import com.haikuMasterTrainingDataProcessor.tagger.cache.StopWordsCache;
import com.haikuMasterTrainingDataProcessor.tagger.data.TokenTagData;
import com.haikuMasterTrainingDataProcessor.writer.TrainingDataWriter;
import org.datavec.api.util.ClassPathResource;
import org.deeplearning4j.models.word2vec.Word2Vec;
import org.deeplearning4j.text.sentenceiterator.BasicLineIterator;
import org.deeplearning4j.text.sentenceiterator.SentenceIterator;
import org.deeplearning4j.text.tokenization.tokenizer.preprocessor.CommonPreprocessor;
import org.deeplearning4j.text.tokenization.tokenizerfactory.DefaultTokenizerFactory;
import org.deeplearning4j.text.tokenization.tokenizerfactory.TokenizerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.io.IOException;
import java.util.*;

/**
 * Created by Oliver on 4/1/2017.
 */
public class HaikuMasterTrainingDataProcessorImpl implements HaikuMasterTrainingDataProcessor {

    private static Logger log = LoggerFactory.getLogger(HaikuMasterTrainingDataProcessorImpl.class);

    private TrainingRawDataPreprocessor trainingRawDataPreprocessor;

    private TokenTagDataMultiListFactory tokenTagDataMultiListFactory;

    private TrainingDataWriter trainingDataWriter;

    private Word2VecMatchTrainingRowFactory word2VecMatchTrainingRowFactory;

    private String outputFilePathWord2Vec = "C:\\Users\\Oliver\\Documents\\NlpTrainingData\\HaikuMasterTrainingData\\Word2VecModelData.txt";

    private String outputFilePathTokenTagData = "C:\\Users\\Oliver\\Documents\\NlpTrainingData\\HaikuMasterTrainingData\\TokenTagData.txt";

    public HaikuMasterTrainingDataProcessorImpl(TrainingRawDataPreprocessor trainingRawDataPreprocessor, TokenTagDataMultiListFactory tokenTagDataMultiListFactory,
                                                TrainingDataWriter trainingDataWriter, Word2VecMatchTrainingRowFactory word2VecMatchTrainingRowFactory) {
        this.trainingRawDataPreprocessor = trainingRawDataPreprocessor;
        this.tokenTagDataMultiListFactory = tokenTagDataMultiListFactory;
        this.trainingDataWriter = trainingDataWriter;
        this.word2VecMatchTrainingRowFactory = word2VecMatchTrainingRowFactory;
    }

    public static void main(String[] args) throws InterruptedException, IOException {
        final ApplicationContext context = new ClassPathXmlApplicationContext("beans.xml");
        final HaikuMasterTrainingDataProcessor haikuMasterTrainingDataProcessor = (HaikuMasterTrainingDataProcessor) context.getBean("haikuMasterTrainingDataProcessor");
        haikuMasterTrainingDataProcessor.preprocess();
        haikuMasterTrainingDataProcessor.process();
    }

    public void preprocess() throws IOException {
        trainingRawDataPreprocessor.preprocess();
    }

    public void process() throws InterruptedException, IOException {
        Map<String, Set<String>> tokenTagDataCache = new HashMap<String, Set<String>>();
        Set<String> vectorDataCache = new HashSet<String>();
        List<String> word2VecTrainingDataRows = new ArrayList<>();
        List<String> tokenTagDataTrainingDataRows = new ArrayList<>();
        int numberOfTaggedWords = 0;
        int numberOfWord2VecWords = 0;
        long startTime = System.currentTimeMillis();

        List<List<TokenTagData>> tokenTagDataMultiList = tokenTagDataMultiListFactory.create();
        String filePath = new ClassPathResource("HaikuMasterTextData.txt").getFile().getAbsolutePath();

        System.out.println("Load & Vectorize Sentences....");
//         Strip white space before and after for each line
        SentenceIterator iter = new BasicLineIterator(filePath);
        // Split on white spaces in the line to get words
        TokenizerFactory t = new DefaultTokenizerFactory();

        /*
            CommonPreprocessor will apply the following regex to each token: [\d\.:,"'\(\)\[\]|/?!;]+
            So, effectively all numbers, punctuation symbols and some special symbols are stripped off.
            Additionally it forces lower case for all tokenizer.
         */
        t.setTokenPreProcessor(new CommonPreprocessor());

        System.out.println("Building model....");
        Word2Vec vec = new Word2Vec.Builder()
                .minWordFrequency(20)
                .useHierarchicSoftmax(true)
                .iterations(15)
                .layerSize(500)
                .seed(42)
                .windowSize(5)
                .iterate(iter)
                .tokenizerFactory(t)
                .stopWords(new ArrayList<String>(StopWordsCache.stopWordsCache))
                .build();

        System.out.println("Fitting Word2Vec model....");
        vec.fit();

        for (int i = 0; i < tokenTagDataMultiList.size(); i++) {
            List<TokenTagData> tokenTagDataList = tokenTagDataMultiList.get(i);
            for (int j = 0; j < tokenTagDataList.size(); j++) {
                String token = tokenTagDataList.get(j).getToken();
                String tag = tokenTagDataList.get(j).getTag();
                if (tokenTagDataCache.containsKey(token)) {
                    if (!tokenTagDataCache.get(token).contains(tag)) {
                        String tokenTagDataTrainingDataRow = token + "#" + tag;
                        tokenTagDataTrainingDataRows.add(tokenTagDataTrainingDataRow);
                        numberOfTaggedWords++;
                        tokenTagDataCache.get(token).add(tag);
                    }
                } else {
                    Set<String> tags = new HashSet<String>();
                    tags.add(tag);
                    tokenTagDataCache.put(token, tags);
                    String tokenTagDataTrainingDataRow = token + "#" + tag;
                    tokenTagDataTrainingDataRows.add(tokenTagDataTrainingDataRow);
                    numberOfTaggedWords++;
                }
                if (!vectorDataCache.contains(token)) {
                    vectorDataCache.add(token);
                    Collection<String> matches = vec.wordsNearest(token, 500);
                    if (matches.size() > 0) {
                        String trainingDataRow = word2VecMatchTrainingRowFactory.create(token, matches);
                        System.out.println(trainingDataRow);
                        word2VecTrainingDataRows.add(trainingDataRow);
                        numberOfWord2VecWords++;
                    }
                }
            }
        }

        trainingDataWriter.writeAnalysedData(word2VecTrainingDataRows, outputFilePathWord2Vec);
        trainingDataWriter.writeAnalysedData(tokenTagDataTrainingDataRows, outputFilePathTokenTagData);
        long stopTime = System.currentTimeMillis();
        long elapsedTime = stopTime - startTime;
        System.out.println("Data processed in " + (elapsedTime / 1000) / 60 + " minutes and " + (elapsedTime / 1000) % 60 + " seconds");
        System.out.println(numberOfTaggedWords + " token tags were added into tags model");
        System.out.println(numberOfWord2VecWords + " tokens were added into word2vec model");
    }
}
