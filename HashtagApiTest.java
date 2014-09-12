import static org.junit.Assert.*;

import java.util.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;


public class HashtagApiTest {
	
	HashtagApi htapi;

	@Before
	public void setUp() throws Exception {
		htapi = new HashtagApi("sample_data/small_with_tweets.json");
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void byHashtagValRocks() {
		Set<Integer> tweets = htapi.getTweetsWithHashtag("#ValRocks!");
		Set<Integer> test = new HashSet<Integer>();
		test.add(-25);
		test.add(-37);
		test.add(-55);
		test.add(-91);
		test.add(-97);
		test.add(-106);
		test.add(-115);
		test.add(-124);
		test.add(-133);
		test.add(-151);
		test.add(-154);
		test.add(-163);
		test.add(-169);
		assertEquals(test, tweets);
	}
	
	@SuppressWarnings("unused")
	@Test (expected=NoSuchElementException.class)
	public void byHashtagNone() throws NoSuchElementException {
		Set<Integer> tweets = htapi.getTweetsWithHashtag("#ThisHashtagWasNotUsed");
	}
	
	@Test
	public void byPersonLamont() {
		Set<Integer> tweets = htapi.getTweetsByPerson(new Person(0, null, "Lamont Oliver"));
		Set<Integer> test = new HashSet<Integer>();
		test.add(-7);
		test.add(-10);
		test.add(-13);
		test.add(-16);
		test.add(-19);
		test.add(-22);
		test.add(-25);
		assertEquals(test, tweets);
	}

	@Test
	public void byPersonRon() {
		Set<Integer> tweets = htapi.getTweetsByPerson(new Person(8, null, "Ron Stutzman"));
		Set<Integer> test = new HashSet<Integer>();
		test.add(-163);
		test.add(-166);
		test.add(-169);
		assertEquals(test, tweets);
	}
	
	@SuppressWarnings("unused")
	@Test (expected=NoSuchElementException.class)
	public void byPersonNone() throws NoSuchElementException {
		Set<Integer> tweets = htapi.getTweetsByPerson(new Person(27, null, "Fake Dude"));
	}
	
}
