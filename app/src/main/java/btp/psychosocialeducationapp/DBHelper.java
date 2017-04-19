package btp.psychosocialeducationapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by gkartik on 18/4/17.
 */

public class DBHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "applicationLogs";

    // Logs table name
    private static final String TABLE_LOGS = "logs";

    private static final String KEY_COLUMN_ID = "_id";
    private static final String KEY_TIMESTAMP = "time_stamp";
    private static final String KEY_USER_ID = "user_id";
    private static final String KEY_EMAIL = "email_id";
    private static final String KEY_LOG_DATA = "log_data";


    public DBHelper(Context context) {
        super(context, DBSchema.DATABASE_NAME, null, DBSchema.DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(DBSchema.CREATE_LOGS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(DBSchema.DELETE_LOGS_TABLE);
        onCreate(db);
    }


//    public void addLog(LogData logData) {
//        SQLiteDatabase db = this.getWritableDatabase();
//
//        ContentValues values = new ContentValues();
//        values.put(KEY_TIMESTAMP, " time('now') ");
//        values.put(KEY_USER_ID, logData.getUserId());
//        values.put(KEY_EMAIL, logData.getEmail());
//        values.put(KEY_LOG_DATA, logData.getLog());
//
//        // Inserting Row
//        db.insert(TABLE_LOGS, null, values);
//        db.close(); // Closing database connection
//    }

//    public LogData getSingleLog(int id) {
//        SQLiteDatabase db = this.getReadableDatabase();
//
//        Cursor cursor = db.query(TABLE_LOGS, new String[] { KEY_USER_ID,
//                        KEY_EMAIL, KEY_LOG_DATA }, KEY_USER_ID + "=?",
//                new String[] { String.valueOf(id) }, null, null, null, null);
//        if (cursor != null)
//            cursor.moveToFirst();
//
//        LogData log = new LogData(cursor.getString(0),
//                cursor.getString(1), cursor.getString(2));
//        return log;
//    }

//    public List<LogData> getAllLogs() {
//        List<LogData> logsList = new ArrayList<LogData>();
//        // Select All Query
//        String selectQuery = "SELECT  * FROM " + TABLE_LOGS;
//
//        SQLiteDatabase db = this.getWritableDatabase();
//        Cursor cursor = db.rawQuery(selectQuery, null);
//
//        // looping through all rows and adding to list
//        if (cursor.moveToFirst()) {
//            do {
//                LogData log = new LogData();
//                log.setUserId(cursor.getString(2));
//                log.setEmail(cursor.getString(3));
//                log.setLog(cursor.getString(4));
//                // Adding log to list
//                logsList.add(log);
//            } while (cursor.moveToNext());
//        }
//
//        // return logs list
//        return logsList;
//    }

//    public int getLogsCount() {
//        String countQuery = "SELECT  * FROM " + TABLE_LOGS;
//        SQLiteDatabase db = this.getReadableDatabase();
//        Cursor cursor = db.rawQuery(countQuery, null);
//        cursor.close();
//
//        // return count
//        return cursor.getCount();
//    }

//    public int updateLog(LogData log) {
//        SQLiteDatabase db = this.getWritableDatabase();
//
//        ContentValues values = new ContentValues();
//        values.put(, contact.getName());
//        values.put(KEY_PH_NO, contact.getPhoneNumber());
//
//        // updating row
//        return db.update(TABLE_CONTACTS, values, KEY_ID + " = ?",
//                new String[] { String.valueOf(contact.getID()) });
//    }

//    public void deleteContact(Contact contact) {
//        SQLiteDatabase db = this.getWritableDatabase();
//        db.delete(TABLE_CONTACTS, KEY_ID + " = ?",
//                new String[] { String.valueOf(contact.getID()) });
//        db.close();
//    }
}
