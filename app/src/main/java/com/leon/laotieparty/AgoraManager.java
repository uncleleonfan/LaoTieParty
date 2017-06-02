package com.leon.laotieparty;

import android.content.Context;
import android.util.SparseArray;
import android.view.SurfaceView;

import java.util.ArrayList;
import java.util.List;

import io.agora.rtc.Constants;
import io.agora.rtc.IRtcEngineEventHandler;
import io.agora.rtc.RtcEngine;
import io.agora.rtc.video.VideoCanvas;

/**
 * Created by Leon on 2017/4/9.
 */

public class AgoraManager {

    private static final String TAG = "AgoraManager";

    public static AgoraManager sAgoraManager;

    private RtcEngine mRtcEngine;

    private OnPartyListener mOnPartyListener;

    private int mLocalUid = 0;

    private AgoraManager() {
        mSurfaceViews = new SparseArray<SurfaceView>();
    }

    private SparseArray<SurfaceView> mSurfaceViews;

    public static AgoraManager getInstance() {
        if (sAgoraManager == null) {
            synchronized (AgoraManager.class) {
                if (sAgoraManager == null) {
                    sAgoraManager = new AgoraManager();
                }
            }
        }
        return sAgoraManager;
    }

    private IRtcEngineEventHandler mRtcEventHandler = new IRtcEngineEventHandler() {

        /**
         * 当获取用户uid的远程视频的回调
         */
        @Override
        public void onFirstRemoteVideoDecoded(int uid, int width, int height, int elapsed) {
            if (mOnPartyListener != null) {
                mOnPartyListener.onGetRemoteVideo(uid);
            }
        }

        /**
         * 加入频道成功的回调
         */
        @Override
        public void onJoinChannelSuccess(String channel, int uid, int elapsed) {
            if (mOnPartyListener != null) {
                mOnPartyListener.onJoinChannelSuccess(channel, uid);
            }
        }

        /**
         * 退出频道
         */
        @Override
        public void onLeaveChannel(RtcStats stats) {
            if (mOnPartyListener != null) {
                mOnPartyListener.onLeaveChannelSuccess();
            }
        }

        /**
         * 用户uid离线时的回调
         */
        @Override
        public void onUserOffline(int uid, int reason) {
            if (mOnPartyListener != null) {
                mOnPartyListener.onUserOffline(uid);
            }
        }
    };

    /**
     * 初始化RtcEngine
     */
    public void init(Context context) {
        //创建RtcEngine对象，mRtcEventHandler为RtcEngine的回调
        mRtcEngine = RtcEngine.create(context, context.getString(R.string.private_app_id), mRtcEventHandler);
        //开启视频功能
        mRtcEngine.enableVideo();
        //视频配置，设置为360P
        mRtcEngine.setVideoProfile(Constants.VIDEO_PROFILE_360P, false);
        mRtcEngine.setChannelProfile(Constants.CHANNEL_PROFILE_COMMUNICATION);//设置为通信模式（默认）
//        mRtcEngine.setChannelProfile(Constants.CHANNEL_PROFILE_LIVE_BROADCASTING);设置为直播模式
//        mRtcEngine.setChannelProfile(Constants.CHANNEL_PROFILE_GAME);设置为游戏模式

    }

    /**
     * 设置本地视频，即前置摄像头预览
     */
    public AgoraManager setupLocalVideo(Context context) {
        //创建一个SurfaceView用作视频预览
        SurfaceView surfaceView = RtcEngine.CreateRendererView(context);
        //将SurfaceView保存起来在SparseArray中，后续会将其加入界面。key为视频的用户id，这里是本地视频, 默认id是0
        mSurfaceViews.put(mLocalUid, surfaceView);
        //设置本地视频，渲染模式选择VideoCanvas.RENDER_MODE_HIDDEN，如果选其他模式会出现视频不会填充满整个SurfaceView的情况，
        //具体渲染模式参考官方文档https://docs.agora.io/cn/user_guide/API/android_api.html#set-local-video-view-setuplocalvideo
        mRtcEngine.setupLocalVideo(new VideoCanvas(surfaceView, VideoCanvas.RENDER_MODE_HIDDEN, mLocalUid));
        return this;//返回AgoraManager以作链式调用
    }

    public AgoraManager setupRemoteVideo(Context context, int uid) {
        SurfaceView surfaceView = RtcEngine.CreateRendererView(context);
        mSurfaceViews.put(uid, surfaceView);
        mRtcEngine.setupRemoteVideo(new VideoCanvas(surfaceView, VideoCanvas.RENDER_MODE_HIDDEN, uid));
        return this;
    }

    public AgoraManager joinChannel(String channel) {
        mRtcEngine.joinChannel(null, channel, null, 0);
        return this;
    }

    public void startPreview() {
        mRtcEngine.startPreview();
    }

    public void stopPreview() {
        mRtcEngine.stopPreview();
    }

    public void leaveChannel() {
        mRtcEngine.leaveChannel();
    }

    public void removeSurfaceView(int uid) {
        mSurfaceViews.remove(uid);
    }

    public interface OnPartyListener {
        void onJoinChannelSuccess(String channel, int uid);

        void onGetRemoteVideo(int uid);

        void onLeaveChannelSuccess();

        void onUserOffline(int uid);
    }

    public AgoraManager setOnPartyListener(OnPartyListener listener) {
        mOnPartyListener = listener;
        return this;
    }

    public List<SurfaceView> getSurfaceViews() {
        List<SurfaceView> list = new ArrayList<SurfaceView>();
        for (int i = 0; i < mSurfaceViews.size(); i++) {
            SurfaceView surfaceView = mSurfaceViews.valueAt(i);
            list.add(surfaceView);
        }
        return list;
    }

    public SurfaceView getLocalSurfaceView() {
        return mSurfaceViews.get(mLocalUid);
    }

    public SurfaceView getSurfaceView(int uid) {
        return mSurfaceViews.get(uid);
    }
}