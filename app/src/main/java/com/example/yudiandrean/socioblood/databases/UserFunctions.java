package com.example.yudiandrean.socioblood.databases;

/**
 * Author :Raj Amal
 * Email  :raj.amalw@learn2crack.com
 * Website:www.learn2crack.com
 **/

import java.util.ArrayList;
import java.util.List;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;
import android.content.Context;


public class UserFunctions {

    private JSONParser jsonParser;

    //URL of the PHP API
    private static String loginURL = "http://api.socioblood.com";
    private static String registerURL = "http://api.socioblood.com";
    private static String forpassURL = "http://api.socioblood.com";
    private static String chgpassURL = "http://api.socioblood.com";
    private static String postURL = "http://api.socioblood.com";
    private static String viewTimelineURL = "http://api.socioblood.com";


    private static String login_tag = "login";
    private static String register_tag = "register";
    private static String forpass_tag = "forpass";
    private static String chgpass_tag = "chgpass";
    private static String post_tag = "postreq";


    // constructor
    public UserFunctions(){
        jsonParser = new JSONParser();
    }

    /**
     * Function to Login
     **/

    public JSONObject loginUser(String email, String password){
        // Building Parameters
        List<NameValuePair> params = new ArrayList<>();
        params.add(new BasicNameValuePair("tag", login_tag));
        params.add(new BasicNameValuePair("email", email));
        params.add(new BasicNameValuePair("password", password));
        JSONObject json = jsonParser.getJSONFromUrl(loginURL, params);
        return json;
    }

    /**
     * Function to change password
     **/

    public JSONObject chgPass(String newpas, String email){
        List<NameValuePair> params = new ArrayList<>();
        params.add(new BasicNameValuePair("tag", chgpass_tag));
        params.add(new BasicNameValuePair("newpas", newpas));
        params.add(new BasicNameValuePair("email", email));
        JSONObject json = jsonParser.getJSONFromUrl(chgpassURL, params);
        return json;
    }





    /**
     * Function to reset the password
     **/

    public JSONObject forPass(String forgotpassword){
        List<NameValuePair> params = new ArrayList<>();
        params.add(new BasicNameValuePair("tag", forpass_tag));
        params.add(new BasicNameValuePair("forgotpassword", forgotpassword));
        JSONObject json = jsonParser.getJSONFromUrl(forpassURL, params);
        return json;
    }






     /**
      * Function to  Register
      **/
    public JSONObject registerUser(String fullname, String username, String email, String password, String gender, String blood_type, String rhesus){
        // Building Parameters
        List<NameValuePair> params = new ArrayList<>();
        params.add(new BasicNameValuePair("tag", register_tag));
        params.add(new BasicNameValuePair("fullname", fullname));
        params.add(new BasicNameValuePair("username", username));
        params.add(new BasicNameValuePair("email", email));
        params.add(new BasicNameValuePair("password", password));
        params.add(new BasicNameValuePair("gender", gender));
        params.add(new BasicNameValuePair("blood_type", blood_type));
        params.add(new BasicNameValuePair("rhesus", rhesus));
        JSONObject json = jsonParser.getJSONFromUrl(registerURL,params);
        return json;
    }

    public JSONObject addPost(int uid, String message, String post_bloodtype, String post_rhesus){
        // Building Parameters
        List<NameValuePair> params = new ArrayList<>();
        params.add(new BasicNameValuePair("tag", post_tag));
        params.add(new BasicNameValuePair("uid", Integer.toString(uid)));
        params.add(new BasicNameValuePair("message", message));
        params.add(new BasicNameValuePair("post_bloodtype", post_bloodtype));
        params.add(new BasicNameValuePair("post_rhesus", post_rhesus));
        JSONObject json = jsonParser.getJSONFromUrl(postURL,params);
        return json;
    }



    /**
     * Function to logout user
     * Resets the temporary data stored in SQLite Database
     * */
    public boolean logoutUser(Context context){
        DatabaseHandler db = new DatabaseHandler(context);
        db.resetTables();
        return true;
    }

}

