import java.io.*;
import java.util.*;
import com.google.gson.stream.JsonReader;

/**
 * A class that parses a data file in JSON form into a graph representation.
 *
 * @author wangrace
 *
 */ 
public class GraphMaker {
	String filename;
	Map<Long, PersonI> refs;
	Map<Long, Set<Long>> friends_long;


	public GraphMaker(String filename) {
		this.filename = filename;
		refs = new HashMap<Long, PersonI>();
		friends_long = new HashMap<Long, Set<Long>>();
	}
	
	
	/**
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
		
		reader.beginObject();
		while (reader.hasNext()) {
			String next = reader.nextName();
			if (next.equals("id")) {
				id = reader.nextInt();
			} else if (next.equals("friends")) {
				friends = readFriends(reader);
			} else if (next.equals("name")) {
				name = reader.nextString();
			} else {
				// TODO : handle bad input
			}
		}
		reader.endObject();
		// Create new person, handle friends later
		Person p = new Person(id, new HashSet<PersonI>(), name);
		// Track list of friends
		friends_long.put(new Long(id) , friends);
		// Track Person object by ID
		refs.put(new Long(id), p);
		return p;
	}
	
	
	/**
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
