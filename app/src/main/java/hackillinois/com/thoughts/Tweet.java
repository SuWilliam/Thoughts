package hackillinois.com.thoughts;

import java.util.Date;

public class Tweet {
	private String tweetBy;
	private String tweet;
	private Date date;


	public Tweet(String tweetBy, String tweet, Date date) {
		this.tweetBy = tweetBy;
		this.tweet = tweet;
		this.date = date;
	}

	public String getTweetBy() {
		return tweetBy;
	}

	public String getTweet() {
		return tweet;
	}

	public Date getDate() {
		return date;
	}
}
