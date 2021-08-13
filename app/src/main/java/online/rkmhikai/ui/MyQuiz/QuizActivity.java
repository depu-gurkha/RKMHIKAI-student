package online.rkmhikai.ui.MyQuiz;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.Observer;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import online.rkmhikai.R;
import online.rkmhikai.Room.MyViewModel;
import online.rkmhikai.Room.Repository;
import online.rkmhikai.Tables.Quiz;
import online.rkmhikai.ui.player.nestedlist.ChapterDetail;

public class QuizActivity extends AppCompatActivity {
    TextView txtQuestion;
    TextView textViewScore, textViewQuestionCount, textViewCountDownTimer;
    TextView textViewCorrect, textViewWrong;
    RadioButton rb1, rb2, rb3, rb4;
    RadioGroup rbGroup;
    Button buttonNext;
    boolean answerd = false;
    List<Quiz> quesList;
    Quiz currentQ;
    private int questionCounter = 0, questionTotalCount;

    private MyViewModel myViewModel;

    private ColorStateList textColorOfButtons;

    private Handler handler = new Handler();

    private int correctAns = 0, wrongAns = 0, score = 0;

    private FinalScoreDialog finalScoreDialog;

    private WrongDialog wrongDialog;

    private CorrectDialog correctDialog;

    private int totalSizeofQuiz;

    private long backPressedTime;

    private static final long COUNTDOWN_IN_MILLIS = 30000;
    private CountDownTimer countDownTimer;
    private long timeLeft;

    int contentID;

    //Extra DATA
    Repository repository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);
        setupUI();

        textColorOfButtons = rb1.getTextColors();     //This is use to change the text colors of the buttons
        contentID = getIntent().getIntExtra("myContentId", 0);
        Log.d("QUIZTAG", "onCreate: " + contentID);
        finalScoreDialog = new FinalScoreDialog(this);
        wrongDialog = new WrongDialog(this);
        correctDialog = new CorrectDialog(this);

        repository = new Repository(getApplication(), this);

        myViewModel = new MyViewModel(getApplication(), this);


        myViewModel.getQuizJson(contentID).observe(this, new Observer<List<Quiz>>() {


            @Override
            public void onChanged(@Nullable List<Quiz> questions) {
                Log.d("QUIZTAG", "onChanged: " + questions);
                Log.d("QUESTIONS", "onChanged: Fired" + contentID);
                List<Quiz> myquestions = new ArrayList<>();
                String result = repository.generateQuestionJSON(contentID);
                Log.d("QUESTION", "RESULT from JSON: " + result);

                JSONArray mainArray = null;
                try {
                    mainArray = new JSONArray(result);
                    for (int i = 0; i < mainArray.length(); i++) {
                        Log.d("Inside For", "For Bithra");
                        JSONObject jobject = mainArray.getJSONObject(i);
                        int id = jobject.getInt("id");
                        int qContentId = jobject.getInt("qContentId");
                        String question = jobject.getString("question");
                        String option1 = jobject.getString("option1");
                        String option2 = jobject.getString("option2");
                        String option3 = jobject.getString("option3");
                        String option4 = jobject.getString("option4");
                        int correctAns = jobject.getInt("correctAns");

                        Quiz quiz = new Quiz(id, qContentId, question, option1, option2, option3, option4, correctAns);
                        Log.d("MyReturnValue", result);
                        myquestions.add(quiz);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                //Toast.makeText(QuizActivity.this, "GOT IT :)", Toast.LENGTH_SHORT).show();
                fetchContent(myquestions);
            }
        });

    }

    private void fetchContent(List<Quiz> questions) {
        quesList = questions;
        startQuiz();
    }

    private void startQuiz() {
        setQuestionView();

        rbGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int checkedId) {
                switch (checkedId) {
                    case R.id.radio_button1:
                        rb1.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.when_answer_selected));

                        rb2.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.default_option_bg));
                        rb3.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.default_option_bg));
                        rb4.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.default_option_bg));
                        break;

                    case R.id.radio_button2:
                        rb2.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.when_answer_selected));
                        rb1.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.default_option_bg));
                        rb3.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.default_option_bg));
                        rb4.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.default_option_bg));
                        break;

                    case R.id.radio_button3:
                        rb3.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.when_answer_selected));
                        rb2.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.default_option_bg));
                        rb1.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.default_option_bg));
                        rb4.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.default_option_bg));
                        break;

                    case R.id.radio_button4:
                        rb4.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.when_answer_selected));
                        rb2.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.default_option_bg));
                        rb3.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.default_option_bg));
                        rb1.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.default_option_bg));
                        break;
                }
            }
        });
        buttonNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (!answerd) {
                    if (rb1.isChecked() || rb2.isChecked() || rb3.isChecked() || rb4.isChecked()) {
                        Log.d("QuizOperation", String.valueOf(rb1.isChecked()));
                        quizOperation();
                    } else {
                        Toast.makeText(QuizActivity.this, "Please select answer", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }

    private void quizOperation() {
        answerd = true;
        countDownTimer.cancel();
        RadioButton rbselected = findViewById(rbGroup.getCheckedRadioButtonId());
        int answerNr = rbGroup.indexOfChild(rbselected) + 1;
        Log.d("Quiz Operation", "QuizOperation");
        Log.d("yoyo", rbselected.toString());
        checkSolution(answerNr, rbselected);
    }

    //Check Solution Method
    private void checkSolution(int answerNr, RadioButton rbselected) {
        switch (currentQ.getCorrectAns()) {

            case 1:
                if (currentQ.getCorrectAns() == answerNr) {
                    rb1.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.when_answer_correct));
                    rb1.setTextColor(Color.WHITE);
                    correctAns++;
                    textViewCorrect.setText("Correct:" + String.valueOf(correctAns));
                    score += 10;
                    textViewScore.setText("Score: " + String.valueOf(score));
                    correctDialog.correctDialog(score, this);

                } else {
                    changeToIncorrectColor(rbselected);
                    wrongAns++;
                    textViewWrong.setText("Wrong:" + String.valueOf(wrongAns));
                    final String correctAnswer = (String) rb1.getText();
                    wrongDialog.WrongDialog(correctAnswer, this);

//                    handler.postDelayed(new Runnable() {
//                        @Override
//                        public void run() {
//                            setQuestionView();
//                        }
//                    }, 500);

                }
                break;

            case 2:
                if (currentQ.getCorrectAns() == answerNr) {
                    rb2.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.when_answer_correct));
                    rb2.setTextColor(Color.WHITE);
                    correctAns++;
                    textViewCorrect.setText("Correct:" + String.valueOf(correctAns));
                    score += 10;
                    textViewScore.setText("Score: " + String.valueOf(score));
                    correctDialog.correctDialog(score, this);
                } else {
                    changeToIncorrectColor(rbselected);
                    wrongAns++;
                    textViewWrong.setText("Wrong:" + String.valueOf(wrongAns));
                    final String correctAnswer = (String) rb2.getText();
                    wrongDialog.WrongDialog(correctAnswer, this);
//                    handler.postDelayed(new Runnable() {
//                        @Override
//                        public void run() {
//                            setQuestionView();
//                        }
//                    }, 500);

                }
                break;

            case 3:
                if (currentQ.getCorrectAns() == answerNr) {
                    rb3.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.when_answer_correct));
                    rb3.setTextColor(Color.WHITE);
                    correctAns++;
                    textViewCorrect.setText("Correct:" + String.valueOf(correctAns));
                    score += 10;
                    textViewScore.setText("Score: " + String.valueOf(score));
                    correctDialog.correctDialog(score, this);

                } else {
                    changeToIncorrectColor(rbselected);
                    wrongAns++;
                    textViewWrong.setText("Wrong:" + String.valueOf(wrongAns));
                    final String correctAnswer = (String) rb3.getText();
                    wrongDialog.WrongDialog(correctAnswer, this);

//                    handler.postDelayed(new Runnable() {
//                        @Override
//                        public void run() {
//                            setQuestionView();
//                        }
//                    }, 500);

                }
                break;

            case 4:
                if (currentQ.getCorrectAns() == answerNr) {
                    rb4.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.when_answer_correct));
                    rb4.setTextColor(Color.WHITE);
                    correctAns++;
                    textViewCorrect.setText("Correct:" + String.valueOf(correctAns));
                    score += 10;
                    textViewScore.setText("Score: " + String.valueOf(score));
                    correctDialog.correctDialog(score, this);

                } else {
                    changeToIncorrectColor(rbselected);
                    wrongAns++;
                    textViewWrong.setText("Wrong:" + String.valueOf(wrongAns));

                    final String correctAnswer = (String) rb4.getText();
                    wrongDialog.WrongDialog(correctAnswer, this);

//                    handler.postDelayed(new Runnable() {
//                        @Override
//                        public void run() {
//                            setQuestionView();
//                        }
//                    }, 500);

                }
                break;
        }
        if (questionCounter == questionTotalCount) {
            buttonNext.setText("Confirm and Finish");
        }
    }

    private void changeToIncorrectColor(RadioButton rbselected) {
        rbselected.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.when_answer_wrong));
        rbselected.setTextColor(Color.WHITE);
    }


    //SetQuestionView() Method

    public void setQuestionView() {
        rbGroup.clearCheck();
        rb1.setTextColor(Color.BLACK);
        rb2.setTextColor(Color.BLACK);
        rb3.setTextColor(Color.BLACK);
        rb4.setTextColor(Color.BLACK);
        rb1.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.default_option_bg));
        rb2.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.default_option_bg));
        rb3.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.default_option_bg));
        rb4.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.default_option_bg));

        questionTotalCount = quesList.size();
        Log.d("Size", String.valueOf(quesList.size()));

        if (questionCounter < questionTotalCount) {
            currentQ = quesList.get(questionCounter);
            String ques = currentQ.getQuestion();
            txtQuestion.setText(Html.fromHtml(ques));
            rb1.setText(currentQ.getOption1());
            rb2.setText(currentQ.getOption2());
            rb3.setText(currentQ.getOption3());
            rb4.setText(currentQ.getOption4());
            questionCounter++;
            answerd = false;
            buttonNext.setText("Confirm");
            textViewQuestionCount.setText("Questions: " + questionCounter + "/" + (questionTotalCount));
            timeLeft = COUNTDOWN_IN_MILLIS;
            startCountDown();
        } else {
            Toast.makeText(this, "Quiz Finished", Toast.LENGTH_SHORT).show();

            //totalSizeofQuiz = quesList.size();

            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    resultData();
                    //finalScoreDialog.finalScoreDialog(correctAns, wrongAns, totalSizeofQuiz);
                }
            }, 1000);
        }
    }


    //The Timer Code
    private void startCountDown() {
        countDownTimer = new CountDownTimer(timeLeft, 1000) {
            @Override
            public void onTick(long l) {
                timeLeft = l;
                updateCountDownText();
            }

            @Override
            public void onFinish() {
                timeLeft = 0;
                updateCountDownText();
            }
        }.start();
    }


    private void updateCountDownText() {
        int minutes = (int) (timeLeft / 1000) / 60;
        int seconds = (int) (timeLeft / 1000) % 60;
        String timeFormatted = String.format(Locale.getDefault(), "%02d:%02d", minutes, seconds);
        textViewCountDownTimer.setText(timeFormatted);

        if (timeLeft < 10000) {
            textViewCountDownTimer.setTextColor(Color.RED);

        } else {
            textViewCountDownTimer.setTextColor(textColorOfButtons);
        }

        if (timeLeft == 0) {
            Toast.makeText(this, "Times Up!", Toast.LENGTH_SHORT).show();

            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    //Intent intent = new Intent(getApplication(), QuizActivity.class);
                    Log.d("TIMEUP", "run: " + timeLeft);
                    setQuestionView();
                }
            }, 1000);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (countDownTimer != null) {
            countDownTimer.cancel();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();

        if (countDownTimer != null) {
            countDownTimer.cancel();
        }
    }


    void setupUI() {
        textViewCorrect = findViewById(R.id.txtCorrect);
        textViewCountDownTimer = findViewById(R.id.txtTimer);
        textViewWrong = findViewById(R.id.txtWrong);
        textViewScore = findViewById(R.id.txtScore);
        textViewQuestionCount = findViewById(R.id.txtTotalQuestion);
        txtQuestion = findViewById(R.id.txtQuestionContainer);

        rbGroup = findViewById(R.id.radio_group);
        rb1 = findViewById(R.id.radio_button1);
        rb2 = findViewById(R.id.radio_button2);
        rb3 = findViewById(R.id.radio_button3);
        rb4 = findViewById(R.id.radio_button4);

        buttonNext = findViewById(R.id.button_Next);
    }

    private void resultData() {
        Intent resultOfQuiz = new Intent(QuizActivity.this, ResultActivity.class);
        resultOfQuiz.putExtra("myContentId", contentID);
        resultOfQuiz.putExtra("UserScore", score);
        resultOfQuiz.putExtra("TotalQuizQuestions", (questionTotalCount - 1));
        resultOfQuiz.putExtra("CorrectQuestions", correctAns);
        resultOfQuiz.putExtra("WrongQuestions", wrongAns);
        startActivity(resultOfQuiz);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
