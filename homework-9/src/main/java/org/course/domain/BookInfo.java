package org.course.domain;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
@Data
public class BookInfo {

    @Column(name = "score_one_count")
    private int scoreOneCount;

    @Column(name = "score_two_count")
    private int scoreTwoCount;

    @Column(name = "score_three_count")
    private int scoreThreeCount;

    @Column(name = "score_four_count")
    private int scoreFourCount;

    @Column(name = "score_five_count")
    private int scoreFiveCount;

    @Column(name = "comment_count")
    private int commentCount;

    public double getAvgScore(){
        double weightSum = scoreOneCount + scoreTwoCount * 2 + scoreThreeCount * 3 + scoreFourCount * 4 + scoreFiveCount * 5;
        double scoreSum = scoreOneCount + scoreTwoCount + scoreThreeCount + scoreFourCount + scoreFiveCount;
        return scoreSum == 0 ? 0 : weightSum / scoreSum;
    }

}
