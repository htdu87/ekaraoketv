package info.ekaraoke.karaoketv.cls;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v17.leanback.widget.BaseCardView;
import android.support.v17.leanback.widget.ImageCardView;
import android.support.v17.leanback.widget.Presenter;
import android.view.View;
import android.view.ViewGroup;

import info.ekaraoke.karaoketv.R;

/**
 * Created by PCNTT on 04/16/2017.
 */

public class CardPresenter extends Presenter {
    private static Context mContext;
    private static int CARD_WIDTH = 313;
    private static int CARD_HEIGHT = 176;

    private static class ViewHolder extends Presenter.ViewHolder {
        private Song mSong;
        private ImageCardView mCardView;
        private Drawable mDefaulCardImage;

        public ViewHolder(View view) {
            super(view);
            mCardView = (ImageCardView)view;
            mDefaulCardImage = mContext.getDrawable(R.drawable.movie);
        }

        public Song getSong(){
            return this.mSong;
        }

        public void setSong(Song song){
            this.mSong=song;
        }

        public ImageCardView getCardView(){
            return mCardView;
        }

        public Drawable getDefaulCardImage(){
            return mDefaulCardImage;
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent) {
        mContext=parent.getContext();
        ImageCardView cardView = new ImageCardView(mContext);
        cardView.setCardType(BaseCardView.CARD_TYPE_INFO_UNDER);
        cardView.setInfoVisibility(BaseCardView.CARD_REGION_VISIBLE_ALWAYS);
        cardView.setFocusable(true);
        cardView.setFocusableInTouchMode(true);
        cardView.setBackgroundColor(mContext.getResources().getColor(R.color.fastlane_background));
        return new ViewHolder(cardView);
    }

    @Override
    public void onBindViewHolder(Presenter.ViewHolder viewHolder, Object item) {
        Song song = (Song) item;
        ((ViewHolder)viewHolder).setSong(song);
        ((ViewHolder)viewHolder).mCardView.setTitleText(song.getName());
        ((ViewHolder)viewHolder).mCardView.setContentText(song.getLyrics());
        ((ViewHolder)viewHolder).mCardView.setMainImageDimensions(CARD_WIDTH, CARD_HEIGHT);
        ((ViewHolder)viewHolder).mCardView.setMainImage(((ViewHolder)viewHolder).getDefaulCardImage());
    }

    @Override
    public void onUnbindViewHolder(Presenter.ViewHolder viewHolder) {

    }
}
