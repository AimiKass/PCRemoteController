package com.example.pcremcont.activities;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import com.example.pcremcont.R;
import com.example.pcremcont.secondaryFunctions.Vibration;
import com.example.pcremcont.database.Database;
import com.example.pcremcont.portCommunicator.SendToServer;

public class SettingsActivity extends AppCompatActivity
{
    ImageButton returnBtn;
    Button closeServerBtn, saveBtn;
    EditText ipEditText, portEditText;
    Database database;


    private static final String splitCharacter = "@";



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_activity);

        init();

        portEditText.setText(database.getPort());
        ipEditText.setText(database.getIP());



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
                // TODO: 4/23/2019 #1 check validity and close the activity if everything is ok
                database.setIP(ipEditText.getText().toString());
                database.setPort(portEditText.getText().toString());

            }
        });
    }

    private void init()
    {
        database = new Database(getApplicationContext());

        returnBtn  = findViewById(R.id.setting_return_button);
        closeServerBtn = findViewById(R.id.close_server_btn);
        ipEditText = findViewById(R.id.ipEditText);
        portEditText = findViewById(R.id.portEditText);
        saveBtn = findViewById(R.id.saveBtn);

    }


    private void sendMessage(String msg)
    {
        SendToServer messageSender = new SendToServer();
        messageSender.execute(msg,database.getPort(),database.getIP());
    }

}
