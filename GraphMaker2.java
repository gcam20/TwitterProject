import java.io.*;
import java.util.*;
import com.google.gson.stream.JsonReader;

/**
 * A class that parses a data file in JSON form into a graph representation.
 *
 * @author wangrace
 *
 */ 
public class GraphMaker2 {
	String filename;
	Map<Long, PersonI> refs;
	Map<Long, Set<Long>> friends_long;


	public GraphMaker2(String filename) {
		this.filename = filename;
		refs = new HashMap<Long, PersonI>();
		friends_long = new HashMap<Long, Set<Long>>();
	}
	
	
	/**
	 * READ_FRIENDS
	 * Method called to read list of friends for each given user
	 * 
	 * @param reader : JsonReader being used throughout
	 * @return set of friends as a HashSet<Long>
	 * @throws IOException
	 */
	private Set<Long> readFriends(JsonReader reader) throws IOException {
		Set<Long> friends = new HashSet<Long>();
		reader.beginArray();
		while (reader.hasNext()) {
			// TODO : maybe make friends list a list of PersonI's
			friends.add(reader.nextLong());	// Friends given by their IDs
		}
		reader.endArray();
		return friends;
	}
	
	
	/**
	 * READ_TWEETS
	 * Goes through array of tweets and adds them all to the set of tweets
	 * to be returned
	 * 
	 * @param reader
	 * @return : set of all tweets
	 * @throws IOException
	 */
	private Set<Tweet> readTweets(JsonReader reader) throws IOException {
		Set<Tweet> tweets = new HashSet<Tweet>();
		reader.beginArray();
		while (reader.hasNext()) {
			tweets.add(getTweet(reader));
		}
		reader.endArray();
		return tweets;
	}
	
	/**
	 * GET_TWEET
	 * Checks each tweet for its text content and id and sets up a Tweet object
	 * to be returned
	 * 
	 * @param reader
	 * @return : Tweet object
	 * @throws IOException
	 */
	private Tweet getTweet(JsonReader reader) throws IOException {
		String text = "";
		int id = -1;
		reader.beginObject();
		while (reader.hasNext()) {
			String next = reader.nextName();
			if (next.equals("text")) {
				text = reader.nextString();
			} else if (next.equals("id")) {
				id = reader.nextInt();
			} else {
				// TODO : handle bad input
			}
		}
		reader.endObject();
		// null user will be handled in Person.setTweets();
		return new Tweet(text, id, null);
	}
	
	/**
	 * READ_PERSON
	 * Method called to read next person from the Json data
	 * 
	 * @param reader : JsonReader used throughout
	 * @return new PersonI using data from the Json doc
	 * @throws IOException
	 */
	private PersonI readPerson(JsonReader reader) throws IOException {
		int id = -1;
		Set<Long> friends = new HashSet<Long>();
		String name = "";
		Set<Tweet> tweets = new HashSet<Tweet>();
		
		reader.beginObject();
		while (reader.hasNext()) {
			String next = reader.nextName();
			if (next.equals("id")) {
				id = reader.nextInt();
			} else if (next.equals("friends")) {
				friends = readFriends(reader);
			} else if (next.equals("name")) {
				name = reader.nextString();
			} else if (next.equals("tweets")){
				tweets = readTweets(reader);
			} else {
				// TODO : handle bad input
			}
		}
		reader.endObject();
		// Create new person, handle friends later
		Person p = new Person(id, new HashSet<PersonI>(), name);
		p.setTweets(tweets);	// set tweets
		// Track list of friends
		friends_long.put(new Long(id) , friends);
		// Track Person object by ID
		refs.put(new Long(id), p);
		return p;
	}
	
	
	/**
	 * READ_PEOPLE
	 * Initial method called to read list of all people
	 * 
	 * @param reader : JsonReader used throughout
	 * @return : graph as HashSet<PersonI>
	 * @throws IOException
	 */
	private Set<PersonI> readPeople(JsonReader reader) throws IOException {
		Set<PersonI> graph = new HashSet<PersonI>();
		reader.beginArray();
		while (reader.hasNext()) {
			graph.add(readPerson(reader));
		}
		reader.endArray();
		return graph;
	}
	
	
	/**
	 * SET_CONNECTIONS
	 * 
	 * Takes the graph and makes all friend/follower connections
	 * 
	 * @param graph
	 */
	private void setConnections(Set<PersonI> graph) {
		Iterator<PersonI> iter_graph = graph.iterator();
		// Check all people on graph
		while (iter_graph.hasNext()) {
			PersonI p = iter_graph.next();
			Long myID = new Long(p.getId());
			// Use this ID to get friends
			Set<Long> friends = friends_long.get(myID);
			Iterator<Long> iter_fr = friends.iterator();
			// Make all connections from friends set
			while (iter_fr.hasNext()) {
				Long frID = iter_fr.next();
				p.addFriend(refs.get(frID));	// Use ID in Set to match to Person obj
			}
		}
	}
	
	
	/**
	 * PARSE
	 * 
	 * Returns a set of all PersonIs defined in the input file.
	 * @throws FileNotFoundException, IOException 
	 */
	public Set<PersonI> parse() throws FileNotFoundException, IOException {
		FileReader r = new FileReader(filename);
		JsonReader reader = new JsonReader(r);
		try {
			Set<PersonI> graph = readPeople(reader);
			setConnections(graph); // Set friend connections after parsing
			return graph;
		} finally {
			reader.close();
		}
	}

	
	public void printGraph() {
		Set<PersonI> graph;
		try {
			graph = parse();
			Iterator<PersonI> iter = graph.iterator();
			System.out.println("Graph for: " + filename);
			while (iter.hasNext()) {
				System.out.println(iter.next().toString());
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
}
