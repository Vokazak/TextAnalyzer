package ru.vokazak;

import ru.vokazak.exceptions.TextAnalyzerException;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
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
        return Arrays.asList(str
                .replaceAll("[^a-zA-ZА-Яа-яЁё0-9]", " ")
                .toLowerCase()
                .split("[ ]+")
        );
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
