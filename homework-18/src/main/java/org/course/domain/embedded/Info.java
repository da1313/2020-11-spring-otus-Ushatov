package org.course.domain.embedded;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class Info {

    private int scoreOneCount;

    private int scoreTwoCount;

    private int scoreThreeCount;

    private int scoreFourCount;

    private int scoreFiveCount;

    private int commentCount;

    public double getAvgScore(){

        int scoreSum = scoreOneCount + scoreTwoCount + scoreThreeCount + scoreFourCount + scoreFiveCount;

        double scoreSumWithWeight = scoreOneCount + scoreTwoCount * 2 + scoreThreeCount * 3 + scoreFourCount * 4 + scoreFiveCount * 5;

        if (scoreSum == 0) {
            return 0;
        }

        return scoreSumWithWeight / scoreSum;

    }

    public List<Integer> getScoreCounts(){
        List<Integer> result = new ArrayList<>();
        result.add(scoreOneCount);
        result.add(scoreTwoCount);
        result.add(scoreThreeCount);
        result.add(scoreFourCount);
        result.add(scoreFiveCount);
        return result;
    }

}
