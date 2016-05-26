package com.kartoshkad.geoquiz;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.app.Activity;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.widget.Button;
import android.widget.TextView;

public class CheatActivity extends Activity {

    private static final String EXTRA_ANSWER_IS_TRUE = "com.kartoshkad.geoquiz.answer_is_true";
    private static final String EXTRA_ANSWER_SHOWN = "com.kartoshkad.geoquiz.answer_shown";
    private static final String KEY_ANSWER_SHOWN = "com.kartoshkad.geoquiz.answer_shown";

    private boolean mAnswerIsTrue;
    private boolean mIsAnswerShown;

    private Button mShowAnswerButton;
    private TextView mAnswerView;
    private TextView mAPIView;

    public static boolean wasAnswerShown(Intent intent) {
        return intent.getBooleanExtra(EXTRA_ANSWER_SHOWN, false);
    }
    public static Intent newIntent(Context packageContext, boolean answerIsTrue) {
        Intent i = new Intent(packageContext, CheatActivity.class);
        i.putExtra(EXTRA_ANSWER_IS_TRUE, answerIsTrue);
        return i;
    }

    private void initShowAnswerButton() {
        mShowAnswerButton = (Button) findViewById(R.id.show_answer_button);
        mShowAnswerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mAnswerIsTrue) mAnswerView.setText(R.string.true_button);
                else mAnswerView.setText(R.string.false_button);
                mIsAnswerShown = true;
                setAnswerShownResult(mIsAnswerShown);

                int cx = mShowAnswerButton.getWidth() / 2;
                int cy = mShowAnswerButton.getHeight() / 2;
                float radius = mShowAnswerButton.getWidth();

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    Animator anim = ViewAnimationUtils.createCircularReveal(mShowAnswerButton, cx, cy, radius, 0);
                    anim.addListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            super.onAnimationEnd(animation);

                            mAnswerView.setVisibility(View.VISIBLE);
                            mShowAnswerButton.setVisibility(View.INVISIBLE);
                        }
                    });
                    anim.start();
                } else {
                    mAnswerView.setVisibility(View.VISIBLE);
                    mShowAnswerButton.setVisibility(View.INVISIBLE);
                }
            }
        });
    }
    private void initAnswerView() {
        mAnswerView = (TextView) findViewById(R.id.answer_view);
    }
    private void initAPIView() {
        mAPIView = (TextView) findViewById(R.id.api_ver);
        mAPIView.setText(R.string.api_lvl + Build.VERSION.SDK_INT);
    }

    private void setAnswerShownResult(boolean isAnswerShown) {
        Intent data = new Intent();
        data.putExtra(EXTRA_ANSWER_SHOWN, isAnswerShown);
        setResult(RESULT_OK, data);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cheat);

        if (savedInstanceState != null)
            mIsAnswerShown = savedInstanceState.getBoolean(KEY_ANSWER_SHOWN, false);

        mAnswerIsTrue = getIntent().getBooleanExtra(EXTRA_ANSWER_IS_TRUE, false);

        initShowAnswerButton();
        initAnswerView();
        initAPIView();
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        savedInstanceState.putBoolean(KEY_ANSWER_SHOWN, mIsAnswerShown);
    }
}
