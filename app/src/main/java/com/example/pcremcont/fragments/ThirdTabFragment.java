package com.example.pcremcont.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.pcremcont.R;
import com.example.pcremcont.portCommunicator.SendToServer;

public class ThirdTabFragment extends Fragment
{
    View view;

    public ThirdTabFragment()
    {

    }



    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        view = (View) inflater.inflate(R.layout.third_tab_fragment,container,false);

        init();
        return view;
    }

    private void init()
    {

    }

    @Override
    public void onStart()
    {


        super.onStart();
    }






    private void sendMessage(String msg)
    {
        SendToServer messageSender = new SendToServer();
        messageSender.execute(msg);
    }
}
