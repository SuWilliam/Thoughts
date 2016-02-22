package hackillinois.com.thoughts;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import twitter4j.GeoLocation;
import twitter4j.Query;
import twitter4j.QueryResult;
import twitter4j.Twitter;
import twitter4j.TwitterFactory;
import twitter4j.auth.OAuth2Token;
import twitter4j.conf.ConfigurationBuilder;

public class TwitterSearchActivity extends Activity {
	Button btnSearch;
	private final String TWIT_CONS_KEY = "usGfpH75Lx3OpxvrN7kw3EF7o";
	private final String TWIT_CONS_SEC_KEY = "HC9rNpWDikFWxwzsUtS6BZBckUvTPoodSadRRTynJNttlasq21";
	static ListView list;
	private LocationManager l;
	public static DisplayUser toDisplay;
    public static ArrayList<Tweet> tweets;
    public static Twitter twitter;
    public static Context con;
    public static ImageView image;
    public static TextView description;
    public static TextView found;
    public static ImageView picture;
    public static ProgressDialog dialog;
    public static SearchOnTwitter sTwitter;
    public static TwitterSearchActivity currInstance;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        con = getApplicationContext();
        description = (TextView) findViewById(R.id.description);
        found = (TextView) findViewById(R.id.found);
        picture = (ImageView) findViewById(R.id.profilePic);
		list = (ListView) findViewById(R.id.list);
		/*btnSearch.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

                new SearchOnTwitter().execute();
			}
		});*/
        currInstance = this;
		l = new LocationManager(this);
        sTwitter = new SearchOnTwitter();
        sTwitter.execute();
	}

    public void finishUp() {
        //creates view
    }

	public class SearchOnTwitter extends AsyncTask<String, Void, Integer> {

		HashMap<String, Long> toProcess;
		final int SUCCESS = 0;
		final int FAILURE = SUCCESS + 1;


		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			dialog = ProgressDialog.show(TwitterSearchActivity.this, "", getString(R.string.searching));
		}

		@Override
		protected Integer doInBackground(String... params) {
			try {
				//establish authentication w/ Twitter
				ConfigurationBuilder builder = new ConfigurationBuilder();
				builder.setUseSSL(true);
				builder.setApplicationOnlyAuthEnabled(true);
				builder.setOAuthConsumerKey(TWIT_CONS_KEY);
				builder.setOAuthConsumerSecret(TWIT_CONS_SEC_KEY);

				OAuth2Token token = new TwitterFactory(builder.build()).getInstance().getOAuth2Token();

				builder = new ConfigurationBuilder();
				builder.setUseSSL(true);
				builder.setApplicationOnlyAuthEnabled(true);
				builder.setOAuthConsumerKey(TWIT_CONS_KEY);
				builder.setOAuthConsumerSecret(TWIT_CONS_SEC_KEY);
				builder.setOAuth2TokenType(token.getTokenType());
				builder.setOAuth2AccessToken(token.getAccessToken());

				twitter = new TwitterFactory(builder.build()).getInstance();

				//query parameters
				Query query = new Query();
				query.setCount(10);
				QueryResult result;
				double RADIUS = 0.5;
				l.findLoc();

				//query result parsing
				result = twitter.search(query.geoCode(new GeoLocation(l.getMyLatitude(), l.getMyLongitude()), RADIUS, Query.MILES));
				System.out.println("LAT: " + l.getMyLatitude() + " LONG: " + l.getMyLongitude());
				List<twitter4j.Status> toParse = result.getTweets();
				if (toParse != null) {
					tweets = new ArrayList<Tweet>();
					toProcess = new HashMap<>();
					long savedId = 0;
					for (twitter4j.Status tweet : toParse) {
						savedId = tweet.getUser().getId();
						toProcess.put(tweet.getUser().getBiggerProfileImageURL(), tweet.getUser().getId());
					}

                    /*KairosUtils.enrollUsers(toProcess,MainActivity.bmpImg, "pic", l.getMyLatitude(), l.getMyLongitude() );
                    Log.d("TwitterSearchActivity", "Processing bmpImg and URLs");
                    getUser(KairosUtils.uID, twitter);*/
					return SUCCESS;
				}
			} catch (Exception e) {
				e.printStackTrace();
			}

			return FAILURE;
		}

		@Override
		protected void onPostExecute(Integer result) {
			super.onPostExecute(result);
			if (result == SUCCESS) {
                //establish authentication w/ Twitter
                KairosUtils.enrollUsers(toProcess, MainActivity.bmpImg, "pic", l.getMyLatitude(), l.getMyLongitude());
                Log.d("TwitterSearchActivity", "Processing bmpImg and URLs");
                TwitterSearchActivity.list.setAdapter(new TweetAdapter(TwitterSearchActivity.con, TwitterSearchActivity.tweets, TwitterSearchActivity.toDisplay));

			} else {
				Toast.makeText(TwitterSearchActivity.this, getString(R.string.error), Toast.LENGTH_LONG).show();
			}

		}
    }


}
