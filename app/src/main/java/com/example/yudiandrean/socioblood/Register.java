package com.example.yudiandrean.socioblood;

/**
 * Created by yudiandrean on 5/8/2015.
 */
        import android.app.Activity;
        import android.app.ProgressDialog;
        import android.content.Context;
        import android.content.Intent;
        import android.net.ConnectivityManager;
        import android.net.NetworkInfo;
        import android.os.AsyncTask;
        import android.os.Bundle;
        import android.view.View;
        import android.view.ViewGroup;
        import android.widget.ArrayAdapter;
        import android.widget.Button;
        import android.widget.EditText;
        import android.widget.Spinner;
        import android.widget.TextView;
        import android.widget.Toast;

        import com.example.yudiandrean.socioblood.databases.DatabaseHandler;
        import com.example.yudiandrean.socioblood.databases.SessionManager;
        import com.example.yudiandrean.socioblood.databases.UserFunctions;
        import org.json.JSONException;
        import org.json.JSONObject;

        import java.io.IOException;
        import java.net.HttpURLConnection;
        import java.net.MalformedURLException;
        import java.net.URL;

public class Register extends Activity {


    /**
     *  JSON Response node names.
     **/
    private static final String TAG = Register.class.getSimpleName();

    private static String KEY_SUCCESS = "success";
    private static String KEY_UID = "uid";
    private static String KEY_FULLNAME = "fullname";
    private static String KEY_USERNAME = "username";
    private static String KEY_EMAIL = "email";
    private static String KEY_CREATED_AT = "created_at";
    private static String KEY_BLOOD_TYPE = "blood_type";
    private static String KEY_RHESUS = "rhesus";
    private static String KEY_GENDER = "gender";
    private static String KEY_ERROR = "error";
    private SessionManager session;

    /**
     * Defining layout items.
     **/

    EditText inputFullName;
    EditText inputUsername;
    EditText inputEmail;
    EditText inputPassword;
    Spinner inputBloodType;
    Spinner inputRhesus;
    Spinner inputGender;
    Button btnRegister;
    Button login;
    TextView registerErrorMsg;


    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register);

        /**
         * Defining all layout items
         **/
        inputFullName = (EditText) findViewById(R.id.fullname);
        inputUsername = (EditText) findViewById(R.id.username);
        inputEmail = (EditText) findViewById(R.id.email);
        inputPassword = (EditText) findViewById(R.id.password);
        btnRegister = (Button) findViewById(R.id.register);
        registerErrorMsg = (TextView) findViewById(R.id.register_error);

        //Blood type spinner
        inputBloodType = (Spinner) findViewById(R.id.bloodtype_spinner);
        ArrayAdapter<String> bloodadapter = new ArrayAdapter<String>(Register.this, android.R.layout.simple_spinner_dropdown_item) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {

                View v = super.getView(position, convertView, parent);
                if (position == getCount()) {
                    ((TextView)v.findViewById(android.R.id.text1)).setText("");
                    ((TextView)v.findViewById(android.R.id.text1)).setHint(getItem(getCount())); //"Hint to be displayed"
                }
                return v;
            }
            @Override
            public int getCount() {
                return super.getCount()-1; // you dont display last item. It is used as hint.
            }
        };

        bloodadapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        bloodadapter.add("O");
        bloodadapter.add("A");
        bloodadapter.add("B");
        bloodadapter.add("AB");
        bloodadapter.add("Blood Type");
        inputBloodType.setAdapter(bloodadapter);
        inputBloodType.setSelection(bloodadapter.getCount()); //display hint

        //Rhesus spinner
        inputRhesus = (Spinner) findViewById(R.id.rhesus_spinner);
        ArrayAdapter<String> rhesusadapter = new ArrayAdapter<String>(Register.this, android.R.layout.simple_spinner_dropdown_item) {

            @Override
            public View getView(int position, View convertView, ViewGroup parent) {

                View v = super.getView(position, convertView, parent);
                if (position == getCount()) {
                    ((TextView)v.findViewById(android.R.id.text1)).setText("");
                    ((TextView)v.findViewById(android.R.id.text1)).setHint(getItem(getCount())); //"Hint to be displayed"
                }

                return v;
            }

            @Override
            public int getCount() {
                return super.getCount()-1; // you dont display last item. It is used as hint.
            }

        };

        rhesusadapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        rhesusadapter.add("+");
        rhesusadapter.add("-");
        rhesusadapter.add("Rhesus");

        inputRhesus.setAdapter(rhesusadapter);
        inputRhesus.setSelection(rhesusadapter.getCount()); //display hint

        inputGender = (Spinner) findViewById(R.id.gender_spinner);
        ArrayAdapter<String> genderadapter = new ArrayAdapter<String>(Register.this, android.R.layout.simple_spinner_dropdown_item) {

            @Override
            public View getView(int position, View convertView, ViewGroup parent) {

                View v = super.getView(position, convertView, parent);
                if (position == getCount()) {
                    ((TextView)v.findViewById(android.R.id.text1)).setText("");
                    ((TextView)v.findViewById(android.R.id.text1)).setHint(getItem(getCount())); //"Hint to be displayed"
                }

                return v;
            }

            @Override
            public int getCount() {
                return super.getCount()-1; // you dont display last item. It is used as hint.
            }

        };

        genderadapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        genderadapter.add("Male");
        genderadapter.add("Female");
        genderadapter.add("Gender");

        inputGender.setAdapter(genderadapter);
        inputGender.setSelection(genderadapter.getCount()); //display hint

/**
 * Gets spinner value
 */



        // Session manager
        session = new SessionManager(getApplicationContext());

        // Check if user is already logged in or not
        if (session.isLoggedIn()) {
            // User is already logged in. Take him to main activity
            Intent intent = new Intent(Register.this,
                    UserPanel.class);
            startActivity(intent);
            finish();
        }

/**
 * Button which Switches back to the login screen on clicked
 **/

        login = (Button) findViewById(R.id.bktologin);
        login.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent myIntent = new Intent(view.getContext(), LoginActivity.class);
                startActivityForResult(myIntent, 0);
                finish();
            }

        }
        );

        /**
         * Register Button click event.
         * A Toast is set to alert when the fields are empty.
         * Another toast is set to alert Username must be 5 characters.
         **/

        btnRegister.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                if (  ( !inputUsername.getText().toString().equals("")) && ( !inputPassword.getText().toString().equals("")) && ( !inputFullName.getText().toString().equals("")) &&
                         ( !inputEmail.getText().toString().equals("")) && ( !inputBloodType.getSelectedItem().toString().equals("")) && ( !inputRhesus.getSelectedItem().toString().equals("")) && ( !inputGender.getSelectedItem().toString().equals(""))  )
                {
                    if ( inputUsername.getText().toString().length() > 4 ){
                        NetAsync(view);

                    }
                    else
                    {
                        Toast.makeText(getApplicationContext(),
                                "Username should be minimum 5 characters", Toast.LENGTH_SHORT).show();
                    }
                }
                else
                {
                    Toast.makeText(getApplicationContext(),
                            "One or more fields are empty", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    /**
     * Async Task to check whether internet connection is working
     **/

    private class NetCheck extends AsyncTask<String,String,Boolean>
    {
        private ProgressDialog nDialog;

        @Override
        protected void onPreExecute(){
            super.onPreExecute();
            nDialog = new ProgressDialog(Register.this);
            nDialog.setMessage("Loading..");
            nDialog.setTitle("Checking Network");
            nDialog.setIndeterminate(false);
            nDialog.setCancelable(true);
            nDialog.show();
        }

        @Override
        protected Boolean doInBackground(String... args){


/**
 * Gets current device state and checks for working internet connection by trying Google.
 **/
            ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo netInfo = cm.getActiveNetworkInfo();
            if (netInfo != null && netInfo.isConnected()) {
                try {
                    URL url = new URL("http://www.google.com");
                    HttpURLConnection urlc = (HttpURLConnection) url.openConnection();
                    urlc.setConnectTimeout(3000);
                    urlc.connect();
                    if (urlc.getResponseCode() == 200) {
                        return true;
                    }
                } catch (MalformedURLException e1) {
                    // TODO Auto-generated catch block
                    e1.printStackTrace();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
            return false;

        }
        @Override
        protected void onPostExecute(Boolean th){

            if(th == true){
                nDialog.dismiss();
                new ProcessRegister().execute();
            }
            else{
                nDialog.dismiss();
                registerErrorMsg.setText("Error in Network Connection");
            }
        }
    }





    private class ProcessRegister extends AsyncTask<String, String, JSONObject> {

        /**
         * Defining Process dialog
         **/
        private ProgressDialog pDialog;

        String email,password,fullname,username,blood_type,gender,rhesus;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            inputUsername = (EditText) findViewById(R.id.username);
            inputPassword = (EditText) findViewById(R.id.password);
            fullname = inputFullName.getText().toString();
            email = inputEmail.getText().toString();
            username= inputUsername.getText().toString();
            password = inputPassword.getText().toString();
            blood_type = inputBloodType.getSelectedItem().toString();
            rhesus = inputRhesus.getSelectedItem().toString();
            gender = inputGender.getSelectedItem().toString();
            pDialog = new ProgressDialog(Register.this);
            pDialog.setTitle("Contacting Servers");
            pDialog.setMessage("Registering ...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }

        @Override
        protected JSONObject doInBackground(String... args) {


            UserFunctions userFunction = new UserFunctions();
            JSONObject json = userFunction.registerUser(fullname, username, email,  password, gender, blood_type, rhesus);

            return json;


        }

        @Override
        protected void onPostExecute(JSONObject json) {
            /**
             * Checks for success message.
             **/
            try {
                if (json.getString(KEY_SUCCESS) != null) {
                    registerErrorMsg.setText("");
                    String res = json.getString(KEY_SUCCESS);

                    String red = json.getString(KEY_ERROR);

                    if(Integer.parseInt(res) == 1){
                        pDialog.setTitle("Getting Data");
                        pDialog.setMessage("Loading Info");

                        registerErrorMsg.setText("Successfully Registered");


                        DatabaseHandler db = new DatabaseHandler(getApplicationContext());
                        JSONObject json_user = json.getJSONObject("user");

                        /**
                         * Removes all the previous data in the SQlite database
                         **/

                        UserFunctions logout = new UserFunctions();
                        logout.logoutUser(getApplicationContext());
                        db.addUser(json_user.getString(KEY_FULLNAME),json_user.getString(KEY_EMAIL),json_user.getString(KEY_USERNAME),json_user.getString(KEY_UID),json_user.getString(KEY_CREATED_AT),json_user.getString(KEY_GENDER), json_user.getString(KEY_BLOOD_TYPE), json_user.getString(KEY_RHESUS) );
                        /**
                         * Stores registered data in SQlite Database
                         * Launch Registered screen
                         **/

                        Intent registered = new Intent(getApplicationContext(), Registered.class);

                        /**
                         * Close all views before launching Registered screen
                         **/
                        registered.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        pDialog.dismiss();
                        startActivity(registered);


                        finish();
                    }

                    else if (Integer.parseInt(red) ==2){
                        pDialog.dismiss();
                        registerErrorMsg.setText("User already exists");
                    }
                    else if (Integer.parseInt(red) ==3){
                        pDialog.dismiss();
                        registerErrorMsg.setText("Invalid Email id");
                    }

                }


                else{
                    pDialog.dismiss();

                    registerErrorMsg.setText("Error occured in registration");
                }

            } catch (JSONException e) {
                e.printStackTrace();


            }
        }}
    public void NetAsync(View view){
        new NetCheck().execute();
    }}



