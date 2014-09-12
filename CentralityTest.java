import static org.junit.Assert.*;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;



public class CentralityTest {
	
	PersonI p1, p2, p3, p4, p5, p6, p7, p8;
	Set<PersonI> graph;

	@Before
	public void setUp() throws Exception {
		p1 = new Person(1, null, null);
		p2 = new Person(2, null, null);
		p3 = new Person(3, null, null);
		p4 = new Person(4, null, null);
		p1.addFriend(p3);
		p1.addFriend(p4);
		p2.addFriend(p1);
		p2.addFriend(p4);
		p3.addFriend(p2);
		p4.addFriend(p1);
		p4.addFriend(p2);
		p4.addFriend(p3);
		graph = new HashSet<PersonI>();
		graph.add(p1);
		graph.add(p2);
		graph.add(p3);
		graph.add(p4);
	}

	@After
	public void tearDown() throws Exception {
	}
	
	@Test
	public void centralityTest() {
		GraphMaker gm = new GraphMaker("sample_data/users.json");
		try {
			Set<PersonI> graph = gm.parse();
			Centrality.betweennessCentrality(graph);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@Test
	public void basicPageRankTest1() { 
		Map<PersonI, Double> ranks = Centrality.pageRank(graph, 1);
		assertEquals(0.85833, ranks.get(p1), 0.01);
		assertEquals(1.28333, ranks.get(p2), 0.01);
		assertEquals(0.85833, ranks.get(p3), 0.01);
		assertEquals(1.00000, ranks.get(p4), 0.01);
	}
	
	@Test
	public void basicPageRankTest2() {
		Map<PersonI, Double> ranks = Centrality.pageRank(graph, 2);
		assertEquals(0.97875, ranks.get(p1), 0.01);
		assertEquals(1.16291, ranks.get(p2), 0.01);
		assertEquals(0.79813, ranks.get(p3), 0.01);
		assertEquals(1.05808, ranks.get(p4), 0.01);
	}
	
	@Test
	public void basicPageRankTest3() {
		Map<PersonI, Double> ranks = Centrality.pageRank(graph, 3);
		assertEquals(0.94403, ranks.get(p1), 0.01);
		assertEquals(1.12820, ranks.get(p2), 0.01);
		assertEquals(0.86576, ranks.get(p3), 0.01);
		assertEquals(1.06021, ranks.get(p4), 0.01);
	}
	
}
