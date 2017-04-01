package com.haikuMasterTrainingDataProcessor.tagger;

import com.haikuMasterTrainingDataProcessor.tagger.data.SentenceData;
import com.haikuMasterTrainingDataProcessor.tagger.data.TokenTagData;

import java.util.List;

/**
 * Created by Oliver on 4/1/2017.
 */
public interface PosTagger {

    List<TokenTagData> tag(String inputSentence);
}
