package com.bilir.mathmania;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.Random;

public class Game extends AppCompatActivity {
    public char mode;

    TextView score, life, time, question;

    EditText answer;

    Button check, next;

    int _score = 0, _life = 3, _time = 30;
    Random random = new Random();

    int number1, number2, resultToCheck;

    CountDownTimer timer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        blueBar();

        Intent prevIntent = getIntent();
        mode = prevIntent.getCharExtra("mode", '+');

        score = findViewById(R.id.score);
        life = findViewById(R.id.life);
        time = findViewById(R.id.time);
        question = findViewById(R.id.question);
        answer = findViewById(R.id.editTextNumber);

        check = findViewById(R.id.button);
        next = findViewById(R.id.button2);

        score.setText("Score: " + _score);
        life.setText("Life: " + _life);
        time.setText("Time: " + _time);


        check.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkQuestion();

            }
        });

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                generateQuestion();
            }
        });


        timer  = new CountDownTimer(31000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                time.setText("Time: " + millisUntilFinished/1000);
            }

            @Override
            public void onFinish() {
                question.setTextColor(Color.RED);
                question.setText(question.getText() + "\nTIME'S UP!");
                answer.setText("");
                life.setText("Life: " + --_life);
                next.setEnabled(true);
                check.setEnabled(false);
                answer.setEnabled(false);

                if(_life == 0) {
                    next.setText("Finish");
                }

            }
        };

        generateQuestion();
    }

    void generateQuestion() {
        if(_life != 0) {
            switch (mode) {
                case '+':
                    number1 = random.nextInt(101);
                    number2 = random.nextInt(101);
                    resultToCheck = number1 + number2;
                    break;
                case '-':
                    number1 = random.nextInt(101);
                    number2 = random.nextInt(number1);
                    resultToCheck = number1 - number2;
                    break;
                case '*':
                    number1 = random.nextInt(21);
                    number2 = random.nextInt(10) + 1;
                    resultToCheck = number1 * number2;
                    break;
            }
            next.setEnabled(false);
            check.setEnabled(true);

            question.setTextColor(Color.BLACK);
            question.setText(number1 + " " + mode + " " + number2);
            answer.setEnabled(true);

            answer.requestFocus();
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);

            answer.setText("");
            timer.start();
        } else {
            // Game over commands here
            //_score
            answer.setEnabled(false);
            Intent intent = new Intent(Game.this, GameOver.class);
            intent.putExtra("finalScore", _score);

            startActivity(intent);
            finish(); // Removing from the backstack
        }
    }

    void checkQuestion() {
        String ans = answer.getText().toString();
        if(!ans.isEmpty() && Integer.parseInt(ans) == resultToCheck) {
            next.setEnabled(true);
            check.setEnabled(false);
            timer.cancel();

            question.setText(question.getText() + " = " + resultToCheck +"\nCORRECT!");
            answer.setEnabled(false);
            question.setTextColor(getResources().getColor(android.R.color.holo_green_dark));

            _score += 10;
            score.setText("Score: " + _score);

        } else if (!ans.isEmpty()){
            question.setTextColor(Color.RED);
            answer.setText("");
            life.setText("Life: " + --_life);

            timer.cancel();
            timer.start();

            if(_life == 0) {
                next.setText("Finish");
                next.setEnabled(true);
                check.setEnabled(false);
                answer.setEnabled(false);
            }
        }

    }

    void blueBar() {
        Window window = getWindow();
        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT_WATCH) {
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(getResources().getColor(R.color.blue));
        }
    }
}