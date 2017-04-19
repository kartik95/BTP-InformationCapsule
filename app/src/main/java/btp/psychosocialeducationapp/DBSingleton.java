package btp.psychosocialeducationapp;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import java.util.List;

/**
 * Created by gkartik on 18/4/17.
 */

public class DBSingleton {

    private static DBSingleton mInstance;
    private static Context mContext;
    private static DBHelper mDbHelper;
    private SQLiteDatabase mDb;
    private DBOperations dbOperations = new DBOperations();

    public DBSingleton(Context context) {
        mContext = context;
        mDbHelper = new DBHelper(mContext);
    }

    public DBSingleton(SQLiteDatabase mDb, Context context, DBHelper helper) {
        this.mDb = mDb;
        mContext = context;
        mDbHelper = helper;
    }

    public static void init(Context context) {
        if (mInstance == null) {
            mInstance = new DBSingleton(context);
        }
    }

    public static DBSingleton getInstance() {
        return mInstance;
    }

    private void getReadOnlyDatabase() {
        if ((mDb == null) || (!mDb.isReadOnly())) {
            mDb = mDbHelper.getReadableDatabase();
        }
    }

    public boolean insertLog(LogData logData) {
        return dbOperations.addLog(logData, mDbHelper);
    }

    public List<LogData> getAllLogs() {
        getReadOnlyDatabase();
        return dbOperations.getAllLogs(mDb);
    }

    public LogData getLastLog() {
        getReadOnlyDatabase();
        return dbOperations.getLastLog(mDb);
    }

    public int getLogsCount() {
        getReadOnlyDatabase();
        return dbOperations.getLogsCount(mDb);
    }

    public boolean createCSVOfLogs(Context context) {
        getReadOnlyDatabase();
        return dbOperations.createCSVOfLogs(mDb, context);
    }
}
