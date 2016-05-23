package com.github.ematiyuk.catsquiz;

import android.app.Activity;
import android.os.Bundle;

public class CheatActivity extends Activity {

    public static final String EXTRA_ANSWER_IS_TRUE = "com.github.ematiyuk.catsquiz.answer_is_true";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cheat);
    }
}
