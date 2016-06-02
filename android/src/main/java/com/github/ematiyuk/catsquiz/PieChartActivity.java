package com.github.ematiyuk.catsquiz;

import android.app.Activity;
import android.content.res.Resources;
import android.os.Bundle;

import com.github.mikephil.charting.data.Entry;

import java.util.ArrayList;

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

        Resources res = getResources();

        final ArrayList<Entry> entries = new ArrayList<>();
        entries.add(new Entry(mCorrectAnswersNumber, 0));
        entries.add(new Entry(mIncorrectAnswersNumber, 1));
        entries.add(new Entry(mCheatedAnswersNumber, 2));

        final ArrayList<String> labels = new ArrayList<String>();
        labels.add(res.getString(R.string.correct_string));
        labels.add(res.getString(R.string.incorrect_string));
        labels.add(res.getString(R.string.cheated_string));
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
