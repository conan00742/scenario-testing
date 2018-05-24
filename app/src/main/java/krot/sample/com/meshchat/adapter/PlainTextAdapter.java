package krot.sample.com.meshchat.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import krot.sample.com.meshchat.R;
import krot.sample.com.meshchat.model.DisplayedMessage;

/**
 * Created by Krot on 5/14/18.
 */

public class PlainTextAdapter extends RecyclerView.Adapter<PlainTextAdapter.PlainTextViewHolder> {

    private List<DisplayedMessage> displayedMessageList;
    private LayoutInflater mInflater;

    public PlainTextAdapter(Context context) {
        displayedMessageList = new ArrayList<>();
        mInflater = LayoutInflater.from(context);
    }

    public void setDisplayedMessageList(List<DisplayedMessage> displayedMessageList) {
        this.displayedMessageList = displayedMessageList;
    }

    @Override
    public PlainTextViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.plain_text_item, parent, false);
        return new PlainTextViewHolder(view);
    }

    @Override
    public void onBindViewHolder(PlainTextViewHolder holder, int position) {
        holder.bindData(getItemAt(position));
    }

    private DisplayedMessage getItemAt(int pos) {
        return displayedMessageList != null ? displayedMessageList.get(pos) : null;
    }

    @Override
    public int getItemCount() {
        return displayedMessageList != null ? displayedMessageList.size() : 0;
    }

    class PlainTextViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.root_text)
        LinearLayout mLlPlainTextRoot;

        @BindView(R.id.tv_user)
        TextView mTvUser;

        @BindView(R.id.tv_message)
        TextView mTvPlainTextMsg;


        public PlainTextViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void bindData(DisplayedMessage currentItem) {
            if (currentItem != null) {
                if (currentItem.getUserMessage().isFromSender()) {
                    mTvUser.setText("> me(" + currentItem.getInstance().getStringIdentifier() + "):");
                    mLlPlainTextRoot.setGravity(Gravity.END);
                } else {
                    mTvUser.setText("> " + currentItem.getInstance().getStringIdentifier() + ":");
                    mLlPlainTextRoot.setGravity(Gravity.START);
                }


                mTvPlainTextMsg.setText(new String(currentItem.getUserMessage().getMessage()));
            }
        }
    }

}
