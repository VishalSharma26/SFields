package com.example.u.sfields;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.Toast;

public class ChooseTestActivity extends AppCompatActivity {

    private static final String MY_PREFS_NAME = "METADATA_PS_OPERATOR";
    SharedPreferences.Editor editor;
    private Switch switchIp;

    private String IP1 = "192.168.43.83";
    private String IP2 = "192.168.43.253";
    private String eye_right = "right";
    private String eye_left = "left";

    private String mainIp = "192.168.43.83";
    private String eye = "left";

    private Button btnMainTest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_test);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        switchIp = (Switch) findViewById(R.id.switchIp);

        btnMainTest = (Button) findViewById(R.id.btnMainTest);

        switchIp.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if (isChecked) {
                    mainIp = IP2;
                    eye = eye_right;
                    Toast.makeText(ChooseTestActivity.this, "right", Toast.LENGTH_SHORT).show();
                } else {
                    mainIp = IP1;
                    eye = eye_left;

                    Toast.makeText(ChooseTestActivity.this, "left", Toast.LENGTH_SHORT).show();
                }

            }
        });

        btnMainTest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editor = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE).edit();
                editor.putString("ip", mainIp);
                editor.putString("eye", eye);
                editor.apply();

                startActivity(new Intent(ChooseTestActivity.this, ThresholdActivity.class));

            }
        });


    }

    public void onBlindSpotTestButton(View view) {
        startActivity(new Intent(ChooseTestActivity.this,BlindSpotTestActivity.class));
    }

    public void onDemoTest(View view) {
        startActivity(new Intent(ChooseTestActivity.this,DemoTestActivity.class));
    }
}
