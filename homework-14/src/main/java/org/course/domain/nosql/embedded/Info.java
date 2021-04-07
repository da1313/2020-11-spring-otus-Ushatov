package org.course.domain.nosql.embedded;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Info {

    private int scoreOneCount;

    private int scoreTwoCount;

    private int scoreThreeCount;

    private int scoreFourCount;

    private int scoreFiveCount;

    private int commentCount;

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
