package com.haikuMasterTrainingDataProcessor.main;

import java.io.IOException;

/**
 * Created by Oliver on 4/1/2017.
 */
public interface HaikuMasterTrainingDataProcessor {

    void preprocess() throws IOException;

    void process() throws InterruptedException, IOException;

}
