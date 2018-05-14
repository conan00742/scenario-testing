package krot.sample.com.meshchat.model;

import com.hypelabs.hype.Instance;

/**
 * Created by Krot on 5/14/18.
 */

public class DisplayedMessage {

    private Instance instance;
    private UserMessage userMessage;

    public DisplayedMessage(Instance instance, UserMessage userMessage) {
        this.instance = instance;
        this.userMessage = userMessage;
    }

    public Instance getInstance() {
        return instance;
    }

    public UserMessage getUserMessage() {
        return userMessage;
    }
}
