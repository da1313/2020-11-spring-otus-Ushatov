package org.course.homework.service;

import org.course.homework.domain.Answer;
import org.course.homework.service.interfaces.NumberParser;
import org.course.homework.service.interfaces.PrintService;
import org.course.homework.service.interfaces.UserService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

    private final PrintService printService;
    private final NumberParser numberParser;

    public UserServiceImpl(PrintService printService, NumberParser numberParser) {
        this.printService = printService;
        this.numberParser = numberParser;
    }

    @Override
    public String readUserName() {
        printService.printf("%s\n", "Введите фамилию и имя (например Иван Иванов)");
        return printService.readLine();
    }

    /**
     * В метод передаются варианты ответов на вопросы, пользователь вводит номера ответов
     * важно передать сюда те-же вопросы что и были напечатаны!
     * @param answers - список объектов ответов на вопрос (порядок может быть любым)
     * @return - объекты ответов которые выбрал пользователь
     */
    @Override
    public List<Answer> readAnswer(List<Answer> answers) {
        List<Integer> answerNumbers = null;
        while (answerNumbers == null){
            String userAnswer = printService.readLine();
            answerNumbers= numberParser.parseAnswerNumbers(answers.size(), userAnswer);
            if (answerNumbers == null){
                printService.printf("%s\n", "Введены неверные значения, повторите ввод");
            }
        }
        return answerNumbers.stream().map(n -> answers.get(n - 1)).collect(Collectors.toList());
    }
}
