package krot.sample.com.meshchat.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.VideoView;

import com.bumptech.glide.Glide;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import krot.sample.com.meshchat.R;
import krot.sample.com.meshchat.model.DisplayedMessage;

/**
 * Created by jake on 19/05/2018.
 */

public class VideoAdapter extends RecyclerView.Adapter<VideoAdapter.VideoViewHolder> {
    private List<DisplayedMessage> displayedMessageList;
    private LayoutInflater mInflater;

//    MediaController mediaC = new MediaController(mInflater.getContext());

    public VideoAdapter(Context context) {
        displayedMessageList = new ArrayList<>();
        mInflater = LayoutInflater.from(context);
    }

    public void setDisplayedMessageList(List<DisplayedMessage> displayedMessageList) {
        this.displayedMessageList = displayedMessageList;
    }

    @Override
    public VideoAdapter.VideoViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.video_item, parent, false);
        return new VideoAdapter.VideoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(VideoAdapter.VideoViewHolder holder, int position) {
        try {
            holder.bindData(getItemAt(position));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private DisplayedMessage getItemAt(int pos) {
        return displayedMessageList != null ? displayedMessageList.get(pos) : null;
    }

    @Override
    public int getItemCount() {
        return displayedMessageList != null ? displayedMessageList.size() : 0;
    }

    class VideoViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.root_img)
        LinearLayout mLlImageRoot;

        @BindView(R.id.tv_user)
        TextView mTvUser;

        @BindView(R.id.iv_message)
        VideoView mTvVideoMsg;

            @OnClick(R.id.btn_play_video)
    public void vidoPlay(View v) {
        mTvVideoMsg.start();
    }


        public VideoViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }


        public void bindData(DisplayedMessage currentItem) throws IOException {
            if (currentItem != null) {
                if (currentItem.getUserMessage().isFromSender()) {
                    mTvUser.setText("> me(" + currentItem.getInstance().getStringIdentifier() + "):");
                    mLlImageRoot.setGravity(Gravity.END);

                } else {
                    mTvUser.setText("> " + currentItem.getInstance().getStringIdentifier() + ":");
                    mLlImageRoot.setGravity(Gravity.START);

                }

//                Log.i("TAG", currentItem.getUserMessage().getMessage().getData().toString());
                Log.i("TAG", "DONEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEE");
//                Glide.with(mInflater.getContext()).load(currentItem.getUserMessage().getMessage().getData()).into(mTvVideoMsg);
//                mTvVideoMsg.setVideoPath(currentItem.getUserMessage().getMessage().getData());
//                mTvVideoMsg.setMediaController(mediaC);

//                String path = currentItem.getUserMessage().getMessage().getData().toString());
////                String uri = "android.resource://" + getPackageNamegeName() + "/" + R.raw.preview;
//                mTvVideoMsg.setVideoPath(path);

                FileOutputStreamExecute(currentItem.getUserMessage().getMessage().getData());


//                    FileInputStream fis = new FileInputStream(file);
//                    DataInputStream in = new DataInputStream(fis);
//
//                    BufferedReader br = new BufferedReader(new InputStreamReader(in));
//                    Log.i("TAG", br.toString());
//                    Log.i("TAG", "outputtttttttttttttttttttttttttttttttttttttttttttttttttttttt");

//
//
//                Log.i("TAG", file.toString();
//                mTvVideoMsg.setVideoPath(file.getPath());

            }
        }

        public void FileOutputStreamExecute(byte[] fileByte) {
            File file = new File("newFile");
            try {
                BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(file));
                bos.write(fileByte);
                bos.flush();
                bos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }



        }
    }
}
