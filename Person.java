import java.util.*;


public class Person implements PersonI {
	
	int id;
	Set<PersonI> friends;
	Set<PersonI> followers;
	String name;
	Set<Tweet> tweets;
	
	public Person(int id, Set<PersonI> friends, String name) {
		this.id = id;
		if (friends == null) {
			friends = new HashSet<PersonI>();
		}
		this.friends = friends;
		if (name == null) {
			name = "";
		}
		this.name = name;
		this.followers = new HashSet<PersonI>();
		this.tweets = new HashSet<Tweet>();
	}

	@Override
	public Set<PersonI> getFriends() {
		return friends;
	}

	@Override
	public void setFriends(Set<PersonI> friends) {
		if (friends == null) {
			throw new IllegalArgumentException();
		}
		Iterator<PersonI> iter = this.friends.iterator();
		// Remove all friends you currently have
		while (iter.hasNext()) {
			PersonI next = iter.next();
			this.removeFriend(next);	// Remove them as friend
		}
		iter = friends.iterator();
		// Add new friends from set
		while (iter.hasNext()) {
			PersonI next = iter.next();
			this.addFriend(next);	// Add them as friend
		}

	}

	// TODO : person can't friend/follow themself
	@Override
	public void addFriend(PersonI friend) {
		if (friend == null) {
			throw new IllegalArgumentException();
		}
		this.friends.add(friend);	// Add to list of friends
		friend.addFollower(this);	// Set connection in other direction
	}

	@Override
	public void removeFriend(PersonI friend) {
		if (friend == null) {
			throw new IllegalArgumentException();
		}
		this.friends.remove(friend);	// Remove from list of friends
		friend.removeFollower(this);	// Remove connection in other direction
	}

	@Override
	public Set<PersonI> getFollowers() {
		return followers;
	}

	@Override
	public void setFollowers(Set<PersonI> followers) {
		if (followers == null) {
			throw new IllegalArgumentException();
		}
		Iterator<PersonI> iter = this.followers.iterator();
		// Remove all followers you currently have
		while (iter.hasNext()) {
			PersonI next = iter.next();
			next.removeFriend(this);	// Remove me as friend (removes them as follower)
		}
		iter = followers.iterator();
		// Add new followers from set
		while (iter.hasNext()) {
			PersonI next = iter.next();
			next.addFriend(this);	// Add me as friend (adds them as follower)
		}
	}

	@Override
	public void addFollower(PersonI follower) {
		if (follower == null) {
			throw new IllegalArgumentException();
		}
		if (!follower.getFriends().contains(this)) {
			follower.addFriend(this);
			// This method will be called again within addFriend
		} else {
			followers.add(follower);
		}
	}

	@Override
	public void removeFollower(PersonI follower) {
		if (follower == null) {
			throw new IllegalArgumentException();
		}
		if (follower.getFriends().contains(this)) {
			follower.removeFriend(this);
		} else {
			followers.remove(follower);
		}
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public void setName(String name) {
		if (name == null) {
			throw new IllegalArgumentException();
		}
		this.name = name;
	}

	@Override
	public int getId() {
		return id;
	}

	@Override
	public void setId(int id) {
		this.id = id;
	}
	
	@Override
	public String toString() {
		String out = new String ("ID: " + id + ",\n");
		out = out.concat("Friends: ");
		Iterator<PersonI> iter = friends.iterator();
		while (iter.hasNext()) {
			PersonI p = iter.next();
			out = out.concat(Integer.toString(p.getId()) + ", ");
		}
		out = out.concat("\nName: " + name + "\n");
		return out;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof Person)) {
			return false;
		}
		PersonI other = (PersonI) obj;
		boolean ret = true;
//		ret = ret && friends.equals(other.getFriends());
//		ret = ret && followers.equals(other.getFollowers());
//		ret = ret && name.equals(other.getName());
		ret = ret && (id == other.getId());
		return ret;
	}
	
	
	// TODO : make better
	@Override
	public int hashCode() {
		int hc = 0;
		hc += id;
//		hc += name.hashCode();
//		Iterator<PersonI> iter_fr = friends.iterator();
//		Iterator<PersonI> iter_fo = followers.iterator();
//		// Get hash code from name of friends
//		while (iter_fr.hasNext()) {
//			PersonI p = iter_fr.next();
//			hc += p.getName().hashCode();
//		}
//		// Get hash code from name of followers
//		while (iter_fo.hasNext()) {
//			PersonI p = iter_fo.next();
//			hc += p.getName().hashCode();
//		}
		return hc;
	}

	@Override
	public Set<Tweet> getTweets() {
		return tweets;
	}

	@Override
	public void setTweets(Set<Tweet> tweets) {
		Iterator<Tweet> iter = tweets.iterator();
		while (iter.hasNext()) {
			Tweet t = iter.next();
			// Add each individually to check user
			this.addTweet(t);
		}
	}

	@Override
	public void addTweet(Tweet tweet) {
		PersonI current = tweet.getUser();
		// If user isn't set correctly
		if (current == null || !current.equals(this)) {
			tweet.setUser(this);
		}
		tweets.add(tweet);
	}

}
