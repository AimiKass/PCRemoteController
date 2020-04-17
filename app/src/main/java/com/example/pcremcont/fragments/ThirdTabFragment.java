package com.example.pcremcont.fragments;

import android.annotation.SuppressLint;
import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;

import com.example.pcremcont.R;
import com.example.pcremcont.animations.MyBounceInterpolator;
import com.example.pcremcont.database.Database;
import com.example.pcremcont.portCommunicator.SendToServer;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.UnknownHostException;

public class ThirdTabFragment extends Fragment
{

    View view;
    Button sendVoiceBtn;

    AudioRecord recorder;
    Database database;

    private int sampleRate = 16000;
    private int channelConfig = AudioFormat.CHANNEL_IN_MONO;
    private int audioFormat = AudioFormat.ENCODING_PCM_16BIT;
    int minBufSize = AudioRecord.getMinBufferSize(sampleRate, channelConfig, audioFormat);
    private boolean status = true;



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

    @SuppressLint("SetTextI18n")
    private void init()
    {
        database = new Database(getContext());

        sendVoiceBtn = view.findViewById(R.id.playPauseBtn);
        sendVoiceBtn.setText("PRESS AND HOLD");

    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public void onStart()
    {



        super.onStart();

        sendVoiceBtn.setOnTouchListener(new View.OnTouchListener()
        {

            @SuppressLint("SetTextI18n")
            @Override
            public boolean onTouch(View v, MotionEvent event)
            {
                if (event.getAction() == MotionEvent.ACTION_DOWN)
                {
                    // Pressed
                    sendVoiceBtn.setText("RELEASE TO STOP");
                    final Animation btnAnimation = AnimationUtils.loadAnimation(getContext(),R.anim.bounce);
                    sendVoiceBtn.startAnimation(btnAnimation);

                    status = true;
                    sendMessage("20");

                    startStreaming();

                } else if (event.getAction() == MotionEvent.ACTION_UP)
                {
                    // Released
                    sendVoiceBtn.setText("PRESS AND HOLD");
                    final Animation btnAnimation = AnimationUtils.loadAnimation(getContext(),R.anim.bounce);
                    MyBounceInterpolator interpolator = new MyBounceInterpolator(0.2, 20);

                    btnAnimation.setInterpolator(interpolator);
                    sendVoiceBtn.startAnimation(btnAnimation);

                    status = false;
                    sendMessage("201");

                }
                return true;
            }
        });


    }

    private void startStreaming()
    {
        Thread streamThread = new Thread(new Runnable()
        {

            @Override
            public void run()
            {
                try
                {

                    DatagramSocket socket = new DatagramSocket();
                    Log.d("VS", "Socket Created");

                    byte[] buffer = new byte[minBufSize];

                    Log.d("VS","Buffer created of size " + minBufSize);
                    DatagramPacket packet;

                    final InetAddress destination = InetAddress.getByName(database.getIP());
                    Log.d("VS", "Address retrieved");


                    recorder = new AudioRecord(MediaRecorder.AudioSource.MIC,sampleRate,channelConfig,audioFormat,minBufSize*10);
                    Log.d("VS", "Recorder initialized");

                    recorder.startRecording();


                    while(status)
                    {

                        //reading data from MIC into buffer
                        minBufSize = recorder.read(buffer, 0, buffer.length);

                        //putting buffer in the packet
                        packet = new DatagramPacket (buffer,buffer.length,destination, Integer.parseInt(database.getPort()));

                        socket.send(packet);
//                        System.out.println("MinBufferSize: " +minBufSize);

                    }


                } catch(UnknownHostException e)
                {
                    Log.e("VS", "UnknownHostException");
                } catch (IOException e) {
                    e.printStackTrace();
                    Log.e("VS", "IOException");
                }
            }

        });
        streamThread.start();
    }


    private void sendMessage(String msg)
    {
        SendToServer messageSender = new SendToServer();
        messageSender.execute(msg,database.getPort(),database.getIP());
    }
}
