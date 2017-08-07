package DataMiningLab.bPlusTree;

public class Node<T extends Comparable <T>> {
	private int n;
	private int numberOfValue;
	private PointerKey<T> arr[];
	private Node parent;
	private boolean isLeafNode;
	public Node(int n){
		this.n = n;
		numberOfValue = 0;
		arr = new PointerKey[n];
		for(int i = 0 ; i < n ; i++){
			arr[i] = new PointerKey(null, null);
		}
		
	}
	
	public void insert(int pos,T V,Node N) {
		if(this.isLeafNode) {
			for(int i=numberOfValue-1;i>=pos;i--) {
				arr[i+1].setPointer(arr[i].getPointer());
				arr[i+1].setValue(arr[i].getValue());
			}
			arr[pos].setPointer(N);
			arr[pos].setValue(V);
			numberOfValue++;
		}
		else {
			for(int i=numberOfValue-1;i>=pos;i--) {
				arr[i+1].setPointer(arr[i+1].getPointer());
				arr[i+1].setValue(arr[i].getValue());
			}
			arr[pos+1].setPointer(N);
			arr[pos].setValue(V);
			numberOfValue++;
		}
	}
	
	public void delete(PointerKey P){
		boolean found = false;
		for(int i = 0 ; i < numberOfValue; i++){
			if(P.getValue().compareTo(arr[i].getValue()) == 0){
				found = true;
				for(int j = i ; j < numberOfValue ; j++){
					arr[j].setValue(arr[j+1].getValue());
				}
				break;
			}
		}
		for(int i = 0 ; i < numberOfValue; i++){
			if(P.getPointer() == arr[i].getPointer()){
				for(int j = i ; j < numberOfValue ; j++){
					arr[j].setPointer(arr[j+1].getPointer());
				}
			}
		}
		if(found)
			numberOfValue--;
	}
	
	public void setParent(Node parent){ this.parent = parent; }
	public Node getParent(){ return parent; }
	
	public void setIsLeafNode(boolean isLeafNode){ this.isLeafNode = isLeafNode;}
	public boolean getIsLeafNode() { return isLeafNode; }
	
	public void setArrPointerKey(int index, PointerKey pk){ arr[index] = pk; }
	public PointerKey getArrPointerKey(int index) { return arr[index]; }
	
	public void setArr(PointerKey arr[]) { this.arr = arr;}
	public PointerKey[] getArr(){ return arr; }
	
	public void setNumberOfValue(int numberOfValue){ this.numberOfValue = numberOfValue; }
	public int getNumberOfValue() {return numberOfValue; }
}
