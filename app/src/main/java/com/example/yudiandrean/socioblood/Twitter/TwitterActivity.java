package com.example.yudiandrean.socioblood.Twitter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.widget.ListView;
import android.widget.Toast;

import com.example.yudiandrean.socioblood.LoginActivity;
import com.example.yudiandrean.socioblood.R;
import com.example.yudiandrean.socioblood.UserPanel;
import com.example.yudiandrean.socioblood.databases.SessionManager;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;


/**
 * Demonstrates how to use a twitter application keys to access a user's timeline
 */
public class TwitterActivity extends Activity {

	final static String ScreenName = "Blood4LifeID";
	final static String LOG_TAG = "rnc";
	private ListView listView;
	private SessionManager session;
	ArrayList<Tweet> tweets = new ArrayList<>();

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.tweet_list);
		listView = (ListView) findViewById(R.id.tweet_list);

		getTwitter();

		session = new SessionManager(getApplicationContext());

		// Check if user is already logged in or not
		if (!session.isLoggedIn()) {
			// User is already logged in. Take him to main activity
			Intent intent = new Intent(TwitterActivity.this, LoginActivity.class);
			Toast.makeText(getApplicationContext(),
					"Login first!", Toast.LENGTH_SHORT).show();
			startActivity(intent);
			finish();
		}

	}

	private void popularListView(ArrayList<Tweet> tweets){
		UserItemAdapter tweetAdapter = new UserItemAdapter(this, R.layout.listitem1, tweets);
		listView.setAdapter(tweetAdapter);
	}


	public void getTwitter()
	{
		ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();

		if (networkInfo != null && networkInfo.isConnected()) {
			new DownloadTwitterTask().execute(ScreenName);
		} else {
			Log.v(LOG_TAG, "No network connection available.");
		}
	}




	// Uses an AsyncTask to download a Twitter user's timeline
	private class DownloadTwitterTask extends AsyncTask<String, Void, ArrayList<Tweet>> {
		final static String CONSUMER_KEY = "uLY62iV2Gyc0FYin6RMede2ph";
		final static String CONSUMER_SECRET = "ixs5d15ExnJageamXjIw787Cw7iOnOy34y5Vjb3uTfmYfPC3ZD";
		final static String TwitterTokenURL = "https://api.twitter.com/oauth2/token";
		final static String TwitterStreamURL = "https://api.twitter.com/1.1/statuses/user_timeline.json?screen_name=";



		private String authenticateApp(){

			HttpURLConnection connect = null;
			OutputStream os = null;
			BufferedReader br = null;
			StringBuilder result = null;

			try {
				URL url = new URL(TwitterTokenURL);
				connect = (HttpURLConnection) url.openConnection();
				connect.setRequestMethod("POST");
				connect.setDoOutput(true);
				connect.setDoInput(true);

				String accessCredentials = CONSUMER_KEY + ":" + CONSUMER_SECRET;
				String authorize = "Basic " + Base64.encodeToString(accessCredentials.getBytes(), Base64.NO_WRAP);
				String parametre = "grant_type=client_credentials";

				connect.addRequestProperty("Authorization", authorize);
				connect.setRequestProperty("Content-Type", "application/x-www-form-urlencoded;charset=UTF-8");
				connect.connect();

				os = connect.getOutputStream();
				os.write(parametre.getBytes());
				os.flush();
				os.close();

				br = new BufferedReader(new InputStreamReader(connect.getInputStream()));
				String linha;
				result = new StringBuilder();

				while ((linha = br.readLine()) != null){
					result.append(linha);
				}

				Log.d("result POST", String.valueOf(connect.getResponseCode()));
				Log.d("Result - access token", result.toString());

			} catch (Exception e) {
				Log.e("Error POST", Log.getStackTraceString(e));

			}finally{
				if (connect != null) {
					connect.disconnect();
				}
			}
			return result.toString();
		}

		@Override
		protected ArrayList<Tweet> doInBackground(String... screenNames) {
			String screenName = screenNames[0];

			ArrayList<Tweet> tweets = new ArrayList<Tweet>();
			HttpURLConnection conexao = null;
			BufferedReader br = null;

			try {
				URL url = new URL(TwitterStreamURL + screenName);
				conexao = (HttpURLConnection) url.openConnection();
				conexao.setRequestMethod("GET");

				// utilizando o token de acesso (formato JSON)
				String jsonString = authenticateApp();
				JSONObject jsonAcesso = new JSONObject(jsonString);
				String tokenPortador = jsonAcesso.getString("token_type") + " " +
						jsonAcesso.getString("access_token");

				conexao.setRequestProperty("Authorization", tokenPortador);
				conexao.setRequestProperty("Content-Type", "application/json");
				conexao.connect();

				// recuperando os tweets da api
				br = new BufferedReader(new InputStreamReader(conexao.getInputStream()));

				String linha;
				StringBuilder resposta = new StringBuilder();

				while ((linha = br.readLine()) != null){
					resposta.append(linha);
				}

				Log.d("Codigo resposta GET", String.valueOf(conexao.getResponseCode()));
				Log.d("Resposta JSON", resposta.toString());

				JSONArray jsonArray = new JSONArray(resposta.toString());
				JSONObject jsonObject;

				for (int i = 0; i < jsonArray.length(); i++) {

					jsonObject = (JSONObject) jsonArray.get(i);
					Tweet tweet = new Tweet();

					tweet.setName(jsonObject.getJSONObject("user").getString("screen_name"));
					tweet.setUrlImagemPerfil(jsonObject.getJSONObject("user").getString("profile_image_url_https"));
					tweet.setText(jsonObject.getString("text"));

					tweets.add(i, tweet);
				}

			} catch (Exception e) {
				Log.e("Erro GET: ", Log.getStackTraceString(e));

			}finally {
				if(conexao != null){
					conexao.disconnect();
				}
			}
			return tweets;
		}

		// onPostExecute convert the JSON results into a Twitter object (which is an Array list of tweets
		@Override
		protected void onPostExecute(ArrayList<Tweet> tweets) {


			if (tweets.isEmpty()) {
				Toast.makeText(TwitterActivity.this, "No Tweets! ",
						Toast.LENGTH_SHORT).show();
			} else {
				popularListView(tweets);
				Toast.makeText(TwitterActivity.this, "Tweets loaded!",
						Toast.LENGTH_SHORT).show();
			}


//			listView.setAdapter(new UserItemAdapter(activity, R.layout.listitem1, tweets));
			// send the tweets to the adapter for rendering
//			ArrayAdapter<Tweet> adapter = new ArrayAdapter<Tweet>(activity, android.R.layout.simple_list_item_1, twits);
//			setListAdapter(adapter);
		}


//		// converts a string of JSON data into a Twitter object
//		private Twitter jsonToTwitter(String result) {
//			Twitter twits = null;
//			if (result != null && result.length() > 0) {
//				try {
//					Gson gson = new Gson();
//					twits = gson.fromJson(result, Twitter.class);
//				} catch (IllegalStateException ex) {
//					// just eat the exception
//				}
//			}
//			return twits;
//		}
//
//		// convert a JSON authentication object into an Authenticated object
//		private Authenticated jsonToAuthenticated(String rawAuthorization) {
//			Authenticated auth = null;
//			if (rawAuthorization != null && rawAuthorization.length() > 0) {
//				try {
//					Gson gson = new Gson();
//					auth = gson.fromJson(rawAuthorization, Authenticated.class);
//				} catch (IllegalStateException ex) {
//					// just eat the exception
//				}
//			}
//			return auth;
//		}
//
//		private String getResponseBody(HttpRequestBase request) {
//			StringBuilder sb = new StringBuilder();
//			try {
//
//				DefaultHttpClient httpClient = new DefaultHttpClient(new BasicHttpParams());
//				HttpResponse response = httpClient.execute(request);
//				int statusCode = response.getStatusLine().getStatusCode();
//				String reason = response.getStatusLine().getReasonPhrase();
//
//				if (statusCode == 200) {
//
//					HttpEntity entity = response.getEntity();
//					InputStream inputStream = entity.getContent();
//
//					BufferedReader bReader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"), 8);
//					String line = null;
//					while ((line = bReader.readLine()) != null) {
//						sb.append(line);
//					}
//				} else {
//					sb.append(reason);
//				}
//			} catch (UnsupportedEncodingException ex) {
//			} catch (ClientProtocolException ex1) {
//			} catch (IOException ex2) {
//			}
//			return sb.toString();
//		}
//
//		private String getTwitterStream(String screenName) {
//			String results = null;
//
//			// Step 1: Encode consumer key and secret
//			try {
//				// URL encode the consumer key and secret
//				String urlApiKey = URLEncoder.encode(CONSUMER_KEY, "UTF-8");
//				String urlApiSecret = URLEncoder.encode(CONSUMER_SECRET, "UTF-8");
//
//				// Concatenate the encoded consumer key, a colon character, and the
//				// encoded consumer secret
//				String combined = urlApiKey + ":" + urlApiSecret;
//
//				// Base64 encode the string
//				String base64Encoded = Base64.encodeToString(combined.getBytes(), Base64.NO_WRAP);
//
//				// Step 2: Obtain a bearer token
//				HttpPost httpPost = new HttpPost(TwitterTokenURL);
//				httpPost.setHeader("Authorization", "Basic " + base64Encoded);
//				httpPost.setHeader("Content-Type", "application/x-www-form-urlencoded;charset=UTF-8");
//				httpPost.setEntity(new StringEntity("grant_type=client_credentials"));
//				String rawAuthorization = getResponseBody(httpPost);
//				Authenticated auth = jsonToAuthenticated(rawAuthorization);
//
//				// Applications should verify that the value associated with the
//				// token_type key of the returned object is bearer
//				if (auth != null && auth.token_type.equals("bearer")) {
//
//					// Step 3: Authenticate API requests with bearer token
//					HttpGet httpGet = new HttpGet(TwitterStreamURL + screenName);
//
//					// construct a normal HTTPS request and include an Authorization
//					// header with the value of Bearer <>
//					httpGet.setHeader("Authorization", "Bearer " + auth.access_token);
//					httpGet.setHeader("Content-Type", "application/json");
//					// update the results with the body of the response
//					results = getResponseBody(httpGet);
//				}
//			} catch (UnsupportedEncodingException ex) {
//			} catch (IllegalStateException ex1) {
//			}
//			return results;
//		}
	}
}
