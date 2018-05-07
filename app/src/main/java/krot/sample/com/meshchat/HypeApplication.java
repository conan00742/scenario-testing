package krot.sample.com.meshchat;

import android.app.Application;
import android.content.Context;

/**
 * Created by Krot on 4/30/18.
 */

public class HypeApplication extends Application {

    private static HypeApplication hypeApplication;

    public HypeApplication() {
        hypeApplication = this;
    }

    public static HypeApplication getInstance() {
        if (hypeApplication == null) {
            hypeApplication = new HypeApplication();
        }

        return hypeApplication;
    }

    public static Context getAppContext() {
        if (hypeApplication != null) {
            return hypeApplication.getApplicationContext();
        }

        return null;
    }
}
