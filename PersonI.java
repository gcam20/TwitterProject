import java.util.Set;

/**
 * PersonI is an interface representing a node in a graph of a social network.
 * Your implementation should include a constructor that takes in an int and
 * sets the id of the PersonI to that int.
 *
 * @author saajid
 */
public interface PersonI {

	/**
	 * Returns a set of PersonIs to which this PersonI is connected.
	 */
	public Set<PersonI> getFriends();

	/**
	 * @param friends
	 *            friends is the set of PersonIs given as friends to this
	 *            PersonI. Previously held friends should no longer belong to
	 *            this PersonI. If friends is null, then throw
	 *            IllegalArgumentException.
	 */
	public void setFriends(Set<PersonI> friends);

	/**
	 *
	 * @param friend
	 *            If friend is null, then throw an IllegalArgumentException.
	 */
	public void addFriend(PersonI friend);

	/**
	 *
	 * Removes a PersonI from this PersonI's friends
	 *
	 * @param friend
	 *            If friend is null or is not a friend of this PersonI, then
	 *            throw an IllegalArgumentException.
	 */
	public void removeFriend(PersonI friend);

	/**
	 * Returns a set of PersonIs that follow this PersonI.
	 */
	public Set<PersonI> getFollowers();

	/**
	 * @param followers
	 *            the set of PersonIs given as inbound connections to this
	 *            PersonI. Previously held followers should no longer belong to
	 *            this PersonI. If followers is null, then throw an
	 *            IllegalArgumentException.
	 */
	public void setFollowers(Set<PersonI> followers);

	/**
	 * @param follower
	 *            If follower is null, then throw an IllegalArgumentException.
	 */
	public void addFollower(PersonI follower);

	/**
	 * Removes a PersonI from this PersonI's friends
	 *
	 * @param friend
	 *            If friend is null or does not follow this PersonI, then throw
	 *            an IllegalArgumentException.
	 */
	public void removeFollower(PersonI follower);

	/**
	 * Returns the name of this PersonI.
	 */
	public String getName();

	/**
	 * @param name
	 *            the name that will be given to this PersonI
	 */
	public void setName(String name);

	/**
	 * Returns the id of this PersonI. Each PersonI must have a unique id.
	 */
	public int getId();

	/**
	 * @param id
	 *            the id that will be given to this PersonI
	 */
	public void setId(int id);
	
	/**
	 * Returns tweets
	 * @return Set<Teet> tweets
	 */
	public Set<Tweet> getTweets();
	
	/**
	 * Sets PersonI's tweets field to the param tweets
	 * @param tweets
	 */
	public void setTweets(Set<Tweet> tweets);
	
	/**
	 * Adds tweet to PersonI's set of tweets
	 * @param tweet
	 */
	public void addTweet(Tweet tweet);

	/**
	 * Returns true if the two PersonIs have the same id.
	 */
	@Override
	public boolean equals(Object o);

	/**
	 * MUST be consistent with equals(). In other words, if a.equals(b), then
	 * a.hashCode() must be the same as b.hashCode().
	 *
	 */
	@Override
	public int hashCode();

}
