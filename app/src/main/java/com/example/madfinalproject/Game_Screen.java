package com.example.madfinalproject;

import static android.content.ContentValues.TAG;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.style.BulletSpan;
import android.util.Log;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.nio.BufferUnderflowException;
import java.util.ArrayList;
import java.util.Objects;
import java.util.Random;

public class Game_Screen extends AppCompatActivity implements View.OnClickListener, SensorEventListener{

    //    Variables
    Button select;
    Button btnClickMe;
    Boolean sequence_showcase_over = false;
    int current_Number = 0;
    int Current_Button_Pressed_Index = 0;

    int Score  = 0;
    ArrayList<Integer> sequence = new ArrayList<>();

    ArrayList<Integer> Buttons_That_Have_Been_Pressed = new ArrayList<Integer>();

    private AlertDialog.Builder dialogBuilder;
    private AlertDialog dialog;

    // Game over popup
    private TextView tvGameOver;
    private Button btnReturn;
    private Button btnRetry;
    private Button btnSaveResult;


    // User Details pop up
    private EditText etUserName;
    private Button btnCancel;
    private Button btnSubmit;


    Button btnTop;
    Button btnRight;
    Button btnBottom;
    Button btnLeft;

    // Tilt Config

    private final double FORWARD = 9.0;
    private final double BACKWARDS = 6.0;

    private SensorManager mSensorManager;
    private Sensor mSensor;

    boolean setToZero = true;




    @Override
    protected  void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.game_screen);



        btnTop = findViewById(R.id.btnTop);
        btnTop.setEnabled(false);
        btnTop.setOnClickListener(this);

        btnRight = findViewById(R.id.btnRight);
        btnRight.setEnabled(false);
        btnRight.setOnClickListener(this);

        btnBottom = findViewById(R.id.btnBottom);
        btnBottom.setEnabled(false);
        btnBottom.setOnClickListener(this);

        btnLeft = findViewById(R.id.btnLeft);
        btnLeft.setEnabled(false);
        btnLeft.setOnClickListener(this);

        btnClickMe = findViewById(R.id.btnClickMe);
        btnRetry = findViewById(R.id.btnRetry);
        btnRetry.setEnabled(false);

        mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        mSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

        btnClickMe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sequence.clear();
                play_game();
            }
        });

        btnRetry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sequence.clear();
                btnRetry.setEnabled(false);
                play_game();
            }
        });
    }


    @Override
    public void onSensorChanged(SensorEvent event) {


        double x = event.values[0];
        int limit = 3;
        float y = event.values[1];


        if (sequence_showcase_over)
        {
            if (sequence.get(Current_Button_Pressed_Index) == 1 || sequence.get(Current_Button_Pressed_Index) == 3){
                if (isZero(x,8)){
                    setToZero = true;
                }
            }
            else   if (sequence.get(Current_Button_Pressed_Index) == 2 || sequence.get(Current_Button_Pressed_Index) == 0){
                if (isZero(y,8)){
                    setToZero = true;
                }
            }
        }



        if (sequence_showcase_over && setToZero){

            Log.v("limit", String.valueOf(isZero(x,8)));

            if (x < -limit){
                Cross_Check_Button_Pressed_With_Sequence(0);
                setToZero = false;
                Animate_Button(R.id.btnTop);

            }
            else if ( y > limit){
                Cross_Check_Button_Pressed_With_Sequence(1);
                setToZero = false;
                Animate_Button(R.id.btnRight);

            }
            else if(x > limit){
                Cross_Check_Button_Pressed_With_Sequence(2);
                setToZero = false;
                Animate_Button(R.id.btnBottom);

            }
            else if (y < -limit){
                Cross_Check_Button_Pressed_With_Sequence(3);
                setToZero = false;
                Animate_Button(R.id.btnLeft);

            }

      }

    }

    public static boolean isZero(double value, int places){
        if (places < 0) throw new IllegalArgumentException();
        long factor = (long) Math.pow(10, places);
        value = value * factor;
        long tmp = Math.round(value);
        return tmp/factor == 0;


    }



    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }


    /*
     * When the app is brought to the foreground - using app on screen
     */
    protected void onResume() {
        super.onResume();
        // turn on the sensor
        mSensorManager.registerListener((SensorEventListener) this, mSensor,
                SensorManager.SENSOR_DELAY_NORMAL);
    }

    /*
     * App running but not on screen - in the background
     */
    protected void onPause() {
        super.onPause();
        mSensorManager.unregisterListener((SensorListener) this);    // turn off listener to save power
    }

        @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View v) {
        if (sequence_showcase_over){

            switch (v.getId()){
                case R.id.btnTop:
//                    Animate_Button(R.id.btnTop);
                    Cross_Check_Button_Pressed_With_Sequence(0);
                    break;
                case R.id.btnRight:
//                    Animate_Button(R.id.btnRight);
                    Cross_Check_Button_Pressed_With_Sequence(1);
                    break;
                case R.id.btnBottom:
//                    Animate_Button(R.id.btnBottom);
                    Cross_Check_Button_Pressed_With_Sequence(2);
                    break;
                case R.id.btnLeft:
//                    Animate_Button(R.id.btnLeft);
                    Cross_Check_Button_Pressed_With_Sequence(3);
                    break;
            }
        }

    }

    private void play_game() {

        sequence_showcase_over = false;
        Generate_Sequence();
        Change_Game_Buttons();
        flashColour();
        btnTop.setEnabled(true);
        btnRight.setEnabled(true);
        btnBottom.setEnabled(true);
        btnLeft.setEnabled(true);
    }

    private void Change_Game_Buttons() { // changes the start game button (click me) to restart button
         if(btnClickMe.getVisibility() == View.VISIBLE){
            btnClickMe.setVisibility(View.GONE);
            btnRetry.setVisibility(View.VISIBLE);
        }
    }

    private void flashColour() {

        int buttonID = R.id.btnTop;
        // Get the length of the sequence
        int seq_length = sequence.size();

        // Assign a number to a button
            switch (sequence.get(current_Number)){
                case 0:
                    buttonID = R.id.btnTop;
                    break;
                case 1:
                    buttonID = R.id.btnRight;
                    break;
                case 2:
                    buttonID = R.id.btnBottom;
                    break;
                case 3:
                    buttonID = R.id.btnLeft;
                    break;
            }

        // Start the Animation for the selected button.
        select=(Button)findViewById(buttonID);
                Animation mAnimation = new AlphaAnimation(1, 0);
                mAnimation.setDuration(500);
                mAnimation.setInterpolator(new LinearInterpolator());
                mAnimation.setRepeatCount(Animation.INFINITE);
                mAnimation.setRepeatMode(Animation.REVERSE);
                select.startAnimation(mAnimation);
                new CountDownTimer(1000, 1000) {
                    public void onTick(long millisUntilFinished) {

                    }
                    public void onFinish() {

                        // Clear the animation
                        select.clearAnimation();

                        // If the next number is smaller than the sequence length increment current_number and call flashColour again
                        if (current_Number + 1 < seq_length){
                            current_Number++;
                            flashColour();
                        }
                        // else colour showcase is over and change showcase boolean to true so buttons can be clicked.
                        else{
                            sequence_showcase_over = true;
                            current_Number = 0;
                            Current_Button_Pressed_Index = 0;
                            btnRetry.setEnabled(true);
                            btnClickMe.setEnabled(true);
                        }
                    }

                }.start();
    }

    private void Cross_Check_Button_Pressed_With_Sequence(Integer buttonPressed) {


        if (!Objects.equals(buttonPressed, sequence.get(Current_Button_Pressed_Index))){
            Game_Over_Screen("Gave Over!");
            Current_Button_Pressed_Index = 0;
        }
        Score = Score + 20;
        Current_Button_Pressed_Index++;
        if(Current_Button_Pressed_Index == sequence.size())
        {
           play_game();
        }
    }

    private void Animate_Button(int buttonID){

        Log.v("message", "Animate_Button Method");

        // Start the Animation for the selected button.
        select=(Button)findViewById(buttonID);
        Animation mAnimation = new AlphaAnimation(1, 0);
        mAnimation.setDuration(250);
        mAnimation.setInterpolator(new LinearInterpolator());
        mAnimation.setRepeatCount(Animation.INFINITE);
        mAnimation.setRepeatMode(Animation.REVERSE);
        select.startAnimation(mAnimation);

        new CountDownTimer(500, 500) {
            public void onTick(long millisUntilFinished) {

                Log.v("ontick", "On Tick -------------------------------------");
            }
            public void onFinish() {
                select.clearAnimation();
            }
        }.start();
    }
    private void Generate_Sequence(){
        Random rnd = new Random();
        int upperBound = 4;
        int random_number = 0;
        //    Check if sequence is empty
        if (sequence.size() < 4 ){
            // if it's empty generate 4 random numbers between 1 & 4
            for (int i = 0; i < 4; i++) {
                // Generate random values from 0-4
                random_number = rnd.nextInt(upperBound);
                sequence.add(random_number);
            }
        }
        else{
            // If it's not generate 2 more random numbers and append to sequence list
            for (int i = 0; i < 2; i++) {
                // Generate random values from 0-4
                        random_number = rnd.nextInt(upperBound);
                sequence.add(random_number);
            }
        }




    }

    public void Game_Over_Screen(String message){

        mSensorManager.unregisterListener(this,mSensorManager.getDefaultSensor(mSensor.TYPE_ACCELEROMETER));

        dialogBuilder = new AlertDialog.Builder(this);
        final View Game_Over_Popup = getLayoutInflater().inflate(R.layout.popup, null);
        tvGameOver = (TextView) Game_Over_Popup.findViewById(R.id.popupTvGameOver);
        tvGameOver.setText(message);
        btnReturn = (Button) Game_Over_Popup.findViewById(R.id.btnReturn);
        btnSaveResult = (Button) Game_Over_Popup.findViewById(R.id.btnSaveResult);

        dialogBuilder.setView(Game_Over_Popup);
        dialog = dialogBuilder.create();
        dialog.show();

        btnReturn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Return Button
                dialog.dismiss();
                sequence.clear();
            }
        });
        final View Enter_Details_Popup = getLayoutInflater().inflate(R.layout.user_details_popup, null);
        etUserName = (EditText) Enter_Details_Popup.findViewById(R.id.etUserName);
        btnSubmit = (Button) Enter_Details_Popup.findViewById(R.id.btnSubmit);
        btnCancel = (Button) Enter_Details_Popup.findViewById(R.id.btnCancel);

        btnSaveResult.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                dialogBuilder.setView(Enter_Details_Popup);
                dialog = dialogBuilder.create();
                dialog.show();
            }
        });
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.v("Score", String.valueOf(Score));
                Intent i = new Intent(Game_Screen.this, HiScoreActivity.class);
                String UserName = etUserName.getText().toString();
                i.putExtra("UserName", UserName);
                i.putExtra("Score", Score);
                startActivity(i);
            }
        });
    }
}


