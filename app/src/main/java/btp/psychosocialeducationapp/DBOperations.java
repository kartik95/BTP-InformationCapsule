package btp.psychosocialeducationapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.media.MediaScannerConnection;
import android.os.Environment;
import android.provider.SyncStateContract;
import android.util.Log;

import com.opencsv.CSVWriter;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Enumeration;
import java.util.List;

/**
 * Created by gkartik on 18/4/17.
 */

public class DBOperations {


    public boolean addLog(LogData logData, DBHelper mDbHelper) {
        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
//        values.put(DBSchema.KEY_TIMESTAMP, new SimpleDateFormat("MM/dd/yyyy h:mm:ss a").format(new Date()));
        values.put(DBSchema.KEY_USER_ID, logData.getUserId());
        values.put(DBSchema.KEY_EMAIL, logData.getEmail());
        values.put(DBSchema.KEY_LOG_DATA, logData.getLog());

        return db.insert(DBSchema.TABLE_LOGS, null, values) > 0;
//        db.close(); // Closing database connection
    }


//    public LogData getSingleLog(int id, DBHelper mDbHelper) {
//        SQLiteDatabase db = mDbHelper.getReadableDatabase();
//
//        Cursor cursor = db.query(DBSchema.TABLE_LOGS, new String[] { DBSchema.KEY_USER_ID,
//                        DBSchema.KEY_EMAIL, DBSchema.KEY_LOG_DATA }, DBSchema.KEY_USER_ID + "=?",
//                new String[] { String.valueOf(id) }, null, null, null, null);
//        if (cursor != null)
//            cursor.moveToFirst();
//
//        LogData log = new LogData(cursor.getString(0),
//                cursor.getString(1), cursor.getString(2));
//        return log;
//    }


    public List<LogData> getAllLogs(SQLiteDatabase mDb) {
        List<LogData> logsList = new ArrayList<LogData>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + DBSchema.TABLE_LOGS;

//        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        Cursor cursor = mDb.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                LogData log = new LogData();
                log.setTimeStamp(cursor.getString(1));
                log.setUserId(cursor.getString(2));
                log.setEmail(cursor.getString(3));
                log.setLog(cursor.getString(4));
                // Adding log to list
                logsList.add(log);
            } while (cursor.moveToNext());
        }

        // return logs list
        return logsList;
    }


    public int getLogsCount(SQLiteDatabase mDb) {
        String countQuery = "SELECT * FROM " + DBSchema.TABLE_LOGS;
//        SQLiteDatabase db = mDbHelper.getReadableDatabase();
        Cursor cursor = mDb.rawQuery(countQuery, null);
        cursor.close();

        // return count
        return cursor.getCount();
    }

    public LogData getLastLog(SQLiteDatabase mDb) {
        String selectQuery = "SELECT * FROM " + DBSchema.TABLE_LOGS;
//        SQLiteDatabase db = mDbHelper.getReadableDatabase();
        Cursor cursor = mDb.rawQuery(selectQuery, null);
        cursor.moveToLast();
        LogData log = new LogData();
        log.setTimeStamp(cursor.getString(1));
        log.setUserId(cursor.getString(2));
        log.setEmail(cursor.getString(3));
        log.setLog(cursor.getString(4));
        cursor.close();

        return log;
    }

    public boolean createCSVOfLogs(SQLiteDatabase mDb, Context context) {
        File exportDir = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/InformationCapsule/CSV/", "");
        if (!exportDir.exists()) {
            boolean mkdirs = exportDir.mkdirs();
            if(!mkdirs) {
                Log.d("CSVError: ", "PING0");
                return false;
            }
        }
        File outputFile = new File(exportDir, "userLogs.csv");
        try{
            if (outputFile.exists()) {
                boolean delete = outputFile.delete();
                if(!delete) {
                    Log.d("CSVError: ", "PING1");
                    return false;
                }
            }
            boolean isCreated = outputFile.createNewFile();
            if (!isCreated) {
                Log.d("CSVError: ", "PING2");
                return false;
            }
            CSVWriter csvWriter = new CSVWriter(new FileWriter(outputFile));
            Cursor curCSV = mDb.rawQuery("SELECT * FROM " + DBSchema.TABLE_LOGS, null);
            if(curCSV != null) {
                csvWriter.writeNext(curCSV.getColumnNames());
                while(curCSV.moveToNext()) {
                    String arrString[] = {curCSV.getString(0), curCSV.getString(1), curCSV.getString(2),
                            curCSV.getString(3), curCSV.getString(4)};
                    csvWriter.writeNext(arrString);
                }
                csvWriter.close();
                curCSV.close();
                MediaScannerConnection.scanFile(context, new String[]{outputFile.getAbsolutePath()}, null, null);
                return true;
            }
            else {
                Log.d("CSVError: ", "PING3");
                return false;
            }

        } catch (Exception e) {
            Log.e("MainActivity", e.getMessage(), e);
//            e.printStackTrace();
            Log.d("CSVError: ", "PING4");
            return false;
        }
    }
}
