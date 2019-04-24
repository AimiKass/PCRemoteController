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
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;

import com.example.pcremcont.R;
import com.example.pcremcont.portCommunicator.SendToServer;

public class SecondTabFragment extends Fragment
{
    View view;
    Switch switcherMonitorsOnOff;
    EditText editTextMinTillMonOff , editTextMinTillPCOff;
    Button shutdownBtn , restartBtn;
    SharedPreferences pref;
    SharedPreferences.Editor edit;

    private static final String KEY_FOR_PORT_NUMBER = "PORT_NUMBER";
    private static final String KEY_FOR_IP_ADDRESS = "IP_ADDRESS";
    private static final String KEY_FOR_SHARED_PREFERENCE = "MyPrefs";

    private static final String splitCharacter = "@";


    public SecondTabFragment()
    {

    }



    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        view = (View) inflater.inflate(R.layout.second_tab_fragment,container,false);

        init();
        return view;
    }

    private void init()
    {
        switcherMonitorsOnOff = view.findViewById(R.id.switchMonitorOnOff);
        editTextMinTillMonOff = view.findViewById(R.id.editTextMinTillMonOff);
        editTextMinTillPCOff = view.findViewById(R.id.editTextMinTillPCOff);
        shutdownBtn = view.findViewById(R.id.shutdownBtn);
        restartBtn = view.findViewById(R.id.restartBtn);

        pref = getContext().getSharedPreferences(KEY_FOR_SHARED_PREFERENCE, Context.MODE_PRIVATE);
    }

    @Override
    public void onStart()
    {
        super.onStart();

        switcherMonitorsOnOff.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
            {
                if (isChecked)
                    sendMessage("8"+splitCharacter);
                else
                    sendMessage("81"+splitCharacter);
            }
        });

        editTextMinTillPCOff.setOnKeyListener(new View.OnKeyListener()
        {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event)
            {
                if ((event.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER))
                {
                    sendMessage("12"+splitCharacter+(Integer.parseInt(editTextMinTillPCOff.getText().toString()) * 60));
                    editTextMinTillPCOff.getText().clear();
                    return true;
                }
                return false;
            }
        });


        editTextMinTillMonOff.setOnKeyListener(new View.OnKeyListener()
        {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event)
            {
                if ((event.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER) )
                {
                    sendMessage("9"+splitCharacter+Integer.parseInt((editTextMinTillMonOff.getText().toString()))*10000);
                    editTextMinTillMonOff.getText().clear();
                    return true;
                }
                return false;
            }
        });



        shutdownBtn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                sendMessage("10"+splitCharacter);
            }
        });

        restartBtn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                sendMessage("11"+splitCharacter);
            }
        });


    }


    private void sendMessage(String msg)
    {
        SendToServer messageSender = new SendToServer();
        messageSender.execute(msg,pref.getString(KEY_FOR_PORT_NUMBER, ""),pref.getString(KEY_FOR_IP_ADDRESS, ""));
    }
}
