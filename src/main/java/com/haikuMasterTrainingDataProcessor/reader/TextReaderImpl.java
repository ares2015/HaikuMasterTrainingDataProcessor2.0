package com.haikuMasterTrainingDataProcessor.reader;

import com.haikuMasterTrainingDataProcessor.preprocessor.data.BookData;

import java.io.*;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Oliver on 4/1/2017.
 */
public class TextReaderImpl implements TextReader {

    @Override
    public BookData readRawData() throws FileNotFoundException {
        List<String> sentences = null;
        String wholeText = "";
        BufferedReader br = null;
        String title = "";
        try {
            br = new BufferedReader(new FileReader("C:\\Users\\Oliver\\Documents\\NlpTrainingData\\TextData.txt"));
        } catch (final FileNotFoundException e) {
            e.printStackTrace();
        }
        try {
            String testDataRow = br.readLine();
            while (testDataRow != null) {
                if (testDataRow.contains("Title:")) {
                    String[] split = testDataRow.split("Title:");
                    title += split[1];
                }
                if (testDataRow.contains("Author:")) {
                    String[] split = testDataRow.split("Author:");
                    title += " - " + split[1];
                }
                if (!"".equals(testDataRow)) {
                    wholeText += testDataRow;
                    wholeText += " ";
                    System.out.println(testDataRow);
                }
                testDataRow = br.readLine();
            }
            sentences = Arrays.asList(wholeText.split("[\\.\\!\\?]"));
        } catch (final IOException e) {
            e.printStackTrace();
        }
        PrintWriter pw = new PrintWriter("C:\\Users\\Oliver\\Documents\\NlpTrainingData\\TextData.txt");
        pw.close();
        BookData bookData = new BookData(title, sentences);
        return bookData;
    }

}
