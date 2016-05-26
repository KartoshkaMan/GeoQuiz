package com.kartoshkad.geoquiz;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.kartoshkad.geoquiz.data.Question;

public class QuizActivity extends AppCompatActivity {

    // Static data
    private static final int REQUEST_CODE_CHEAT = 0;
    private static final String KEY_INDEX = "index";
    private static final String KEY_IS_CHEATER = "is_cheater";
    private static final String TAG = "QuizActivity";

    // Buttons
    private Button mTrueButton;
    private Button mFalseButton;
    private Button mPrevButton;
    private Button mNextButton;
    private Button mCheatButton;

    // View
    private TextView mQuestionView;

    // Data
    private boolean[] mIsCheaterBank = new boolean[] {
            false, false, false, false, false
    };

    private Question[] mQuestionBank = new Question[] {
            new Question(R.string.question_ocean, true),
            new Question(R.string.question_mideast, false),
            new Question(R.string.question_africa, false),
            new Question(R.string.question_americas, true),
            new Question(R.string.question_asia, true)
    };

    private int mCurrentIndex = 0;

    private void checkAnswer(boolean userPressed) {
        int resultStringID;
        boolean rightAnswer = mQuestionBank[mCurrentIndex].getAnswer();

        if (mIsCheaterBank[mCurrentIndex]) {
            resultStringID = R.string.judgment_toast;
            mIsCheaterBank[mCurrentIndex] = false;
        } else {
            if (rightAnswer == userPressed) resultStringID = R.string.correct_toast;
            else resultStringID = R.string.incorrect_toast;
        }

        Toast.makeText(QuizActivity.this, resultStringID, Toast.LENGTH_SHORT).show();
        mNextButton.callOnClick();
    }

    private void updateQuestion() {
        int question = mQuestionBank[mCurrentIndex].getTextResId();
        mQuestionView.setText(question);
    }

    private void initTrueButton() {
        mTrueButton = (Button) findViewById(R.id.true_button);
        mTrueButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkAnswer(true);
            }
        });
    }
    private void initFalseButton() {
        mFalseButton = (Button) findViewById(R.id.false_button);
        mFalseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkAnswer(false);
            }
        });
    }
    private void initNextButton() {
        mNextButton = (Button) findViewById(R.id.next_button);
        mNextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mCurrentIndex = (mCurrentIndex + 1) % mQuestionBank.length;
                updateQuestion();
            }
        });
    }
    private void initPrevButton() {
        mPrevButton = (Button) findViewById(R.id.prev_button);
        mPrevButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int length = mQuestionBank.length;
                mIsCheaterBank[mCurrentIndex] = false;
                mCurrentIndex = length - (length - mCurrentIndex) % length - 1;
                updateQuestion();
            }
        });
    }
    private void initCheatButton(){
        mCheatButton = (Button) findViewById(R.id.cheat_button);
        mCheatButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = CheatActivity.newIntent(
                        QuizActivity.this,
                        mQuestionBank[mCurrentIndex].getAnswer()
                );
                startActivityForResult(i, REQUEST_CODE_CHEAT);
            }
        });
    }

    private void initQuestionView() {
        mQuestionView = (TextView) findViewById(R.id.question_view);
        updateQuestion();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);

        if (savedInstanceState != null) {
            mCurrentIndex = savedInstanceState.getInt(KEY_INDEX);
            mIsCheaterBank = savedInstanceState.getBooleanArray(KEY_IS_CHEATER);
        }
        else Log.d(TAG, "savedInstanceState == null");

        initTrueButton();
        initFalseButton();
        initNextButton();
        initPrevButton();
        initCheatButton();

        initQuestionView();
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        savedInstanceState.putInt(KEY_INDEX, mCurrentIndex);
        savedInstanceState.putBooleanArray(KEY_IS_CHEATER, mIsCheaterBank);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_quiz, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != Activity.RESULT_OK)
            return;

        if (requestCode == REQUEST_CODE_CHEAT) {
            if (data == null)
                return;

            mIsCheaterBank[mCurrentIndex] = CheatActivity.wasAnswerShown(data);
        }
    }
}
