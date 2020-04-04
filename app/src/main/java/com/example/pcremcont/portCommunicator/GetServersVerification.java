package com.example.pcremcont.portCommunicator;

import android.os.AsyncTask;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;

public class GetServersVerification extends AsyncTask<Object,String,String>
{
    Socket socket;
    PrintWriter printWriter;
    String verificationMsg ;


    @Override
    protected String doInBackground(Object... message)
    {
        //[0]-> message , [1]-> port , [2]-> IP
        String msg = (String) message[0];
        try {

            SocketAddress socketAddress = new InetSocketAddress((String)message[2], Integer.parseInt((String) message[1]));
            socket = new Socket();
            socket.connect(socketAddress,1000);
            printWriter = new PrintWriter(socket.getOutputStream());
            printWriter.println(msg);
            printWriter.flush();

            InputStreamReader inputStreamReader = new InputStreamReader(socket.getInputStream());
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            verificationMsg = bufferedReader.readLine();

            printWriter.close();
            socket.close();

        } catch (IOException e)
        {
            e.printStackTrace();
        }


        return verificationMsg;
    }
}
