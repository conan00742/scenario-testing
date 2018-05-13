package krot.sample.com.meshchat.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.bumptech.glide.Glide;

import butterknife.BindView;
import butterknife.ButterKnife;
import krot.sample.com.meshchat.R;
import krot.sample.com.meshchat.adapter.MessageAdapter;

/**
 * Created by Krot on 5/13/18.
 */

public class ImageFragment extends Fragment {

    @BindView(R.id.rv_chat_list)
    RecyclerView mRvImageMessage;

    @BindView(R.id.btn_pick_image)
    Button mBtnPickImage;

    private MessageAdapter mMessageAdapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_send_image, container, false);
        ButterKnife.bind(this, view);
        setupMessageAdapter();
        return view;
    }

    private void setupMessageAdapter() {
        mMessageAdapter = new MessageAdapter(getActivity(), Glide.with(this));
        mRvImageMessage.setAdapter(mMessageAdapter);
        mRvImageMessage.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        mRvImageMessage.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL));

    }

}
