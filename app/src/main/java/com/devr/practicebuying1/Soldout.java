package com.devr.practicebuying1;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;

import java.util.Random;


public class Soldout extends AppCompatActivity {

    private AdView mAdView;
    ProgressBar progressBar;
    Random rand;
    ImageView picture;
    TextView refreshtext;
    RelativeLayout buying;
    RelativeLayout buyplus;
    int rantime;

    long mBaseTime;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.soldout);

        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });
        mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);



        rand = new Random();
        progressBar = (ProgressBar)findViewById(R.id.progressBar);
        picture = (ImageView)findViewById(R.id.image);
        refreshtext = (TextView) findViewById(R.id.retext);
        buying = (RelativeLayout)findViewById(R.id.buyingtap);
        buyplus = (RelativeLayout)findViewById(R.id.buyplus);
        rantime=0;







    }


    public void refresh(View view) {


        showProgressDialog();

    }


    private void showProgressDialog() {
        if(rantime>575) {
            Toast.makeText(MainActivity.context, "[실패] 구매하기 버튼을 놓치셨습니다.",
                    Toast.LENGTH_SHORT).show();
            MainActivity.finishCode=1;
            finish();
            return;
        }
        rantime=rand.nextInt(600);
        System.out.println("랜덤수= "+rantime+"\n");
        if(progressBar.getVisibility()== View.VISIBLE) {
            System.out.println("보임");
        }else {
            // 2초간 멈추게 하고싶다면
            System.out.println("안보임");
            progressBar.setVisibility(View.VISIBLE);
            picture.setVisibility(View.GONE);
            refreshtext.setVisibility(View.VISIBLE);
            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                public void run() {
                    picture.setVisibility(View.VISIBLE);
                    progressBar.setVisibility(View.INVISIBLE);
                    refreshtext.setVisibility(View.INVISIBLE);
                }
            }, rantime);  // 2000은 2초를 의미합니다

            if(rantime>575){
                buying.setVisibility(View.VISIBLE);
                buyplus.setVisibility(View.GONE);
            }else{
                buying.setVisibility(View.GONE);
                buyplus.setVisibility(View.GONE);
            }

            System.out.println("끝남");
        }


    }


    public void buy(View view) {
        if(buyplus.getVisibility()==View.GONE){
            buyplus.setVisibility(View.VISIBLE);
        }else{
            Intent intent = new Intent(this, Paying.class);
            startActivity(intent);
            finish();
        }


    }
}
