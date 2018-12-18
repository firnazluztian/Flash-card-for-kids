package com.example.firnazluztian.flashcardsforkids;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.wajahatkarim3.easyflipview.EasyFlipView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class CardActivity extends AppCompatActivity {
    //create Buttons and TextView for UI communication
    private Button mTrueButton;
    private Button mFalseButton;
    private Button mNextButton;
    private TextView mQuestionTextView;
    private TextView mAnswerTextView;
    private CardView mAnswerPic;
    private ProgressBar mProgressBar;

    DatabaseHelper mDatabaseHelper;

    //create the question bank
    private CardActivity.QuestionsModel[] mQuestionBank = new CardActivity.QuestionsModel[]{
            new CardActivity.QuestionsModel(R.string.question5, R.string.answer5, R.drawable.ans5,true),
            new CardActivity.QuestionsModel(R.string.question6, R.string.answer6, R.drawable.ans6,false),
            new CardActivity.QuestionsModel(R.string.question7, R.string.answer7, R.drawable.ans7,true),
            new CardActivity.QuestionsModel(R.string.question8, R.string.answer8, R.drawable.ans8,false)
    };
    private int mCurrentIndex = 0;
    private int count = 1;
    private int score = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("Question " + count);
        setContentView(R.layout.activity_card);

        mDatabaseHelper = new DatabaseHelper(this);
        mQuestionTextView = (TextView) findViewById(R.id.questions);
        mAnswerTextView = (TextView) findViewById(R.id.answer);
        mAnswerPic = (CardView) findViewById(R.id.answerImg);
        mTrueButton = (Button) findViewById(R.id.true_button);
        mTrueButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //will define method in a moment
                checkAnswer(true);
            }
        });
        mFalseButton = (Button) findViewById(R.id.false_button);
        mFalseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkAnswer(false);
            }
        });

        mNextButton = (Button) findViewById(R.id.next_button);
        mNextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nextQuestion();
                updateQuestion();
            }
        });

        final EasyFlipView easyFlipView = (EasyFlipView) findViewById(R.id.cardFlip);
        easyFlipView.setOnTouchListener(new onSwipeTouchListener(CardActivity.this) {
            public void onSwipeTop() {
                Intent get = getIntent();
                final String name = get.getExtras().getString("name");

                Intent intent = new Intent(CardActivity.this, MenuActivity.class);
                intent.putExtra("name",name);
                startActivity(intent);
            }
            public void onSwipeRight() {
                easyFlipView.flipTheView();
                easyFlipView.flipTheView(false);
            }
            public void onSwipeLeft() {
                easyFlipView.flipTheView();
                easyFlipView.flipTheView(false);
            }
        });

        // progress bar code
        mProgressBar = (ProgressBar) findViewById(R.id.progressBar);
        mProgressBar.setMax(mQuestionBank.length - 1);
        mProgressBar.setProgress(0);

        score = 0;
        updateQuestion();
    }

    private void updateQuestion(){
        int question = mQuestionBank[mCurrentIndex].getTextId();
        int answer = mQuestionBank[mCurrentIndex].getAnswerId();
        int answerPic = mQuestionBank[mCurrentIndex].getAnswerPic();
        mQuestionTextView.setText(question);
        mAnswerTextView.setText(answer);
        mAnswerPic.setBackgroundResource(answerPic);
    }

    private void checkAnswer(boolean userPress){
        Intent intent = getIntent();
        final String name = intent.getExtras().getString("name");

        boolean answerTrue = mQuestionBank[mCurrentIndex].isAnswer();
        int messageResId = 0;
        String button1;
        String button2;
        if (userPress == answerTrue){
            messageResId = R.string.correct_toast;
            button2 = "See explanation";
            button1 = "Next question";
            score = score+1;
        }else{
            messageResId = R.string.incorrect_toast;
            button2 = "See answer";
            button1 = "Skip question";
        }

        if ((mCurrentIndex+1) == mQuestionBank.length) {
            String alert;
            if (score == mQuestionBank.length) {
                alert = "Congratulations! " + name + ", you got " + score + " out of " + mQuestionBank.length + " questions correct";
            }
            else {
                alert = name + ", you got " + score + " out of " + mQuestionBank.length + " questions correct";
            }

            // STORING DATA TO DB

            // get date
            String date = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
            String newEntry = "User: " + name + "\nScore: " + score + ", out of " + mQuestionBank.length + "\nDate: " + date;

            AddData(newEntry);

            button1 = "try again?";
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(CardActivity.this);
            alertDialogBuilder.setMessage(alert);
            alertDialogBuilder.setCancelable(false);
            alertDialogBuilder.setPositiveButton(button1,
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface arg0, int arg1) {
                            score = 0;
                            nextQuestion();
                        }
                    });
            alertDialogBuilder.setNegativeButton("exit",
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface arg0, int arg1) {
                            Intent intent = new Intent(CardActivity.this, MenuActivity.class);
                            intent.putExtra("name",name);
                            startActivity(intent);
                        }
                    });
            AlertDialog alertDialog = alertDialogBuilder.create();
            alertDialog.show();

        } else {
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(CardActivity.this);
            alertDialogBuilder.setMessage(messageResId);
            alertDialogBuilder.setCancelable(false);
            alertDialogBuilder.setPositiveButton(button1,
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface arg0, int arg1) {
                            nextQuestion();
                        }
                    });
            AlertDialog alertDialog = alertDialogBuilder.create();
            alertDialog.show();
        }

    }

    public class QuestionsModel {
        private int mTextId;
        private int mAnswerId;
        private int mAnswerPic;
        private boolean mAnswer;

        //create a constructor
        public QuestionsModel(int textId, int answerId, int answerPic, boolean answer) {
            mTextId = textId;
            mAnswerId = answerId;
            mAnswerPic = answerPic;
            mAnswer = answer;
        }
        public int getTextId() {
            return mTextId;
        }
        public int getAnswerId() {
            return mAnswerId;
        }
        public int getAnswerPic() {
            return mAnswerPic;
        }
        public boolean isAnswer() {
            return mAnswer;
        }
    }

    private void nextQuestion() {
        mCurrentIndex = (mCurrentIndex + 1) % mQuestionBank.length;
        count = (mCurrentIndex + 1) % mQuestionBank.length;
        if (count == 0) {
            count = mQuestionBank.length;
        }
        setTitle("Question " + count );
        mProgressBar.setProgress(mCurrentIndex);

        updateQuestion();
    }

    public void AddData(String newEntry) {
        boolean insertData = mDatabaseHelper.addData(newEntry);
        if (insertData) {

        } else {
            ToastMessage("Something went wrong");
        }
    }

    private void ToastMessage (String message) {
        Toast.makeText(this,message, Toast.LENGTH_SHORT).show();
    }
}
