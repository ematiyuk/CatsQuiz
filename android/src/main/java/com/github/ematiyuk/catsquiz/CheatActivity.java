package com.github.ematiyuk.catsquiz;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class CheatActivity extends Activity {

    public static final String EXTRA_ANSWER_IS_TRUE = "com.github.ematiyuk.catsquiz.answer_is_true";
    public static final String EXTRA_ANSWER_SHOWN = "com.github.ematiyuk.catsquiz.answer_shown";

    private TextView mAnswerTextView;
    private Button mShowAnswer;

    private boolean mAnswerIsTrue;
    private boolean mAnswerIsShown;

    private void setAnswerShownResult() {
        Intent data = new Intent();
        data.putExtra(EXTRA_ANSWER_SHOWN, mAnswerIsShown);
        setResult(RESULT_OK, data);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cheat);

        mAnswerIsTrue = getIntent().getBooleanExtra(EXTRA_ANSWER_IS_TRUE, false);

        mAnswerTextView = (TextView) findViewById(R.id.answerTextView);
        mShowAnswer = (Button) findViewById(R.id.showAnswerButton);

        mShowAnswer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mAnswerIsShown = true;
                updateWidgetsVisibility();
                if (mAnswerIsTrue)
                    mAnswerTextView.setText(R.string.true_button);
                else
                    mAnswerTextView.setText(R.string.false_button);
                setAnswerShownResult();
            }
        });

        // answer will not be shown until the user presses the button
        setAnswerShownResult();
    }

    private void updateWidgetsVisibility() {
        if (mAnswerIsShown) {
            mShowAnswer.setVisibility(View.GONE);
            mAnswerTextView.setVisibility(View.VISIBLE);
        } else {
            mShowAnswer.setVisibility(View.VISIBLE);
            mAnswerTextView.setVisibility(View.GONE);
        }
    }
}
