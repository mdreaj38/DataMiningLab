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
	
	public void insert(int pos,T V,Node N) {
		if(this.isLeafNode) {
			for(int i=numberOfValue-1;i>=pos;i--) {
				arr[i+1].pointer = arr[i].pointer;
				arr[i+1].value = arr[i].value;
			}
			arr[pos].pointer = N;
			arr[pos].value = V;
			numberOfValue++;
		}
		else {
			for(int i=numberOfValue-1;i>=pos;i--) {
				arr[i+2].pointer = arr[i+1].pointer;
				arr[i+1].value = arr[i].value;
			}
			arr[pos+1].pointer = N;
			arr[pos].value = V;
			numberOfValue++;
		}
	}
	
	public void delete(PointerKey P){
		boolean found = false;
		for(int i = 0 ; i < numberOfValue; i++){
			if(P.value.compareTo(arr[i].value) == 0){
				found = true;
				for(int j = i ; j < numberOfValue ; j++){
					arr[j].value = arr[j+1].value;
				}
				break;
			}
		}
		for(int i = 0 ; i < numberOfValue; i++){
			if(P.pointer == arr[i].pointer){
				for(int j = i ; j < numberOfValue ; j++){
					arr[j].pointer = arr[j+1].pointer;
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
	
	public void setArr(PointerKey arr[]) { this.arr = arr;}
	public PointerKey[] getArr(){ return arr; }
	
	public void setNumberOfValue(int numberOfValue){ this.numberOfValue = numberOfValue; }
	public int getNumberOfValue() {return numberOfValue; }
}
