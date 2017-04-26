package info.ekaraoke.karaoketv.cls;

import android.support.v17.leanback.widget.AbstractDetailsDescriptionPresenter;

/**
 * Created by du on 4/26/2017.
 */

public class DetailsDescriptionPresenter extends AbstractDetailsDescriptionPresenter {
    @Override
    protected void onBindDescription(ViewHolder vh, Object item) {
        Song song = (Song)item;
        if(song != null){
            vh.getTitle().setText(song.getName());
            vh.getSubtitle().setText(song.getStrFormat());
            vh.getBody().setText(song.getPath());
        }
    }
}
