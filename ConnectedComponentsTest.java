import static org.junit.Assert.*;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;


public class ConnectedComponentsTest {

	GraphMaker gm;
	Set<PersonI> graph;
	PersonI p0, p1, p2, p3, p4, p5, p6, p7, p8;
	
	@Before
	public void setUp() throws Exception {
		gm = new GraphMaker("sample_data/small.json");
		graph = gm.parse();
		
		
		p0 = new Person(0, new HashSet<PersonI>(), "Lamont Oliver");
		p1 = new Person(1, new HashSet<PersonI>(), "Sharon Faulkner");
		p2 = new Person(2, new HashSet<PersonI>(), "Ashley Stansbery");
		p3 = new Person(3, new HashSet<PersonI>(), "Cruz Tucker");
		p4 = new Person(4, new HashSet<PersonI>(), "Mary Valone");
		p5 = new Person(5, new HashSet<PersonI>(), "Clarence Oaks");
		p6 = new Person(6, new HashSet<PersonI>(), "Jessica Brennan");
		p7 = new Person(7, new HashSet<PersonI>(), "Jeffrey Gralak");
		p8 = new Person(8, new HashSet<PersonI>(), "Ron Stutzman");
		p0.addFriend(p2);
		p0.addFriend(p1);
		p0.addFriend(p4);
		p0.addFriend(p3);
		p1.addFriend(p0);
		p1.addFriend(p4);
		p1.addFriend(p2);
		p2.addFriend(p4);
		p2.addFriend(p1);
		p3.addFriend(p0);
		p3.addFriend(p1);
		p4.addFriend(p0);
		p4.addFriend(p1);
		p5.addFriend(p7);
		p5.addFriend(p8);
		p5.addFriend(p6);
		p6.addFriend(p5);
		p6.addFriend(p8);
		p6.addFriend(p7);
		p7.addFriend(p5);
		p7.addFriend(p8);
		p7.addFriend(p6);
		p8.addFriend(p5);
		p8.addFriend(p6);
		p8.addFriend(p7);
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testIterator1() {
		PersonI check = new Person(-1, null, ""); // Holder
		Iterator<PersonI> iter_set = graph.iterator();
		Stack<PersonI> stack = new Stack<PersonI>();
		// p = p1
		while (iter_set.hasNext()) {
			PersonI p = iter_set.next();
			if (p.getId() == 1) {
				check = p;
				break;
			}
		}
		// Traverse down
		if (!(check.getId() == -1)) {
			Iterator<PersonI> dfs = ConnectedComponents.dfsIterator(check, stack);
			PersonI p = dfs.next();
			assertEquals(p1, p);
		} else {
			fail("Element not found");
		}
	}
	
	@Test
	public void testIterator2() {
		PersonI check = new Person(-1, null, ""); // Holder
		Iterator<PersonI> iter_set = graph.iterator();
		Stack<PersonI> stack = new Stack<PersonI>();
		// p = p1
		while (iter_set.hasNext()) {
			PersonI p = iter_set.next();
			if (p.getId() == 1) {
				check = p;
				break;
			}
		}
		// Traverse down
		if (!(check.getId() == -1)) {
			Iterator<PersonI> dfs = ConnectedComponents.dfsIterator(check, stack);
			dfs.next();
			assertEquals(p0, dfs.next());
		} else {
			fail("Element not found");
		}
	}
	
	@Test
	public void testIterator3() {
		PersonI check = new Person(-1, null, ""); // Holder
		Iterator<PersonI> iter_set = graph.iterator();
		Stack<PersonI> stack = new Stack<PersonI>();
		// p = p1
		while (iter_set.hasNext()) {
			PersonI p = iter_set.next();
			if (p.getId() == 1) {
				check = p;
				break;
			}
		}
		// Traverse down
		if (!(check.getId() == -1)) {
			Iterator<PersonI> dfs = ConnectedComponents.dfsIterator(check, stack);
			dfs.next();
			dfs.next();
			assertEquals(p2, dfs.next());
		} else {
			fail("Element not found");
		}
	}
	
	@Test
	public void testWCC1() {
		Set<PersonI> target = new HashSet<PersonI>();
		target.add(p0);
		target.add(p1);
		target.add(p2);
		target.add(p3);
		target.add(p4);
		Set<PersonI> wcc = ConnectedComponents.weaklyConnectedComponent(graph);
		assertEquals(target, wcc);
	}
	
	@Test
	public void testWCC2() throws FileNotFoundException, IOException {
		// People from users.json; can ignore friends/followers
		PersonI p7 = new Person(7, new HashSet<PersonI>(), "John Hang");
		PersonI p8 = new Person(8, new HashSet<PersonI>(), "Joseph Green");
		PersonI p9 = new Person(9, new HashSet<PersonI>(), "Samuel Reinking");
		PersonI p10 = new Person(10, new HashSet<PersonI>(), "Darrell Williams");
		PersonI p11 = new Person(11, new HashSet<PersonI>(), "Harold Bell");
		PersonI p12 = new Person(12, new HashSet<PersonI>(), "Victor Gadbaw");
		PersonI p13 = new Person(13, new HashSet<PersonI>(), "Jenifer Mitchell");
		PersonI p14 = new Person(14, new HashSet<PersonI>(), "Lisa Dubois");
		PersonI p15 = new Person(15, new HashSet<PersonI>(), "Michael Reid");
		PersonI p16 = new Person(16, new HashSet<PersonI>(), "Angela Palmer");
		Set<PersonI> target = new HashSet<PersonI>();
		target.add(p7);
		target.add(p8);
		target.add(p9);
		target.add(p10);
		target.add(p11);
		target.add(p12);
		target.add(p13);
		target.add(p14);
		target.add(p15);
		target.add(p16);
		GraphMaker gm = new GraphMaker("sample_data/users.json");
		Set<PersonI> g = gm.parse();
		Set<PersonI> wcc = ConnectedComponents.weaklyConnectedComponent(g);
		assertEquals(target, wcc);
	}
	
	//@Test
	public void testKosaraju1() {
		Set<PersonI> target = new HashSet<PersonI>();
		target.add(p5);
		target.add(p6);
		target.add(p7);
		target.add(p8);
		Set<PersonI> scc = ConnectedComponents.kosaraju(graph);
		assertEquals(target, scc);
	}
	
	@Test
	public void testKosaraju2() {
		Set<PersonI> testgraph = new HashSet<PersonI>();
		Person p0 = new Person(0, new HashSet<PersonI>(), "p0");
		Person p1 = new Person(1, new HashSet<PersonI>(), "p1");
		Person p2 = new Person(2, new HashSet<PersonI>(), "p2");
		Person p3 = new Person(3, new HashSet<PersonI>(), "p3");
		Person p4 = new Person(4, new HashSet<PersonI>(), "p4");
		Person p5 = new Person(5, new HashSet<PersonI>(), "p5");
		Person p6 = new Person(6, new HashSet<PersonI>(), "p6");
		Person p7 = new Person(7, new HashSet<PersonI>(), "p7");
		Person p8 = new Person(8, new HashSet<PersonI>(), "p8");
		Person p9 = new Person(9, new HashSet<PersonI>(), "p9");
		Person p10 = new Person(10, new HashSet<PersonI>(), "p10");
		Person p11 = new Person(11, new HashSet<PersonI>(), "p11");
		Person p12 = new Person(12, new HashSet<PersonI>(), "p12");
		p0.addFriend(p5);
		p0.addFriend(p1);
		p2.addFriend(p0);
		p2.addFriend(p3);
		p3.addFriend(p2);
		p3.addFriend(p5);
		p4.addFriend(p2);
		p4.addFriend(p3);
		p5.addFriend(p4);
		p6.addFriend(p0);
		p6.addFriend(p4);
		p6.addFriend(p8);
		p6.addFriend(p9);
		p7.addFriend(p6);
		p7.addFriend(p9);
		p8.addFriend(p6);
		p9.addFriend(p10);
		p9.addFriend(p11);
		p10.addFriend(p12);
		p11.addFriend(p4);
		p11.addFriend(p12);
		p12.addFriend(p9);
		testgraph.add(p0);
		testgraph.add(p1);
		testgraph.add(p2);
		testgraph.add(p3);
		testgraph.add(p4);
		testgraph.add(p5);
		testgraph.add(p6);
		testgraph.add(p7);
		testgraph.add(p8);
		testgraph.add(p9);
		testgraph.add(p10);
		testgraph.add(p11);
		testgraph.add(p12);
		Set<PersonI> scc = ConnectedComponents.kosaraju(testgraph);
		Set<PersonI> target = new HashSet<PersonI>();
		target.add(p0);
		target.add(p2);
		target.add(p3);
		target.add(p4);
		target.add(p5);
		assertEquals(target, scc);
	}
}
