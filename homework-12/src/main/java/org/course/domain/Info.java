package org.course.domain;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
@Data
public class Info {

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

    @Column(name = "avg_score")
    private double avgScore;

    public void addScore(int value){
        switch (value){
            case 1:
                scoreOneCount++;
                break;
            case 2:
                scoreTwoCount++;
                break;
            case 3:
                scoreThreeCount++;
                break;
            case 4:
                scoreFourCount++;
                break;
            case 5:
                scoreFiveCount++;
                break;
        }
    }

    public void removeScore(int value){
        switch (value){
            case 1:
                scoreOneCount--;
                break;
            case 2:
                scoreTwoCount--;
                break;
            case 3:
                scoreThreeCount--;
                break;
            case 4:
                scoreFourCount--;
                break;
            case 5:
                scoreFiveCount--;
                break;
        }
    }

    public void setAvgScore(){
        double sum = scoreOneCount + scoreTwoCount + scoreThreeCount + scoreFourCount + scoreFiveCount;
        avgScore = sum == 0? 0 : (scoreOneCount + 2 * scoreTwoCount + 3 * scoreThreeCount + 4 * scoreFourCount + 5 * scoreFiveCount) / sum;
    }
}
