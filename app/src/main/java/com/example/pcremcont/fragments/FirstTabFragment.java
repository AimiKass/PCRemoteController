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
import com.example.pcremcont.portCommunicator.SendToServer;

public class FirstTabFragment extends Fragment
{

    View view;
    Button playPauseButton , previousButton , nextButton;
    SeekBar seekBar;
    Switch muteUnmute;
    EditText editTextSpeak;

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
        messageSender.execute(msg);
    }

}
