package com.devr.practicebuying1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.drive.Drive;
import com.google.android.gms.games.Games;
import com.google.android.gms.games.GamesCallbackStatusCodes;
import com.google.android.gms.games.GamesClientStatusCodes;
import com.google.android.gms.games.InvitationsClient;
import com.google.android.gms.games.Player;
import com.google.android.gms.games.PlayersClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.auth.PlayGamesAuthProvider;

public class MainActivity extends AppCompatActivity {
    ImageButton btn_blue;
    ImageButton btn_info;
    ImageButton btn_rank;
    MyTimer myTimer;
    private AdView mAdView;
    static TextView mEllapse;
    static TextView scorenow;
    static long mBaseTime;
    long mPauseTime;
    int mSplitCount;
    int HighScore=0;
    static long ell;
    String TAG="TAG ";
    public static int finishCode;
    int highscore;

    private SignInButton signInButton;
    private FirebaseAuth mAuth = null;
    private GoogleSignInClient mGoogleSignInClient;
    private static final int RC_SIGN_IN = 9001;
    private static final int RC_LEADERBOARD_UI = 9004;

    static Context context;

    String sfName = "HighScore";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        context=this;
        finishCode=0;

        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });
        mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);


        mEllapse = (TextView)findViewById(R.id.score);
        scorenow = (TextView)findViewById(R.id.scorenow);


        // 지난번 저장해놨던 사용자 입력값을 꺼내서 보여주기
        SharedPreferences sf = getSharedPreferences(sfName, 0);
        String str = sf.getString("score", ""); // 키값으로 꺼냄
        if(!str.equals("")){
            mEllapse.setVisibility(View.VISIBLE);
            highscore=Integer.parseInt(str);
            String sEll = String.format("%02d:%02d:%02d", highscore / 1000 / 60, (highscore/1000)%60, (highscore %1000)/10);
            mEllapse.setText(sEll); // EditText에 반영함
        }else{
            mEllapse.setVisibility(View.VISIBLE);
            mEllapse.setText("아직 최고 점수가 없습니다.");
            highscore=999999999;
            System.out.println("Str = null\n");
        }





/*
        mAuth = FirebaseAuth.getInstance();
        if (mAuth.getCurrentUser() != null) {
            Intent intent = new Intent(getApplication(), AfterActivity.class);
            startActivity(intent);
            finish();
        }
        // Configure Google Sign In
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_GAMES_SIGN_IN)
                .requestServerAuthCode(getString(R.string.default_web_client_id))
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               // startSignInIntent();
               signIn();
            }
        });

*/




        btn_rank= (ImageButton)findViewById(R.id.rankbtn);
        btn_rank.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view)
            {
                //데이터 담아서 팝업(액티비티) 호출
                Intent intent = new Intent(MainActivity.this, PopupActivity2.class);
                intent.putExtra("data", "Test Popup");
                startActivityForResult(intent, 1);

            }
        });





        btn_info= (ImageButton)findViewById(R.id.infobtn);
        btn_info.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view)
            {
                //데이터 담아서 팝업(액티비티) 호출
                Intent intent = new Intent(MainActivity.this, PopupActivity.class);
                intent.putExtra("data", "Test Popup");
                startActivityForResult(intent, 1);

            }
        });














        btn_blue = (ImageButton)findViewById(R.id.startbtn);
        btn_blue.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view)
            {
               Intent intent = new Intent(MainActivity.this, Soldout.class);
                startActivity(intent);
                mBaseTime = SystemClock.elapsedRealtime();
                //핸드러로 메시지를 보낸다
                mTimer.sendEmptyMessage(0);

            }
        });





    }


/*
    // [START signin]
    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuthWithGoogle(account);
            } catch (ApiException e) {
            }
        }
    }



    private void showLeaderboard() {
        Games.getLeaderboardsClient(this, GoogleSignIn.getLastSignedInAccount(this))
                .getLeaderboardIntent(getString(R.string.leaderboard_fastest))
                .addOnSuccessListener(new OnSuccessListener<Intent>() {
                    @Override
                    public void onSuccess(Intent intent) {
                        startActivityForResult(intent, RC_LEADERBOARD_UI);
                    }
                });
    }



    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {

        Log.d(TAG, "firebaseAuthWithPlayGames:" + acct.getId());

        final FirebaseAuth auth = FirebaseAuth.getInstance();
        AuthCredential credential = PlayGamesAuthProvider.getCredential(acct.getServerAuthCode());
        auth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithCredential:success");
                            FirebaseUser user = auth.getCurrentUser();
                            updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            Toast.makeText(MainActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            Retry();

                            updateUI(null);
                        }

                        // ...
                    }

                    private void Retry() {

                        // Configure Google Sign In
                        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_GAMES_SIGN_IN)
                                .requestServerAuthCode(getString(R.string.default_web_client_id))
                                .build();
                        mGoogleSignInClient = GoogleSignIn.getClient(MainActivity.this, gso);

                        signIn();

                    }
                });
    }

    private void updateUI(FirebaseUser user) { //update ui code here
        if (user != null) {
            //Intent intent = new Intent(this, AfterActivity.class);
            //startActivity(intent);
            //finish();
            signInButton.setVisibility(View.INVISIBLE);


        }
    }
*/

    String calcul(int num){
        String sEll = String.format("%02d:%02d:%02d", num / 1000 / 60, (num/1000)%60, (num %1000)/10);

        return sEll;
    }






    //스톱워치는 위해 핸들러를 만든다.

    static Handler mTimer = new Handler(){
        //핸들러는 기본적으로 handleMessage에서 처리한다.
        public void handleMessage(android.os.Message msg) {
            //텍스트뷰를 수정해준다.
            //mEllapse.setText(getEllapse());
            scorenow.setText(getEllapse());
            //메시지를 다시 보낸다.
            mTimer.sendEmptyMessage(0);//0은 메시지를 구분하기 위한 것

        };

    };


    static String getEllapse(){
        long now = SystemClock.elapsedRealtime();
        ell = now - mBaseTime;//현재 시간과 지난 시간을 빼서 ell값을 구하고
        //아래에서 포맷을 예쁘게 바꾼다음 리턴해준다.
        String sEll = String.format("%02d:%02d:%02d", ell / 1000 / 60, (ell/1000)%60, (ell %1000)/10);
        return sEll;
    }



    protected void onResume() {
        mTimer.removeMessages(0);
        TextView aa = (TextView)findViewById(R.id.scorerank);
        scorenow.setText(getEllapse());
        if (finishCode == 1) {
            aa.setText("구매 버튼을 놓치셨어요!\n탈락!");
            scorenow.setVisibility(View.INVISIBLE);
            finishCode=0;
            super.onResume();
            return;
        }else if(finishCode == 0){
            aa.setText("연습만이 살 길이다!");
            scorenow.setVisibility(View.INVISIBLE);
            finishCode=0;
            super.onResume();
            return;
        }else if(finishCode == 3){
            scorenow.setVisibility(View.VISIBLE);
        }
        finishCode=0;


        if(((ell/1000)%60)<20&& (ell / 1000 / 60)<1){


            aa.setText("좀 하시는데요?\n조만간 마스크 복 터지시겠어요!");

        }else if(((ell/1000)%60)<10&& (ell / 1000 / 60)<1){

            aa.setText("매크로세요?\n 신의 손!!!");
        }else if(((ell/1000)%60)>20&& (ell / 1000 / 60)<1) {

            aa.setText("아직 멀으셨네요\n분발하세요~");
        }else{
            aa.setText("이 정도로는 마스크를 못 사요!\n분발하세요~");
        }
        //mEllapse.getText();
        int buff =safeLongToInt(ell);
        System.out.println("ell 값 = "+ell+"--------------------------------------버프 값="+ buff);

        if(ell<highscore){
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                highscore= Math.toIntExact(ell);
            }else{
                highscore= (int) ell;

            }
            mEllapse.setText(calcul(highscore));

        }

        /*if(buff!=0 || buff>0){
            System.out.println("--------------------------------------입력");
            Games.getLeaderboardsClient(this, GoogleSignIn.getLastSignedInAccount(this))
                    .submitScore(getString(R.string.leaderboard_fastest), buff);
        }*/
        //signInSilently();

        super.onResume();
    }

    public static int safeLongToInt(long l) {
        int i = (int)l;
        if ((long)i != l) {
            throw new IllegalArgumentException(l + " cannot be cast to int without changing its value.");
        }
        return i;
    }

    @Override
    protected void onPause() {
        super.onPause();


    }

    @Override
    protected void onStop() {
        super.onStop();
        // Activity 가 종료되기 전에 저장한다
        // SharedPreferences 에 설정값(특별히 기억해야할 사용자 값)을 저장하기
        SharedPreferences sf = getSharedPreferences(sfName, 0);
        SharedPreferences.Editor editor = sf.edit();//저장하려면 editor가 필요
        String str = highscore+""; // 사용자가 입력한 값
        editor.putString("score", str); // 입력

        editor.commit(); // 파일에 최종 반영함
    }




}
