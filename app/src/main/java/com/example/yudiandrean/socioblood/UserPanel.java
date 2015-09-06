package com.example.yudiandrean.socioblood;

/**
 * Created by yudiandrean on 5/9/2015.
 */
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.yudiandrean.socioblood.databases.SessionManager;
import com.example.yudiandrean.socioblood.databases.UserFunctions;
import com.example.yudiandrean.socioblood.databases.DatabaseHandler;
import com.twitter.sdk.android.Twitter;
import com.twitter.sdk.android.core.AppSession;
import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.TwitterAuthConfig;
import com.twitter.sdk.android.core.TwitterAuthToken;
import com.twitter.sdk.android.core.TwitterCore;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.TwitterSession;
import com.twitter.sdk.android.core.identity.TwitterLoginButton;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;

import io.fabric.sdk.android.Fabric;


public class UserPanel extends Activity {
    Button btnLogout;
    Button changepas;
    private SessionManager session;
    private TwitterLoginButton loginButton;
    // Note: Your consumer key and secret should be obfuscated in your source code before shipping.
    private static final String TWITTER_KEY = "uLY62iV2Gyc0FYin6RMede2ph";
    private static final String TWITTER_SECRET = "ixs5d15ExnJageamXjIw787Cw7iOnOy34y5Vjb3uTfmYfPC3ZD";

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_panel);
        changepas = (Button) findViewById(R.id.btchangepass);
        btnLogout = (Button) findViewById(R.id.logout);


        TwitterAuthConfig authConfig = new TwitterAuthConfig(TWITTER_KEY, TWITTER_SECRET);
        Fabric.with(this, new Twitter(authConfig));
        loginButton = (TwitterLoginButton) findViewById(R.id.twitter_login_button);
        // session manager

        session = new SessionManager(getApplicationContext());

        // Check if user is already logged in or not
        if (!session.isLoggedIn()) {
            // User is already logged in. Take him to main activity
            Intent intent = new Intent(UserPanel.this, LoginActivity.class);
            Toast.makeText(getApplicationContext(),
                    "Login first!", Toast.LENGTH_SHORT).show();
            startActivity(intent);
            finish();
        }


        try {
            PackageInfo info = getPackageManager().getPackageInfo(
                    getPackageName(), PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.d("KeyHash:",
                        Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }
        } catch (PackageManager.NameNotFoundException e) {
        } catch (NoSuchAlgorithmException e) {
        }

        DatabaseHandler db = new DatabaseHandler(getApplicationContext());

        /**
         * Hashmap to load data from the Sqlite database
         **/
        HashMap<String,String> user = new HashMap<>();
        user = db.getUserDetails();
        /**
         * Change Password Activity Started
         **/
        changepas.setOnClickListener(new View.OnClickListener(){
            public void onClick(View arg0){
                Intent chgpass = new Intent(getApplicationContext(), ChangePassword.class);
                startActivity(chgpass);
            }

        });
        /**
         *Logout from the User Panel which clears the data in Sqlite database
         **/
        btnLogout.setOnClickListener(new View.OnClickListener() {

            public void onClick(View arg0) {
                UserFunctions logout = new UserFunctions();
                logout.logoutUser(getApplicationContext());
                session.setLogin(false);
                session.setCurrentUser(0);
                Intent login = new Intent(getApplicationContext(), LoginActivity.class);
                login.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(login);
                finish();
            }
        });

        //twitter login
        loginButton.setCallback(new Callback<TwitterSession>() {
            @Override
            public void success(Result<TwitterSession> result) {
                // Do something with result, which provides a TwitterSession for making API calls
                String output = "Status: " +
                        "Your login was successful " +
                        result.data.getUserName() +
                        "\nAuth Token Received: " +
                        result.data.getAuthToken().token;

                Toast.makeText(getApplicationContext(),
                        output, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void failure(TwitterException exception) {
                // Do something on failure
                Toast.makeText(getApplicationContext(),
                        "Login failed!", Toast.LENGTH_SHORT).show();
            }
        });




        //Guest Authentication
        TwitterCore.getInstance().logInGuest(new Callback<AppSession>() {
            @Override
            public void success(Result<AppSession> result) {
                AppSession guestAppSession = result.data;
            }

            @Override
            public void failure(TwitterException exception) {
                // unable to get an AppSession with guest auth
            }
        });

/**
 * Sets user first name and last name in text view.
 **/
        final TextView login = (TextView) findViewById(R.id.textwelcome);
        login.setText("Welcome  "+user.get("fullname"));
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        //manage login result
        //mCallbackManager.onActivityResult(requestCode, resultCode, data);
        loginButton.onActivityResult(requestCode, resultCode, data);
    }




}