package org.course.homework.service;

import org.course.homework.domain.Answer;
import org.course.homework.domain.Question;
import org.course.homework.service.interfaces.PrintService;
import org.course.homework.service.interfaces.QuestionService;
import org.course.homework.service.interfaces.RandomGenerator;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class QuestionServiceImpl implements QuestionService {

    private final RandomGenerator randomGenerator;
    private final PrintService printService;
    private final int count;

    public QuestionServiceImpl(RandomGenerator randomGenerator, PrintService printService, @Value("${questionCount}") int count) {
        this.randomGenerator = randomGenerator;
        this.printService = printService;
        this.count = count;
    }

    @Override
    public List<Question> getQuestions() {
        return randomGenerator.generate(count);
    }

    @Override
    public void printQuestion(Question question, int number) {
        printService.printf("%d %s\n", number, question.getQuestionContent());
    }

    @Override
    public void printAnswers(List<Answer> answers) {
        for (int j = 0; j < answers.size(); j++) {
            printService.printf("\t%d %s\n", j + 1, answers.get(j).getAnswerContent());
        }
    }
}
