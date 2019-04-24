package com.example.pcremcont.activities;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import com.example.pcremcont.R;
import com.example.pcremcont.SecondaryFunctions.Vibration;
import com.example.pcremcont.portCommunicator.SendToServer;

public class SettingsActivity extends AppCompatActivity
{
    ImageButton returnBtn;
    Button closeServerBtn, saveBtn;
    EditText ipEditText, portEditText;
    SharedPreferences pref;
    SharedPreferences.Editor edit;


    private static final String splitCharacter = "@";
    private static final String KEY_FOR_PORT_NUMBER = "PORT_NUMBER";
    private static final String KEY_FOR_IP_ADDRESS = "IP_ADDRESS";
    private static final String KEY_FOR_SHARED_PREFERENCE = "MyPrefs";



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_activity);

        init();

        portEditText.setText(pref.getString(KEY_FOR_PORT_NUMBER, ""));
        ipEditText.setText(pref.getString(KEY_FOR_IP_ADDRESS, ""));



        closeServerBtn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                sendMessage("0"+splitCharacter);
                Vibration vibration = new Vibration();
                vibration.effect(new long[]{0, 100, 0}, new int[]{0, 50, 0},getSystemService(Context.VIBRATOR_SERVICE));
            }
        });

        returnBtn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                finish();
            }
        });

        saveBtn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                // TODO: 4/23/2019 #1 check validity !!!!!
                edit.putString(KEY_FOR_PORT_NUMBER, portEditText.getText().toString());
                edit.putString(KEY_FOR_IP_ADDRESS, ipEditText.getText().toString());
                edit.apply();

            }
        });
    }

    private void init()
    {
        pref = getSharedPreferences(KEY_FOR_SHARED_PREFERENCE, Context.MODE_PRIVATE);
        edit = pref.edit();

        returnBtn  = findViewById(R.id.setting_return_button);
        closeServerBtn = findViewById(R.id.close_server_btn);
        ipEditText = findViewById(R.id.ipEditText);
        portEditText = findViewById(R.id.portEditText);
        saveBtn = findViewById(R.id.saveBtn);

    }


    private void sendMessage(String msg)
    {
        SendToServer messageSender = new SendToServer();
        System.out.println(msg);
        messageSender.execute(msg,pref.getString(KEY_FOR_PORT_NUMBER, ""),pref.getString(KEY_FOR_IP_ADDRESS, ""));
    }

}
