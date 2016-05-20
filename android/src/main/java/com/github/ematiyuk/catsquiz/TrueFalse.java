package com.github.ematiyuk.catsquiz;

public class TrueFalse {
    private int mQuestion;
    private int mAnswer;
    private boolean mTrueQuestion;

    public TrueFalse(int question, int answer, boolean trueQuestion) {
        this.mQuestion = question;
        this.mAnswer = answer;
        this.mTrueQuestion = trueQuestion;
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

    public int getAnswer() {
        return mAnswer;
    }

    public void setAnswer(int answer) {
        this.mAnswer = answer;
    }
}
