package com.example.pcremcont.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.Switch;

import com.example.pcremcont.R;
import com.example.pcremcont.portCommunicator.SendToServer;

public class FirstTabFragment extends Fragment
{

    View view;
    Button playPauseButton , previousButton , nextButton;
    SeekBar seekBar;
    Switch muteUnmute;
    EditText editTextSpeak;
    SharedPreferences pref;
    SharedPreferences.Editor edit;

    private static final String KEY_FOR_PORT_NUMBER = "PORT_NUMBER";
    private static final String KEY_FOR_IP_ADDRESS = "IP_ADDRESS";
    private static final String KEY_FOR_SHARED_PREFERENCE = "MyPrefs";
    private static final String splitCharacter = "@";


    public FirstTabFragment()
    {

    }



    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        view = (View) inflater.inflate(R.layout.first_tab_fragment,container,false);

        init();
        return view;
    }

    private void init()
    {
        playPauseButton = view.findViewById(R.id.playPauseBtn);
        previousButton = view.findViewById(R.id.previousBtn);
        nextButton = view.findViewById(R.id.nextBtn);
        seekBar = view.findViewById(R.id.seekBar);
        muteUnmute = view.findViewById(R.id.muteUnmute);
        editTextSpeak = view.findViewById(R.id.editTextSpeak);

        pref = getContext().getSharedPreferences(KEY_FOR_SHARED_PREFERENCE, Context.MODE_PRIVATE);
    }


    @Override
    public void onStart()
    {
        super.onStart();


        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener()
        {

            int progressValue;


            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser)
            {
                progressValue = progress;
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar)
            {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar)
            {
                sendMessage("2"+splitCharacter+(65535 * progressValue)/100);
            }
        });


        muteUnmute.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                sendMessage("3"+splitCharacter);
            }
        });

        playPauseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendMessage("4"+splitCharacter);
            }
        });

        nextButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                sendMessage("6"+splitCharacter);
            }
        });

        previousButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                sendMessage("5"+splitCharacter);
            }
        });

        editTextSpeak.setOnKeyListener(new View.OnKeyListener()
        {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event)
            {
                if ((event.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER))
                {
                    sendMessage("7"+splitCharacter+editTextSpeak.getText().toString());
                    return true;
                }
                return false;
            }
        });

    }




    private void sendMessage(String msg)
    {
        SendToServer messageSender = new SendToServer();
        messageSender.execute(msg,pref.getString(KEY_FOR_PORT_NUMBER, ""),pref.getString(KEY_FOR_IP_ADDRESS, ""));
    }

}
