package krot.sample.com.meshchat;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.hypelabs.hype.Error;
import com.hypelabs.hype.Hype;
import com.hypelabs.hype.Instance;
import com.hypelabs.hype.Message;
import com.hypelabs.hype.MessageInfo;
import com.hypelabs.hype.MessageObserver;
import com.hypelabs.hype.NetworkObserver;
import com.hypelabs.hype.State;
import com.hypelabs.hype.StateObserver;


import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import krot.sample.com.meshchat.adapter.CustomPagerAdapter;
import krot.sample.com.meshchat.adapter.InstanceAdapter;
import krot.sample.com.meshchat.fragment.ImageFragment;
import krot.sample.com.meshchat.fragment.PlainTextFragment;
import krot.sample.com.meshchat.fragment.VideoFragment;
import krot.sample.com.meshchat.model.DisplayedMessage;
import krot.sample.com.meshchat.model.UserMessage;
import krot.sample.com.meshchat.repository.HypeRepository;
import krot.sample.com.meshchat.widget.EventClearMessage;

public class MainActivity extends AppCompatActivity implements StateObserver, NetworkObserver, MessageObserver {

    private static final int PERMISSION_CODE = 101;
    private static final int REQUEST_PERMISSION_SETTING = 102;
    private static final String TITLE = "Home";

    @Nullable
    @BindView(R.id.main_toolbar)
    Toolbar mToolbar;

    @Nullable
    @BindView(R.id.tv_headerDeviceName)
    TextView mDeviceName;

    @Nullable
    @BindView(R.id.iv_status)
    ImageView mIvStatus;

    @Nullable
    @BindView(R.id.recycler_view)
    RecyclerView mRecyclerView;

    @Nullable
    @BindView(R.id.main_tabs)
    TabLayout mMainTabs;

    @Nullable
    @BindView(R.id.main_pager)
    ViewPager mMainPager;

    private Context mContext;
    private Unbinder mUnbinder;
    private InstanceAdapter mAdapter;
    private boolean isStarted = false;
    private boolean isOnline = false;
    private static final int PICK_FILE_REQUEST_CODE = 1001;
    public static final String PLAIN_TEXT_MESSAGE = "PLAIN_TEXT_TYPE";
    public static final String PICTURE_MESSAGE = "IMAGE_TYPE";
    public static final String VIDEO_MESSAGE = "VIDEO_MESSAGE";
    private byte[] msgData;
    private String msgType = PLAIN_TEXT_MESSAGE;
    private CustomPagerAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mUnbinder = ButterKnife.bind(this);
        mContext = this;
        mDeviceName.setText(Build.MODEL);
        mToolbar.setTitle(TITLE);
        mToolbar.setTitleTextColor(ContextCompat.getColor(mContext, R.color.toolbar_title));
        setSupportActionBar(mToolbar);
        setupViewPager();
        setupTabsLayout();

        //check permission
        String[] permission = {Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE};
        if (!hasPermission(this, permission)) {
            ActivityCompat.requestPermissions(this, permission, PERMISSION_CODE);
        } else {
            //do something else
            setupAdapter();
            Hype.setContext(mContext);
            Hype.setAppIdentifier("f0441ff3");
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.application_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.mn_broadcast:
                return true;

            case R.id.mn_multicast:
                return true;

            case R.id.mn_unicast:
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        stopHype();
        Hype.removeMessageObserver(this);
        Hype.removeNetworkObserver(this);
        Hype.removeStateObserver(this);
    }


    //MESSAGE OBSERVER
    @Override
    public void onHypeMessageReceived(Message message, final Instance instance) {
        //làm sao để detect message type là gì
        Fragment currentFragment = adapter.getFragmentList().get(mMainPager.getCurrentItem());
        if (currentFragment instanceof PlainTextFragment) {
            final PlainTextFragment plainTextFragment = (PlainTextFragment) currentFragment;

            UserMessage plainTextMsg = new UserMessage(message.getData(), false);
            DisplayedMessage displayedMessage = new DisplayedMessage(instance, plainTextMsg);
            HypeRepository.getRepository().addDisplayedPlainTextMsg(displayedMessage);
            plainTextFragment.getPlainTextAdapter().setDisplayedMessageList(HypeRepository.getRepository().getPlainTextMessageList());
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(mContext, "receive from " + instance.getStringIdentifier(), Toast.LENGTH_SHORT).show();
                    plainTextFragment.getPlainTextAdapter().notifyDataSetChanged();
                }
            });
        } else if (currentFragment instanceof ImageFragment) {
            final ImageFragment imageFragment = (ImageFragment) currentFragment;
            UserMessage imageMsg = new UserMessage(message.getData(), false);
            DisplayedMessage displayedMessage = new DisplayedMessage(instance, imageMsg);
            HypeRepository.getRepository().addImageMsg(displayedMessage);
            imageFragment.getImageAdapter().setDisplayedMessageList(HypeRepository.getRepository().getImageMessageList());

            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(mContext, "receive from " + instance.getStringIdentifier(), Toast.LENGTH_SHORT).show();
                    imageFragment.getImageAdapter().notifyDataSetChanged();
                }
            });

        } else if (currentFragment instanceof VideoFragment) {
            final VideoFragment videoFragment = (VideoFragment) currentFragment;
            UserMessage videoMsg = new UserMessage(message.getData(), false);
            videoMsg.setVideoPath(Utils.saveFile(message.getData()));
            DisplayedMessage videoDisplayedMessage = new DisplayedMessage(Hype.getHostInstance(), videoMsg);
            HypeRepository.getRepository().addVideoMsg(videoDisplayedMessage);
            videoFragment.getVideoAdapter().setDisplayedMessageList(HypeRepository.getRepository().getVideoMessageList());
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(mContext, "receive from " + instance.getStringIdentifier(), Toast.LENGTH_SHORT).show();
                    videoFragment.getVideoAdapter().notifyDataSetChanged();
                }
            });
        }

//        for (int i = 0; i < HypeRepository.getRepository().getInstanceList().size(); i++) {
//            Instance currentInstance = HypeRepository.getRepository().getInstanceList().get(i);
//            if (!TextUtils.equals(currentInstance.getStringIdentifier(), instance.getStringIdentifier())) {
//                Hype.send(message.getData(), currentInstance, true);
//            }
//        }
    }

    @Override
    public void onHypeMessageFailedSending(MessageInfo messageInfo, final Instance instance, final Error error) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(mContext, "failed sending to " + instance.getStringIdentifier(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onHypeMessageSent(MessageInfo messageInfo, final Instance instance, float v, boolean b) {
        if (v == 1.0) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(mContext, "sent to " + instance.getStringIdentifier(), Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    @Override
    public void onHypeMessageDelivered(MessageInfo messageInfo, final Instance instance, float v, boolean b) {
        if (v == 1.0) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(mContext, "delivered to " + instance.getStringIdentifier(), Toast.LENGTH_SHORT).show();
                }
            });
        }
    }


    //NETWORK OBSERVER
    @Override
    public void onHypeInstanceFound(final Instance instance) {
        Hype.resolve(instance);
    }

    @Override
    public void onHypeInstanceLost(final Instance instance, Error error) {
        if (HypeRepository.getRepository().isInstanceExisted(instance)) {
            HypeRepository.getRepository().removeInstance(instance);
            mAdapter.setInstanceList(HypeRepository.getRepository().getInstanceList());
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(mContext, "Lost: " + instance.getStringIdentifier(), Toast.LENGTH_SHORT).show();
                    mAdapter.notifyDataSetChanged();
                }
            });
        }
    }

    @Override
    public void onHypeInstanceResolved(final Instance instance) {
        if (!HypeRepository.getRepository().isInstanceExisted(instance)) {
            HypeRepository.getRepository().addInstance(instance);
            mAdapter.setInstanceList(HypeRepository.getRepository().getInstanceList());
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(mContext, "Found + Resolved: " + instance.getStringIdentifier(), Toast.LENGTH_SHORT).show();
                    mAdapter.notifyDataSetChanged();
                }
            });
        }
    }

    @Override
    public void onHypeInstanceFailResolving(Instance instance, Error error) {

    }


    //STATE OBSERVER
    @Override
    public void onHypeStart() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(mContext, "STARTED", Toast.LENGTH_SHORT).show();
                if (mIvStatus != null) {
                    mIvStatus.setImageDrawable(ContextCompat.getDrawable(mContext, R.drawable.ic_online));
                }
            }
        });

        isStarted = true;
        isOnline = true;

    }

    @Override
    public void onHypeStop(Error error) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(mContext, "STOPPED", Toast.LENGTH_SHORT).show();
                mIvStatus.setImageDrawable(ContextCompat.getDrawable(mContext, R.drawable.ic_offline));
            }
        });

        isStarted = false;
        isOnline = false;
    }

    @Override
    public void onHypeFailedStarting(Error error) {
        Log.i("WTF", "onHypeFailedStarting");
    }

    @Override
    public void onHypeReady() {
    }

    @Override
    public void onHypeStateChange() {
        //0 Idle
        //1 Starting
        //2 Running
        //3 Stopping

        if (Hype.getState() == State.Stopping) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    mIvStatus.setImageDrawable(ContextCompat.getDrawable(mContext, R.drawable.ic_offline));
                }
            });

        } else {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    mIvStatus.setImageDrawable(ContextCompat.getDrawable(mContext, R.drawable.ic_online));
                }
            });

        }
    }

    @Override
    public String onHypeRequestAccessToken(int i) {
        return "064e04e5ab0669db7eaa5561eb8dde";
    }


    //runtime permission
    private boolean hasPermission(Context context, String... permissionQueue) {
        if (context != null && permissionQueue != null) {
            for (String permission : permissionQueue) {
                if (ContextCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
        }

        return true;
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_CODE:
                if (grantResults.length > 0) {
                    if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                        setupAdapter();
                        Hype.setContext(mContext);
                        Hype.setAppIdentifier("f0441ff3");
                    } else if (grantResults[0] == PackageManager.PERMISSION_DENIED) {
                        boolean shouldShowRationale = ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_COARSE_LOCATION);
                        if (shouldShowRationale) {
                            //show the reason why user must grant STORAGE permission
                            //show dialog
                            new AlertDialog.Builder(this).setTitle("Permission Denied").setMessage(R.string.permission_rationale).setPositiveButton("RE-TRY", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, PERMISSION_CODE);
                                }
                            }).setNegativeButton("I'M SURE", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    dialogInterface.dismiss();
                                }
                            }).show();

                        } else {
                            //never ask again
                            //close dialog and do nothing
                            new AlertDialog.Builder(this)
                                    .setTitle("Grant permission")
                                    .setMessage(R.string.app_setting_permission)
                                    .setPositiveButton("Open", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {
                                            Intent appSettingIntent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                                            Uri uri = Uri.fromParts("package", getPackageName(), null);
                                            appSettingIntent.setData(uri);
                                            startActivityForResult(appSettingIntent, REQUEST_PERMISSION_SETTING);
                                        }
                                    })
                                    .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {
                                            dialogInterface.dismiss();
                                        }
                                    }).show();
                        }
                    }
                }
                break;
        }
    }

//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        if (requestCode == PICK_FILE_REQUEST_CODE) {
//
//            if (resultCode == RESULT_OK && data != null && data.getData() != null) {
//                Uri filePath = data.getData();
//                Log.i("WTF", "data = " + data.getData());
//                String messageType = "";
//
//                if (filePath != null) {
//
//                    String filePathInString = filePath.toString();
//                    Log.i("WTF", "filePathInString = " + filePathInString);
//                    if (filePathInString.contains("image")) {
//                        messageType = PICTURE_MESSAGE;
//                    } else if (filePathInString.contains("video")) {
//                        messageType = VIDEO_MESSAGE;
//                    }
//
//                    Log.i("WTF", "scheme = " + filePath.getScheme());
//                    String fileName = filePath.getLastPathSegment();
//
//
//                    if (messageType.equals(PICTURE_MESSAGE)) {
//                        try {
//                            Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
//
//                            //rotate if needed
//                            Bitmap newBitmapImg = ImageUtils.rotateImageIfRequired(bitmap, this, filePath);
//                            int imageWidth = newBitmapImg.getWidth();
//                            int imageHeight = newBitmapImg.getHeight();
//
//
//                            //show dialog
//                            createImageSettingDialog(newBitmapImg, fileName, filePath, imageWidth, imageHeight);
//                        } catch (FileNotFoundException e) {
//                            e.printStackTrace();
//                        } catch (IOException e) {
//                            e.printStackTrace();
//                        }
//                    }
//
//                }
//
//            }
//        }
//    }


//    public void createImageSettingDialog(final Bitmap bitmap, final String fileName, final Uri imgPath, final int imageWidth, final int imageHeight) {
//        final android.support.v7.app.AlertDialog.Builder mSettingDialogBuilder = new android.support.v7.app.AlertDialog.Builder(this);
//        LayoutInflater mInflater = this.getLayoutInflater();
//        final View mDialogView = mInflater.inflate(R.layout.image_setting_dialog, null);
//        mSettingDialogBuilder.setView(mDialogView);
//        final android.support.v7.app.AlertDialog settingDialog = mSettingDialogBuilder.create();
//
//        //findViewByIds
//        CustomChatMessageImageView mSelectedPicture = mDialogView.findViewById(R.id.selected_picture);
//        ImageView mIconCloseDialog = mDialogView.findViewById(R.id.icon_close_dialog);
//        ImageView mIconSendPicture = mDialogView.findViewById(R.id.icon_send_picture);
//
//        mSelectedPicture.setImageWidth(imageWidth);
//        mSelectedPicture.setImageHeight(imageHeight);
//        mSelectedPicture.setImageBitmap(bitmap);
//        mIconCloseDialog.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                settingDialog.dismiss();
//            }
//        });
//
//        mIconSendPicture.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                //upload image to firebase storage
//                settingDialog.dismiss();
//                //send image message
//                sendImageMessage(bitmap);
//            }
//        });
//
//
//        settingDialog.show();
//    }


    //instance adapter
    private void setupAdapter() {
        mAdapter = new InstanceAdapter(mContext);
        if (mRecyclerView != null) {
            mRecyclerView.setAdapter(mAdapter);
            mRecyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this, LinearLayoutManager.VERTICAL, false));
        }

    }

    //message adapter
//    private void setupMessageAdapter() {
//        mMessageAdapter = new MessageAdapter(mContext, Glide.with(this));
//        if (mRvChatList != null) {
//            mRvChatList.setAdapter(mMessageAdapter);
//            mRvChatList.setLayoutManager(new LinearLayoutManager(MainActivity.this, LinearLayoutManager.VERTICAL, false));
//            mRvChatList.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
//        }
//
//    }

    @OnClick({R.id.tv_start, R.id.tv_stop})
    public void handleEventClick(View view) {
        switch (view.getId()) {
            case R.id.tv_start:
                startHype();
                break;
            case R.id.tv_stop:
                if (Hype.getState() == State.Running) {
                    stopHype();
                }
                break;
        }
    }

//    private void chooseFile() {
//        Intent chooseImageIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
//        chooseImageIntent.setType("*/*");
//        startActivityForResult(Intent.createChooser(chooseImageIntent, "Choose an app below: "), PICK_FILE_REQUEST_CODE);
//    }

//    private void sendPlainTextMessage(String message) {
//        msgData = message.getBytes();
//        for (int i = 0; i < HypeRepository.getRepository().getInstanceList().size(); i++) {
//            Instance currentInstance = HypeRepository.getRepository().getInstanceList().get(i);
//            Hype.send(msgData, currentInstance);
//        }
//    }

//    private void sendImageMessage(Bitmap imgMessageData) {
//        msgType = PICTURE_MESSAGE;
//        ByteArrayOutputStream stream = new ByteArrayOutputStream();
//        imgMessageData.compress(Bitmap.CompressFormat.PNG, 100, stream);
//        byte[] byteArray = stream.toByteArray();
//        imgMessageData.recycle();
//        msgData = byteArray;
//        for (int i = 0; i < HypeRepository.getRepository().getInstanceList().size(); i++) {
//            Instance currentInstance = HypeRepository.getRepository().getInstanceList().get(i);
//            Hype.send(msgData, currentInstance);
//        }
//    }


    private void startHype() {
        Hype.addStateObserver(this);
        Hype.addNetworkObserver(this);
        Hype.addMessageObserver(this);
        Hype.start();
    }

    private void stopHype() {
        Hype.stop();
        HypeRepository.getRepository().getInstanceList().clear();
        HypeRepository.getRepository().getPlainTextMessageList().clear();
        HypeRepository.getRepository().getImageMessageList().clear();
        HypeRepository.getRepository().getVideoMessageList().clear();

        mAdapter.setInstanceList(HypeRepository.getRepository().getInstanceList());
        mAdapter.notifyDataSetChanged();
        EventBus.getDefault().post(new EventClearMessage());
    }

    private void setupTabsLayout() {
        if (mMainTabs != null) {
            mMainTabs.setTabTextColors(Color.BLACK, Color.MAGENTA);
            mMainTabs.setupWithViewPager(mMainPager);
        }

    }

    private void setupViewPager() {
        List<Fragment> fragmentList = new ArrayList<>();
        fragmentList.add(new PlainTextFragment());
        fragmentList.add(new ImageFragment());
        fragmentList.add(new VideoFragment());
        adapter = new CustomPagerAdapter(fragmentList, getSupportFragmentManager());
        if (mMainPager != null) {
            mMainPager.setAdapter(adapter);
        }
    }




}
