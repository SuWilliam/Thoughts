package hackillinois.com.thoughts;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.kairos.Kairos;
import com.kairos.KairosListener;
import com.loopj.android.http.AsyncHttpClient;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import twitter4j.TwitterException;
import twitter4j.User;

/**
 * Created by Jay on 2/20/16.
 */
public class KairosUtils {
    private static Kairos myKairos;
    private static KairosListener listener;
    private static KairosListener recognizeListener;
    private static Map<String, Long> users;
    private static List<String> galleries;
    public static long uID;
    public List<twitter4j.Status> statusList;

    public static void init(final Context c) {
        final int DEFAULT_TIMEOUT = 20 * 100000;
        AsyncHttpClient aClient = new AsyncHttpClient();
        aClient.setTimeout(DEFAULT_TIMEOUT);
        galleries = new LinkedList<String>();
        myKairos = new Kairos();
        // set authentication
        String app_id = "44213f54";
        String api_key = "ff7c57c6a89a20e1fac40ef2bd626573";
        myKairos.setAuthentication(c, app_id, api_key);
        listener = new KairosListener() {

            @Override
            public void onSuccess(String response) {
                // your code here!
                Log.d("KAIROS DEMO", response);
            }

            @Override
            public void onFail(String response) {
                // your code here!
                Log.d("KAIROS DEMO", response);
            }
        };

        recognizeListener = new KairosListener() {

            @Override
            public void onSuccess(String s) {
                try {
                    Log.d("KAIROSUTILS", "REACHED HERE");
                    Log.d("KAIROSUTILS", s);
                    JSONObject obj = new JSONObject(s);
                    JSONArray imagesArr = (JSONArray) obj.get("images");
                    JSONObject highestConf = (JSONObject) imagesArr.get(0);
                    JSONObject jobj = (JSONObject) highestConf.get("transaction");
                    String subj = (String) jobj.get("subject");
                    Log.d("KAIROSUTILS", subj);
                    Log.d("TODISPLAY", "asdfasdfasdf");
                    uID = (Long.parseLong(subj));

                    //unlocks
                    final Long userId = KairosUtils.uID;
                    if (userId == null) {
                        Log.d("Will", "I AM STILL NULL FUCKKKK");
                    }
                    Log.d("TODISPLAY", "ALJKSDHALSJD");

                    class Test extends AsyncTask<Long, Void, List<twitter4j.Status>> {

                        @Override
                        protected List<twitter4j.Status> doInBackground(Long... params) {
                            try {
                                List<twitter4j.Status> list = TwitterSearchActivity.twitter.getUserTimeline(userId);
                                return list;
                            } catch (TwitterException e) {
                                e.printStackTrace();
                            }
                            return null;
                        }

                        @Override
                        protected void onPostExecute(List<twitter4j.Status> result) {
                            if (result == null) {
                                return;
                            }
                            for (twitter4j.Status tweet : result) {
                                TwitterSearchActivity.tweets.add(new Tweet("@" + tweet.getUser().getScreenName(), tweet.getText(), tweet.getCreatedAt()));
                            }
                            if (!result.isEmpty()) {
                                User toUse = result.get(0).getUser();
                                TwitterSearchActivity.toDisplay = new DisplayUser(toUse.getName(), toUse.getFollowersCount(), toUse.getOriginalProfileImageURL());
                            }

                            Picasso.with(TwitterSearchActivity.con).load(TwitterSearchActivity.toDisplay.getImage()).into(TwitterSearchActivity.picture);

                            TwitterSearchActivity.description.setText(TwitterSearchActivity.toDisplay.getName() + "\n" + "Followers: " + TwitterSearchActivity.toDisplay.getFollowers());
                            TwitterSearchActivity.found.setText("You found...");
                            TwitterSearchActivity.list.setAdapter(new TweetAdapter(TwitterSearchActivity.con, TwitterSearchActivity.tweets, TwitterSearchActivity.toDisplay));
                        }
                    }
                    new Test().execute(userId);
                    TwitterSearchActivity.dialog.dismiss();

//                    if (statusList.size() == 0) {
//                        Log.d("TODISPLAY", " is null");
//                    } else {
//                        Log.d("TODISPLAY", " is populated");
//                    }
//                    for (twitter4j.Status tweet: statusList) {
//                        TwitterSearchActivity.tweets.add(new Tweet("@" + tweet.getUser().getScreenName(), tweet.getText(), tweet.getCreatedAt()));
//                    }
//                    if (!statusList.isEmpty()) {
//                        User toUse = statusList.get(0).getUser();
//                        TwitterSearchActivity.toDisplay = new DisplayUser(toUse.getName(), toUse.getFollowersCount(), toUse.getOriginalProfileImageURL());
//                    }
//
//                    if (TwitterSearchActivity.toDisplay == null) {
//                        Log.d("TODISPLAY", "display is null");
//                    } else {
//                        Log.d("TODISPLAY", "display is populated");
//                    }

                    //finish up

                    //TwitterSearchActivity.list.setAdapter(new TweetAdapter(TwitterSearchActivity.con, TwitterSearchActivity.tweets, TwitterSearchActivity.toDisplay));
//
//                    Picasso.with(TwitterSearchActivity.con).load(TwitterSearchActivity.toDisplay.getImage()).into(TwitterSearchActivity.picture);
//
//
//                    TwitterSearchActivity.description.setText(TwitterSearchActivity.toDisplay.getName() + "\n" + "Followers: " + TwitterSearchActivity.toDisplay.getFollowers());
//                    TwitterSearchActivity.found.setText("You found...");
//                } catch (TwitterException e) {
//                    System.out.println(e.getMessage() + "Twitter");
                } catch (JSONException d) {
                    System.out.println(d.getMessage() + "JSON");
                }
            }

            @Override
            public void onFail(String s) {
                Log.d("KAIROSUTILS", "REACHED FAILURE");
                Log.d("FAILURE", s);
                TwitterSearchActivity.dialog.dismiss();
                Toast.makeText(TwitterSearchActivity.con, "Unable to find a matching profile.", Toast.LENGTH_LONG).show();
            }
        };
    }

    public static void enrollUsers(Map<String, Long> map, Bitmap bmp, String pic, double lat, double lng) {
        String gallery = (int)lat + "D" + (int)((lat - (int)lat) * 10000000) + "A" + (int)lng + "D" + (int)((lng - (int)lng) * 10000000);
        Log.d("AYYO", gallery);
        boolean galleryExists = false;
        for (String s : galleries) {
            String[] arr = s.split("A");
            String[] temp = arr[0].split("D");
            String[] temp2 = arr[1].split("D");
            /*if(Math.abs(Double.parseDouble(temp[0] + temp[1]) - lat) < 0.05
                    && Math.abs(Double.parseDouble(temp2[0] + temp2[1]) - lng) < 0.05) {
                    galleryExists = true;
                    gallery = Integer.parseInt(arr[0]) + "A" + Integer.parseInt(arr[1]);
            }*/
        }
        if(!galleryExists) {
            galleries.add(gallery);
            for (String s : map.keySet()) {
                try {
                    Log.d("map", s);
                    Log.d("map.get(s)", map.get(s) + "");
                    myKairos.enroll(s, map.get(s) + "", gallery, "FULL", "false", "0.125", listener);
                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            }
        }
        try {
            myKairos.recognize(bmp, gallery, "FULL", "0.0", "0.125", "1", recognizeListener);
            //
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        Log.d("Kairos", "Waiting...");
        /*while(uID == null) {
            //
        }*/
        Log.d("Kairos", "Done.");
        Log.d("LONG ID", uID + "");
    }

    public static void enroll(Bitmap rotatedBmp, String pic, String gallery) {
        try {
            myKairos.enroll(rotatedBmp, pic, gallery, "FACE", "false", "0.3", listener);
        } catch(JSONException e) {
            e.printStackTrace();
        } catch(UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }



}

//KairosUtils.enroll
//
