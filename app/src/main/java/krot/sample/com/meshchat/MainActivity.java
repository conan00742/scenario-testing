package krot.sample.com.meshchat;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
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

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import krot.sample.com.meshchat.adapter.InstanceAdapter;
import krot.sample.com.meshchat.repository.InstanceRepository;

public class MainActivity extends AppCompatActivity implements StateObserver, NetworkObserver, MessageObserver {

    private static final int PERMISSION_CODE = 101;
    private static final int REQUEST_PERMISSION_SETTING = 102;
    private static final String TITLE = "Home";

    @BindView(R.id.main_toolbar)
    Toolbar mToolbar;
    @BindView(R.id.fragment_container)
    RelativeLayout mFragmentContainer;
    @BindView(R.id.tv_headerDeviceName)
    TextView mDeviceName;
    @BindView(R.id.iv_status)
    ImageView mIvStatus;
    @BindView(R.id.recycler_view)
    RecyclerView mRecyclerView;
    @BindView(R.id.edt_message)
    EditText mEdtMessage;
    @BindView(R.id.tv_message_content)
    TextView mTvMessageContent;

    private Context mContext;
    private Unbinder mUnbinder;
    private InstanceAdapter mAdapter;
    private boolean isStarted = false;
    private boolean isOnline = false;


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
        mUnbinder.unbind();
        Log.i("WTF", "Hype.getState() = " + Hype.getState());
        stopHype();
    }

    //MESSAGE OBSERVER
    @Override
    public void onHypeMessageReceived(Message message, final Instance instance) {
        final String receivedMessage = new String(message.getData());
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(mContext, "received message from " + instance.getStringIdentifier(), Toast.LENGTH_SHORT).show();
                mTvMessageContent.setText(receivedMessage);
            }
        });

        for (int i = 0; i < InstanceRepository.getRepository().getInstanceList().size(); i++) {
            Instance currentInstance = InstanceRepository.getRepository().getInstanceList().get(i);
            if (!TextUtils.equals(currentInstance.getStringIdentifier(), instance.getStringIdentifier())) {
                Hype.send(message.getData(), currentInstance);
            }
        }
    }

    @Override
    public void onHypeMessageFailedSending(MessageInfo messageInfo, final Instance instance, final Error error) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(mContext, "failed sending to " + instance.getStringIdentifier() + " /// reason = " + error.getReason() + " /// description = " + error.getDescription(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onHypeMessageSent(MessageInfo messageInfo, final Instance instance, float v, boolean b) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(mContext, "sent to " + instance.getStringIdentifier(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onHypeMessageDelivered(MessageInfo messageInfo, final Instance instance, float v, boolean b) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(mContext, "delivered to " + instance.getStringIdentifier(), Toast.LENGTH_SHORT).show();
            }
        });
    }


    //NETWORK OBSERVER
    @Override
    public void onHypeInstanceFound(Instance instance) {
        Log.i("WTF", "found " + instance.getStringIdentifier());
        Hype.resolve(instance);
    }

    @Override
    public void onHypeInstanceLost(Instance instance, Error error) {
        Log.i("WTF", "lost " + instance.getStringIdentifier());
        if (InstanceRepository.getRepository().isInstanceExisted(instance)) {
            Log.i("WTF", "removed");
            InstanceRepository.getRepository().removeInstance(instance);
            mAdapter.setInstanceList(InstanceRepository.getRepository().getInstanceList());
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    mAdapter.notifyDataSetChanged();
                }
            });
        }
    }

    @Override
    public void onHypeInstanceResolved(Instance instance) {
        Log.i("WTF", "resolved " + instance.getStringIdentifier());
        if (!InstanceRepository.getRepository().isInstanceExisted(instance)) {
            Log.i("WTF", "added");
            InstanceRepository.getRepository().addInstance(instance);
            mAdapter.setInstanceList(InstanceRepository.getRepository().getInstanceList());
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
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
        Log.i("WTF", "onHypeStart");
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mIvStatus.setImageDrawable(ContextCompat.getDrawable(mContext, R.drawable.ic_online));
            }
        });

        isStarted = true;
        isOnline = true;

    }

    @Override
    public void onHypeStop(Error error) {
        Log.i("WTF", "onHypeStop: error = " + error.getDescription());
        InstanceRepository.getRepository().getInstanceList().clear();
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mIvStatus.setImageDrawable(ContextCompat.getDrawable(mContext, R.drawable.ic_offline));
            }
        });

        isStarted = false;
        isOnline = false;
    }

    @Override
    public void onHypeFailedStarting(Error error) {
        Log.i("WTF", "onHypeFailedStarting: error = " + error.getDescription());
    }

    @Override
    public void onHypeReady() {
        Log.i("WTF", "onHypeReady");
    }

    @Override
    public void onHypeStateChange() {
        //0 Idle
        //1 Starting
        //2 Running
        //3 Stopping
        Log.i("WTF", "state = " + Hype.getState());

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
        Log.i("WTF", "onHypeRequestAccessToken: token = " + i);
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
                        Log.i("WTF", "PERMISSION GRANTED: Hype.getState() = " + Hype.getState());
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

    //adapter
    private void setupAdapter() {
        mAdapter = new InstanceAdapter(mContext);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this, LinearLayoutManager.VERTICAL, false));
    }

    @OnClick({R.id.tv_start, R.id.tv_stop, R.id.btn_send})
    public void startStopHype(View view) {
        switch (view.getId()) {
            case R.id.tv_start:
                Log.i("WTF", "PRESS START: Hype.getState() = " + Hype.getState());
                if (Hype.getState() != State.Running) {
                    startHype();
                }
                break;
            case R.id.tv_stop:
                Log.i("WTF", "PRESS STOP: Hype.getState() = " + Hype.getState());
                stopHype();
                break;
            case R.id.btn_send:
                //send message
                String message = mEdtMessage.getText().toString().trim();
                if (!TextUtils.isEmpty(message)) {
                    sendMessage(message);
                    mEdtMessage.setText(null);
                }
                break;
        }
    }

    private void sendMessage(String message) {
        byte[] mesData = message.getBytes();

        for (int i = 0; i < InstanceRepository.getRepository().getInstanceList().size(); i++) {
            Instance currentInstance = InstanceRepository.getRepository().getInstanceList().get(i);
            Hype.send(mesData, currentInstance);
        }
    }


    private void startHype() {
        Hype.addStateObserver(this);
        Hype.addNetworkObserver(this);
        Hype.addMessageObserver(this);
        Hype.start();
    }

    private void stopHype() {
        Hype.stop();
        Hype.removeStateObserver(this);
        Hype.removeNetworkObserver(this);
        Hype.removeMessageObserver(this);
    }

}
