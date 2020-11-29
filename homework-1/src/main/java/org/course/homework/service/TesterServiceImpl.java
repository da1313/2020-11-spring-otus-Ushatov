package org.course.homework.service;

import org.course.homework.domain.Answer;
import org.course.homework.domain.Question;

import java.util.List;
import java.util.Scanner;

public class TesterServiceImpl implements TesterService {
    private final RandomGenerator randomGenerator;
    private final int questionCount;

    public TesterServiceImpl(RandomGenerator inquirerService, int questionCount) {
        this.randomGenerator = inquirerService;
        this.questionCount = questionCount;
    }

    public void run(){
        Scanner scanner = new Scanner(System.in);
        List<Question> questions = randomGenerator.generate(questionCount);
        for (int i = 0; i < questions.size(); i++) {
            Question question = questions.get(i);
            System.out.printf("%d %s\n", i + 1, question.getContent());
            //Collections.shuffle(question.getAnswers());
            List<Answer> answers = question.getAnswers();
            for (int j = 0; j < answers.size(); j++) {
                System.out.printf("\t%d %s\n", j + 1, answers.get(j).getContent());
            }
            //System.out.println("Введите ответ");
            //String userAnswer = scanner.nextLine();
        }
    }
}
