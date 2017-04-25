package info.ekaraoke.karaoketv.activity;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;

import java.util.List;

import info.ekaraoke.karaoketv.R;
import info.ekaraoke.karaoketv.fragment.MainFragment;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        Fragment fragment = getFragmentManager().findFragmentById(R.id.main_browse_fragment);
        ((MainFragment)fragment).requestPermissionsResult(requestCode, permissions,grantResults);
    }


}
