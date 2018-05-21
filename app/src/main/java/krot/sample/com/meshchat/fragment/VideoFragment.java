package krot.sample.com.meshchat.fragment;

import android.content.Intent;
import android.database.Cursor;
import android.icu.text.SymbolTable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
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
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.VideoView;

import com.bumptech.glide.Glide;
import com.hypelabs.hype.Hype;
import com.hypelabs.hype.Instance;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import krot.sample.com.meshchat.R;
import krot.sample.com.meshchat.adapter.ImageAdapter;
import krot.sample.com.meshchat.adapter.MessageAdapter;
import krot.sample.com.meshchat.adapter.VideoAdapter;
import krot.sample.com.meshchat.repository.HypeRepository;

import static android.app.Activity.RESULT_OK;

/**
 * Created by Krot on 5/13/18.
 */

public class VideoFragment extends Fragment {

    @BindView(R.id.rv_chat_list)
    RecyclerView mRvVideoMessage;

    @BindView(R.id.btn_pick_video)
    Button mBtnPickVideo;

    private VideoAdapter mMessageAdapter;
    private static final int SELECT_VIDEO = 3;

    private String selectedPath;
    private byte[] msgData;

    @BindView(R.id.textView)
    TextView textView;

//    @BindView(R.id.videoView)
//    VideoView videoView;

    MediaController mediaC;

    public VideoAdapter getVideoAdapter() {
        return mMessageAdapter;
    }

    public byte[] getMessageData() {
        return msgData;
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mediaC = new MediaController(this.getContext());
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_send_video, container, false);
        ButterKnife.bind(this, view);
        setupMessageAdapter();
        return view;
    }


    private void setupMessageAdapter() {
        mMessageAdapter = new VideoAdapter(getActivity());
        mRvVideoMessage.setAdapter(mMessageAdapter);
        mRvVideoMessage.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        mRvVideoMessage.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL));

    }

    @OnClick(R.id.btn_pick_video)
    public void chooseVideo(View view) {
        Intent intent = new Intent();
        intent.setType("video/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select a Video "), SELECT_VIDEO);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            if (requestCode == SELECT_VIDEO) {
                System.out.println("SELECT_VIDEO");
                Uri selectedImageUri = data.getData();
                selectedPath = getPath(selectedImageUri);
                textView.setText(selectedPath);
//                videoView.setVideoPath(selectedPath);
//                videoView.setMediaController(mediaC);

                byte[] bFile = readBytesFromFile(selectedPath);
                msgData = bFile;

                for (int i = 0; i < HypeRepository.getRepository().getInstanceList().size(); i++) {
                    Instance currentInstance = HypeRepository.getRepository().getInstanceList().get(i);
                    Hype.send(msgData, currentInstance);
                }
            }

        }
    }

    public String getPath(Uri uri) {
        Cursor cursor = getContext().getContentResolver().query(uri, null, null, null, null);
        cursor.moveToFirst();
        String document_id = cursor.getString(0);
        document_id = document_id.substring(document_id.lastIndexOf(":") + 1);
        cursor.close();

        cursor = getContext().getContentResolver().query(
                android.provider.MediaStore.Video.Media.EXTERNAL_CONTENT_URI,
                null, MediaStore.Images.Media._ID + " = ? ", new String[]{document_id}, null);
        cursor.moveToFirst();
        String path = cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media.DATA));
        cursor.close();

        return path;
    }

//    @OnClick(R.id.btn_play_video)
//    public void vidoPlay(View v) {
//        videoView.start();
//    }

    private static byte[] readBytesFromFile(String filePath) {

        FileInputStream fileInputStream = null;
        byte[] bytesArray = null;

        try {

            File file = new File(filePath);
            bytesArray = new byte[(int) file.length()];

            //read file into bytes[]
            fileInputStream = new FileInputStream(file);
            fileInputStream.read(bytesArray);

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (fileInputStream != null) {
                try {
                    fileInputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        }

        return bytesArray;

    }
}
