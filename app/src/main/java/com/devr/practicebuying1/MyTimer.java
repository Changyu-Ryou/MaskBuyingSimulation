package com.devr.practicebuying1;

import android.os.CountDownTimer;

class MyTimer extends CountDownTimer
{
    public MyTimer(long millisInFuture, long countDownInterval)
    {
        super(millisInFuture, countDownInterval);
    }

    @Override
    public void onTick(long millisUntilFinished) {


        //textView.setText(millisUntilFinished/1000 + " 초");
    }

    @Override
    public void onFinish() {
        //textView.setText("0 초");
    }
}