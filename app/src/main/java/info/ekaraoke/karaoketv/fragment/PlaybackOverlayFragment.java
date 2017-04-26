package info.ekaraoke.karaoketv.fragment;


import android.app.Activity;
import android.os.Bundle;
import android.support.v17.leanback.widget.Action;
import android.support.v17.leanback.widget.ArrayObjectAdapter;
import android.support.v17.leanback.widget.ClassPresenterSelector;
import android.support.v17.leanback.widget.ControlButtonPresenterSelector;
import android.support.v17.leanback.widget.ListRow;
import android.support.v17.leanback.widget.ListRowPresenter;
import android.support.v17.leanback.widget.OnActionClickedListener;
import android.support.v17.leanback.widget.PlaybackControlsRow;
import android.support.v17.leanback.widget.PlaybackControlsRowPresenter;

import info.ekaraoke.karaoketv.activity.SingActivity;
import info.ekaraoke.karaoketv.cls.DetailsDescriptionPresenter;
import info.ekaraoke.karaoketv.cls.Song;

/**
 * Created by du on 4/25/2017.
 */

public class PlaybackOverlayFragment extends android.support.v17.leanback.app.PlaybackOverlayFragment {
    private Song song;
    private ArrayObjectAdapter rowsAdapter;
    private PlaybackControlsRow mPlaybackControlsRow;
    private ArrayObjectAdapter mPrimaryActionAdapter;
    private ArrayObjectAdapter mSecondaryActionAdapter;

    private PlaybackControlsRow.PlayPauseAction mPlayPauseAction;
    private PlaybackControlsRow.RepeatAction mRepeatAction;
    private PlaybackControlsRow.ThumbsUpAction mThumbsUpAction;
    private PlaybackControlsRow.ThumbsDownAction mThumbsDownAction;
    private PlaybackControlsRow.ShuffleAction mShuffleAction;
    private PlaybackControlsRow.SkipNextAction mSkipNextAction;
    private PlaybackControlsRow.SkipPreviousAction mSkipPreviousAction;
    private PlaybackControlsRow.FastForwardAction mFastForwardAction;
    private PlaybackControlsRow.RewindAction mRewindAction;
    private PlaybackControlsRow.HighQualityAction mHighQualityAction;
    private PlaybackControlsRow.ClosedCaptioningAction mClosedCaptioningAction;
    private PlaybackControlsRow.MoreActions mMoreActions;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        song = getActivity().getIntent().getParcelableExtra("SONG2SING");
        setBackgroundType(PlaybackOverlayFragment.BG_LIGHT);
        setFadingEnabled(true);

        setUpRows();
    }

    private void setUpRows() {
        ClassPresenterSelector ps = new ClassPresenterSelector();

        PlaybackControlsRowPresenter playbackControlsRowPresenter;
        playbackControlsRowPresenter = new PlaybackControlsRowPresenter(new DetailsDescriptionPresenter());
        playbackControlsRowPresenter.setOnActionClickedListener(new OnActionClickedListener() {
            @Override
            public void onActionClicked(Action action) {
                if(action.getId()== mPlayPauseAction.getId()){
                    SingActivity activity = (SingActivity)getActivity();
                    if(activity.getState()==3) {
                        activity.Pause();
                        mPlayPauseAction.setIndex(PlaybackControlsRow.PlayPauseAction.PLAY);
                        mPlayPauseAction.setIcon(mPlayPauseAction.getDrawable(PlaybackControlsRow.PlayPauseAction.PLAY));
                    } else {
                        activity.Play();
                        mPlayPauseAction.setIndex(PlaybackControlsRow.PlayPauseAction.PAUSE);
                        mPlayPauseAction.setIcon(mPlayPauseAction.getDrawable(PlaybackControlsRow.PlayPauseAction.PAUSE));
                    }
                    notifyChanged(mPlayPauseAction);
                }else if(action.getId()==mShuffleAction.getId()){
                    ((SingActivity)getActivity()).audioTrackToggle();
                }
            }
        });

        ps.addClassPresenter(PlaybackControlsRow.class, playbackControlsRowPresenter);
        ps.addClassPresenter(ListRow.class, new ListRowPresenter());
        rowsAdapter = new ArrayObjectAdapter(ps);

        /*
         * Add PlaybackControlsRow to mRowsAdapter, which makes video control UI.
         * PlaybackControlsRow is supposed to be first Row of mRowsAdapter.
         */
        addPlaybackControlsRow();

        /* add ListRow to second row of mRowsAdapter */
        //addOtherRows();

        setAdapter(rowsAdapter);
    }

    private void addPlaybackControlsRow() {
        mPlaybackControlsRow = new PlaybackControlsRow(song);
        rowsAdapter.add(mPlaybackControlsRow);

        ControlButtonPresenterSelector presenterSelector = new ControlButtonPresenterSelector();
        mPrimaryActionAdapter = new ArrayObjectAdapter(presenterSelector);
        mSecondaryActionAdapter = new ArrayObjectAdapter(presenterSelector);
        mPlaybackControlsRow.setPrimaryActionsAdapter(mPrimaryActionAdapter);
        //mPlaybackControlsRow.setSecondaryActionsAdapter(mSecondaryActionAdapter);

        Activity activity = getActivity();
        mPlayPauseAction = new PlaybackControlsRow.PlayPauseAction(activity);
        mPlayPauseAction.setIcon(mPlayPauseAction.getDrawable(PlaybackControlsRow.PlayPauseAction.PAUSE));
        mRepeatAction = new PlaybackControlsRow.RepeatAction(activity);
        mThumbsUpAction = new PlaybackControlsRow.ThumbsUpAction(activity);
        mThumbsDownAction = new PlaybackControlsRow.ThumbsDownAction(activity);
        mShuffleAction = new PlaybackControlsRow.ShuffleAction(activity);
        mSkipNextAction = new PlaybackControlsRow.SkipNextAction(activity);
        mSkipPreviousAction = new PlaybackControlsRow.SkipPreviousAction(activity);
        mFastForwardAction = new PlaybackControlsRow.FastForwardAction(activity);
        mRewindAction = new PlaybackControlsRow.RewindAction(activity);
        mHighQualityAction = new PlaybackControlsRow.HighQualityAction(activity);
        mClosedCaptioningAction = new PlaybackControlsRow.ClosedCaptioningAction(activity);
        mMoreActions = new PlaybackControlsRow.MoreActions(activity);

        /* PrimaryAction setting */
        mPrimaryActionAdapter.add(mSkipPreviousAction);
        //mPrimaryActionAdapter.add(mRewindAction);
        mPrimaryActionAdapter.add(mShuffleAction);
        mPrimaryActionAdapter.add(mPlayPauseAction);
        //mPrimaryActionAdapter.add(mFastForwardAction);
        mPrimaryActionAdapter.add(mSkipNextAction);

        /* SecondaryAction setting */
        /*mSecondaryActionAdapter.add(mThumbsUpAction);
        mSecondaryActionAdapter.add(mThumbsDownAction);
        mSecondaryActionAdapter.add(mRepeatAction);
        mSecondaryActionAdapter.add(mShuffleAction);
        mSecondaryActionAdapter.add(mHighQualityAction);
        mSecondaryActionAdapter.add(mClosedCaptioningAction);
        mSecondaryActionAdapter.add(mMoreActions);*/

        mPlaybackControlsRow.setTotalTime(((SingActivity)getActivity()).getLength());
    }

    private void notifyChanged(Action action) {
        ArrayObjectAdapter adapter = mPrimaryActionAdapter;
        if (adapter.indexOf(action) >= 0) {
            adapter.notifyArrayItemRangeChanged(adapter.indexOf(action), 1);
            return;
        }

        adapter = mSecondaryActionAdapter;
        if (adapter.indexOf(action) >= 0) {
            adapter.notifyArrayItemRangeChanged(adapter.indexOf(action), 1);
            return;
        }
    }
}
