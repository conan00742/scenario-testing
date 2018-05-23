package krot.sample.com.meshchat.fragment;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.MediaController;
import android.widget.TextView;
import com.hypelabs.hype.Hype;
import com.hypelabs.hype.Instance;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import krot.sample.com.meshchat.R;
import krot.sample.com.meshchat.adapter.VideoAdapter;
import krot.sample.com.meshchat.repository.HypeRepository;

import static android.app.Activity.RESULT_OK;

/**
 * Created by Krot on 5/13/18.
 */

public class VideoFragment extends Fragment {

    private final int PICK_FILE_REQUEST = 109;

    @BindView(R.id.rv_chat_list)
    RecyclerView mRvVideoMessage;

    @BindView(R.id.btn_pick_video)
    Button mBtnPickVideo;

    private VideoAdapter videoAdapter;
    private byte[] messageData;
    private Uri videoUri;
    private String videoPath;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    public byte[] getMessageData() {
        return messageData;
    }

    public Uri getVideoUri() {
        return videoUri;
    }

    public String getVideoPath() {
        return videoPath;
    }

    public VideoAdapter getVideoAdapter() {
        return videoAdapter;
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
        videoAdapter = new VideoAdapter(getActivity());
        mRvVideoMessage.setAdapter(videoAdapter);
        mRvVideoMessage.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        mRvVideoMessage.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL));

    }

    @OnClick(R.id.btn_pick_video)
    public void sendVideoMessage(View view) {
        if (HypeRepository.getRepository().getInstanceCount() > 0) {
            Intent chooseImageIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Video.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(Intent.createChooser(chooseImageIntent, "Choose an app below: "), PICK_FILE_REQUEST);
        }
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PICK_FILE_REQUEST) {

            if (resultCode == RESULT_OK && data != null && data.getData() != null) {
                videoUri = data.getData();
                Log.i("WTF", "data[1] = " + videoUri + " - fileName = " + videoUri.getLastPathSegment());
                videoPath = getPath(videoUri);
                Log.i("WTF", "path = " + videoPath);

                try {
                    messageData = convert(videoPath);
                    Log.i("WTF", "message = " + messageData + " /// length = " + messageData.length);
                    for (int i = 0; i < HypeRepository.getRepository().getInstanceCount(); i++) {
                        Instance currentInstance = HypeRepository.getRepository().getInstanceList().get(i);
                        Hype.send(messageData, currentInstance);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        }
    }



//    public String getPath(Uri uri) {
//        String res = null;
//        String[] proj = {MediaStore.Images.Media.DATA};
//        Cursor cursor = getActivity().getContentResolver().query(uri, proj, null, null, null);
//        if (cursor.moveToFirst()) {
//            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
//            res = cursor.getString(column_index);
//        }
//        cursor.close();
//        return res;
//    }
//
//
//    public byte[] convert(Uri videoUri) throws IOException {
//
////        InputStream inputStream = getActivity().getContentResolver().openInputStream(videoUri);
////        ByteArrayOutputStream byteBuffer = new ByteArrayOutputStream();
////
////        // this is storage overwritten on each iteration with bytes
////        int bufferSize = 1024;
////        byte[] buffer = new byte[bufferSize];
////
////        // we need to know how may bytes were read to write them to the byteBuffer
////        int len = 0;
////        while ((len = inputStream.read(buffer)) != -1) {
////            byteBuffer.write(buffer, 0, len);
////        }
////
////        Log.i("WTF", "videoBytes[] = " + byteBuffer.toByteArray());
////
////        // and then we can return your byte array.
////        return byteBuffer.toByteArray();
//
//
//        ByteArrayOutputStream baos = new ByteArrayOutputStream();
//        FileInputStream fis;
//        try {
//            fis = new FileInputStream(new File(videoUri.getPath()));
//            byte[] buf = new byte[16384];
//            int n;
//            while (-1 != (n = fis.read(buf)))
//                baos.write(buf, 0, n);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        byte[] bbytes = baos.toByteArray();
//
//
//        Log.i("WTF", "videoBytes[] = " + bbytes.toString() + " /// length = " + bbytes.length);
//
//        return bbytes;
//    }


    public String getPath(Uri uri) {
        String res = null;
        String[] proj = {MediaStore.Images.Media.DATA};
        Cursor cursor = getActivity().getContentResolver().query(uri, proj, null, null, null);
        if (cursor.moveToFirst()) {
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            res = cursor.getString(column_index);
        }
        cursor.close();
        return res;
    }


    public byte[] convert(String path) throws IOException {

        FileInputStream fis = new FileInputStream(path);
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        byte[] b = new byte[16 * 1024];

        for (int readNum; (readNum = fis.read(b)) != -1;) {
            bos.write(b, 0, readNum);
        }

        byte[] bytes = bos.toByteArray();

        return bytes;
    }



}
