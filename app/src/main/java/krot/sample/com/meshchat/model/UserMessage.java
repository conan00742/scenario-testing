package krot.sample.com.meshchat.model;

import android.net.Uri;

import com.hypelabs.hype.Message;

/**
 * Created by Krot on 5/10/18.
 */

public class UserMessage {

    private Message message;
    private String msgType;
    private String videoPath;
    private boolean fromSender;

    public UserMessage(Message message, String msgType, boolean fromSender) {
        this.message = message;
        this.msgType = msgType;
        this.fromSender = fromSender;
    }

    public String getVideoPath() {
        return videoPath;
    }

    public void setVideoPath(String videoPath) {
        this.videoPath = videoPath;
    }

    public Message getMessage() {
        return message;
    }

    public String getMsgType() {
        return msgType;
    }

    public boolean isFromSender() {
        return fromSender;
    }
}
