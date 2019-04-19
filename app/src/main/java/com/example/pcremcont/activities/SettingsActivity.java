package com.example.pcremcont.activities;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import com.example.pcremcont.R;
import com.example.pcremcont.portCommunicator.SendToServer;

public class SettingsActivity extends AppCompatActivity
{
    ImageButton returnBtn;
    Button closeServerBtn;

    private static final String splitCharacter = "@";


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_activity);

        returnBtn  = findViewById(R.id.setting_return_button);
        closeServerBtn = findViewById(R.id.close_server_btn);


        closeServerBtn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                sendMessage("0"+splitCharacter);
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
    }


    private void sendMessage(String msg)
    {
        SendToServer messageSender = new SendToServer();
        messageSender.execute(msg);
    }

}
