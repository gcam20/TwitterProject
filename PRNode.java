import java.util.Set;


public class PRNode {
	private PersonI p;
	private double rank;
	
	public PRNode(PersonI p) {
		this.p = p;
		this.rank = 1;
	}
	
	public PersonI getP() {
		return p;
	}
	
	public Set<PersonI> getFriends() {
		return p.getFriends();
	}
	
	public Set<PersonI> getFollowers() {
		return p.getFollowers();
	}
	
	public void setRank(double r) {
		rank = r;
	}
	
	public double getRank() {
		return rank;
	}
	
	@Override
	public String toString() {
		return new String("Person: " + p.toString() + "; Rank: " + Double.toString(rank));
	}
}
