package krot.sample.com.meshchat.repository;

import com.hypelabs.hype.Instance;

import java.util.ArrayList;
import java.util.List;

import krot.sample.com.meshchat.model.DisplayedMessage;
import krot.sample.com.meshchat.model.UserMessage;

/**
 * Created by Krot on 4/30/18.
 */

public class HypeRepository {

    private static HypeRepository repository;
    private static List<Instance> instanceList;
    private static List<UserMessage> messageList;
    private static List<DisplayedMessage> plainTextMessageList;
    private static List<DisplayedMessage> imageMessageList;
    private static List<DisplayedMessage> videoMessageList;

    private HypeRepository() {
        instanceList = new ArrayList<>();
        messageList = new ArrayList<>();
        plainTextMessageList = new ArrayList<>();
        imageMessageList = new ArrayList<>();
        videoMessageList = new ArrayList<>();

    }


    public static HypeRepository getRepository() {
        if (repository == null) {
            repository = new HypeRepository();
        }

        return repository;
    }


    public void addInstance(Instance instance) {
        instanceList.add(instance);
    }

    public void addMessage(UserMessage message) {
        messageList.add(message);
    }

    public void addDisplayedPlainTextMsg(DisplayedMessage displayedMessage) {
        plainTextMessageList.add(displayedMessage);
    }

    public void addImageMsg(DisplayedMessage displayedImgMessage) {
        imageMessageList.add(displayedImgMessage);
    }

    public void addVideoMsg(DisplayedMessage displayedMessage) {
        videoMessageList.add(displayedMessage);
    }

    public void removeInstance(Instance instance) {
        instanceList.remove(instance);
    }

    public void removeMessage(UserMessage message) {
        messageList.remove(message);
    }

    public List<Instance> getInstanceList() {
        return instanceList;
    }

    public List<UserMessage> getMessageList() {return messageList;}

    public List<DisplayedMessage> getPlainTextMessageList() {
        return plainTextMessageList;
    }

    public List<DisplayedMessage> getImageMessageList() {
        return imageMessageList;
    }

    public List<DisplayedMessage> getVideoMessageList() {
        return videoMessageList;
    }


    public boolean isEmpty() {
        if (instanceList.isEmpty()) {
            return true;
        } else {
            return false;
        }
    }


    public boolean isInstanceExisted(Instance instance) {
        for (int i = 0; i < instanceList.size(); i++) {
            if (instanceList.get(i).getStringIdentifier().equals(instance.getStringIdentifier())) {
                return true;
            }
        }

        return false;
    }


    public int getInstanceCount() {
        return instanceList != null ? instanceList.size() : 0;
    }

    public int getMessageCount() {
        return messageList != null ? messageList.size() : 0;
    }

}
