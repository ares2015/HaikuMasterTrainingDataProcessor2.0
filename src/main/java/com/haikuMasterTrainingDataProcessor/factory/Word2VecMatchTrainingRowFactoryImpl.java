package com.haikuMasterTrainingDataProcessor.factory;

import java.util.Collection;
import java.util.List;

/**
 * Created by Oliver on 4/1/2017.
 */
public class Word2VecMatchTrainingRowFactoryImpl  implements Word2VecMatchTrainingRowFactory {

    @Override
    public String create(String token, Collection<String> matches) {
        int i = 0;
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(token).append("#");
        for (String match : matches) {
            if (i > 0) {
                stringBuilder.append(match).append("@");
            }
            i++;
        }
        return stringBuilder.toString();
    }

}