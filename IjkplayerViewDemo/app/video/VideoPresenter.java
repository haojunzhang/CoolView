package idv.haojun.aplayer.app.video;

import android.content.Context;

public class VideoPresenter implements VideoContract.Presenter {
    private VideoContract.View mView;
    private Context context;

    VideoPresenter(VideoContract.View mView) {
        this.mView = mView;
        this.context = (Context) mView;
    }
}
