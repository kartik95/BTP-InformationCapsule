package btp.psychosocialeducationapp;

import android.provider.BaseColumns;

/**
 * Created by gkartik on 18/4/17.
 */

public class DBSchema implements BaseColumns{

    public static final int DATABASE_VERSION = 1;

    // Database Name
    public static final String DATABASE_NAME = "applicationLogs.db";
    public static final String TABLE_LOGS = "logs";

    public static final String KEY_COLUMN_ID = "_id";
    public static final String KEY_TIMESTAMP = "time_stamp";
    public static final String KEY_USER_ID = "user_id";
    public static final String KEY_EMAIL = "email_id";
    public static final String KEY_LOG_DATA = "log_data";

    public DBSchema() {

    }

    public static final String CREATE_LOGS_TABLE = "CREATE TABLE " + TABLE_LOGS + "("
            + KEY_COLUMN_ID + " integer primary key autoincrement, " + KEY_TIMESTAMP + " TIMESTAMP DEFAULT CURRENT_TIMESTAMP, " + KEY_USER_ID + " TEXT, "
            + KEY_EMAIL + " TEXT, " + KEY_LOG_DATA + " TEXT" + ");";

    public static final String DELETE_LOGS_TABLE = "DROP TABLE IF EXISTS " + TABLE_LOGS;

}
