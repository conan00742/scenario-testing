package krot.sample.com.meshchat.repository;

import com.hypelabs.hype.Instance;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Krot on 4/30/18.
 */

public class InstanceRepository {

    private static InstanceRepository repository;
    private static List<Instance> instanceList;

    private InstanceRepository() {
        instanceList = new ArrayList<>();
    }

    public static InstanceRepository getRepository() {
        if (repository == null) {
            repository = new InstanceRepository();
        }

        return repository;
    }

    public void addInstance(Instance instance) {
        instanceList.add(instance);
    }

    public void removeInstance(Instance instance) {
        instanceList.remove(instance);
    }

    public List<Instance> getInstanceList() {
        return instanceList;
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
            if (instanceList.get(i).getStringIdentifier().equals(instance.getAppStringIdentifier())) {
                return true;
            }
        }

        return false;
    }


}
