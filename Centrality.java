import java.util.*;


/**
 * Centrality is a class that processes a graph to produce measures of
 * popularity.
 *
 * Do not change method headers that are provided.
 *
 * @author scheiber
 */
public class Centrality {

	/**
	 * BETWEENNESS_CENTRALITY
	 * Returns a mapping from each person in the graph to a double representing
	 * his or her betweenness centrality.
	 *
	 * @param graph
	 *            if graph is null, then throw an IllegalArgumentException
	 */
	public static Map<PersonI, Double> betweennessCentrality(Set<PersonI> graph) {
		if (graph == null) {
			throw new IllegalArgumentException();
		}
		Map<PersonI, Double> centralities = new HashMap<PersonI, Double>();
		if (graph.size() == 1) {
			Iterator<PersonI> iter = graph.iterator();
			centralities.put(iter.next(), 0.0);
		} else {	// graph.size() > 1
			Map<PersonI, Node> refs = createNodes(graph);	// Nodes with distance values
			Collection<Node> nodes = refs.values();
			getCentralities(graph, refs, nodes);
			nodes = refs.values();
			Iterator<Node> iter = nodes.iterator();
			while(iter.hasNext()) {
				Node n = iter.next();
				centralities.put(n.getP(), n.getCentrality());
			}
		}
		return centralities;
		
	}
	
	/**
	 * CREATE_NODES
	 * Takes all users in the graph and makes a Node object for them. Puts
	 * these paired values in a map
	 * 
	 * @param graph : set of all users
	 * @return : map of PersonI->Node pairs
	 */
	private static Map<PersonI, Node> createNodes(Set<PersonI> graph) {
		Map<PersonI, Node> refs = new HashMap<PersonI, Node>();
		Iterator<PersonI> iter = graph.iterator();
		while (iter.hasNext()) {
			PersonI p = iter.next();
			refs.put(p, new Node(p));
		}
		return refs;
	}
	
	/**
	 * GET_CENTRALITIES
	 * Pop
	 * 
	 * @param graph
	 * @param refs
	 * @param nodes
	 */
	private static void getCentralities(Set<PersonI> graph, 
								Map<PersonI, Node> refs, Collection<Node> nodes){
		Iterator<PersonI> iter = graph.iterator();
		// Calculate centrality for each person on graph
		while (iter.hasNext()) { // O(V)
			resetBig(nodes);		// reset predecessor lists, distances, and sigmas
			PersonI p = iter.next();
			Stack<Node> stack = BFS(p, refs);
			resetAllDeps(nodes);	// reset dependecies
			calcCentrality(stack, p);
		}
	}
	
	/**
	 * RESET_BIG
	 * Resets sigma, predecessor and distance values for each Node
	 * 
	 * @param nodes
	 */
	private static void resetBig(Collection<Node> nodes) {
		Iterator<Node> iter = nodes.iterator();
		while (iter.hasNext()) {
			Node n = iter.next();
			n.reset();
		}
	}
	
	/**
	 * BFS
	 * Performs a Breadth-First Search starting at the root user. Updates sigma
	 * values, updates shortest path distances, and sets predecessors for each
	 * person in the graph.
	 * 
	 * @param root : user from which to start
	 * @param refs : mapping of PersonI->Node
	 * @return
	 */
	private static Stack<Node> BFS(PersonI root, Map<PersonI, Node> refs) {
		Stack<Node> stack = new Stack<Node>();
		LinkedList<Node> queue = new LinkedList<Node>();
		Node rootnode = refs.get(root);
		rootnode.setDistance(0);	// distance(root) = 0
		rootnode.setSigma(1.0);
		queue.addFirst(rootnode);	// Add root to queue; FIFO
		while (!queue.isEmpty()) { 	// O(V) --> conjunction with below code
			Node n = queue.removeFirst();
			stack.push(n);
			Set<PersonI> friends = n.getFriends();
			Iterator<PersonI> iter = friends.iterator();
			while (iter.hasNext()) { // O(E)
				PersonI p_friend = iter.next();
				Node n_friend = refs.get(p_friend);
				double new_dist = n.getDistance() + 1;	// get new distance
				// Friend seen for first time
				if (n_friend.getDistance() == Double.POSITIVE_INFINITY) {
					n_friend.setDistance(new_dist);
					queue.addLast(n_friend);	// FIFO
				}
				// shortest path to n_friend via n?
				if (new_dist == n_friend.getDistance()) {
					n_friend.combineSigmas(n);		// sigma(friend) = sigma(n) + sigma(friend)
					n_friend.addPred(n);			// append n to Predecessors(friend)
				}
			}
		}
		return stack;
	}
	
	/**
	 * CALC_CENTRALITY
	 * Pops stack until it is empty. Updates dependencies of each friend and 
	 * then sets the centrality for each user
	 * 
	 * @param stack : stack built during BFS
	 * @param root : node that was used as the root in the BFS
	 */
	private static void calcCentrality(Stack<Node> stack, PersonI root) {
		while (!stack.empty()) {
			Node w = stack.pop();
			List<Node> pred = w.getPred();
			Iterator<Node> iter = pred.iterator();
			while (iter.hasNext()) {
				Node friend = iter.next();
				friend.updateDep(w);
			}
			if (!w.getP().equals(root)) {
				w.setCentrality(w.getCentrality() + w.getDep());	// c(w) = c(w) + dep(w)
			}
		}
	}
	
	/**
	 * RESET_ALL_DEPS
	 * Resets dependencies for each Node
	 * 
	 * @param nodes
	 */
	private static void resetAllDeps(Collection<Node> nodes) {
		Iterator<Node> iter = nodes.iterator();
		while (iter.hasNext()) {
			Node n = iter.next();
			n.resetDep();
		}
	}
	
////////////////////////////////////////////////////////////////////////////////
////////////////////////////////////////////////////////////////////////////////
////////////////////////////////////////////////////////////////////////////////

	/**
	 * PAGE_RANK
	 * Returns a mapping from each person in the graph to his or her PageRank
	 * after the specified number of iterations.
	 *
	 * @param graph
	 *            if graph is null, then throw an IllegalArgumentException
	 */
	public static Map<PersonI, Double> pageRank(Set<PersonI> graph,
			int iterations) {
		if (graph == null) {
			throw new IllegalArgumentException();
		}
		if (graph.size() == 1) {
			Map<PersonI, Double> ret = new HashMap<PersonI, Double>();
			Iterator<PersonI> iter = graph.iterator();
			ret.put(iter.next(), 1.0);
			return ret;
		}
		double damping = 0.85;
		Map<PersonI, PRNode> refs = createPRNodes(graph);
		// Get all Persons as Nodes
		Collection<PRNode> nodes = refs.values();
		// Run this 'iterations' times
		while (iterations > 0) {
			Iterator<PRNode> iter = nodes.iterator();
			// Grab all ranks from previous iteration
			Map<PRNode, Double> old_ranks = markRanks(nodes);
			while (iter.hasNext()) {	// Check all nodes
				PRNode n = iter.next();
				double sum = getSum(n, refs, old_ranks);
				n.setRank((1 - damping) + (damping * sum));
			}
			iterations--;
		}
		return getRankMap(nodes);
	}
	
	/**
	 * CREATE_PR_NODES
	 * Goes through graph of all users to create a PRNode object for each one
	 * of them
	 * 
	 * @param graph : all users
	 * @return mapping of PersonI->PRNode
	 */
	private static Map<PersonI, PRNode> createPRNodes(Set<PersonI> graph) {
		Map<PersonI, PRNode> refs = new HashMap<PersonI, PRNode>();
		Iterator<PersonI> iter = graph.iterator();
		while (iter.hasNext()) {
			PersonI p = iter.next();
			refs.put(p, new PRNode(p));
		}
		return refs;
	}
	
	/**
	 * MARK_RANKS
	 * Grabs ranks for all users so that they can be used in getSum()
	 * 
	 * @param nodes
	 * @return : mapping of PRNode->Rank
	 */
	private static Map<PRNode, Double> markRanks(Collection<PRNode> nodes) {
		Map<PRNode, Double> ranks = new HashMap<PRNode, Double>();
		Iterator<PRNode> iter = nodes.iterator();
		while (iter.hasNext()) {
			PRNode n = iter.next();
			ranks.put(n, n.getRank());
		}
		return ranks;
	}
	
	/**
	 * GET_SUM
	 * For a given PRNode n, checks all friends/followers to calculate a
	 * sum that will then be multiplied by the damping ratio
	 * 
	 * @param n : node to get rank for
	 * @param refs : PersonI->PRNode mapping
	 * @param old_ranks : ranks from previous iteration
	 * @return : sum = PR[t-1](follower)/|O(follower)| for all followers of n
	 */
	private static double getSum(PRNode n, Map<PersonI, PRNode> refs, Map<PRNode, Double> old_ranks) {
		double sum = 0;
		Set<PersonI> fo = n.getFollowers();	// I(n) : set of 'in' vertices
		Iterator<PersonI> iter = fo.iterator();
		while (iter.hasNext()) { // For each 'in' vertex
			PersonI p = iter.next();
			PRNode follower = refs.get(p);
			// |O(follower)| : size of set of 'out vertices
			double o_follower = follower.getFriends().size();
			// sum += PR[t-1](follower)/|O(follower)|
			sum += old_ranks.get(follower)/(o_follower);
		}
		return sum;
	}
	
	/**
	 * GET_RANK_MAP
	 * Goes through every PRNode and gets their rank. Creates a map of each
	 * PersonI->Rank
	 * 
	 * @param nodes : all users represented as PRNode
	 * @return : map of user to rank
	 */
	private static Map<PersonI, Double> getRankMap(Collection<PRNode> nodes) {
		Map<PersonI, Double> ranks = new HashMap<PersonI, Double>();
		Iterator<PRNode> iter = nodes.iterator();
		while (iter.hasNext()) {
			PRNode n = iter.next();
			ranks.put(n.getP(), n.getRank());
		}
		return ranks;
	}
}
