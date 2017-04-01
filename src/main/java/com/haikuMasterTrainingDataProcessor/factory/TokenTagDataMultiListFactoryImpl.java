package com.haikuMasterTrainingDataProcessor.factory;

import com.haikuMasterTrainingDataProcessor.tagger.PosTagger;
import com.haikuMasterTrainingDataProcessor.tagger.data.TokenTagData;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Oliver on 4/1/2017.
 */
public class TokenTagDataMultiListFactoryImpl implements TokenTagDataMultiListFactory {

    private PosTagger posTagger;

    public TokenTagDataMultiListFactoryImpl(PosTagger posTagger) {
        this.posTagger = posTagger;
    }

    @Override
    public List<List<TokenTagData>> create() {
        List<List<TokenTagData>> tokenTagDataMultiList = new ArrayList<>();
        BufferedReader br = null;
        try {
            br = new BufferedReader(new FileReader("C:\\Users\\Oliver\\Documents\\NlpTrainingData\\HaikuMasterTrainingData\\HaikuMasterTextData.txt"));
        } catch (final FileNotFoundException e) {
            e.printStackTrace();
        }
        try {
            System.out.println("Entering sentence preprocessor...");
            String sentence = br.readLine();
            while (sentence != null) {
                if (!"".equals(sentence)) {
                    System.out.println("Tagging sentence: " + sentence);
                    List<TokenTagData> tokenTagDataList = posTagger.tag(sentence);
                    if (tokenTagDataList.size() > 0) {
                        tokenTagDataMultiList.add(tokenTagDataList);
                    }
                }
                sentence = br.readLine();
            }
        } catch (final IOException e) {
            e.printStackTrace();
        }
        return tokenTagDataMultiList;
    }
}
