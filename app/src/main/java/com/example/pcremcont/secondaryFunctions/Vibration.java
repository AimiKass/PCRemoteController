package com.example.pcremcont.secondaryFunctions;

import android.os.VibrationEffect;
import android.os.Vibrator;

public class Vibration
{

    public void effect(long[] waveTime, int[] waveAmpl, Object SystemService)
    {
        Vibrator vibrator = (Vibrator)  SystemService;

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O)
        {
            VibrationEffect vibrationEffect = null;
            vibrationEffect = VibrationEffect.createWaveform(waveTime, waveAmpl, -1);
            vibrator.vibrate(vibrationEffect);
        }
    }
}
