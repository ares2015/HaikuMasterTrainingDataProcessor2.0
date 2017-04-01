package com.haikuMasterTrainingDataProcessor.tokenizer;

/**
 * Created by Oliver on 4/1/2017.
 */
public class TokenizerImpl implements Tokenizer {

    @Override
    public boolean containsSpecialChars(String token) {
        return token.contains(".") || token.contains(",") || token.contains("!") || token.contains("?")
                || token.contains(":") || token.contains(";")
                || token.contains("\"") || token.contains("\'");
    }

    @Override
    public boolean isLowerCase(String token) {
        return String.valueOf(token.charAt(0)).toLowerCase().equals(String.valueOf(token.charAt(0)));
    }

    @Override
    public String removeSpecialCharacters(String token) {
        StringBuilder stringBuilder = new StringBuilder();
        char[] chars = token.toCharArray();
        for (char c : chars) {
            if ((",".equals(String.valueOf(c)) || "'".equals(String.valueOf(c)) || "’".equals(String.valueOf(c)))
                    || ((Character.isDigit(c) || Character.isLetter(c)))) {
                stringBuilder.append(String.valueOf(c));
            }
        }
        return stringBuilder.toString();
    }

    @Override
    public String decapitalize(String token) {
        char c[] = token.toCharArray();
        c[0] = Character.toLowerCase(c[0]);
        return new String(c);
    }

}
