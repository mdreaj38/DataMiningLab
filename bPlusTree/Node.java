package DataMiningLab.bPlusTree;

public class Node<T extends Comparable <T>> {
	int n;
	int numberOfValue;
	PointerKey<T> arr[];
	Node parent;
	boolean isLeafNode;
	public Node(int n){
		this.n = n;
		numberOfValue = 0;
		arr = new PointerKey[n];
		for(int i = 0 ; i < n ; i++){
			arr[i] = new PointerKey(null, null);
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
