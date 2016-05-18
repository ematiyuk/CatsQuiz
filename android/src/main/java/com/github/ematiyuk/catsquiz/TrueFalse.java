package com.github.ematiyuk.catsquiz;

public class TrueFalse {
    private int mQuestion;
    private boolean mTrueQuestion;

    public TrueFalse(int mQuestion, boolean mTrueQuestion) {
        this.mQuestion = mQuestion;
        this.mTrueQuestion = mTrueQuestion;
    }

    public void setTrueQuestion(boolean trueQuestion) {
        this.mTrueQuestion = trueQuestion;
    }

    public void setQuestion(int question) {
        this.mQuestion = question;
    }

    public int getQuestion() {
        return mQuestion;
    }

    public boolean isTrueQuestion() {
        return mTrueQuestion;
    }
}
