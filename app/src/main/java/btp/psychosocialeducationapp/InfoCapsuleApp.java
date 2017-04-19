package btp.psychosocialeducationapp;

import android.app.Application;

/**
 * Created by gkartik on 18/4/17.
 */

public class InfoCapsuleApp extends Application{

    @Override
    public void onCreate() {
        super.onCreate();
        DBSingleton.init(getApplicationContext());
    }
}
