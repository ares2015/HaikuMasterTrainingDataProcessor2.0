package com.haikuMasterTrainingDataProcessor.tokenizer;

/**
 * Created by Oliver on 4/1/2017.
 */
public interface Tokenizer {

    boolean containsSpecialChars(String token);

    boolean isLowerCase(String token);

    String removeSpecialCharacters(String token);

    String decapitalize(String token);

}
