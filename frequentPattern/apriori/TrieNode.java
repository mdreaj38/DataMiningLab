package DataMiningLab.frequentPattern.apriori;

import java.util.ArrayList;

public class TrieNode {
	private String ID;
	private int count;
	private ArrayList <TrieNode> child;
	private TrieNode parent;
	
	public TrieNode(String ID, int count, TrieNode parent){
		this.ID = ID;
		this.count = count;
		this.parent = parent;
		child = new ArrayList <TrieNode>();
	}
	public String getID(){return ID;}
	public int getCount(){return count;}
	public void setCount(int count) { this.count = count; }
	public int getChildCount(){return child.size();}
	public void addChild(TrieNode c){ child.add(c);}
	public TrieNode getChild(int index){return child.get(index);}
	public void removeChild(TrieNode c) { child.remove(c); }
	public void removeChild(int index) { child.remove(index); }
	public TrieNode getParent() { return parent; }
}
