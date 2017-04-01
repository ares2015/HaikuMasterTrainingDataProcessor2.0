package com.haikuMasterTrainingDataProcessor.factory;

import com.haikuMasterTrainingDataProcessor.tagger.data.TokenTagData;

import java.util.List;

/**
 * Created by Oliver on 4/1/2017.
 */
public interface TokenTagDataMultiListFactory {

    List<List<TokenTagData>> create();

}
