import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.*;

/**
 * A collection of methods that allow for hashtag-based queries over a directed
 * user graph.
 *
 * @author scheiber
 */
public class HashtagApi {

	Map<String, Set<Integer>> tagged_tweets;
	Set<PersonI> graph;
	
	/**
	 * HASHTAG_API
	 * Performs the necessary parsing and pre-processing of your graph. This
	 * involves creating a new GraphMaker2 class to handle the more complex
	 * parsing.
	 * 
	 * @throws IOException 
	 * @throws FileNotFoundException 
	 */
	public HashtagApi(String filename) throws FileNotFoundException, IOException {
		GraphMaker2 gm = new GraphMaker2(filename);
		graph = gm.parse();
		tagged_tweets = classifyByHashtag();	// group tweets by hashtag
	}
	
	/**
	 * CLASSIFY_BY_HASHTAG
	 * Goes through all users in the graph and categorizes their tweet by
	 * hashtag
	 * 
	 * @return : map of hashtag to all the tweets with that hashtag
	 */
	private Map<String, Set<Integer>> classifyByHashtag() {
		Map<String, Set<Integer>> tagged = new HashMap<String, Set<Integer>>();
		Iterator<PersonI> iter = graph.iterator();
		// Classify iteratively by checking each person's tweets
		while (iter.hasNext()) {
			PersonI p = iter.next();
			Set<Tweet> tweets = p.getTweets();
			handleTweets(tweets, tagged);
		}
		return tagged;
	}
	
	/**
	 * HANDLE_TWEETS
	 * Parses each user's tweets to search for hashtags. If one is found, it
	 * is categorized in tagged.
	 * 
	 * @param tweets : all tweets from a certain user
	 * @param tagged : the map for hashtag->tweets; gets set up in this method
	 */
	private void handleTweets(Set<Tweet> tweets, Map<String, Set<Integer>> tagged) {
		Iterator<Tweet> iter = tweets.iterator();
		// Check all tweets for this user
		while (iter.hasNext()) {
			Tweet t = iter.next();
			int id = t.getID();
			Set<String> hashtags = t.getHashtags();
			Iterator<String> hash_iter = hashtags.iterator();
			// Check all hashtags for given tweet
			while (hash_iter.hasNext()) {
				String ht = hash_iter.next();
				if (tagged.containsKey(ht)) {	// hashtag seen before
					Set<Integer> ids = tagged.get(ht);
					ids.add(id);
				} else {	// first time finding this hashtag
					Set<Integer> ids = new HashSet<Integer>();
					ids.add(id);
					tagged.put(ht, ids);
				}
			}
		}
	}

	/**
	 * GET_TWEETS_BY_PERSON
	 * Returns a set of Integers, each corresponding to the unique ID of a tweet
	 * by the specified person.
	 *
	 * @param person
	 *            if person is null, then throw an IllegalArgumentException
	 */
	public Set<Integer> getTweetsByPerson(PersonI person) {
		if (person == null) {
			throw new IllegalArgumentException();
		}
		if (!graph.contains(person)) {
			throw new NoSuchElementException();
		}
		Set<Integer> tweets_id = new HashSet<Integer>();
		Iterator<PersonI> iter_p = graph.iterator();
		// Find arg person in graph
		while (iter_p.hasNext()) {
			PersonI p = iter_p.next();
			// if the arg person matches the PersonI p from graph
			if (person.equals(p)) {
				Set<Tweet> tweets = p.getTweets();
				Iterator<Tweet> iter = tweets.iterator();
				// Go through all tweets and add their ID to tweets_id
				while (iter.hasNext()) {
					Tweet t = iter.next();
					tweets_id.add(t.getID());
				}
			} else {
				continue;
			}
		}
		return tweets_id;
	}

	/**
	 * GET_TWEETS_WITH_HASHTAG
	 * Returns a set of Integers, each corresponding to the unique ID of a tweet
	 * containing the specified hashtag.
	 *
	 * @param hashtag
	 *            is non-null and begins with a pound sign
	 */
	public Set<Integer> getTweetsWithHashtag(String hashtag) {
		if (hashtag == null || !(hashtag.charAt(0) == '#')) {
			throw new IllegalArgumentException();
		}
		if (!tagged_tweets.containsKey(hashtag)) {
			throw new NoSuchElementException();
		}
		return tagged_tweets.get(hashtag);
	}
}