package btp.psychosocialeducationapp;

import android.app.Application;
import android.icu.text.IDNA;

/**
 * Created by gkartik on 18/4/17.
 */

public class InfoCapsuleApp extends Application{
    private static InfoCapsuleApp instance;

    @Override
    public void onCreate() {
        super.onCreate();
        DBSingleton.init(getApplicationContext());
        instance = this;
    }

    public static InfoCapsuleApp getInstance() {
        return instance;
    }
}
