package com.github.ematiyuk.catsquiz;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

public class QuizActivity extends Activity {

    private Button mTrueButton;
    private Button mFalseButton;
    private ImageButton mPrevButton;
    private ImageButton mNextButton;
    private TextView mQuestionTextView;
    private TextView mAnswerTextView;

    private TrueFalse[] mQuestionBank = new TrueFalse[] {
            new TrueFalse(R.string.question_whiskers_hunt, R.string.answer_whiskers_hunt, true),
            new TrueFalse(R.string.question_heart_beat, R.string.answer_heart_beat, true),
            new TrueFalse(R.string.question_see_in_darkness, R.string.answer_see_in_darkness, false),
            new TrueFalse(R.string.question_jump_high, R.string.answer_jump_high, true),
            new TrueFalse(R.string.question_drink_saltwater, R.string.answer_drink_saltwater, true),
    };

    private int mCurrentIndex = 0;

    private void updateQuestion() {
        int question = mQuestionBank[mCurrentIndex].getQuestion(); // get question string resID
        mQuestionTextView.setText(question);
        mPrevButton.setVisibility(View.GONE);
        mNextButton.setVisibility(View.GONE);
        mAnswerTextView.setVisibility(View.GONE);
        mTrueButton.setVisibility(View.VISIBLE);
        mFalseButton.setVisibility(View.VISIBLE);
    }

    private void checkAnswer(boolean userPressedTrue) {
        boolean answerIsTrue = mQuestionBank[mCurrentIndex].isTrueQuestion();

        int messageResId = 0;

        messageResId = (userPressedTrue == answerIsTrue) ? R.string.correct_toast : R.string.incorrect_toast;

        mTrueButton.setVisibility(View.GONE);
        mFalseButton.setVisibility(View.GONE);
        Toast.makeText(this, messageResId, Toast.LENGTH_SHORT).show();
        int answer = mQuestionBank[mCurrentIndex].getAnswer(); // get answer string resID
        mAnswerTextView.setText(answer);
        mAnswerTextView.setVisibility(View.VISIBLE);
        if (mCurrentIndex > 0)
            mPrevButton.setVisibility(View.VISIBLE);
        mNextButton.setVisibility(View.VISIBLE);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);

        mQuestionTextView = (TextView) findViewById(R.id.question_text_view);
        mAnswerTextView = (TextView) findViewById(R.id.answer_text_view);

        mTrueButton = (Button) findViewById(R.id.true_button);
        mTrueButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkAnswer(true);
            }
        });
        mFalseButton = (Button) findViewById(R.id.false_button);
        mFalseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkAnswer(false);
            }
        });
        mPrevButton = (ImageButton) findViewById(R.id.prev_button);
        mPrevButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mCurrentIndex = ((mCurrentIndex - 1) % mQuestionBank.length < 0) ? 0
                        : (mCurrentIndex - 1) % mQuestionBank.length;
                updateQuestion();
            }
        });
        mNextButton = (ImageButton) findViewById(R.id.next_button);
        mNextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mCurrentIndex = (mCurrentIndex + 1) % mQuestionBank.length;
                updateQuestion();
            }
        });
        mPrevButton.setVisibility(View.GONE);
        mNextButton.setVisibility(View.GONE);
        mAnswerTextView.setVisibility(View.GONE);

        updateQuestion();

        mQuestionTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mCurrentIndex = (mCurrentIndex + 1) % mQuestionBank.length;
                updateQuestion();
            }
        });
    }
}
