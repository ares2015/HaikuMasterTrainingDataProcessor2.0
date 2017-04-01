package com.haikuMasterTrainingDataProcessor.reader;

import com.haikuMasterTrainingDataProcessor.preprocessor.data.BookData;

import java.io.FileNotFoundException;

/**
 * Created by Oliver on 4/1/2017.
 */
public interface TextReader {

    BookData readRawData() throws FileNotFoundException;

}
