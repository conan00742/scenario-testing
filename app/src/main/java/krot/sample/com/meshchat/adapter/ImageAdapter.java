package krot.sample.com.meshchat.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import krot.sample.com.meshchat.R;
import krot.sample.com.meshchat.model.DisplayedMessage;

/**
 * Created by Krot on 5/15/18.
 */

public class ImageAdapter extends RecyclerView.Adapter<ImageAdapter.ImageMessageViewHolder> {

    private LayoutInflater mInflater;
    private List<DisplayedMessage> imageMessageList;
    private Context context;

    public ImageAdapter(Context context) {
        this.context = context;
        imageMessageList = new ArrayList<>();
        mInflater = LayoutInflater.from(context);
    }

    public void setImageMessageList(List<DisplayedMessage> imageMessageList) {
        this.imageMessageList = imageMessageList;
    }

    @Override
    public ImageMessageViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.image_item, parent, false);
        return new ImageMessageViewHolder(view, context);
    }

    @Override
    public void onBindViewHolder(ImageMessageViewHolder holder, int position) {
        holder.bindImageMessage(getMessageAt(position));
    }

    private DisplayedMessage getMessageAt(int pos) {
        return imageMessageList != null ? imageMessageList.get(pos) : null;
    }

    @Override
    public int getItemCount() {
        return imageMessageList != null ? imageMessageList.size() : 0;
    }

    class ImageMessageViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.root_img)
        LinearLayout mLlImageRoot;

        @BindView(R.id.tv_user)
        TextView mTvUser;

        @BindView(R.id.iv_message)
        ImageView mImgMsg;

        private Context mContext;

        public ImageMessageViewHolder(View itemView, Context context) {
            super(itemView);
            mContext = context;
            ButterKnife.bind(this, itemView);
        }

        public void bindImageMessage(DisplayedMessage currentMessage) {
            if (currentMessage != null) {
                if (currentMessage.getUserMessage().isFromSender()) {
                    mTvUser.setText("> me (" + currentMessage.getInstance().getStringIdentifier() + "):");
                    mLlImageRoot.setGravity(Gravity.END);
                } else {
                    mTvUser.setText("> " + currentMessage.getInstance().getStringIdentifier() + ":");
                    mLlImageRoot.setGravity(Gravity.START);
                }

                Glide.with(mContext).load(currentMessage.getUserMessage().getMessage().getData()).apply(new RequestOptions().centerCrop()).into(mImgMsg);
            }
        }
    }
}
