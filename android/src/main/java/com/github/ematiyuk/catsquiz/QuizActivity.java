package com.github.ematiyuk.catsquiz;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Locale;

public class QuizActivity extends Activity {

    private Button mTrueButton;
    private Button mFalseButton;
    private ImageButton mNextButton;
    private Button mCheatButton;
    private TextView mQuestionTextView;
    private TextView mAnswerTextView;
    private TextView mQuestionNumberTextView;

    private TrueFalse[] mQuestionBank = new TrueFalse[] {
            new TrueFalse(R.string.question_whiskers_hunt, R.string.answer_whiskers_hunt, true),
            new TrueFalse(R.string.question_heart_beat, R.string.answer_heart_beat, true),
            new TrueFalse(R.string.question_see_in_darkness, R.string.answer_see_in_darkness, false),
            new TrueFalse(R.string.question_jump_high, R.string.answer_jump_high, true),
            new TrueFalse(R.string.question_drink_saltwater, R.string.answer_drink_saltwater, true),
            new TrueFalse(R.string.question_cat_breeds, R.string.answer_cat_breeds, false),
            new TrueFalse(R.string.question_paw_sweat, R.string.answer_paw_sweat, true),
            new TrueFalse(R.string.question_first_space_cat, R.string.answer_first_space_cat, true),
            new TrueFalse(R.string.question_more_domestic_dogs, R.string.answer_more_domestic_dogs, false),
            new TrueFalse(R.string.question_egyptian_cats, R.string.answer_egyptian_cats, false),
            new TrueFalse(R.string.question_toxic_chocolate, R.string.answer_toxic_chocolate, true),
            new TrueFalse(R.string.question_boarding_ships, R.string.answer_boarding_ships, false),
            new TrueFalse(R.string.question_oldest_cats_breed, R.string.answer_oldest_cats_breed, true),
            new TrueFalse(R.string.question_meow_communication, R.string.answer_meow_communication, true),
            new TrueFalse(R.string.question_colors_spectrum, R.string.answer_colors_spectrum, false),
            new TrueFalse(R.string.question_distance_near_vision, R.string.answer_distance_near_vision, true),
            new TrueFalse(R.string.question_cats_dream, R.string.answer_cats_dream, true),
            new TrueFalse(R.string.question_deep_purring, R.string.answer_deep_purring, true),
            new TrueFalse(R.string.question_smallest_breed, R.string.answer_smallest_breed, true),
            new TrueFalse(R.string.question_refined_palate, R.string.answer_refined_palate, false),
            new TrueFalse(R.string.question_paw_pads, R.string.answer_paw_pads, false),
            new TrueFalse(R.string.question_head_rubbing, R.string.answer_head_rubbing, true),
            new TrueFalse(R.string.question_domesticated_cats, R.string.answer_domesticated_cats, false),
            new TrueFalse(R.string.question_turkish_van_cats, R.string.answer_turkish_van_cats, true),
            new TrueFalse(R.string.question_hear_better_than_humans, R.string.answer_hear_better_than_humans, true),
            new TrueFalse(R.string.question_speed_run, R.string.answer_speed_run, true),
            new TrueFalse(R.string.question_fewer_bones, R.string.answer_fewer_bones, false),
            new TrueFalse(R.string.question_sense_of_smell, R.string.answer_sense_of_smell, false),
            new TrueFalse(R.string.question_ear_muscles, R.string.answer_ear_muscles, true),
            new TrueFalse(R.string.question_taste_sugar, R.string.answer_taste_sugar, true),
            new TrueFalse(R.string.question_hot_food, R.string.answer_hot_food, false),
            new TrueFalse(R.string.question_awake_life, R.string.answer_awake_life, true),
            new TrueFalse(R.string.question_collarbones, R.string.answer_collarbones, true),
    };

    /** contains flags for each question that defines cheat usage by user */
    private boolean[] mCheatedQuestionBank = new boolean[mQuestionBank.length];

    private int mCurrentIndex = 0;
    /** defines either Question or Answer mode*/
    private boolean mQuestionMode = true;
    /** defines whether user used cheat at CheatActivity for current question or not */
    private boolean mIsCheater;
    /** counts times of correctly answered questions */
    private int mCorrectAnswersNumber = 0;
    /** counts times of wrong answered questions */
    private int mIncorrectAnswersNumber = 0;
    private static final String KEY_INDEX = "index";
    private static final String KEY_MODE = "mode";
    private static final String KEY_IS_CHEATER = "isCheater";
    private static final String KEY_CHEATED_Q_BANK = "cheatedQuestionBank";
    private static final String KEY_CORRECT_ANSWERS_NUMBER = "correctAnswersNumber";
    private static final String KEY_INCORRECT_ANSWERS_NUMBER = "incorrectAnswersNumber";


    private void updateQuestion() {
        int question = mQuestionBank[mCurrentIndex].getQuestion(); // get question string resID
        mQuestionTextView.setText(question);
        mQuestionNumberTextView.setText(String.format(Locale.ENGLISH, "%d/%d", (mCurrentIndex + 1), mQuestionBank.length));
        updateWidgetsVisibility();
    }

    private void updateAnswer() {
        Resources res = getResources();
        if (mCheatedQuestionBank[mCurrentIndex]) {
            String answerStr = String.format("<b><font color=red>%s</font></b>", res.getString(R.string.judgment_toast));
            mAnswerTextView.setText(Html.fromHtml(answerStr));
        }
        else {
            int answer = mQuestionBank[mCurrentIndex].getAnswer(); // get answer string resID
            String extendedAnswer = String.format("<b>%s %s.</b>&nbsp;", res.getString(R.string.answer_is),
                    (mQuestionBank[mCurrentIndex].isTrueQuestion()) ? res.getString(R.string.true_button)
                            : res.getString(R.string.false_button));
            extendedAnswer = extendedAnswer + res.getString(answer);
            mAnswerTextView.setText(Html.fromHtml(extendedAnswer));
        }
    }

    private void checkAnswer(boolean userPressedTrue) {
        boolean answerIsTrue = mQuestionBank[mCurrentIndex].isTrueQuestion();

        int messageResId;

        if (mCheatedQuestionBank[mCurrentIndex])
            messageResId = R.string.judgment_toast;
        else {
            if (userPressedTrue == answerIsTrue) {
                messageResId = R.string.correct_string;
                mCorrectAnswersNumber++;
            } else {
                messageResId = R.string.incorrect_string;
                mIncorrectAnswersNumber++;
            }
        }

        Toast.makeText(this, messageResId, Toast.LENGTH_SHORT).show();
        updateAnswer();
        updateWidgetsVisibility();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);

        if (savedInstanceState != null) {
            /* get data from Bundle */
            mCurrentIndex = savedInstanceState.getInt(KEY_INDEX, 0);
            mQuestionMode = savedInstanceState.getBoolean(KEY_MODE, true);
            mIsCheater = savedInstanceState.getBoolean(KEY_IS_CHEATER, false);
            mCheatedQuestionBank = savedInstanceState.getBooleanArray(KEY_CHEATED_Q_BANK).clone();
            mCorrectAnswersNumber = savedInstanceState.getInt(KEY_CORRECT_ANSWERS_NUMBER, 0);
            mIncorrectAnswersNumber = savedInstanceState.getInt(KEY_INCORRECT_ANSWERS_NUMBER, 0);
        }

        mQuestionTextView = (TextView) findViewById(R.id.question_text_view);
        mAnswerTextView = (TextView) findViewById(R.id.answer_text_view);
        mQuestionNumberTextView = (TextView) findViewById(R.id.question_number_text_view);
        mTrueButton = (Button) findViewById(R.id.true_button);
        mFalseButton = (Button) findViewById(R.id.false_button);
        mNextButton = (ImageButton) findViewById(R.id.next_button);
        mCheatButton = (Button) findViewById(R.id.cheat_button);

        mTrueButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!mCheatedQuestionBank[mCurrentIndex]) // if current question is not cheated,
                    // in other words mCheatedQuestionBank[mCurrentIndex] == false
                    mCheatedQuestionBank[mCurrentIndex] = mIsCheater; // set current question mIsCheater value
                mQuestionMode = false;
                checkAnswer(true);
            }
        });

        mFalseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!mCheatedQuestionBank[mCurrentIndex])
                    mCheatedQuestionBank[mCurrentIndex] = mIsCheater;
                mQuestionMode = false;
                checkAnswer(false);
            }
        });

        mNextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if ((mCurrentIndex + 1) == mQuestionBank.length) { // if it's the last question
                    Intent intent = new Intent(QuizActivity.this, PieChartActivity.class);
                    intent.putExtra(PieChartActivity.EXTRA_CORRECT_ANSWERS_NUMBER, mCorrectAnswersNumber);
                    intent.putExtra(PieChartActivity.EXTRA_INCORRECT_ANSWERS_NUMBER, mIncorrectAnswersNumber);
                    intent.putExtra(PieChartActivity.EXTRA_CHEATED_ANSWERS_BANK, mCheatedQuestionBank);
                    startActivity(intent);
                }
                // go to the first question after the last one
                mCurrentIndex = (mCurrentIndex + 1) % mQuestionBank.length;

                mQuestionMode = true;
                mIsCheater = false;
                updateQuestion();
            }
        });

        mCheatButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(QuizActivity.this, CheatActivity.class);
                boolean answerIsTrue = mQuestionBank[mCurrentIndex].isTrueQuestion();
                intent.putExtra(CheatActivity.EXTRA_ANSWER_IS_TRUE, answerIsTrue);
                startActivityForResult(intent, 0);
            }
        });

        mQuestionNumberTextView.setText(String.format(Locale.ENGLISH, "%d/%d", (mCurrentIndex + 1), mQuestionBank.length));
        updateQuestion();
        updateAnswer();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        /* put data to Bundle */
        outState.putInt(KEY_INDEX, mCurrentIndex);
        outState.putBoolean(KEY_MODE, mQuestionMode);
        outState.putBoolean(KEY_IS_CHEATER, mIsCheater);
        outState.putBooleanArray(KEY_CHEATED_Q_BANK, mCheatedQuestionBank);
        outState.putInt(KEY_CORRECT_ANSWERS_NUMBER, mCorrectAnswersNumber);
        outState.putInt(KEY_INCORRECT_ANSWERS_NUMBER, mIncorrectAnswersNumber);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (data == null)
            return;
        mIsCheater = data.getBooleanExtra(CheatActivity.EXTRA_ANSWER_SHOWN, false);
    }

    private void updateWidgetsVisibility() {
        if (mQuestionMode) {
            mNextButton.setVisibility(View.GONE);
            mAnswerTextView.setVisibility(View.GONE);
            mTrueButton.setVisibility(View.VISIBLE);
            mFalseButton.setVisibility(View.VISIBLE);
            mCheatButton.setVisibility(View.VISIBLE);
        } else {
            mTrueButton.setVisibility(View.GONE);
            mFalseButton.setVisibility(View.GONE);
            mCheatButton.setVisibility(View.GONE);
            mAnswerTextView.setVisibility(View.VISIBLE);
            mNextButton.setVisibility(View.VISIBLE);
        }
    }
}
