package com.example.pcremcont.activities;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.example.pcremcont.R;
import com.example.pcremcont.portCommunicator.SendToServer;

import io.github.controlwear.virtual.joystick.android.JoystickView;

public class JoystickActivity extends AppCompatActivity
{


    Button leftClickBtn , rightClickButton;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.joystick);

        leftClickBtn = findViewById(R.id.joystick_left_btn);
        rightClickButton = findViewById(R.id.joystick_right_btn);


        leftClickBtn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                sendMessage("15@");
            }
        });

        rightClickButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                sendMessage("14@");
            }
        });

        final JoystickView joystickRight = (JoystickView) findViewById(R.id.joystickView_right);
        joystickRight.setOnMoveListener(new JoystickView.OnMoveListener()
        {
            @SuppressLint("DefaultLocale")
            @Override
            public void onMove(int angle, int strength)
            {




                sendMessage("1@"+String.valueOf(joystickRight.getNormalizedX()-50)+"#"+String.valueOf(joystickRight.getNormalizedY()-50));


            }
        });
    }



    private void sendMessage(String msg)
    {
        SendToServer messageSender = new SendToServer();
        messageSender.execute(msg);
    }
}