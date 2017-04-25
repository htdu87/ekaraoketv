package info.ekaraoke.karaoketv.fragment;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.ThumbnailUtils;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v17.leanback.app.BrowseFragment;

import android.support.v17.leanback.app.HeadersFragment;
import android.support.v17.leanback.app.HeadersSupportFragment;
import android.support.v17.leanback.widget.ArrayObjectAdapter;
import android.support.v17.leanback.widget.HeaderItem;
import android.support.v17.leanback.widget.ListRow;
import android.support.v17.leanback.widget.ListRowPresenter;
import android.support.v17.leanback.widget.OnItemViewClickedListener;
import android.support.v17.leanback.widget.OnItemViewSelectedListener;
import android.support.v17.leanback.widget.Presenter;
import android.support.v17.leanback.widget.Row;
import android.support.v17.leanback.widget.RowHeaderPresenter;
import android.support.v17.leanback.widget.RowPresenter;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.TextView;

import java.io.File;

import info.ekaraoke.karaoketv.R;
import info.ekaraoke.karaoketv.activity.SingActivity;
import info.ekaraoke.karaoketv.cls.CardPresenter;
import info.ekaraoke.karaoketv.cls.SimpleBackgroundManager;
import info.ekaraoke.karaoketv.cls.Song;
import info.ekaraoke.karaoketv.utils.SONG_FORMAT;

/**
 * Created by PCNTT on 04/16/2017.
 */

public class MainFragment extends BrowseFragment implements OnItemViewSelectedListener, OnItemViewClickedListener {
    private static final int GRID_ITEM_WIDTH = 300;
    private static final int GRID_ITEM_HEIGHT = 200;
    private SimpleBackgroundManager simpleBackgroundManager;
    private ArrayObjectAdapter mRowsAdapter;


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setupUIElement();
        loadRows();
        simpleBackgroundManager=new SimpleBackgroundManager(getActivity());
        simpleBackgroundManager.clearBackground();
        setOnItemViewSelectedListener(this);
        setOnItemViewClickedListener(this);
    }


    public void requestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        Log.d("htdu87",permissions[0]);
        if(requestCode==1504 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
            loadSong();
        }
    }

    private void loadSong(){
        HeaderItem cardItemPresenterHeader = new HeaderItem(1, "Video karaoke");
        CardPresenter mCardPresenter = new CardPresenter();
        ArrayObjectAdapter cardRowAdapter = new ArrayObjectAdapter(mCardPresenter);

        String sdcardState = Environment.getExternalStorageState();
        int count = 0;
        if(Environment.MEDIA_MOUNTED.equals(sdcardState)) {
            File f = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/karaoke");
            if (f.exists()) {
                File[] files = f.listFiles();
                for (File inFile : files) {
                    if (inFile.isFile()) {
                        Log.d("htdu87", inFile.getAbsolutePath());
                        Bitmap thumbnail = ThumbnailUtils.createVideoThumbnail(inFile.getAbsolutePath(), MediaStore.Video.Thumbnails.MICRO_KIND);
                        String fname = inFile.getAbsolutePath();
                        fname = fname.substring(fname.lastIndexOf('.'));
                        cardRowAdapter.add(new Song(0, inFile.getName(), fname, inFile.getAbsolutePath(), thumbnail, SONG_FORMAT.VOB));
                        count++;
                    }
                }
            } else {
                Log.d("htdu87", "Folder not exists");
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle("Folder not exists")
                        .setMessage("Karaoke source folder not exist")
                        .setPositiveButton("OK",null)
                        .create().show();
            }
        }else{
            Log.d("htdu87", "External Storage Error: "+Environment.MEDIA_MOUNTED);
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setTitle("External Storage Error")
                    .setMessage(Environment.MEDIA_MOUNTED)
                    .setPositiveButton("OK",null)
                    .create().show();
        }
        mRowsAdapter.add(new ListRow(cardItemPresenterHeader, cardRowAdapter));
        mRowsAdapter.notifyArrayItemRangeChanged(1, count);
    }

    private void setupUIElement(){
        setTitle(getString(R.string.app_name));
        setHeadersState(HEADERS_ENABLED);
        setHeadersTransitionOnBackEnabled(true);
        setBrandColor(getResources().getColor(R.color.fastlane_background));
        setSearchAffordanceColor(getResources().getColor(R.color.search_opaque));
    }

    private void loadRows(){
        mRowsAdapter = new ArrayObjectAdapter(new ListRowPresenter());
        HeaderItem gridItemPresenterHeader = new HeaderItem(0, "GridItemPresenter");
        GridItemPresenter mGirdPresenter = new GridItemPresenter();
        ArrayObjectAdapter gridRowAdapter = new ArrayObjectAdapter(mGirdPresenter);
        gridRowAdapter.add("ITEM 1");
        gridRowAdapter.add("ITEM 2");
        gridRowAdapter.add("ITEM 3");
        mRowsAdapter.add(new ListRow(gridItemPresenterHeader, gridRowAdapter));

        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1504);
        }else {
            loadSong();

        }
        setAdapter(mRowsAdapter);
    }

    private class GridItemPresenter extends Presenter {

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent) {
            TextView view = new TextView(parent.getContext());
            view.setLayoutParams(new ViewGroup.LayoutParams(GRID_ITEM_WIDTH, GRID_ITEM_HEIGHT));
            view.setFocusable(true);
            view.setFocusableInTouchMode(true);
            view.setBackgroundColor(getResources().getColor(R.color.default_background));
            view.setTextColor(Color.WHITE);
            view.setGravity(Gravity.CENTER);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(ViewHolder viewHolder, Object item) {
            ((TextView) viewHolder.view).setText((String) item);
        }

        @Override
        public void onUnbindViewHolder(ViewHolder viewHolder) {

        }
    }

    @Override
    public void onItemSelected(Presenter.ViewHolder itemViewHolder, Object item, RowPresenter.ViewHolder rowViewHolder, Row row) {
        /*if (item instanceof String) { // GridItemPresenter row
            simpleBackgroundManager.clearBackground();
        } else if (item instanceof Song) { // CardPresenter row
            simpleBackgroundManager.updateBackground(getActivity().getDrawable(R.drawable.grid_bg));
        }*/
    }

    @Override
    public void onItemClicked(Presenter.ViewHolder itemViewHolder, Object item, RowPresenter.ViewHolder rowViewHolder, Row row) {
        if(item instanceof Song){
            Song song = (Song) item;
            Intent intent = new Intent(getActivity(), SingActivity.class);
            intent.putExtra("SONG2SING",song);
            startActivity(intent);
        }
    }
}
