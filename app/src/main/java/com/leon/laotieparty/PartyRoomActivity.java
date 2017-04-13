package com.leon.laotieparty;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by Leon on 2017/4/13.
 */

public class PartyRoomActivity extends AppCompatActivity {

    private PartyRoomLayout mPartyRoomLayout;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_party_room);

        mPartyRoomLayout = (PartyRoomLayout) findViewById(R.id.party_room_layout);

        //设置前置摄像头预览并开启
        AgoraManager.getInstance()
                .setupLocalVideo(getApplicationContext())
                .startPreview();
        //将摄像头预览的SurfaceView加入PartyRoomLayout
        mPartyRoomLayout.addView(AgoraManager.getInstance().getLocalSurfaceView());

    }


}
