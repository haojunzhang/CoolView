package idv.haojun.aplayer.ijkplayer;

import android.content.Context;
import android.util.AttributeSet;

import idv.haojun.aplayer.app.video.VideoContract;

public class AMediaController extends CustomMediaController implements IMediaController {

    private VideoContract.View mView;

    public AMediaController(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    public AMediaController(Context context) {
        super(context);
        initView(context);
    }

    private void initView(Context context) {

    }

    public void bindVideoContractView(VideoContract.View mView) {
        this.mView = mView;
        if (isShowing()){
            mView.show();
        }else{
            mView.hide();
        }
    }

    @Override
    public void show() {
        super.show();
        if (mView != null)
            mView.show();
    }

    @Override
    public void hide() {
        super.hide();
        if (mView != null)
            mView.hide();
    }
}
