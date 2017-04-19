package btp.psychosocialeducationapp;

/**
 * Created by gkartik on 18/4/17.
 */

public class LogData {

    private String userId;
    private String email;
    private String log;

    public LogData() {

    }

    public LogData(String userId, String email, String log){
        this.userId = userId;
        this.email = email;
        this.log = log;
    }

    public String getUserId() {return this.userId;}
    public void setUserId(String userId) {this.userId = userId;}

    public String getEmail() {return this.email;}
    public void setEmail(String email) {this.email = email;}

    public String getLog() {return this.log;}
    public void setLog(String log) {this.log = log;}

}
