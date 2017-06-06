package btp.psychosocialeducationapp.API;

import com.google.gson.annotations.SerializedName;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import btp.psychosocialeducationapp.NewsFeed;

/**
 * Created by gkartik on 19/4/17.
 */

public class User {

    @SerializedName("uId")
    private String uId;

    @SerializedName("emailId")
    private String emailId;

    @SerializedName("gender")
    private String gender;

    @SerializedName("age")
    private String age;

    @SerializedName("primaryContact")
    private String primaryContact;

    @SerializedName("altContact")
    private String altContact;

    @SerializedName("savedPreferencesPosition")
    private List<String> savedPreferencesPosition;

    @SerializedName("savedPreferences")
    private List<NewsFeed> savedPreferences;

    public User(String uId, String emailId, String gender, String age, String primaryContact, String altContact, List<String> savedPreferencesPosition, List<NewsFeed> savedPreferences){
        this.uId= uId;
        this.emailId = emailId;
        this.gender = gender;
        this.age = age;
        this.savedPreferencesPosition = savedPreferencesPosition;
        this.savedPreferences = savedPreferences;
        this.primaryContact = primaryContact;
        this.altContact = altContact;
    }

    public String getuId() {return this.uId;}
    public void setUserId(String uId) {this.uId = uId;}

    public String getEmailId() {return this.emailId;}
    public void setEmailId(String emailId) {this.emailId = emailId;}

    public String getGender() {return this.gender;}
    public void setGender(String gender) {this.gender = gender;}

    public String getAge() {return this.age;}
    public void setAge(String age) {this.age = age;}

    public String getPrimaryContact() {return this.primaryContact;}
    public void setPrimaryContacte(String primaryContact) {this.primaryContact = primaryContact;}

    public String getAltContact() {return this.altContact;}
    public void setAltContact(String altContact) {this.altContact = altContact;}

//    public HashMap<String, NewsFeed> getPreferences() {return this.preferences;}
//    public void setSaved_news_feeds(HashMap<String, NewsFeed> preferences) {this.preferences = preferences;}

    public List<String> getSavedPreferencesPositions() {return this.savedPreferencesPosition;}
    public void setSavedPreferencesPositions(List<String> savedPreferencesPositions) {this.savedPreferencesPosition = savedPreferencesPositions;}

    public List<NewsFeed> getSavedPreferences() {return this.savedPreferences;}
    public void setSavedPreferences(List<NewsFeed> savedPreferences) {this.savedPreferences = savedPreferences;}
}
