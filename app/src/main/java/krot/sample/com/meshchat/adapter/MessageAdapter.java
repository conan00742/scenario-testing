package krot.sample.com.meshchat.adapter;


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.RequestManager;
import com.hypelabs.hype.Message;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import krot.sample.com.meshchat.R;
import krot.sample.com.meshchat.model.UserMessage;

/**
 * Created by Krot on 5/12/18.
 */

public class MessageAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int PLAINTEXT = 1;
    private static final int IMAGE = 2;

    private Context mContext;
    private RequestManager mGlideManager;
    private List<UserMessage> messageList;
    private LayoutInflater mInflater;
    private String messageType;

    public MessageAdapter(Context context, RequestManager glideManager) {
        this.mContext = context;
        this.mGlideManager = glideManager;
        messageList = new ArrayList<>();
        mInflater = LayoutInflater.from(context);
    }

    public void setMessageList(List<UserMessage> messageList) {
        this.messageList = messageList;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == PLAINTEXT) {
            View view = mInflater.inflate(R.layout.plain_text_item, parent, false);
            return new PlainTextMessageVH(view);
        } else if (viewType == IMAGE){
            View view = mInflater.inflate(R.layout.image_item, parent, false);
            return new ImageMessageVH(view);
        } else {
            return null;
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder.getItemViewType() == PLAINTEXT) {
            ((PlainTextMessageVH) holder).bindPlainTextData(getMessageAt(position));
        } else if (holder.getItemViewType() == IMAGE){
            ((ImageMessageVH) holder).bindImageData(getMessageAt(position));
        } else {

        }
    }

    @Override
    public int getItemCount() {
        return messageList != null ? messageList.size() : 0;
    }

    @Override
    public int getItemViewType(int position) {
        UserMessage currentMessage = getMessageAt(position);
        if (currentMessage != null) {
            if (TextUtils.equals(currentMessage.getMsgType(), "PLAIN_TEXT_TYPE")) {
                return PLAINTEXT;
            } else {
                return IMAGE;
            }
        } else {
            return -1;
        }

    }

    public void setMessageType(String messageType) {
        this.messageType = messageType;
    }

    private UserMessage getMessageAt(int position) {
        return messageList != null ? messageList.get(position) : null;
    }


    class PlainTextMessageVH extends RecyclerView.ViewHolder {

        @BindView(R.id.root_text)
        LinearLayout mLlPlainTextRoot;

        @BindView(R.id.tv_message)
        TextView mTvPlainTextMsg;

        public PlainTextMessageVH(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void bindPlainTextData(UserMessage currentMessage) {
            if (currentMessage != null) {
                if (currentMessage.isFromSender()) {
                    mTvPlainTextMsg.setGravity(Gravity.END);
                } else {
                    mTvPlainTextMsg.setGravity(Gravity.START);
                }

                mTvPlainTextMsg.setText(new String(currentMessage.getMessage().getData()));
            }
        }
    }


    class ImageMessageVH extends RecyclerView.ViewHolder {

        @BindView(R.id.root_img)
        LinearLayout mLlImageRoot;

        @BindView(R.id.iv_message)
        ImageView mImgMsg;

        public ImageMessageVH(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void bindImageData(UserMessage currentMessage) {
            if (currentMessage != null) {
                if (currentMessage.isFromSender()) {
                    mLlImageRoot.setGravity(Gravity.END);
                } else {
                    mLlImageRoot.setGravity(Gravity.START);
                }

                mGlideManager.load(currentMessage.getMessage().getData()).into(mImgMsg);
            }
        }
    }

}
