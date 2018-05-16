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
 * Created by jake on 14/05/2018.
 */

public class ImageAdapter extends RecyclerView.Adapter<ImageAdapter.ImageViewHolder> {
    private List<DisplayedMessage> displayedMessageList;
    private LayoutInflater mInflater;

    public ImageAdapter(Context context) {
        displayedMessageList = new ArrayList<>();
        mInflater = LayoutInflater.from(context);
    }


    public void setDisplayedMessageList(List<DisplayedMessage> displayedMessageList) {
        this.displayedMessageList = displayedMessageList;
    }

    @Override
    public ImageViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.image_item, parent, false);
        return new ImageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ImageViewHolder holder, int position) {
        holder.bindData(getItemAt(position));
    }

    private DisplayedMessage getItemAt(int pos) {
        return displayedMessageList != null ? displayedMessageList.get(pos) : null;
    }

    @Override
    public int getItemCount() {
        return displayedMessageList != null ? displayedMessageList.size() : 0;
    }

    class ImageViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.root_img)
        LinearLayout mLlImageRoot;

        @BindView(R.id.tv_user)
        TextView mTvUser;

        @BindView(R.id.iv_message)
        ImageView mTvImageMsg;



        public ImageViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void bindData(DisplayedMessage currentItem) {
            if (currentItem != null) {
                if (currentItem.getUserMessage().isFromSender()) {
                    mTvUser.setText("> me(" + currentItem.getInstance().getStringIdentifier() + "):");
                    mLlImageRoot.setGravity(Gravity.END);

                } else {
                    mTvUser.setText("> " + currentItem.getInstance().getStringIdentifier() + ":");
                    mLlImageRoot.setGravity(Gravity.START);

                }

                Glide.with(mInflater.getContext()).load(currentItem.getUserMessage().getMessage().getData()).into(mTvImageMsg);

            }
        }
    }

}
