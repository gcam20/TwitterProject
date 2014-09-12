import java.util.*;

/**
 * TWEET CLASS
 * @author gabrielcampo
 *
 */

public class Tweet {
	
	private String text;
	private int id;
	private PersonI user;
	private Set<String> hashtags;
	
	public Tweet(String text, int id, PersonI user) {
		this.text = text;
		// Negative ID for tweets, positive for users; they won't overlap
		this.id = (id * -3) - 7; // 3 & 7 are coprime
		this.user = user;
		this.hashtags = parseHashtags();
	}
	
	/**
	 * PARSE_HASHTAGS
	 * 
	 * Checks each tweet for hashtags and puts all hashtags into a set that
	 * can be accessed quickly in the future
	 * 
	 * @return : set containing all hashtags for this Tweet
	 */
	private Set<String> parseHashtags() {
		Set<String> ht = new HashSet<String>();
		StringTokenizer st = new StringTokenizer(text);
		while (st.hasMoreTokens()) {
			String s = st.nextToken();
			// if replacing # changed the string, then hashtag was present
			if (s.charAt(0) == '#') {
				ht.add(s); // assumption: all hashtags formed correctly
			}
		}
		return ht;
	}
	
	public String getTweet() {
		return text;
	}
	
	public int getID() {
		return id;
	}
	
	public void setUser(PersonI user) {
		this.user = user;
	}
	
	public PersonI getUser() {
		return user;
	}
	
	public Set<String> getHashtags() {
		return hashtags;
	}
	
	public void addHashtag(String hashtag) {
		hashtags.add(hashtag);
	}
	
	public void setHashtags(Set<String> hashtags) {
		this.hashtags = hashtags;
	}

}
