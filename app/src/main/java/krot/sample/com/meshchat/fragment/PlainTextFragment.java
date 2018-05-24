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
import android.widget.EditText;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.hypelabs.hype.Hype;
import com.hypelabs.hype.Instance;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import krot.sample.com.meshchat.R;
import krot.sample.com.meshchat.adapter.MessageAdapter;
import krot.sample.com.meshchat.adapter.PlainTextAdapter;
import krot.sample.com.meshchat.repository.HypeRepository;
import krot.sample.com.meshchat.widget.EventClearMessage;

/**
 * Created by Krot on 5/13/18.
 */

public class PlainTextFragment extends Fragment {

    @BindView(R.id.rv_chat_list)
    RecyclerView mRvTextMessage;

    @BindView(R.id.edt_message)
    EditText mEdtMessage;

    @BindView(R.id.btn_send)
    Button mBtnSend;

    private PlainTextAdapter mPlainTextAdapter;
    private byte[] msgData;

    public PlainTextAdapter getPlainTextAdapter() {
        return mPlainTextAdapter;
    }

    public byte[] getMessageData() {
        return msgData;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_send_plain_text, container, false);
        ButterKnife.bind(this, view);
        setupMessageAdapter();
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

    private void setupMessageAdapter() {
        mPlainTextAdapter = new PlainTextAdapter(getActivity());
        mRvTextMessage.setAdapter(mPlainTextAdapter);
        mRvTextMessage.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        mRvTextMessage.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL));

    }


    @OnClick(R.id.btn_send)
    public void sendPlainTextMessage(View view) {
        if (HypeRepository.getRepository().getInstanceCount() > 0) {
            doSendMessage(mEdtMessage.getText().toString().trim());
            mEdtMessage.setText(null);
        } else {
            Toast.makeText(getActivity(), "No instance found, can't send.", Toast.LENGTH_SHORT).show();
        }

    }


    private void doSendMessage(String message) {
        msgData = message.getBytes();
        for (int i = 0; i < HypeRepository.getRepository().getInstanceList().size(); i++) {
            Instance currentInstance = HypeRepository.getRepository().getInstanceList().get(i);
            Hype.send(msgData, currentInstance);
        }
    }

    @Subscribe
    public void clearMessage(EventClearMessage eventClearMessage) {
        mPlainTextAdapter.setDisplayedMessageList(HypeRepository.getRepository().getPlainTextMessageList());
        mPlainTextAdapter.notifyDataSetChanged();
    }
}
