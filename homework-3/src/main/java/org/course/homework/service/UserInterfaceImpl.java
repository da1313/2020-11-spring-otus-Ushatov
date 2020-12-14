package org.course.homework.service;

import lombok.AllArgsConstructor;
import org.course.homework.aspect.LogUserInput;
import org.course.homework.config.TestProperties;
import org.course.homework.domain.Answer;
import org.course.homework.domain.Question;
import org.course.homework.service.interfaces.NumberParser;
import org.course.homework.service.interfaces.PrintService;
import org.course.homework.service.interfaces.UserInterface;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class UserInterfaceImpl implements UserInterface {

    private final PrintService printService;
    private final NumberParser numberParser;
    private final MessageSource messageSource;
    private final TestProperties testProperties;

    @Override
    public void printQuestion(Question question, int questionNumber) {
        printService.printf("%d %s\n", questionNumber, question.getQuestionContent());
    }

    @Override
    public void printAnswers(List<Answer> answerList) {
        for (int j = 0; j < answerList.size(); j++) {
            printService.printf("\t%d %s\n", j + 1, answerList.get(j).getAnswerContent());
        }
    }

    @LogUserInput
    @Override
    public List<Answer> readUserAnswers(Question question) {
        List<Answer> answers = question.getAnswers();
        int size = question.getAnswers().size();
        List<Integer> answerNumbers = null;
        while (answerNumbers == null){
            String userAnswer = printService.readLine();
            answerNumbers= numberParser.parseAnswerNumbers(size, userAnswer);
            if (answerNumbers == null){
                String message = messageSource.getMessage("user.enter.question-number.incorrect",new String[]{}, testProperties.getLocale());
                printService.printf("%s\n", message);
            }
        }
        return answerNumbers.stream().map(n -> answers.get(n - 1)).collect(Collectors.toList());
    }

    @LogUserInput
    @Override
    public String readUserName() {
        String message = messageSource.getMessage("user.enter.username",new String[]{}, testProperties.getLocale());
        printService.printf("%s\n", message);
        return printService.readLine();
    }

    @Override
    public void printResult(String userName, double userRate, double successRate) {
        String message;
        if (userRate < successRate) {
            message = messageSource.getMessage("test.output.result.false", new String[]{userName}, testProperties.getLocale());
        } else {
            message = messageSource.getMessage("test.output.result.true", new String[]{userName}, testProperties.getLocale());
        }
        printService.printf(message);
    }

}
