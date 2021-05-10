package org.course.domain.embedded;

public enum ScoreNumber {

    SCORE_ONE,
    SCORE_TWO,
    SCORE_THREE,
    SCORE_FOUR,
    SCORE_FIVE;

    public static ScoreNumber of(int value){
        switch (value){
            case 1:
                return SCORE_ONE;
            case 2:
                return SCORE_TWO;
            case 3:
                return SCORE_THREE;
            case 4:
                return SCORE_FOUR;
            case 5:
                return SCORE_FIVE;
            default:
                throw new IllegalArgumentException("Incorrect scoreValue " + value);
        }
    }
}
