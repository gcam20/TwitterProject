import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;


public class GraphMaker2Test {

	GraphMaker2 gm1, gm2;
	
	@Before
	public void setUp() throws Exception {
		gm1 = new GraphMaker2("sample_data/small_with_tweets.json");
		gm2 = new GraphMaker2("sample_data/users_with_tweets.json");
	}

	@After
	public void tearDown() throws Exception {
	}

	// Print test to check that data read in correctly
	@Test
	public void eyeTest1() {
		gm1.printGraph();
	}
	
	// Print test to check that data read in correctly
	@Test
	public void eyeTest2() {
		gm2.printGraph();
	}

}
