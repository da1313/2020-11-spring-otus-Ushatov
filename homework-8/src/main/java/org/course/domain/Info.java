package org.course.domain;

import lombok.Data;

@Data
public class Info {

    private int one;

    private int two;

    private int three;

    private int four;

    private int five;

    private double avg;

    private int count;

    public void calculate(){
        avg = ((double)(one + two * 2 + three * 3 + four * 4 + five * 5)) / (one + two + three + four + five);
    }

}
