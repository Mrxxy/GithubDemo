package mrxxy.com.githubdemo;

import android.app.Application;

import com.orhanobut.logger.Logger;


/**
 * Created by xiao
 * on 16/11/28.
 */

public class App extends Application {
    private static final String TAG = "GitHub";

    @Override
    public void onCreate() {
        super.onCreate();
        Logger.init(TAG);
    }
}
