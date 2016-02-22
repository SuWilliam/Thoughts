package hackillinois.com.thoughts;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

public class TweetAdapter extends BaseAdapter {
	private static final int IO_BUFFER_SIZE = 4 * 1024;

	ArrayList<Tweet> tweetList;
	Context context;
	DisplayUser toDisplay;

	public TweetAdapter(Context context, ArrayList<Tweet> tweetList, DisplayUser toDisplay) {
		this.tweetList = tweetList;
		this.context = context;
		this.toDisplay = toDisplay;
	}

	@Override
	public int getCount() {
		return tweetList.size();
	}

	@Override
	public Object getItem(int position) {
		return null;
	}

	@Override
	public long getItemId(int position) {
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			LayoutInflater inflater = LayoutInflater.from(context);
			convertView = inflater.inflate(R.layout.tweet_list_item, null);
		}

		Tweet tweet = tweetList.get(position);
		LinearLayout linearLayout = (LinearLayout) convertView.findViewById(R.id.linlay);
		TextView txtTweet = (TextView) convertView.findViewById(R.id.txtTweet);
		TextView txtTweetBy = (TextView) convertView.findViewById(R.id.txtTweetDate);

		txtTweet.setText(tweet.getTweet());
		txtTweetBy.setText("" + tweet.getDate());
		if (("" + txtTweet.getText()).contains("solves")) {
			linearLayout.setBackgroundColor(Color.parseColor("#DC143C"));
		} else {
			linearLayout.setBackgroundColor(Color.parseColor("#00BFFF"));
		}

		return convertView;
	}
}
