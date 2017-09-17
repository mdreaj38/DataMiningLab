package DataMiningLab.frequentPattern.fpGrowth;

import java.util.ArrayList;

public class FPNode {
	private String ID;
	private int count;
	private FPNode parent;
	private ArrayList<FPNode> children;
	
	public FPNode(String ID, int count, FPNode parent){
		this.ID = ID;
		this.count = count;
		this.parent = parent;
		children = new ArrayList<FPNode>();
	}
	
	public String getID(){ return ID; }
	public int getCount(){ return count; }
	public void incCountBy(int number) { count += number; }
	public FPNode getParent() { return parent; }
	public void incCountOfChildBy(int index, int number) { children.get(index).incCountBy(number); }
	public int getChildrenCount() { return children.size(); }
	public void setChild(FPNode node) { children.add(node);}
	public FPNode getChild(int index) { return children.get(index); }
	public void add (FPNode n) { 
		if(!children.contains(n))
			children.add(n); 
	}
	public int search(String ID) { 
		for(int i = 0 ; i < children.size(); i++){
			if(children.get(i).getID().equals(ID)){
				return i;
			}
		}
		return -1;
	}
	@Override
	public String toString(){
		if(parent == null)
			return "ID: " + ID + " support count: "+ count;
		return "ID: " + ID + " support count: "+ count + " Parent: " + parent.getID();
	}
}
