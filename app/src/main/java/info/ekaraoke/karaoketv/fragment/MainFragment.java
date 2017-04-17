package info.ekaraoke.karaoketv.fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v17.leanback.app.BrowseFragment;

import android.support.v17.leanback.app.HeadersFragment;
import android.support.v17.leanback.app.HeadersSupportFragment;
import android.support.v17.leanback.widget.ArrayObjectAdapter;
import android.support.v17.leanback.widget.HeaderItem;
import android.support.v17.leanback.widget.ListRow;
import android.support.v17.leanback.widget.ListRowPresenter;
import android.support.v17.leanback.widget.OnItemViewSelectedListener;
import android.support.v17.leanback.widget.Presenter;
import android.support.v17.leanback.widget.Row;
import android.support.v17.leanback.widget.RowHeaderPresenter;
import android.support.v17.leanback.widget.RowPresenter;
import android.util.Log;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.TextView;

import info.ekaraoke.karaoketv.R;
import info.ekaraoke.karaoketv.cls.CardPresenter;
import info.ekaraoke.karaoketv.cls.SimpleBackgroundManager;
import info.ekaraoke.karaoketv.cls.Song;
import info.ekaraoke.karaoketv.utils.SONG_FORMAT;

/**
 * Created by PCNTT on 04/16/2017.
 */

public class MainFragment extends BrowseFragment implements OnItemViewSelectedListener {
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
        HeaderItem gridItemPresenterHeader = new HeaderItem(0,"GridItemPresenter");
        GridItemPresenter mGirdPresenter = new GridItemPresenter();
        ArrayObjectAdapter gridRowAdapter = new ArrayObjectAdapter(mGirdPresenter);
        gridRowAdapter.add("ITEM 1");
        gridRowAdapter.add("ITEM 2");
        gridRowAdapter.add("ITEM 3");
        mRowsAdapter.add(new ListRow(gridItemPresenterHeader,gridRowAdapter));

        HeaderItem cardItemPresenterHeader = new HeaderItem(1,"CardPresenter");
        CardPresenter mCardPresenter = new CardPresenter();
        ArrayObjectAdapter cardRowAdapter = new ArrayObjectAdapter(mCardPresenter);
        cardRowAdapter.add(new Song(0,"Chú ếch con","Có chú là chú ếch con có hai là hai...", SONG_FORMAT.MID));
        cardRowAdapter.add(new Song(1,"Con heo đất","Mẹ mua cho con heo đất í a í a...", SONG_FORMAT.MID));
        cardRowAdapter.add(new Song(2,"Cả nhà thương nhau","Cha thương con vì con giống mẹ...", SONG_FORMAT.MID));
        mRowsAdapter.add(new ListRow(cardItemPresenterHeader,cardRowAdapter));

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
        if (item instanceof String) { // GridItemPresenter row
            simpleBackgroundManager.clearBackground();
        } else if (item instanceof Song) { // CardPresenter row
            simpleBackgroundManager.updateBackground(getActivity().getDrawable(R.drawable.grid_bg));
        }
    }
}