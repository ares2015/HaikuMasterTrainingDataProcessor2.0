package com.haikuMasterTrainingDataProcessor.writer;

import com.haikuMasterTrainingDataProcessor.preprocessor.data.BookData;

import java.io.IOException;
import java.util.List;

/**
 * Created by Oliver on 4/1/2017.
 */
public interface TrainingDataWriter {

    void writePreprocessedDataIntoAnalysisFile(List<String> sentences);

    void writePreprocessedDataIntoBooksDirectory(BookData bookData) throws IOException;

    void writeAnalysedData(List<String> trainingDataRow, String outputFilePath) throws IOException;

}
