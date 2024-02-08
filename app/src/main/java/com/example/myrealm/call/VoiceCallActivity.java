package com.example.myrealm.call;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myrealm.R;
import com.example.myrealm.call.utils.RtcTokenBuilder2;
import com.example.myrealm.call.utils.TokenUtils;
import com.example.myrealm.call.widget.AudioOnlyLayout;
import com.example.myrealm.call.widget.AudioSeatManager;

import java.util.Random;
import java.util.UUID;

import io.agora.rtc2.ChannelMediaOptions;
import io.agora.rtc2.Constants;
import io.agora.rtc2.IRtcEngineEventHandler;
import io.agora.rtc2.RtcEngine;
import io.agora.rtc2.RtcEngineConfig;

public class VoiceCallActivity extends AppCompatActivity {

    private TextView infoText;
    private Button btnJoin;

    private boolean isJoined = false;
    private String appId;
    private String channelName;
    private String token;
    static int tokenExpirationInSeconds = 3600;
    static int privilegeExpirationInSeconds = 3600;
    private String certificate;
    private int uid = 0;
    private RtcEngine agoraEngine;

    private AudioSeatManager audioSeatManager;
    private AudioOnlyLayout audioOnlyLayoutOne;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_voice_call);

        infoText = findViewById(R.id.infoText);
        btnJoin = findViewById(R.id.joinLeaveButton);
        audioOnlyLayoutOne = findViewById(R.id.audioLayoutOne);

        uid = new Random().nextInt(10000);

//        uid = 3912;

        appId = getString(R.string.agora_app_id);
        certificate = getString(R.string.agora_app_certificate);
        channelName = "9876543210";
//        token = "007eJxTYEi7Ff/PokPn4ad53fEHt1aeeuHsEaxVsanOOnuS6MHoD1cVGCwTjc1NktKMEg1NLU3MTAwsklItkwyNLdMMzY0sjczS/safT2kIZGRY8msmMyMDBIL4XAyGRsYmpmbmFpYGDAwAu+kiag==";


        audioSeatManager = new AudioSeatManager(
                audioOnlyLayoutOne
//                findViewById(R.id.audioLayoutTwo),
//                findViewById(R.id.audioLayoutThree)
        );

        if(checkSelfPermission()){
            setupVoiceSDKEngine();
        }else{
            requestPermission();
        }


        btnJoin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!isJoined){

                   btnJoin.setText("Leave");
                   joinChannel();

//                    if (AndPermission.hasPermissions(this, Permission.Group.STORAGE, Permission.Group.MICROPHONE, Permission.Group.CAMERA)) {
//                        joinChannel(channelId);
//                        audioProfileInput.setEnabled(false);
//                        channelProfileInput.setEnabled(false);
//                        return;
//                    }
                }else{
                    isJoined = false;
                    btnJoin.setText("Join");
                    agoraEngine.leaveChannel();
                    audioSeatManager.downAllSeats();
                }
            }
        });
    }

    private void joinChannel() {
        ChannelMediaOptions options = new ChannelMediaOptions();
        options.autoSubscribeAudio = true;
        // Set both clients as the BROADCASTER.
        options.clientRoleType = Constants.CLIENT_ROLE_BROADCASTER;
        // Set the channel profile as BROADCASTING.
        options.channelProfile = Constants.CHANNEL_PROFILE_LIVE_BROADCASTING;

        // Join the channel with a temp token.
        // You need to specify the user ID yourself, and ensure that it is unique in the channel.
//        agoraEngine.joinChannel(token, channelName, uid, options);
        channelName = "9876543210";

        RtcTokenBuilder2 rtcTokenBuilder2 = new RtcTokenBuilder2();
        token = rtcTokenBuilder2.buildTokenWithUid(appId, certificate, channelName, uid, RtcTokenBuilder2.Role.ROLE_SUBSCRIBER, tokenExpirationInSeconds, privilegeExpirationInSeconds);

        Log.i("token",uid + " >>> " + token);

        agoraEngine.joinChannel(token,channelName,uid,options);
//        TokenUtils.gen(this,channelName,uid, responseToken -> {
//            int res = agoraEngine.joinChannel(responseToken,channelName,uid,options);
//            if(res != 0){//failed
//                Log.e("agora","Error join :: " + RtcEngine.getErrorDescription(Math.abs(res)));
//                Toast.makeText(this, "Failed to join voice call", Toast.LENGTH_SHORT).show();
//            }
//        });
    }


    private boolean checkSelfPermission(){
        return ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_GRANTED;
    }

    private void requestPermission() {
        ActivityCompat.requestPermissions(this,
                new String[]{
                        Manifest.permission.RECORD_AUDIO},
                223);

        ActivityCompat.shouldShowRequestPermissionRationale(this,
                Manifest.permission.BLUETOOTH);
    }

    private void setupVoiceSDKEngine() {
        try{
            RtcEngineConfig config = new RtcEngineConfig();
            config.mContext = getBaseContext();
            config.mAppId = appId;
            config.mEventHandler = mRtcEventHandler;
            agoraEngine = RtcEngine.create(config);
        }catch (Exception e){
            throw new RuntimeException("Check the error");
        }
    }

    private final IRtcEngineEventHandler mRtcEventHandler = new IRtcEngineEventHandler() {
        @Override
        // Listen for the remote user joining the channel.
        public void onUserJoined(int uid, int elapsed) {
            runOnUiThread(()->infoText.setText("Remote user joined: " + uid));
            runOnUiThread(() -> {
                audioSeatManager.upRemoteSeat(uid);
            });
        }

        @Override
        public void onJoinChannelSuccess(String channel, int uid, int elapsed) {
            // Successfully joined a channel
            isJoined = true;
            btnJoin.setText("Leave");
            runOnUiThread(()->infoText.setText("Waiting for a remote user to join"));
            runOnUiThread(() -> audioSeatManager.upLocalSeat(uid));
        }

        @Override
        public void onUserOffline(int uid, int reason) {
            // Listen for remote users leaving the channel
            if (isJoined) runOnUiThread(()->infoText.setText("Waiting for a remote user to join"));
            runOnUiThread(() -> {
                audioSeatManager.downSeat(uid);
            });
        }

        @Override
        public void onLeaveChannel(RtcStats 	stats) {
            // Listen for the local user leaving the channel
            runOnUiThread(()->infoText.setText("Press the button to join a channel"));
            btnJoin.setText("Join");
            isJoined = false;
        }
    };


    @Override
    protected void onDestroy() {
        super.onDestroy();
        agoraEngine.leaveChannel();

        new Thread(() -> {
            RtcEngine.destroy();
            agoraEngine = null;
        }).start();
    }
}