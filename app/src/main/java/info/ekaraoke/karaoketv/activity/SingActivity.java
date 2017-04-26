package info.ekaraoke.karaoketv.activity;

import android.app.Activity;
import android.content.res.Configuration;
import android.graphics.PixelFormat;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.widget.FrameLayout;

import org.videolan.libvlc.IVLCVout;
import org.videolan.libvlc.LibVLC;
import org.videolan.libvlc.Media;
import org.videolan.libvlc.MediaPlayer;

import java.util.ArrayList;

import info.ekaraoke.karaoketv.R;
import info.ekaraoke.karaoketv.cls.Song;

import static android.content.ContentValues.TAG;

public class SingActivity extends Activity implements IVLCVout.Callback {
    private static final int SURFACE_BEST_FIT = 0;
    private static final int SURFACE_FIT_SCREEN = 1;
    private static final int SURFACE_FILL = 2;
    private static final int SURFACE_16_9 = 3;
    private static final int SURFACE_4_3 = 4;
    private static final int SURFACE_ORIGINAL = 5;
    private static int CURRENT_SIZE = SURFACE_BEST_FIT;
    private Song song;
    private LibVLC libVLC = null;
    private MediaPlayer mediaPlayer = null;
    private FrameLayout videoSurfaceFrame = null;
    private SurfaceView videoSurface = null;
    private SurfaceView subtitlesSurface = null;
    private View.OnLayoutChangeListener onLayoutChangeListener = null;
    private final Handler handler = new Handler();
    private int mVideoHeight = 0;
    private int mVideoWidth = 0;
    private int mVideoVisibleHeight = 0;
    private int mVideoVisibleWidth = 0;
    private int mVideoSarNum = 0;
    private int mVideoSarDen = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sing);

        song = getIntent().getParcelableExtra("SONG2SING");

        final ArrayList<String> args = new ArrayList<>();
        args.add("-vvv");
        libVLC = new LibVLC(this, args);
        mediaPlayer = new MediaPlayer(libVLC);

        videoSurfaceFrame = (FrameLayout) findViewById(R.id.video_surface_frame);
        videoSurface = (SurfaceView) findViewById(R.id.video_surface);
        final ViewStub stub = (ViewStub) findViewById(R.id.subtitles_stub);
        subtitlesSurface = (SurfaceView) stub.inflate();
        subtitlesSurface.setZOrderMediaOverlay(true);
        subtitlesSurface.getHolder().setFormat(PixelFormat.TRANSLUCENT);
    }

    @Override
    protected void onStart() {
        super.onStart();
        final IVLCVout vlcVout = mediaPlayer.getVLCVout();
        vlcVout.setVideoView(videoSurface);
        if (subtitlesSurface != null)
            vlcVout.setSubtitlesView(subtitlesSurface);
        vlcVout.attachViews();

        Media media = new Media(libVLC, song.getPath());
        mediaPlayer.setMedia(media);
        mediaPlayer.play();

        if (onLayoutChangeListener == null) {
            onLayoutChangeListener = new View.OnLayoutChangeListener() {
                private final Runnable runnable = new Runnable() {
                    @Override
                    public void run() {
                        updateVideoSurfaces();
                    }
                };

                @Override
                public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
                    if (left != oldLeft || top != oldTop || right != oldRight || bottom != oldBottom) {
                        handler.removeCallbacks(runnable);
                        handler.post(runnable);
                    }
                }
            };
        }

        videoSurfaceFrame.addOnLayoutChangeListener(onLayoutChangeListener);
    }

    @Override
    protected void onStop() {
        super.onStop();
        mediaPlayer.stop();
        mediaPlayer.getVLCVout().detachViews();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mediaPlayer.release();
        libVLC.release();
    }

    @Override
    public void onNewLayout(IVLCVout vlcVout, int width, int height, int visibleWidth, int visibleHeight, int sarNum, int sarDen) {
        mVideoWidth = width;
        mVideoHeight = height;
        mVideoVisibleWidth = visibleWidth;
        mVideoVisibleHeight = visibleHeight;
        mVideoSarNum = sarNum;
        mVideoSarDen = sarDen;
        updateVideoSurfaces();
    }

    @Override
    public void onSurfacesCreated(IVLCVout vlcVout) {

    }

    @Override
    public void onSurfacesDestroyed(IVLCVout vlcVout) {

    }

    @Override
    public void onHardwareAccelerationError(IVLCVout vlcVout) {

    }

    private void updateVideoSurfaces() {
        int sw = getWindow().getDecorView().getWidth();
        int sh = getWindow().getDecorView().getHeight();
        // sanity check
        if (sw * sh == 0) {
            Log.e(TAG, "Invalid surface size");
            return;
        }
        mediaPlayer.getVLCVout().setWindowSize(sw, sh);

        ViewGroup.LayoutParams lp = videoSurface.getLayoutParams();
        if (mVideoWidth * mVideoHeight == 0) {
            /* Case of OpenGL vouts: handles the placement of the video using MediaPlayer API */
            lp.width  = ViewGroup.LayoutParams.MATCH_PARENT;
            lp.height = ViewGroup.LayoutParams.MATCH_PARENT;
            videoSurface.setLayoutParams(lp);
            lp = videoSurfaceFrame.getLayoutParams();
            lp.width  = ViewGroup.LayoutParams.MATCH_PARENT;
            lp.height = ViewGroup.LayoutParams.MATCH_PARENT;
            videoSurfaceFrame.setLayoutParams(lp);
            //changeMediaPlayerLayout(sw, sh);
            return;
        }
        if (lp.width == lp.height && lp.width == ViewGroup.LayoutParams.MATCH_PARENT) {
            /* We handle the placement of the video using Android View LayoutParams */
            //mediaPlayer.setAspectRatio(null);
            //mediaPlayer.setScale(0);
        }

        double dw = sw, dh = sh;
        final boolean isPortrait = getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT;
        if (sw > sh && isPortrait || sw < sh && !isPortrait) {
            dw = sh;
            dh = sw;
        }
        // compute the aspect ratio
        double ar, vw;
        if (mVideoSarDen == mVideoSarNum) {
            /* No indication about the density, assuming 1:1 */
            vw = mVideoVisibleWidth;
            ar = (double)mVideoVisibleWidth / (double)mVideoVisibleHeight;
        } else {
            /* Use the specified aspect ratio */
            vw = mVideoVisibleWidth * (double)mVideoSarNum / mVideoSarDen;
            ar = vw / mVideoVisibleHeight;
        }

        // compute the display aspect ratio
        double dar = dw / dh;
        switch (CURRENT_SIZE) {
            case SURFACE_BEST_FIT:
                if (dar < ar)
                    dh = dw / ar;
                else
                    dw = dh * ar;
                break;
            case SURFACE_FIT_SCREEN:
                if (dar >= ar)
                    dh = dw / ar; /* horizontal */
                else
                    dw = dh * ar; /* vertical */
                break;
            case SURFACE_FILL:
                break;
            case SURFACE_16_9:
                ar = 16.0 / 9.0;
                if (dar < ar)
                    dh = dw / ar;
                else
                    dw = dh * ar;
                break;
            case SURFACE_4_3:
                ar = 4.0 / 3.0;
                if (dar < ar)
                    dh = dw / ar;
                else
                    dw = dh * ar;
                break;
            case SURFACE_ORIGINAL:
                dh = mVideoVisibleHeight;
                dw = vw;
                break;
        }

        // set display size
        lp.width  = (int) Math.ceil(dw * mVideoWidth / mVideoVisibleWidth);
        lp.height = (int) Math.ceil(dh * mVideoHeight / mVideoVisibleHeight);
        videoSurface.setLayoutParams(lp);
        if (subtitlesSurface != null)
            subtitlesSurface.setLayoutParams(lp);
        // set frame size (crop if necessary)
        lp = videoSurfaceFrame.getLayoutParams();
        lp.width = (int) Math.floor(dw);
        lp.height = (int) Math.floor(dh);
        videoSurfaceFrame.setLayoutParams(lp);
        videoSurface.invalidate();
        if (subtitlesSurface != null)
            subtitlesSurface.invalidate();
    }

    public void audioTrackToggle(){
        int count = mediaPlayer.getAudioTracksCount();
        int curr = mediaPlayer.getAudioTrack();
        Log.d("htdu87","Audio track count: "+count);
        Log.d("htdu87","Current audio track: "+curr);

        if(curr+1<count) {
            Log.d("htdu87","Setting audio track to: "+(curr+1));
            mediaPlayer.setAudioTrack(curr + 1);
        }
        else {
            Log.d("htdu87","Setting audio track to: 1");
            mediaPlayer.setAudioTrack(1);
        }

        Log.d("htdu87","-----------------------");
        Log.d("htdu87","Current audio track: "+mediaPlayer.getAudioTrack());
    }

    public void Pause(){
        mediaPlayer.pause();
    }

    public void Play(){
        mediaPlayer.play();
    }

    public int getState(){
        return mediaPlayer.getPlayerState();
    }

    public int getLength(){
        return (int) mediaPlayer.getLength();
    }
}
