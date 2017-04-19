package btp.psychosocialeducationapp.API;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by gkartik on 19/4/17.
 */

public class Log {

    @SerializedName("logData")
    String logData;

    @SerializedName("timeStamp")
    String timeStamp;

    public Log(String logData, String timeStamp) {
        this.timeStamp = timeStamp;
        this.logData = logData;
    }

    public String getLogData() {return this.logData;}
    public void setLogData(String logData) {this.logData = logData;}

    public String getTimeStamp() {return this.timeStamp;}
    public void setTimeStamp(String timeStamp) {this.timeStamp = timeStamp;}
}
