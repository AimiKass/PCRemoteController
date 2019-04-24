package com.example.pcremcont.portCommunicator;

import android.os.AsyncTask;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;

public class SendToServer extends AsyncTask<Object,Void,Void>
{
    Socket socket;
    PrintWriter printWriter;


    @Override
    protected Void doInBackground(Object... message)
    {
        //[0]-> message , [1]-> port , [2]-> IP
        String msg = (String) message[0];
        try {

            SocketAddress socketAddress = new InetSocketAddress((String)message[2], Integer.parseInt((String) message[1]));
            socket = new Socket();
            socket.connect(socketAddress,1000);
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
