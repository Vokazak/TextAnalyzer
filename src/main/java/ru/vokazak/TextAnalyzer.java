package ru.vokazak;

import ru.vokazak.exceptions.TextAnalyzerException;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class TextAnalyzer {

    public String readDataFromFile(File file) throws TextAnalyzerException {
        StringBuilder sb = new StringBuilder();

        try(BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line = br.readLine();

            while (line != null) {
                sb.append(line);
                sb.append(System.lineSeparator());
                line = br.readLine();
            }
        } catch (IOException e) {
            throw new TextAnalyzerException("Exception while reading \"" + file.getName() + '\"', e);
        }

        return sb.toString();
    }

    public List<String> extractWordsFromString(String str) {

        Pattern pattern = Pattern.compile("[a-zA-ZА-Яа-яЁё0-9]+(?:-[a-zA-ZА-Яа-яЁё0-9]+)*");
        Matcher matcher = pattern.matcher(str.toLowerCase());

        List<String> result = new ArrayList<>();
        while (matcher.find()) {
            result.add(matcher.group());
        }

        return result;
    }

    public Map<String, Integer> countWordFrequency(List<String> words) {
        return words.stream()
                .collect(Collectors.toMap(
                        s -> s,
                        s -> 1,
                        Integer::sum)
                );
    }

}
