package com.devr.practicebuying1;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;

public class Paying extends AppCompatActivity {
    RelativeLayout cardview;
    RelativeLayout normalview;
    ImageView cardbtn;
    ImageView normalbtn;
    TextView sincar;
    TextView phon;
    TextView plater;
    RadioButton radio;

    private InterstitialAd mInterstitialAd;
    private AdView mAdView;
    int checkflag=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.payment);
        cardview = (RelativeLayout) findViewById(R.id.persons3);
        normalview= (RelativeLayout) findViewById(R.id.persons5);
        cardbtn = (ImageView)findViewById(R.id.ciecleicon1);
        normalbtn = (ImageView)findViewById(R.id.ciecleicon2);

        sincar = (TextView)findViewById(R.id.sincard);
        phon = (TextView)findViewById(R.id.phone);
        plater = (TextView)findViewById(R.id.plater);
        radio = (RadioButton)findViewById(R.id.radioButton);



        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });
        mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        mInterstitialAd = new InterstitialAd(this);
        mInterstitialAd.setAdUnitId("ca-app-pub-7742126992195898/4787789370");
        mInterstitialAd.loadAd(new AdRequest.Builder().build());


    }


    public void card(View view) {

        cardview.setVisibility(View.VISIBLE);
        normalview.setVisibility(View.GONE);
        cardbtn.setImageResource(R.drawable.ic_radio_button_checked_black_24dp);
        normalbtn.setImageResource(R.drawable.ic_radio_button_unchecked_black_24dp);

    }

    public void normal(View view) {


        cardview.setVisibility(View.GONE);
        normalview.setVisibility(View.VISIBLE);
        cardbtn.setImageResource(R.drawable.ic_radio_button_unchecked_black_24dp);
        normalbtn.setImageResource(R.drawable.ic_radio_button_checked_black_24dp);

    }

    public void sincard(View view) {
        int cColor = getResources().getColor(R.color.choose);
        int unColor = getResources().getColor(R.color.unchoose);
        checkflag=1;
        sincar.setBackgroundColor(cColor);
        phon.setBackgroundColor(unColor);
        plater.setBackgroundColor(unColor);
    }

    public void phone(View view) {
        int cColor = getResources().getColor(R.color.choose);
        int unColor = getResources().getColor(R.color.unchoose);
        sincar.setBackgroundColor(unColor);
        phon.setBackgroundColor(cColor);
        plater.setBackgroundColor(unColor);
        checkflag=2;

    }

    public void paylater(View view) {
        checkflag=3;
        int cColor = getResources().getColor(R.color.choose);
        int unColor = getResources().getColor(R.color.unchoose);
        sincar.setBackgroundColor(unColor);
        phon.setBackgroundColor(unColor);
        plater.setBackgroundColor(cColor);
    }

    public void finish(View view) {

        if(radio.isChecked()){
            if(checkflag==3){
                if(normalview.getVisibility()==View.VISIBLE){
                    MainActivity.finishCode=3;

                    if (mInterstitialAd.isLoaded()) {
                        mInterstitialAd.show();
                    } else {
                        Log.d("TAG", "The interstitial wasn't loaded yet.");
                    }
                    //성공 끝
                    finish();

                }else {
                    Toast myToast = Toast.makeText(this.getApplicationContext(), "일반 결제를 눌러주세요", Toast.LENGTH_SHORT);
                    myToast.show();
                }

            }else {
                Toast myToast = Toast.makeText(this.getApplicationContext(), "나중에 결제를 눌러주세요", Toast.LENGTH_SHORT);
                myToast.show();
            }
        }else {

            Toast myToast = Toast.makeText(this.getApplicationContext(), "약관을 전체동의 해주세요.", Toast.LENGTH_SHORT);
            myToast.show();
        }

    }
}
