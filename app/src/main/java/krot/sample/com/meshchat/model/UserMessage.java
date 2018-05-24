package krot.sample.com.meshchat.model;

import android.net.Uri;

import com.hypelabs.hype.Message;

/**
 * Created by Krot on 5/10/18.
 */

public class UserMessage {

    private byte[] message;
    private String videoPath;
    private boolean fromSender;

    public UserMessage(byte[] message, boolean fromSender) {
        this.message = message;
        this.fromSender = fromSender;
    }

    public String getVideoPath() {
        return videoPath;
    }

    public void setVideoPath(String videoPath) {
        this.videoPath = videoPath;
    }

    public byte[] getMessage() {
        return message;
    }


    public boolean isFromSender() {
        return fromSender;
    }
}
