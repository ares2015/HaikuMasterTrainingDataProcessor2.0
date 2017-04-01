package com.haikuMasterTrainingDataProcessor.tagger;

import com.haikuMasterTrainingDataProcessor.morphology.MorphologicalProcessor;
import com.haikuMasterTrainingDataProcessor.morphology.MorphologicalProcessorImpl;
import com.haikuMasterTrainingDataProcessor.tagger.cache.StopWordsCache;
import com.haikuMasterTrainingDataProcessor.tagger.cache.TagsCache;
import com.haikuMasterTrainingDataProcessor.tagger.data.TokenTagData;
import com.haikuMasterTrainingDataProcessor.tokenizer.Tokenizer;
import com.haikuMasterTrainingDataProcessor.tokenizer.TokenizerImpl;
import edu.stanford.nlp.ling.CoreAnnotations;
import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.util.CoreMap;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/**
 * Created by Oliver on 4/1/2017.
 */
public class PosTaggerImpl implements PosTagger {

    private Properties props;

    private StanfordCoreNLP pipeline;

    private MorphologicalProcessor morphologicalProcessor;

    private Tokenizer tokenizer;

    public PosTaggerImpl() {
        props = new Properties();
        props.setProperty("annotators", "tokenize, ssplit, pos");
        pipeline = new StanfordCoreNLP(props);
        morphologicalProcessor = new MorphologicalProcessorImpl();
        tokenizer = new TokenizerImpl();
    }

    @Override
    public List<TokenTagData> tag(String inputSentence) {
        List<TokenTagData> tokenTagDataList = new ArrayList<>();
        Annotation annotation = new Annotation(inputSentence);
        pipeline.annotate(annotation);
        if (annotation.get(CoreAnnotations.SentencesAnnotation.class).size() > 0) {
            CoreMap processedSentence = annotation.get(CoreAnnotations.SentencesAnnotation.class).get(0);
            for (CoreLabel token : processedSentence.get(CoreAnnotations.TokensAnnotation.class)) {
                String word = token.get(CoreAnnotations.TextAnnotation.class);
                String tag = token.get(CoreAnnotations.PartOfSpeechAnnotation.class);
                if (TagsCache.tags.contains(tag) && !StopWordsCache.stopWordsCache.contains(word)) {
                    if (TagsCache.verbTags.contains(tag) && ((word.endsWith("ing") || word.endsWith("ed")))) {
                        word = morphologicalProcessor.removeSuffix(word);
                    }
                    try {
                        if (!tokenizer.isLowerCase(word)) {
                            word = tokenizer.decapitalize(word);
                        }
                    } catch (StringIndexOutOfBoundsException e) {
                        System.out.println(word);
                    }
                    TokenTagData tokenTagData = new TokenTagData(word, TagsCache.nounTags.contains(tag), TagsCache.adjectiveTags.contains(tag),
                            TagsCache.verbTags.contains(tag), TagsCache.adverbTags.contains(tag), TagsCache.tagsConversionMap.get(tag));
                    tokenTagDataList.add(tokenTagData);
                }
            }
        }
        return tokenTagDataList;
    }


}
