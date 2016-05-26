package com.kartoshkad.geoquiz.data;

/**
 * Created by user on 1/14/16.
 */
public class Question {

    private int mTextResId;
    private boolean mAnswer;

    public Question(int textResId, boolean answer) {
        mAnswer = answer;
        mTextResId = textResId;
    }

    public int getTextResId() {
        return mTextResId;
    }

    public boolean getAnswer() {
        return mAnswer;
    }
}
