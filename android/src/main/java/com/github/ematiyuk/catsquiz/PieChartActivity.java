package com.github.ematiyuk.catsquiz;

import android.app.Activity;
import android.os.Bundle;

public class PieChartActivity extends Activity {
    public static final String EXTRA_CORRECT_ANSWERS_NUMBER = "com.github.ematiyuk.catsquiz.correct_answers_number";
    public static final String EXTRA_INCORRECT_ANSWERS_NUMBER = "com.github.ematiyuk.catsquiz.incorrect_answers_number";
    public static final String EXTRA_CHEATED_ANSWERS_BANK = "com.github.ematiyuk.catsquiz.cheated_answers_bank";

    private int mCorrectAnswersNumber = 0;
    private int mIncorrectAnswersNumber = 0;
    private int mCheatedAnswersNumber = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_piechart);

        mCorrectAnswersNumber = getIntent().getIntExtra(EXTRA_CORRECT_ANSWERS_NUMBER, 0);
        mIncorrectAnswersNumber = getIntent().getIntExtra(EXTRA_INCORRECT_ANSWERS_NUMBER, 0);
        mCheatedAnswersNumber = getCheatedAnswersNumber(
                getIntent().getBooleanArrayExtra(EXTRA_CHEATED_ANSWERS_BANK));
    }

    private int getCheatedAnswersNumber(boolean[] cheatedQuestionBank) {
        int cheatedQBankLength = cheatedQuestionBank.length;
        int count = 0;
        for (int i = 0; i < cheatedQBankLength; i++) {
            if (cheatedQuestionBank[i])
                count++;
        }
        return count;
    }
}
