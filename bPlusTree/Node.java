package DataMiningLab.bPlusTree;

public class Node {
	int n;
	int numberOfValue;
	PointerKey arr[];
	Node parent;
	boolean isLeafNode;
	public Node(int n){
		this.n = n;
		numberOfValue = 0;
		arr = new PointerKey[n];
		for(int i = 0 ; i < n ; i++){
			arr[i] = new PointerKey(null, Integer.MAX_VALUE);
		}
		
	}
	public void setParent(Node parent){ this.parent = parent; }
	public Node getParent(){ return parent; }
	
	public void setIsLeafNode(boolean isLeafNode){ this.isLeafNode = isLeafNode;}
	public boolean getIsLeafNode() { return isLeafNode; }
	
	public void setArr(PointerKey arr[]) { this.arr = arr;}
	public PointerKey[] getArr(){ return arr; }
	
	public void setNumberOfValue(int numberOfValue){ this.numberOfValue = numberOfValue; }
	public int getNumberOfValue() {return numberOfValue; }
}
