package idv.haojun.aplayer.adapter;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.io.File;

import idv.haojun.aplayer.R;
import idv.haojun.aplayer.helper.TimeHelper;
import idv.haojun.aplayer.model.AVideo;

public class VideoRVAdapter extends BaseRVAdapter<AVideo> {

    private int thumbnailWidth;
    private int thumbnailHeight;

    public VideoRVAdapter(Context context, int layoutId) {
        super(context, layoutId);
        thumbnailWidth = (int) (120 * context.getResources().getDisplayMetrics().density);
        thumbnailHeight = (int) (90 * context.getResources().getDisplayMetrics().density);
    }

    @Override
    protected void convert(Context context, BaseViewHolder holder, AVideo video) {
        holder.setText(R.id.tv_item_rv_video_duration, TimeHelper.getVideoFormat(video.getDuration()))
                .setText(R.id.tv_item_rv_video_title, video.getTitle());

        Glide.with(context)
                .load(new File(video.getData()))
                .apply(new RequestOptions()
                        .error(R.drawable.ic_error_black_24dp)
                        .placeholder(R.drawable.ic_error_black_24dp)
                        .override(thumbnailWidth, thumbnailHeight)
                        .centerCrop()
                )
                .into((ImageView) holder.getView(R.id.iv_item_rv_video_thumbnail));
    }
}