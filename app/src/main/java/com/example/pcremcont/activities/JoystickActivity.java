package com.example.pcremcont.activities;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.MultiAutoCompleteTextView;

import com.example.pcremcont.R;
import com.example.pcremcont.portCommunicator.SendToServer;

import io.github.controlwear.virtual.joystick.android.JoystickView;

public class JoystickActivity extends AppCompatActivity
{

    // TODO: 4/18/2019 add scroll wheel
    Button leftClickBtn , rightClickButton;
    EditText keyboard;

    private static final String splitCharacter = "@";



    
    private void init()
    {
        leftClickBtn = findViewById(R.id.joystick_left_btn);
        rightClickButton = findViewById(R.id.joystick_right_btn);
        keyboard = findViewById(R.id.keyboard);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.joystick);

        init();

        leftClickBtn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                sendMessage("15"+splitCharacter);
            }
        });

        rightClickButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                sendMessage("14"+splitCharacter);
            }
        });

        final JoystickView joystickRight = (JoystickView) findViewById(R.id.joystickView_right);
        joystickRight.setOnMoveListener(new JoystickView.OnMoveListener()
        {
            @SuppressLint("DefaultLocale")
            @Override
            public void onMove(int angle, int strength)
            {
                sendMessage("1"+splitCharacter+ (joystickRight.getNormalizedX() - 50) +"#"+ (joystickRight.getNormalizedY() - 50));

            }
        });


        // TODO: 4/19/2019 backspace error and space too
        // TODO: 4/19/2019 https://docs.microsoft.com/en-us/windows/desktop/inputdev/virtual-key-codes

        keyboard.addTextChangedListener(new TextWatcher()
        {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after)
            {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count)
            {
                if (count != -1)
                    if (count != 0)
                        if (s.charAt(s.length()-1) == ' ')
                            sendMessage("17"+splitCharacter+"0x20");
                        else
                            sendMessage("17"+splitCharacter+s.charAt(s.length()-1));

            }

            @Override
            public void afterTextChanged(Editable s)
            {
            }
        });

        keyboard.setOnKeyListener(new View.OnKeyListener()
        {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event)
            {
                if (keyCode == KeyEvent.KEYCODE_DEL)
                {
                    sendMessage("17"+splitCharacter+"0x08");
                    System.out.println("exe");
                }
                    return false;
            }
        });

    }



    private void sendMessage(String msg)
    {
        SendToServer messageSender = new SendToServer();
        messageSender.execute(msg);
    }
}