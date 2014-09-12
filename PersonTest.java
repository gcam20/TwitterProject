import static org.junit.Assert.*;

import java.util.HashSet;
import java.util.Set;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;


public class PersonTest {

	GraphMaker gm;
	PersonI josh, ben, mike, greg, daniel;
	
	@Before
	public void setUp() throws Exception {
		gm = new GraphMaker("sample_data/small.json");
		gm.parse(); 	// Set up graph
		josh = new Person(1, new HashSet<PersonI>(), "josh");
		ben = new Person(2, new HashSet<PersonI>(), "ben");
		mike = new Person(3, new HashSet<PersonI>(), "mike");
		greg = new Person(4, new HashSet<PersonI>(), "greg");
		daniel = new Person(5, new HashSet<PersonI>(), "daniel");
		
		// Friending
		josh.addFriend(ben);
		ben.addFriend(josh);
		ben.addFriend(mike);
		ben.addFriend(greg);
		mike.addFriend(josh);
		mike.addFriend(greg);
		greg.addFriend(daniel);
		daniel.addFriend(mike);
		daniel.addFriend(greg);
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testFriends1() {
		Set<PersonI> a = new HashSet<PersonI>();
		a.add(josh);
		a.add(mike);
		a.add(greg);
		assertEquals(a, ben.getFriends());
	}
	
	@Test
	public void testFriends2() {
		Set<PersonI> a = new HashSet<PersonI>();
		a.add(mike);
		a.add(greg);
		assertEquals(a, daniel.getFriends());
	}
	
	@Test
	public void testFollowers1() {
		Set<PersonI> a = new HashSet<PersonI>();
		a.add(ben);
		a.add(mike);
		a.add(daniel);
		assertEquals(a, greg.getFollowers());
	}
	
	@Test
	public void testFollowers2() {
		Set<PersonI> a = new HashSet<PersonI>();
		a.add(greg);
		assertEquals(a, daniel.getFollowers());
	}
	
	@Test
	public void testRemoveFriend1() {
		ben.removeFriend(mike);
		Set<PersonI> a = new HashSet<PersonI>();
		a.add(josh);
		a.add(greg);
		assertEquals(a, ben.getFriends());
	}
	
	@Test
	public void testRemoveFriend2() {
		greg.removeFriend(daniel);
		Set<PersonI> a = new HashSet<PersonI>();
		assertEquals(a, greg.getFriends());
	}
	
	@Test
	public void testRemoveFollower1() {
		mike.removeFollower(ben);
		Set<PersonI> a = new HashSet<PersonI>();
		a.add(daniel);
		assertEquals(a, mike.getFollowers());
	}
	
	@Test
	public void testRemoveFollower2() {
		greg.removeFollower(ben);
		mike.removeFollower(ben);
		Set<PersonI> a = new HashSet<PersonI>();
		a.add(josh);
		assertEquals(a, ben.getFriends());
	}

}
