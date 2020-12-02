package org.course.homework;

import org.course.homework.service.TesterServiceImpl;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class Main {
    public static void main(String[] args) {
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("context.xml");
        TesterServiceImpl tester = context.getBean(TesterServiceImpl.class);
        tester.run();
    }
}
