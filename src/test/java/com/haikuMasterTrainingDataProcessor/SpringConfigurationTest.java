package com.haikuMasterTrainingDataProcessor;

import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import static junit.framework.Assert.assertTrue;

/**
 * Created by Oliver on 4/1/2017.
 */
public class SpringConfigurationTest {

    @Test
    public void testConfiguration(){
        ApplicationContext context = new ClassPathXmlApplicationContext("beans.xml");
        assertTrue(context != null);
    }
}
