package com.github.ematiyuk.catsquiz;

import android.app.Activity;
import android.os.Bundle;

public class PieChartActivity extends Activity {
    public static final String EXTRA_CORRECT_ANSWERS_NUMBER = "com.github.ematiyuk.catsquiz.correct_answers_number";
    public static final String EXTRA_INCORRECT_ANSWERS_NUMBER = "com.github.ematiyuk.catsquiz.incorrect_answers_number";
    public static final String EXTRA_CHEATED_ANSWERS_BANK = "com.github.ematiyuk.catsquiz.cheated_answers_bank";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_piechart);
    }
}
