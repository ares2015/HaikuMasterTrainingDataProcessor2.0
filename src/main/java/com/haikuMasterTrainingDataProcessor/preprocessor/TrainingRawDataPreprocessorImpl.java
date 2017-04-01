package com.haikuMasterTrainingDataProcessor.preprocessor;

import com.haikuMasterTrainingDataProcessor.preprocessor.data.BookData;
import com.haikuMasterTrainingDataProcessor.preprocessor.filter.SentenceFilter;
import com.haikuMasterTrainingDataProcessor.reader.TextReader;
import com.haikuMasterTrainingDataProcessor.writer.TrainingDataWriter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Oliver on 4/1/2017.
 */
public class TrainingRawDataPreprocessorImpl implements TrainingRawDataPreprocessor {

    private TextReader textReader;

    private SentenceFilter sentenceFilter;

    private TrainingDataWriter trainingDataWriter;

    public TrainingRawDataPreprocessorImpl(TextReader textReader, SentenceFilter sentenceFilter,
                                           TrainingDataWriter trainingDataWriter) {
        this.textReader = textReader;
        this.sentenceFilter = sentenceFilter;
        this.trainingDataWriter = trainingDataWriter;
    }

    @Override
    public void preprocess() throws IOException {
        BookData bookData = textReader.readRawData();
        List<String> sentences = bookData.getSentences();
        List<String> preprocessedSentences = new ArrayList<>();
        for (String sentence : sentences) {
            String preprocessedSentence = sentenceFilter.filter(sentence);
            preprocessedSentences.add(preprocessedSentence);
        }
        trainingDataWriter.writePreprocessedDataIntoBooksDirectory(bookData);
        trainingDataWriter.writePreprocessedDataIntoAnalysisFile(preprocessedSentences);
    }


}
