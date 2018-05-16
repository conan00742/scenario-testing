package krot.sample.com.meshchat.fragment;

import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
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

import com.bumptech.glide.Glide;
import com.hypelabs.hype.Hype;
import com.hypelabs.hype.Instance;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import krot.sample.com.meshchat.HypeApplication;
import krot.sample.com.meshchat.ImageUtils;
import krot.sample.com.meshchat.MainActivity;
import krot.sample.com.meshchat.R;
import krot.sample.com.meshchat.adapter.ImageAdapter;
import krot.sample.com.meshchat.adapter.MessageAdapter;
import krot.sample.com.meshchat.repository.HypeRepository;
import krot.sample.com.meshchat.widget.CustomChatMessageImageView;

import static android.app.Activity.RESULT_OK;
import static krot.sample.com.meshchat.MainActivity.PICTURE_MESSAGE;

/**
 * Created by Krot on 5/13/18.
 */

public class ImageFragment extends Fragment {
    private static final int RESULT_LOAD_IMAGE = 1;

    private final int PICK_FILE_REQUEST_CODE = 405;

    @BindView(R.id.rv_chat_list)
    RecyclerView mRvImageMessage;

    @BindView(R.id.btn_pick_image)
    Button mBtnPickImage;

    private ImageAdapter mMessageAdapter;
    private byte[] msgData;

    public ImageAdapter getImageAdapter() {
        return mMessageAdapter;
    }

    public byte[] getMessageData() {
        return msgData;
    }

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
        mMessageAdapter = new ImageAdapter(getActivity());
        mRvImageMessage.setAdapter(mMessageAdapter);
        mRvImageMessage.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        mRvImageMessage.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL));

    }


    @OnClick(R.id.btn_pick_image)
    public void sendPickImage(View view) {
        if (HypeRepository.getRepository().getInstanceCount() > 0) {
            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(intent, RESULT_LOAD_IMAGE);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK && data != null) {
            Uri selectedImage = data.getData();
            byte[] imageByte = convertImageToByte(selectedImage);
            msgData = imageByte;
            for (int i = 0; i < HypeRepository.getRepository().getInstanceList().size(); i++) {
                Instance currentInstance = HypeRepository.getRepository().getInstanceList().get(i);
                Hype.send(imageByte, currentInstance);
            }
        }
    }


    public void createImageSettingDialog(final Bitmap bitmap, final String fileName, final Uri imgPath, final int imageWidth, final int imageHeight) {
        final android.support.v7.app.AlertDialog.Builder mSettingDialogBuilder = new android.support.v7.app.AlertDialog.Builder(HypeApplication.getAppContext());
        LayoutInflater mInflater = this.getLayoutInflater();
        final View mDialogView = mInflater.inflate(R.layout.image_setting_dialog, null);
        mSettingDialogBuilder.setView(mDialogView);
        final android.support.v7.app.AlertDialog settingDialog = mSettingDialogBuilder.create();

        //findViewByIds
        CustomChatMessageImageView mSelectedPicture = mDialogView.findViewById(R.id.selected_picture);
        ImageView mIconCloseDialog = mDialogView.findViewById(R.id.icon_close_dialog);
        ImageView mIconSendPicture = mDialogView.findViewById(R.id.icon_send_picture);

        mSelectedPicture.setImageWidth(imageWidth);
        mSelectedPicture.setImageHeight(imageHeight);
        mSelectedPicture.setImageBitmap(bitmap);
        mIconCloseDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                settingDialog.dismiss();
            }
        });

        mIconSendPicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //upload image to firebase storage
                settingDialog.dismiss();
                //send image message
                sendImageMessage(bitmap);
            }
        });


        settingDialog.show();
    }


    private void sendImageMessage(Bitmap imgMessageData) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        imgMessageData.compress(Bitmap.CompressFormat.PNG, 100, stream);
        byte[] byteArray = stream.toByteArray();
        imgMessageData.recycle();
        msgData = byteArray;
        for (int i = 0; i < HypeRepository.getRepository().getInstanceList().size(); i++) {
            Instance currentInstance = HypeRepository.getRepository().getInstanceList().get(i);
            Hype.send(msgData, currentInstance);
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

}
