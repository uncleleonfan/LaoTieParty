package com.leon.laotieparty;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

/**
 * Created by Leon on 2017/4/13.
 */

public class PartyRoomActivity extends AppCompatActivity {

    private PartyRoomLayout mPartyRoomLayout;

    private TextView mChannel;

    private ImageButton mEndCall;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_party_room);

        mPartyRoomLayout = (PartyRoomLayout) findViewById(R.id.party_room_layout);

        mChannel = (TextView) findViewById(R.id.channel);
        String channel = getIntent().getStringExtra("Channel");
        mChannel.setText(channel);

        mEndCall = (ImageButton) findViewById(R.id.end_call);
        mEndCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //AgoraManager里面封装了挂断的API, 退出频道
                AgoraManager.getInstance().leaveChannel();
                finish();
            }
        });

        //设置前置摄像头预览并开启
        AgoraManager.getInstance()
                .setupLocalVideo(getApplicationContext())
                .joinChannel(channel)
                .startPreview();
        //将摄像头预览的SurfaceView加入PartyRoomLayout
        mPartyRoomLayout.addView(AgoraManager.getInstance().getLocalSurfaceView());

    }


}
