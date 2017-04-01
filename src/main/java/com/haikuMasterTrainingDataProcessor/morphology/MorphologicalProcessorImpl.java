package com.haikuMasterTrainingDataProcessor.morphology;

/**
 * Created by Oliver on 4/1/2017.
 */
public class MorphologicalProcessorImpl implements MorphologicalProcessor {

    public String removeSuffix(String token) {
        if (token.endsWith("ing")) {
            return token.substring(0, token.length() - 3);
        }
        if (token.endsWith("ed")) {
            return token.substring(0, token.length() - 2);
        }
        return token;
    }
}
