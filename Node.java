import java.util.ArrayList;
import java.util.List;
import java.util.Set;


public class Node implements Comparable<Node> {
	private double distance;
	private double sigma;
	private double dependency;
	private double centrality;
	private PersonI p;
	private PersonI parent;
	private List<Node> pred;
	
	public Node(PersonI p) {
		this.p = p;
		this.distance = Double.POSITIVE_INFINITY;
		this.sigma = 0;
		this.dependency = 0;
		this.pred = new ArrayList<Node>();
	}
	
	public void setParent(Person parent) {
		this.parent = parent;
	}
	
	public PersonI getParent() {
		return this.parent;
	}
	
	public double getDistance() {
		return distance;
	}
	
	public void setDistance(double distance) {
		this.distance = distance;
	}
	
	public PersonI getP() {
		return p;
	}
	
	public Set<PersonI> getFriends() {
		return p.getFriends();
	}
	
	public List<Node> getPred() {
		return pred;
	}

	public void addPred(Node n) {
		pred.add(n);
	}
	
	public void setPredList(List<Node> p) {
		pred = p;
	}
	
	public double getSigma() {
		return sigma;
	}
	
	public void setSigma(double s) {
		sigma = s;
	}
	
	public void combineSigmas(Node n) {
		sigma += n.getSigma();
	}
	
	public void reset() {
		this.distance = Double.POSITIVE_INFINITY;
		this.sigma = 0;
		this.pred = new ArrayList<Node>();
	}
	
	public void resetDep() {
		dependency = 0.0;
	}
	
	public double getDep() {
		return dependency;
	}
	
	public void updateDep(Node n) {
		dependency += (sigma/n.getSigma()) * (1 + n.getDep());
	}
	
	public double getCentrality() {
		return centrality;
	}
	
	public void setCentrality(double c) {
		centrality = c;
	}
	
	@Override
	public int compareTo(Node other) {
		double dif = this.distance - other.distance;
		if (dif > 0) {
			return -1;
		} else if (dif < 0) {
			return 1;
		} else {
			return 0;
		}
	}
	
	@Override
	public String toString() {
		return new String("Person: " + p.toString() + "; Distance: " + 
												Double.toString(distance));
	}
}
