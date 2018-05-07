package krot.sample.com.meshchat.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import krot.sample.com.meshchat.HypeApplication;

/**
 * Created by Krot on 4/29/18.
 */

public abstract class BaseAdapter<V, T extends RecyclerView.ViewHolder> extends RecyclerView.Adapter<T> {

    protected List<V> instanceList;
    private Context mContext;
    private LayoutInflater mInflater;

    public BaseAdapter(Context context) {
        instanceList = new ArrayList<>();
        mContext = context;
        mInflater = LayoutInflater.from(context);
    }

    public void setInstanceList(List<V> instanceList) {
        this.instanceList = instanceList;
    }

    @Override
    public T onCreateViewHolder(ViewGroup parent, int viewType) {
        return createViewHolder(parent);
    }

    @Override
    public void onBindViewHolder(T holder, int position) {
        bindData(holder, position);
    }

    @Override
    public int getItemCount() {
        return (instanceList != null ? instanceList.size() : 0);
    }

    protected V getItemAt(int position) {
        return (instanceList != null ? instanceList.get(position) : null);
    }

    public abstract T createViewHolder(ViewGroup parent);

    public abstract void bindData(T viewHolder, int position);

    protected View inflateLayout(int resId, ViewGroup parent) {
        if (mInflater == null) {
            if (mContext == null) {
                mInflater = LayoutInflater.from(HypeApplication.getAppContext());
            }

            else {
                mInflater = LayoutInflater.from(mContext);
            }
        }

        return mInflater.inflate(resId, parent, false);
    }
}
