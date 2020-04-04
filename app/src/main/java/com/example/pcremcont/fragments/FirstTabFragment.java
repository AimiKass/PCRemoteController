package com.example.pcremcont.fragments;

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
import com.example.pcremcont.database.Database;
import com.example.pcremcont.portCommunicator.GetSystemVolume;
import com.example.pcremcont.portCommunicator.SendToServer;

import java.util.concurrent.ExecutionException;

public class FirstTabFragment extends Fragment
{

    View view;
    Button playPauseButton , previousButton , nextButton;
    SeekBar seekBar;
    Switch muteUnmute;
    EditText editTextSpeak;
    Database database;


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
        database = new Database(getContext());

    }


    @Override
    public void onStart()
    {

        super.onStart();


        setSeekBarProgress();

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener()
        {

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser)
            {
                // TODO: 3/23/2020 change somehow the progress frequency in order to prevent crashes of frequent requests or copy paste the code to onStopTrackingTouch function
                sendMessage("2"+splitCharacter+(65535 * progress)/100);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar)
            {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar)
            {
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

    /**
     * initialize the seekBar by getting the volume status from the server
     */
    public void setSeekBarProgress()
    {
        // TODO: 3/18/2020 check the refresh ratio . It is changing every two fragments.
        GetSystemVolume systemVolume = new GetSystemVolume();

        try {
            seekBar.setProgress(systemVolume.execute("18",database.getPort(),database.getIP()).get());
        } catch (ExecutionException | InterruptedException e)
        {
            seekBar.setProgress(0);
        }
    }


    private void sendMessage(String msg)
    {
        SendToServer messageSender = new SendToServer();
        messageSender.execute(msg,database.getPort(),database.getIP());
    }
}