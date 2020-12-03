package org.course.homework.service;

import org.course.homework.service.interfaces.NumberParser;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class NumberParserImpl implements NumberParser {

    @Override
    public List<Integer> parseAnswerNumbers(int size, String userAnswers) {
        String[] tokens = userAnswers.split(",");
        if (userAnswers.equals("")){
            //пустая строка
            return null;
        } else if (tokens.length > size){
            //ответов слишком много
            return null;
        }
        try{
            ArrayList<Integer> result = Arrays.stream(tokens)
                    .map(String::trim)
                    .map(Integer::parseInt)
                    .collect(Collectors.toCollection(ArrayList::new));
            if (Collections.max(result) > size || Collections.min(result) < 1){
                //одного из ответов нет
                return null;
            } else {
                return result;
            }
        } catch (NumberFormatException e){
            return null;
        }
    }
}
