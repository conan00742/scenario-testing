package krot.sample.com.meshchat.adapter;

import android.content.Context;
import android.media.MediaPlayer;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;


import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import krot.sample.com.meshchat.R;
import krot.sample.com.meshchat.model.DisplayedMessage;
import krot.sample.com.meshchat.widget.CustomVideoView;

/**
 * Created by Krot on 5/20/18.
 */

public class VideoAdapter extends RecyclerView.Adapter<VideoAdapter.VideoMessageViewHolder> {

    private List<DisplayedMessage> displayedMessageList;
    private LayoutInflater mInflater;

    public VideoAdapter(Context context) {
        this.displayedMessageList = new ArrayList<>();
        this.mInflater = LayoutInflater.from(context);
    }

    @Override
    public VideoMessageViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.video_item, parent, false);
        return new VideoMessageViewHolder(view);
    }

    public void setDisplayedMessageList(List<DisplayedMessage> displayedMessageList) {
        this.displayedMessageList = displayedMessageList;
    }

    @Override
    public void onBindViewHolder(VideoMessageViewHolder holder, int position) {
        holder.bindData(getItemAt(position));
    }

    private DisplayedMessage getItemAt(int position) {
        return displayedMessageList != null ? displayedMessageList.get(position) : null;
    }

    @Override
    public int getItemCount() {
        return displayedMessageList != null ? displayedMessageList.size() : 0;
    }




    class VideoMessageViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.root_video)
        LinearLayout mLlPlainTextRoot;
        @BindView(R.id.tv_user)
        TextView mTvUser;
        @BindView(R.id.cvv_message)
        CustomVideoView mCvvMessage;
//        @BindView(R.id.tv_path)
//        TextView mTvPath;


        public VideoMessageViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

            mCvvMessage.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {
                    mp.setLooping(true);
                }
            });


        }

        public void bindData(DisplayedMessage currentMessage) {
            if (currentMessage != null) {
                if (currentMessage.getUserMessage().isFromSender()) {
                    mTvUser.setText("> me(" + currentMessage.getInstance().getStringIdentifier() + "):");
                    mLlPlainTextRoot.setGravity(Gravity.END);
                    mCvvMessage.setVideoPath(currentMessage.getUserMessage().getVideoPath());
                } else {
                    mTvUser.setText("> " + currentMessage.getInstance().getStringIdentifier() + ":");
                    mLlPlainTextRoot.setGravity(Gravity.START);
//                    mCvvMessage.setVideoURI(Uri.fromFile(new File(currentMessage.getUserMessage().getVideoPath())));
                    mCvvMessage.setVideoPath(currentMessage.getUserMessage().getVideoPath());
                }


                mCvvMessage.start();

            }

        }

    }


}
