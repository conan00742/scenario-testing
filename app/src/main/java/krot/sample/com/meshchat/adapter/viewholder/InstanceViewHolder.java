package krot.sample.com.meshchat.adapter.viewholder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import krot.sample.com.meshchat.R;


/**
 * Created by Krot on 4/30/18.
 */

public class InstanceViewHolder extends RecyclerView.ViewHolder {

    public TextView mTvInstanceId;

    public InstanceViewHolder(View itemView) {
        super(itemView);
        mTvInstanceId = itemView.findViewById(R.id.tv_instanceId);
    }
}
