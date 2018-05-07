package krot.sample.com.meshchat.adapter;

import android.content.Context;
import android.view.ViewGroup;

import com.hypelabs.hype.Instance;

import krot.sample.com.meshchat.R;
import krot.sample.com.meshchat.adapter.viewholder.InstanceViewHolder;


/**
 * Created by Krot on 4/30/18.
 */

public class InstanceAdapter extends BaseAdapter<Instance, InstanceViewHolder> {

    public InstanceAdapter(Context context) {
        super(context);
    }

    @Override
    public InstanceViewHolder createViewHolder(ViewGroup parent) {
        return new InstanceViewHolder(inflateLayout(R.layout.instance_item, parent));
    }

    @Override
    public void bindData(InstanceViewHolder viewHolder, int position) {
        viewHolder.mTvInstanceId.setText(getItemAt(position).getStringIdentifier());
    }
}
