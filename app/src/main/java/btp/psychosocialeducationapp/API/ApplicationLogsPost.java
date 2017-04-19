package btp.psychosocialeducationapp.API;

import java.util.ArrayList;

/**
 * Created by gkartik on 19/4/17.
 */

public class ApplicationLogsPost {

    public UserInfo userInfo;
    public ArrayList<Log> logsList;

    public ApplicationLogsPost(UserInfo userInfo, ArrayList<Log> logsList) {
        this.userInfo = userInfo;
        this.logsList = logsList;
    }
}
