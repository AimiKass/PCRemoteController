package com.example.pcremcont.portCommunicator;

import android.os.AsyncTask;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;

public class SendToServer extends AsyncTask<String,Void,Void>
{
    Socket socket;
    PrintWriter printWriter;


    @Override
    protected Void doInBackground(String... message)
    {
        String msg = message[0];
        try {

            socket = new Socket("192.168.1.3",7800);
            printWriter = new PrintWriter(socket.getOutputStream());
            printWriter.write(msg);
            printWriter.flush();
            printWriter.close();
            socket.close();

        } catch (IOException e)
        {
            e.printStackTrace();
        }

        return null;
    }
}
