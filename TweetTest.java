import static org.junit.Assert.*;

import java.util.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;


public class TweetTest {
	
	Tweet t1, t2, t3, t4;

	@Before
	public void setUp() throws Exception {
		t1 = new Tweet("hello world", 1, null);
		t2 = new Tweet("hello #world", 2, null);
		t3 = new Tweet("#hello #world", 3, null);
		t4 = new Tweet("#hello#world", 4, null);
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testTweet1() {
		assertTrue(t1.getHashtags().isEmpty());
	}

	@Test
	public void testTweet2() {
		Set<String> ht = t2.getHashtags();
		assertTrue(ht.size() == 1);
		assertTrue(ht.contains("#world"));
	}
	
	@Test
	public void testTweet3() {
		Set<String> ht = t3.getHashtags();
		assertTrue(ht.size() == 2);
		assertTrue(ht.contains("#hello"));
		assertTrue(ht.contains("#world"));
	}
	
	@Test
	public void testTweet4() {
		Set<String> ht = t4.getHashtags();
		assertTrue(ht.size() == 1);
		assertTrue(ht.contains("#hello#world"));
	}
	
}
