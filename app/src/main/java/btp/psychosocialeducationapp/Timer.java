package btp.psychosocialeducationapp;

import java.util.Calendar;
import java.util.TimerTask;

/**
 * Created by gkartik on 6/4/17.
 */

public class Timer {

    private static Timer timer = new Timer();
    private Timer mTimer = null;
    private TimerTask mTimerTask=null;

    private Timer() {}

//    public void startTime() {
//        if(cal == null){
//            cal = Calendar.getInstance();
//            start = cal.getTimeInMillis();
//        }
//    }
//
//    public void stopTime() {
//        if(cal == null){
//            cal = Calendar.getInstance();
//            end = cal.getTimeInMillis();
//        }
//    }
//
//    public Long getTimeSpent() {
//        return end - start;
//    }

//    private void startTimer(){
//        if (mTimer == null) {
//            mTimer = new Timer();
//        }
//        if (mTimerTask == null) {
//            mTimerTask = new TimerTask() {
//                @Override
//                public void run() {
//                    Log.i(TAG, "count: "+String.valueOf(count));
//                    sendMessage(UPDATE_TEXTVIEW);
//
//                    do {
//                        try {
//                            Log.i(TAG, "sleep(1000)...");
//                            Thread.sleep(1000);
//                        } catch (InterruptedException e) {
//                        }
//                    } while (isPause);
//                    count ++;
//                }
//            };
//        }
//        if(mTimer != null && mTimerTask != null )
//            mTimer.schedule(mTimerTask, delay, period);
//    }

//    public String valueOfTimer()
//    {
//        return String.valueOf(count);
//    }
}
