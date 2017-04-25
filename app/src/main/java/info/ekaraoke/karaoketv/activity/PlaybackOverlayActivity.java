package info.ekaraoke.karaoketv.activity;

import android.app.Activity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import info.ekaraoke.karaoketv.R;

public class PlaybackOverlayActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_playback_overlay);
    }
}
