package com.example.u.sfields;

import android.app.Activity;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.media.AudioManager;
import android.media.ToneGenerator;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Objects;

/**
 * Created by U on 1/30/2018.
 */

public class DemoTestActivity extends Activity {
    private String ip;
    private String ip_port;
    private String testForEye;
    private String tMsg;
    private static final String MY_PREFS_NAME = "METADATA_PS_OPERATOR";
    boolean isThreadRunning = false;
    boolean click = false;
    Thread cycleThread;
    int score_i = 0;
    int x,y;
    // region canvas
    Paint stdPaint = new Paint();
    Bitmap stdBmp,copy_stdBmp;
    Canvas stdCanvas,circleCanvas;
    // endregion
    // region degree
    int[] coordinate = new int[2];
    int loc_3_degree;
    int loc_9_degree;
    int loc_15_degree;
    int loc_21_degree;
    int loc_27_degree;
    private static  int blindSpot_x = 280;
    private static  int blindSpot_y = 30;
    // endregion
    private ImageView feedbackImageView;
    int array[] = {6,9,14,15,20,35,40,41,46,49,1};
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_threshold);

        feedbackImageView = (ImageView) findViewById(R.id.testingActImageView);
        SharedPreferences prefs = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
        String restoredText1 = prefs.getString("eye", "left");
        String restoredText2 = prefs.getString("ip", "192.168.43.83");
        testForEye = restoredText1;
        ip = restoredText2;
        ip_port = ip+":5000";


        stdPaint.setColor(Color.BLACK);
        stdPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        stdBmp = Bitmap.createBitmap(1200,1200, Bitmap.Config.ARGB_8888);
        stdCanvas = new Canvas(stdBmp);

        drawLinesAndDots();
    }

    private void drawLinesAndDots() {
        // region Initializing 'look' of image view
        // initializing look of image view

        /*
        *       Algo :
        *
        *           1 - Show axis
        *           2 - Make cuts for angle - 10 | 20 | 30 - resized as per screen
        *           3 - show solid small dots are corresponding point locations
        *           4 - Show blind spot
        *           5 - Set bitmap on image view
        * */
        // region Step 1|2 : Making rectangles and cuts on axis

        int width = 4;

        float left = 600-width/2;
        float top = 0;
        float right = left+width;
        float bottom  = 1200;

        stdCanvas.drawRect(left,top,right,bottom,stdPaint);
        left = 0;
        top = 600-width/2;
        right = 1200;
        bottom = top+width;
        stdCanvas.drawRect(left,top,right,bottom,stdPaint);

        int distancePoint_10degree = (int) 183.33;
        int distancePoint_20degree = (int) (183.33*2);
        int distancePoint_30degree = (int) (183.33*3);

        left = 600+distancePoint_10degree-width;
        top = 600-3*width;
        right = left+width;
        bottom = top+6*width;
        stdCanvas.drawRect(left,top,right,bottom,stdPaint);

        left = 600+distancePoint_20degree-width;
        top = 600-3*width;
        right = left+width;
        bottom = top+6*width;
        stdCanvas.drawRect(left,top,right,bottom,stdPaint);

        left = 600+distancePoint_30degree-width;
        top = 600-3*width;
        right = left+width;
        bottom = top+6*width;
        stdCanvas.drawRect(left,top,right,bottom,stdPaint);

        left = 600-distancePoint_10degree-width;
        top = 600-3*width;
        right = left+width;
        bottom = top+6*width;
        stdCanvas.drawRect(left,top,right,bottom,stdPaint);


        left = 600-distancePoint_20degree-width;
        top = 600-3*width;
        right = left+width;
        bottom = top+6*width;
        stdCanvas.drawRect(left,top,right,bottom,stdPaint);


        left = 600-distancePoint_30degree-width;
        top = 600-3*width;
        right = left+width;
        bottom = top+6*width;
        stdCanvas.drawRect(left,top,right,bottom,stdPaint);
        // endregion

        // region Step 3 :  Solid small dots


        for(int i = 1;i<=54;i++){

            int coor[] = getCoordinates(i,testForEye,0);
            stdCanvas.drawCircle(coor[0]+600,coor[1]+600,4,stdPaint);

        }

        // endregion

        // region Step 4 : Blind Spot

        int loc_12_degree = (int) 18.33*12;
        int loc_15_degree = (int) 18.33*15;
        int loc_5_degree = (int) 18.33*5;
        int loc_1_5_degree = (int) ((int) 18.33*1.5);


        // making triangle
        stdPaint.setStrokeWidth(5);
        if(testForEye == "right") {
            stdCanvas.drawLine(600 + loc_12_degree, 600 + loc_1_5_degree, 600 + loc_15_degree, 600 + loc_1_5_degree, stdPaint);
            stdCanvas.drawLine(600 + loc_12_degree, 600 + loc_1_5_degree, 600 + loc_12_degree + 27, 600 + loc_5_degree, stdPaint);
            stdCanvas.drawLine(600 + loc_15_degree, 600 + loc_1_5_degree, 600 + loc_12_degree + 27, 600 + loc_5_degree, stdPaint);
        } else{
            stdCanvas.drawLine(600 - loc_12_degree, 600 + loc_1_5_degree, 600 - loc_15_degree, 600 + loc_1_5_degree, stdPaint);
            stdCanvas.drawLine(600 - loc_12_degree, 600 + loc_1_5_degree, 600 - loc_12_degree - 27, 600 + loc_5_degree, stdPaint);
            stdCanvas.drawLine(600 - loc_15_degree, 600 + loc_1_5_degree, 600 - loc_12_degree - 27, 600 + loc_5_degree, stdPaint);

        }
        // endregion

        feedbackImageView.setImageBitmap(stdBmp);



        // endregion
    }



    // region Function : Get coordinates corresponding to point number
    public int[] getCoordinates (int pointNumber, String eye,int operatorORPatient){

        if(operatorORPatient==0) {
            loc_3_degree = 55;
            loc_9_degree = 165;
            loc_15_degree = 275;
            loc_21_degree = 385;
            loc_27_degree = 495;
        } else{
            loc_3_degree = 60;
            loc_9_degree = 182;
            loc_15_degree = 305;
            loc_21_degree = 430;
            loc_27_degree = 550;
        }


        switch (pointNumber) {
            case 0:
                coordinate[0] = 2000;
                coordinate[1] = 2000;
                break;
            case 1:
                coordinate[0] = -loc_9_degree;
                coordinate[1] = loc_21_degree;
                break;

            case 2:
                coordinate[0] = -loc_3_degree;
                coordinate[1] = loc_21_degree;
                break;
            case 3:
                coordinate[0] = loc_3_degree;
                coordinate[1] = loc_21_degree;
                break;
            case 4:
                coordinate[0] = loc_9_degree;
                coordinate[1] = loc_21_degree;
                break;

            case 5:
                coordinate[0] = -loc_15_degree;
                coordinate[1] = loc_15_degree;
                break;
            case 6:
                coordinate[0] = -loc_9_degree;
                coordinate[1] = loc_15_degree;
                break;
            case 7:
                coordinate[0] = -loc_3_degree;
                coordinate[1] = loc_15_degree;
                break;
            case 8:
                coordinate[0] = loc_3_degree;
                coordinate[1] = loc_15_degree;
                break;
            case 9:
                coordinate[0] = loc_9_degree;
                coordinate[1] = loc_15_degree;
                break;
            case 10:
                coordinate[0] = loc_15_degree;
                coordinate[1] = loc_15_degree;
                break;

            case 11:
                coordinate[0] = -loc_21_degree;
                coordinate[1] = loc_9_degree;
                break;
            case 12:
                coordinate[0] = -loc_15_degree;
                coordinate[1] = loc_9_degree;
                break;
            case 13:
                coordinate[0] = -loc_9_degree;
                coordinate[1] = loc_9_degree;
                break;
            case 14:
                coordinate[0] = -loc_3_degree;
                coordinate[1] = loc_9_degree;
                break;
            case 15:
                coordinate[0] = loc_3_degree;
                coordinate[1] = loc_9_degree;
                break;
            case 16:
                coordinate[0] = loc_9_degree;
                coordinate[1] = loc_9_degree;
                break;
            case 17:
                coordinate[0] = loc_15_degree;
                coordinate[1] = loc_9_degree;
                break;
            case 18:
                coordinate[0] = loc_21_degree;
                coordinate[1] = loc_9_degree;
                break;
            case 19:
                if(eye =="right"){
                    coordinate[0] = -loc_27_degree;
                    coordinate[1] = loc_3_degree;
//                        Log.d("LOG_DEBUG","Entered case 19 - R");
                }
                else {
                    coordinate[0] = loc_27_degree;
                    coordinate[1] = loc_3_degree;
//                        Log.d("LOG_DEBUG","Entered case 19- L");
                }
//                    Log.d("LOG_DEBUG","Entered case 19");
                break;
            case 20:
                coordinate[0] = -loc_21_degree;
                coordinate[1] = loc_3_degree;
                break;
            case 21:
                coordinate[0] = -loc_15_degree;
                coordinate[1] = loc_3_degree;
                break;
            case 22:
                coordinate[0] = -loc_9_degree;
                coordinate[1] = loc_3_degree;
                break;
            case 23:
                coordinate[0] = -loc_3_degree;
                coordinate[1] = loc_3_degree;
                break;
            case 24:
                coordinate[0] = loc_3_degree;
                coordinate[1] = loc_3_degree;
                break;
            case 25:
                coordinate[0] = loc_9_degree;
                coordinate[1] = loc_3_degree;
                break;
            case 26:
                coordinate[0] = loc_15_degree;
                coordinate[1] = loc_3_degree;
                break;
            case 27:
                coordinate[0] = loc_21_degree;
                coordinate[1] = loc_3_degree;
                break;
            case 28:
                if(eye=="right"){
                    coordinate[0] = -loc_27_degree;
                    coordinate[1] = -loc_3_degree;
//                        Log.d("LOG_DEBUG","Entered case 28 - R");

                }
                else {
                    coordinate[0] = loc_27_degree;
                    coordinate[1] = -loc_3_degree;
//                        Log.d("LOG_DEBUG","Entered case 28 - L");

                }
//                    Log.d("LOG_DEBUG","Entered case 28");
                break;
            case 29:
                coordinate[0] = -loc_21_degree;
                coordinate[1] = -loc_3_degree;
                break;
            case 30:
                coordinate[0] = -loc_15_degree;
                coordinate[1] = -loc_3_degree;
                break;
            case 31:
                coordinate[0] = -loc_9_degree;
                coordinate[1] = -loc_3_degree;
                break;
            case 32:
                coordinate[0] = -loc_3_degree;
                coordinate[1] = -loc_3_degree;
                break;
            case 33:
                coordinate[0] = loc_3_degree;
                coordinate[1] = -loc_3_degree;
                break;
            case 34:
                coordinate[0] = loc_9_degree;
                coordinate[1] = -loc_3_degree;
                break;
            case 35:
                coordinate[0] = loc_15_degree;
                coordinate[1] = -loc_3_degree;
                break;
            case 36:
                coordinate[0] = loc_21_degree;
                coordinate[1] = -loc_3_degree;
                break;
            case 37:
                coordinate[0] = -loc_21_degree;
                coordinate[1] = -loc_9_degree;
                break;
            case 38:
                coordinate[0] = -loc_15_degree;
                coordinate[1] = -loc_9_degree;
                break;
            case 39:
                coordinate[0] = -loc_9_degree;
                coordinate[1] = -loc_9_degree;
                break;
            case 40:
                coordinate[0] = -loc_3_degree;
                coordinate[1] = -loc_9_degree;
                break;
            case 41:
                coordinate[0] = loc_3_degree;
                coordinate[1] = -loc_9_degree;
                break;
            case 42:
                coordinate[0] = loc_9_degree;
                coordinate[1] = -loc_9_degree;
                break;
            case 43:
                coordinate[0] = loc_15_degree;
                coordinate[1] = -loc_9_degree;
                break;
            case 44:
                coordinate[0] = loc_21_degree;
                coordinate[1] = -loc_9_degree;
                break;
            case 45:
                coordinate[0] = -loc_15_degree;
                coordinate[1] = -loc_15_degree;
                break;
            case 46:
                coordinate[0] = -loc_9_degree;
                coordinate[1] = -loc_15_degree;
                break;
            case 47:
                coordinate[0] = -loc_3_degree;
                coordinate[1] = -loc_15_degree;
                break;
            case 48:
                coordinate[0] = loc_3_degree;
                coordinate[1] = -loc_15_degree;
                break;
            case 49:
                coordinate[0] = loc_9_degree;
                coordinate[1] = -loc_15_degree;
                break;
            case 50:
                coordinate[0] = loc_15_degree;
                coordinate[1] = -loc_15_degree;
                break;
            case 51:
                coordinate[0] = -loc_9_degree;
                coordinate[1] = -loc_21_degree;
                break;
            case 52:
                coordinate[0] = -loc_3_degree;
                coordinate[1] = -loc_21_degree;
                break;
            case 53:
                coordinate[0] = loc_3_degree;
                coordinate[1] = -loc_21_degree;
                break;
            case 54:
                coordinate[0] = loc_9_degree;
                coordinate[1] = -loc_21_degree;
                break;

            // adding point location for blind spot

            case 55:
                showBlinkSpot();
                break;
            case 56:
                showBlinkSpot();
                break;
            case 57:
                showBlinkSpot();
                break;
            case 58 :
                showBlinkSpot();
                break;
            case 59:
                showBlinkSpot();
                break;
            case 60 :
                coordinate[0] = 2000;
                coordinate[1] = 2000;
                break;
            case 61 :
                coordinate[0] = 2000;
                coordinate[1] = 2000;
                break;
            case 62 :
                coordinate[0] = 2000;
                coordinate[1] = 2000;
                break;
            case 63 :
                coordinate[0] = 2000;
                coordinate[1] = 2000;
                break;
            case 64 :
                coordinate[0] = 2000;
                coordinate[1] = 2000;
                break;

        }

        return coordinate;
    }

    private void showBlinkSpot() {
        if (testForEye == "right") {
            coordinate[0]  = blindSpot_x;
            coordinate[1] = blindSpot_y;
        } else{
            coordinate[0]  = -1 * blindSpot_x;
            coordinate[1] = blindSpot_y;
        }
    }


    // endregion
    public void onStartClick(View view) {
        Toast.makeText(this, "Start", Toast.LENGTH_SHORT).show();
        if(!isThreadRunning) {
            isThreadRunning = true;
            StartThread();                   // <--------------------- START THREAD ----------------
        }else{
            Toast.makeText(this, "Thread is already running!!", Toast.LENGTH_SHORT).show();
        }

    }

    private void StartThread() {
        cycleThread = new Thread() {
            @Override
            public void run() {
                while (isThreadRunning && score_i<11) {
                    x = getCoordinates(array[score_i] , testForEye, 0)[0] +600;
                    y = getCoordinates(array[score_i] , testForEye, 0)[1] +600;
                    if(Objects.equals(testForEye, "right")) {
                        tMsg = "," + (((x) / 3) + 400) + "," + ((y) / 3);
                    }else{
                        tMsg = "," + ((x) / 3) + "," + ((y) / 3);
                    }

                    try {
                        MyClientTask myClientTask = new MyClientTask("192.168.43.83", Integer.parseInt("5204"), tMsg);
                        myClientTask.execute();
                    } catch (Exception e) {

                    }


                    click = false;

                    // making temporary bmp
                    final Bitmap tempBmp = stdBmp.copy(stdBmp.getConfig(), true);
                    final Canvas tempCanvas = new Canvas(tempBmp);
                    circleCanvas = new Canvas(stdBmp);
                    tempCanvas.drawCircle(x, y , 30, stdPaint);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Log.d("LOG_TAG2","Showing Point : "+score_i);
                            feedbackImageView.setImageBitmap(tempBmp);

                        }
                    });

                    try {
                        Thread.sleep(1300);
                    } catch (InterruptedException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            feedbackImageView.setImageBitmap(stdBmp);
                        }
                    });

                    try {
                        Thread.sleep(300);
                    } catch (InterruptedException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }



                    if (click) {
                        stdPaint.setStyle(Paint.Style.STROKE);
                        circleCanvas.drawCircle(x, y, 15, stdPaint);

                        stdPaint.setStyle(Paint.Style.FILL_AND_STROKE);

                        click = false;

                    } else {
                        click = false;

                    }


                    score_i++;
                }

            }
        };
        cycleThread.start();
        score_i = 0;
    }

    public void onPauseClick(View view) {
        isThreadRunning = false;
    }
    public void onStopClick(View view) {
        if(!isThreadRunning) {
            finish();
        }
    }

    //region BroadcastReceiver : Receiving Click from bluetooth

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(true) {
            if (keyCode == 97) {
                Log.i("custom", "Button 2");

            } else if (keyCode == 96) {
                Log.i("custom", "Button 1");
            } else if (keyCode == 25) {
                Log.i("custom", "Joystick is down");

            } else if (keyCode == 4 || keyCode == 24 || keyCode == 92 || keyCode == 93) {
                if (!click) {
                    // if click is false
                    click = true;

                    // playing sound
                    playSound();
                }
            } else if (keyCode == 21 || keyCode == 88) {
                Log.i("custom", "Joystick is left");
            } else if (keyCode == 22 || keyCode == 87) {
                Log.i("custom", "Joystick is right");


            } else if (keyCode == 85) {
                Log.i("custom", "Button A");
            }

        }
        return true;

    }

    //endregion

    public void playSound() {
        ToneGenerator toneGenerator = new ToneGenerator(AudioManager.STREAM_MUSIC,100);
        toneGenerator.startTone(3);
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        toneGenerator.stopTone();
        toneGenerator.release();

    }



    public class MyClientTask extends AsyncTask<Void, Void, Void> {

        String dstAddress;
        int dstPort;
        String response = "";
        String msgToServer;

        MyClientTask(String addr, int port, String msgTo) {
            dstAddress = addr;
            dstPort = port;
            msgToServer = msgTo;
        }

        @Override
        protected Void doInBackground(Void... arg0) {

            Socket socket = null;
            DataOutputStream dataOutputStream = null;
            DataInputStream dataInputStream = null;

            try {
                Log.d("teg_try","Try--**********************----------------***********************--------------******");

                socket = new Socket(dstAddress, dstPort);
                dataOutputStream = new DataOutputStream(socket.getOutputStream());
                dataInputStream = new DataInputStream(socket.getInputStream());

                if(msgToServer != null){
                    dataOutputStream.writeUTF(msgToServer);
                }

//                response = dataInputStream.readUTF();

            } catch (UnknownHostException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                response = "UnknownHostException: " + e.toString();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                response = "IOException: " + e.toString();
            } finally {
                Log.d("teg_finally","Finally--**********************----------------***********************--------------******");

                if (socket != null) {
                    try {
                        socket.close();
                    } catch (IOException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }

                if (dataOutputStream != null) {
                    try {
                        dataOutputStream.close();
                    } catch (IOException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }

                if (dataInputStream != null) {
                    try {
                        dataInputStream.close();
                    } catch (IOException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
        }

    }
}
