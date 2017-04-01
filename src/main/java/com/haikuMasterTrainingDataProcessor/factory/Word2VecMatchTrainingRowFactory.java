package com.haikuMasterTrainingDataProcessor.factory;

import java.util.Collection;

/**
 * Created by Oliver on 4/1/2017.
 */
public interface Word2VecMatchTrainingRowFactory {

    String create(String token, Collection<String> matches);

}
