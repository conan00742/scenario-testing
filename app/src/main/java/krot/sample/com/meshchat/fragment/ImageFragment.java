package krot.sample.com.meshchat.fragment;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.hypelabs.hype.Hype;
import com.hypelabs.hype.Instance;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import krot.sample.com.meshchat.MainActivity;
import krot.sample.com.meshchat.R;
import krot.sample.com.meshchat.adapter.MessageAdapter;
import krot.sample.com.meshchat.repository.HypeRepository;

import static android.app.Activity.RESULT_OK;

/**
 * Created by Krot on 5/13/18.
 */

public class ImageFragment extends Fragment {

    @BindView(R.id.rv_chat_list)
    RecyclerView mRvImageMessage;

    @BindView(R.id.btn_pick_image)
    Button mBtnPickImage;

    @BindView(R.id.imageToUpload)
    ImageView imageToUpload;

    private MessageAdapter mMessageAdapter;

    private static final int RESULT_LOAD_IMAGE = 1;

    private byte[] imageByte;

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

    @OnClick(R.id.btn_pick_image)
    public void sendPickImage(View view) {
        Log.i("TAG", "sendPickImage");
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, RESULT_LOAD_IMAGE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK && data != null) {
            Uri selectedImage = data.getData();
            byte[] imageByte = convertImageToByte(selectedImage);
//            Glide.with(ImageFragment.this).load(imageByte).into(imageToUpload);
//            imageToUpload.setImageURI(selectedImage);
//            runOnUiThread(new Runnable() {
//                @Override
//                public void run() {
//
//                }
//            });
//            Hype.send(im)
            for (int i = 0; i < HypeRepository.getRepository().getInstanceList().size(); i++) {
                Instance currentInstance = HypeRepository.getRepository().getInstanceList().get(i);
                Hype.send(imageByte, currentInstance);
            }
        }
    }

    public byte[] convertImageToByte(Uri uri){
        byte[] data = null;
        try {
            ContentResolver cr = getActivity().getBaseContext().getContentResolver();
            InputStream inputStream = cr.openInputStream(uri);
            Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
            data = baos.toByteArray();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return data;
    }

    public void showImageToUpload(final byte[] imageByte) {
        Log.i("TAG", imageToUpload.toString());
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Glide.with(ImageFragment.this).load(imageByte).into(imageToUpload);
            }
        });
    }

}
