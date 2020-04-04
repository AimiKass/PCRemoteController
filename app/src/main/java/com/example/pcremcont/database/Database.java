package com.example.pcremcont.database;

import android.content.Context;
import android.content.SharedPreferences;

public class Database
{
    SharedPreferences pref;
    SharedPreferences.Editor edit;

    private static final String KEY_FOR_PORT_NUMBER = "PORT_NUMBER";
    private static final String KEY_FOR_IP_ADDRESS = "IP_ADDRESS";
    private static final String KEY_FOR_SHARED_PREFERENCE = "MyPrefs";

    public Database(Context context)
    {
        pref = context.getSharedPreferences(KEY_FOR_SHARED_PREFERENCE, context.MODE_PRIVATE);
        edit = pref.edit();
    }


    public void setPort(String port)
    {
        edit.putString(KEY_FOR_PORT_NUMBER, port);
        edit.apply();
    }

    public void setIP(String ip)
    {
        edit.putString(KEY_FOR_IP_ADDRESS, ip);
        edit.apply();
    }


    public String getPort()
    {
        return pref.getString(KEY_FOR_PORT_NUMBER, "");
    }

    public String getIP()
    {
        return pref.getString(KEY_FOR_IP_ADDRESS, "");
    }

    public boolean ipExist ()
    {
        return pref.contains(KEY_FOR_IP_ADDRESS);
    }

    public boolean portExist()
    {
        return pref.contains(KEY_FOR_PORT_NUMBER);
    }

}
