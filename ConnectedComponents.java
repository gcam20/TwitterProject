import java.util.*;

/**
 * ConnectedComponents is a class that processes a graph.
 *
 * Do not change method headers that are given to you. Be careful about
 * modifying the graphs that are passed into your methods. Make sure you
 * understand what you are referencing in your algorithms and tests.
 *
 * @author saajid
 */
public class ConnectedComponents {

	/**
	 * Returns an iterator (defined as an inner class) of the reachable nodes in
	 * the graph from the source node. Nodes are returned in depth-first order
	 * where edges are traversed in increasing order by Ids of the target
	 * PersonIs. Your iterator should be lazy; it should not prepare all the
	 * elements to be returned by next() until they are needed (hasNext() should
	 * have similar behavior). Your inner class should have a constructor that
	 * takes in the stack provided in the method stub. This stack will be used
	 * to test for the lazy property of your iterator. Make sure not to pop
	 * PersonIs from your stack until they are required by a call to next().
	 *
	 * Your iterator should return an UnsupportedOperationException if the
	 * remove() method is called.
	 *
	 * @param root
	 *            - if root is null, then throw IllegalArgumentException
	 */
	public static Iterator<PersonI> dfsIterator(PersonI root,
												Stack<PersonI> iteratorStack) {
		// Use this stack to instantiate an instance of your Iterator<PersonI>
		// inner class
		if (root == null || iteratorStack == null) {
			throw new IllegalArgumentException();
		}
		return new graphIterator(root, iteratorStack);
	}
	
	private static class graphIterator implements Iterator<PersonI> {
		Stack<PersonI> stack;
		Set<PersonI> visited;
		

		public graphIterator(PersonI root, Stack<PersonI> iteratorStack) {
			this.stack = iteratorStack;
			this.visited = new HashSet<PersonI>();
			stack.push(root);
		}
		
		@Override
		public boolean hasNext() {
			if (!stack.empty()) {
				// Check that node hasn't already been visited
				while (!stack.empty() && visited.contains(stack.peek())) {
					stack.pop();
				}
			} return !stack.empty();
		}
		
		private List<PersonI> order(Set<PersonI> people) {
			List<PersonI> out = new LinkedList<PersonI>();
			Iterator<PersonI> iter = people.iterator();
			// Iterate over list of Persons given
			while (iter.hasNext()) {
				PersonI p = iter.next();
				int index = 0;
				// Iterate down current 'out' list to see where to insert p
				Iterator<PersonI> iter_out = out.iterator();
				// As long as list still has elements and the next ID is greater than
				while (iter_out.hasNext() && (iter_out.next().getId() > p.getId())) {
					index++;
				}
				out.add(index, p);
			}
			return out;
		}

		@Override
		public PersonI next() {	// O(V+E)
			if (!hasNext()) {	// O(V)
				throw new NoSuchElementException();
			}
			PersonI next = stack.pop();
			visited.add(next); // Keep track of nodes visited
			Set<PersonI> friends = next.getFriends();
			List<PersonI> ordered = order(friends); // Order friends
			Iterator<PersonI> iter_fr = ordered.iterator();
			while (iter_fr.hasNext()) {	// O(E)
				PersonI p = iter_fr.next();
				// If friend hasn't been visited, add to stack/seen set
				if (!visited.contains(p)) {
					stack.push(p);
				}
			}
			return next;
		}

		@Override
		public void remove() {
			throw new UnsupportedOperationException();
		}
	}

	/**
	 * Returns the maximal weakly connected component of the graph.
	 *
	 * @param graph
	 *            - If graph is null, then throw an IllegalArgumentException
	 */
	public static Set<PersonI> weaklyConnectedComponent(Set<PersonI> graph) {
		if (graph == null) {
			throw new IllegalArgumentException();
		}
		if (graph.size() == 1) {
			return graph;
		}
		Iterator<PersonI> iter = graph.iterator();
		if (iter.hasNext()) {
			graph = modifyGraph(graph);
			return getWeaklyConnected(graph);
		} else {
			throw new IllegalArgumentException();
		}
	}
	
	private static Set<PersonI> modifyGraph(Set<PersonI> graph) {
		Iterator<PersonI> iter = graph.iterator();
		while (iter.hasNext()) {
			// Get friends/followers for each Person
			PersonI p = iter.next();
			Set<PersonI> followers = p.getFollowers();
			Iterator<PersonI> iter_followers = followers.iterator();
			// add all followers as friends
			while (iter_followers.hasNext()) {
				p.addFriend(iter_followers.next());
			}
		}
		return graph;
	}
	
	private static Set<PersonI> getWeaklyConnected(Set<PersonI> graph) {
		Set<PersonI> wcc = new HashSet<PersonI>();	// largest wcc seen
		Set<PersonI> contained = new HashSet<PersonI>(); // set of people in a wcc
		Iterator<PersonI> iter_graph = graph.iterator();
		Stack<PersonI> stack = new Stack<PersonI>();
		// check everyone in graph
		while (iter_graph.hasNext()) {
			PersonI p = iter_graph.next();
			// if this person is not in one of the wcc that has been seen
			if (!contained.contains(p)) {
				Iterator<PersonI> iter = dfsIterator(p, stack);	// dfs iterator to build wcc
				Set<PersonI> component = new HashSet<PersonI>();
				// build component and add to contained set
				while (iter.hasNext()) {
					PersonI in = iter.next();
					component.add(in);
					contained.add(in);
				}
				// if new component is larger, update largest wcc seen
				if (component.size() > wcc.size()) {
					wcc = component;
				}
			}
		}
		return wcc;
	}

	/**
	 * Returns the maximal strongly connected component of the graph.
	 *
	 * @param graph
	 *            - If graph is null, then throw an IllegalArgumentException.
	 */
	public static Set<PersonI> kosaraju(Set<PersonI> graph) {
		if (graph == null) {
			throw new IllegalArgumentException();
		}
		if (graph.size() == 1) {
			return graph;
		}
		Iterator<PersonI> iter = graph.iterator();
		Stack<PersonI> stack = new Stack<PersonI>();
		Set<PersonI> seen = new HashSet<PersonI>();
		while (iter.hasNext()) {
			PersonI root = iter.next();
			while (seen.contains(root) && iter.hasNext()) {
				root = iter.next();
			}
			if (iter.hasNext()) {
				revDFS(root, stack, seen);
			}
		}
		return getStronglyConnected(stack);
	}
	
	//For testing...
	private static void printStack(Stack<PersonI> stack) {
		Stack<PersonI> hold = new Stack<PersonI>();
		System.out.println("STACK:");
		while (!stack.empty()) {
			PersonI p = stack.pop();
			System.out.println("  ID: " + p.getId());
			hold.push(p);
		}
		while (!hold.empty()) {
			stack.push(hold.pop());
		}
	}
	
	private static void revDFS(PersonI root, Stack<PersonI> stack, 
														Set<PersonI> seen) {
		// get followers instead for reverse; same otherwise
		Set<PersonI> friends = root.getFollowers(); 
		List<PersonI> friends_rev = orderRev(friends); // reorder to go to higher order
		Iterator<PersonI> iter = friends_rev.iterator();
		while (iter.hasNext()) {	// O(E)
			PersonI p = iter.next();
			// if person hasn't been seen
			if (!seen.contains(p)) {	// Happens V overall times
				seen.add(p);
				revDFS(p, stack, seen);
			}
		}
		// Once out of loop (i.e. all friends in stack), add this root
		if (stack.contains(root)) { // special case; hacky
			Stack<PersonI> hold = new Stack<PersonI>();
			PersonI p = stack.pop();
			while (!p.equals(root)) {
				hold.push(p);
				p = stack.pop();
			}
			while (!hold.empty()) {
				stack.push(hold.pop());
			}
			stack.push(root);
		} else {
			stack.push(root);
		}
	}
	
	private static List<PersonI> orderRev(Set<PersonI> people) {
		List<PersonI> out = new LinkedList<PersonI>();
		Iterator<PersonI> iter = people.iterator();
		// Iterate over list of Persons given
		while (iter.hasNext()) {
			PersonI p = iter.next();
			int index = 0;
			// Iterate down current 'out' list to see where to insert p
			Iterator<PersonI> iter_out = out.iterator();
			// As long as list still has elements and the next ID is less than
			while (iter_out.hasNext() && (iter_out.next().getId() > p.getId())) {
				index++;
			}
			out.add(index, p);
		}
		return out;
	}
	
	private static Set<PersonI> getStronglyConnected(Stack<PersonI> stack) {
		Set<PersonI> scc = new HashSet<PersonI>();	// keep track of largest scc seen
		Set<PersonI> seen = new HashSet<PersonI>();
		while (!stack.empty()) { // O(V)
			PersonI p = stack.pop();
			Set<PersonI> target = new HashSet<PersonI>();
			target.add(p);
			seen.add(p);
			makeStronglyConnected(p, target, seen);
			// Remove all Persons already accounted for in a scc
			while (!stack.empty() && seen.contains(stack.peek())) { // O(V)
				stack.pop();
			}
			if (target.size() > scc.size()) { // update scc as needed
				scc = target;
			}
		}
		return scc;
	} 
	
	
	private static void makeStronglyConnected(PersonI root, Set<PersonI> target, Set<PersonI> seen) {
		Set<PersonI> friends = root.getFriends(); // get friends
		List<PersonI> friends_rev = orderRev(friends);
		Iterator<PersonI> iter = friends_rev.iterator();
		while (iter.hasNext()) { // check all in rev order
			PersonI p = iter.next();
			if (!seen.contains(p)) {
				seen.add(p);
				target.add(p);
				makeStronglyConnected(p, target, seen);
			}
		}
	}
	
}
