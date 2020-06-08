package com.zhong.voiplib.webrtc;

import android.content.Context;

import org.webrtc.Camera1Enumerator;
import org.webrtc.DefaultVideoDecoderFactory;
import org.webrtc.DefaultVideoEncoderFactory;
import org.webrtc.EglBase;
import org.webrtc.PeerConnectionFactory;
import org.webrtc.SurfaceTextureHelper;
import org.webrtc.SurfaceViewRenderer;
import org.webrtc.VideoCapturer;
import org.webrtc.VideoDecoderFactory;
import org.webrtc.VideoEncoderFactory;
import org.webrtc.VideoSource;
import org.webrtc.VideoTrack;
import org.webrtc.audio.JavaAudioDeviceModule;

public class CreateSurfaceViewStream {

    private static VideoSource videoSource;
    private static VideoCapturer videoCapturer;
    private static PeerConnectionFactory peerConnectionFactory;
    private static SurfaceTextureHelper surfaceTextureHelper;
    public static final int VIDEO_RESOLUTION_WIDTH = 320;
    public static final int VIDEO_RESOLUTION_HEIGHT = 240;
    public static final int FPS = 10;
    public static final String VIDEO_TRACK_ID = "ARDAMSv0";
    private static PeerConnectionFactory factory;
    private static VideoCapturer videoCapture;
    private static SurfaceTextureHelper captureThread;

    private static VideoCapturer createCameraCapturer(boolean isFront) {
        Camera1Enumerator enumerator = new Camera1Enumerator(false);
        final String[] deviceNames = enumerator.getDeviceNames();

        // First, try to find front facing camera
        for (String deviceName : deviceNames) {
            if (isFront ? enumerator.isFrontFacing(deviceName) : enumerator.isBackFacing(deviceName)) {
                VideoCapturer videoCapturer = enumerator.createCapturer(deviceName, null);

                if (videoCapturer != null) {
                    return videoCapturer;
                }
            }
        }
        return null;
    }

    public static void stopCollectStream() {
        if (videoSource != null) {
            videoSource.dispose();
            videoSource = null;
        }

        if (videoCapturer != null) {
            try {
                videoCapturer.stopCapture();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            videoCapturer.dispose();
            videoCapturer = null;
        }

        if (surfaceTextureHelper != null) {
            surfaceTextureHelper.dispose();
            surfaceTextureHelper = null;
        }

        if (peerConnectionFactory != null) {
            peerConnectionFactory.dispose();
            peerConnectionFactory = null;
        }


        if (factory != null) {
            factory.dispose();
            factory = null;
        }

        if (videoCapture != null) {
            videoCapture.dispose();
            videoCapture = null;
        }

        if (captureThread != null) {
            captureThread.dispose();
            captureThread = null;
        }
    }

    // ===================================以下属自定义========================================

    private static PeerConnectionFactory createCustomConnectionFactory(Context _context, EglBase eglBase) {
        PeerConnectionFactory.initialize(
                PeerConnectionFactory.InitializationOptions.builder(_context)
                        .createInitializationOptions());

        final VideoEncoderFactory encoderFactory;
        final VideoDecoderFactory decoderFactory;

        encoderFactory = new DefaultVideoEncoderFactory(
                eglBase.getEglBaseContext(),
                true,
                true);
        decoderFactory = new DefaultVideoDecoderFactory(eglBase.getEglBaseContext());

        PeerConnectionFactory.Options options = new PeerConnectionFactory.Options();

        return PeerConnectionFactory.builder()
                .setOptions(options)
                .setAudioDeviceModule(JavaAudioDeviceModule.builder(_context).createAudioDeviceModule())
                .setVideoEncoderFactory(encoderFactory)
                .setVideoDecoderFactory(decoderFactory)
                .createPeerConnectionFactory();
    }

    // 创建本地流
    public static void createLocalCustomStream(Context _context, SurfaceViewRenderer surfaceViewRenderer) {
        EglBase eglBase = EglBase.create();
        factory = createCustomConnectionFactory(_context, eglBase);
        videoCapture = createCameraCapturer(true);
        // 视频
        captureThread = SurfaceTextureHelper.create("CaptureThread", eglBase.getEglBaseContext());
        videoSource = factory.createVideoSource(videoCapture.isScreencast());
        videoCapture.initialize(captureThread, _context, videoSource.getCapturerObserver());
        videoCapture.startCapture(VIDEO_RESOLUTION_WIDTH, VIDEO_RESOLUTION_HEIGHT, FPS);
        VideoTrack videoTrack = factory.createVideoTrack(VIDEO_TRACK_ID, videoSource);
        videoTrack.addSink(surfaceViewRenderer);
    }
}
