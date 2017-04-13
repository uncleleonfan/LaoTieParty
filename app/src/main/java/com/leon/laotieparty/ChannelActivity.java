package com.leon.laotieparty;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.FrameLayout;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Leon on 2017/4/6.
 */

public class ChannelActivity extends AppCompatActivity {

    private static final String TAG = "ChannelActivity";

    private RecyclerView mRecyclerView;
    private FrameLayout mFrameLayout;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_channel);
        initRecyclerView();
        mFrameLayout = (FrameLayout) findViewById(R.id.surface_view_container);

    }

    /**
     * 初始化频道列表
     */
    private void initRecyclerView() {
        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setAdapter(new ChannelAdapter(this, mockChannelList()));
    }

    /**
     *  模拟频道数据
     */
    private List<ChannelItem> mockChannelList() {
        List<ChannelItem> channelItems = new ArrayList<ChannelItem>();
        for (int i = 0; i < 10; i++) {
            ChannelItem channelItem = new ChannelItem();
            channelItem.setChannelId("Channel" + String.valueOf(i));
            channelItems.add(channelItem);
        }
        return channelItems;
    }

    @Override
    protected void onResume() {
        super.onResume();
        //先清空容器
        mFrameLayout.removeAllViews();
        //设置本地前置摄像头预览并启动
        AgoraManager.getInstance().setupLocalVideo(getApplicationContext()).startPreview();
        //将本地摄像头预览的SurfaceView添加到容器中
        mFrameLayout.addView(AgoraManager.getInstance().getLocalSurfaceView());
    }

    /**
     * 停止预览
     */
    @Override
    protected void onPause() {
        super.onPause();
        AgoraManager.getInstance().stopPreview();
    }
}
