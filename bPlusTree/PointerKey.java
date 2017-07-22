package DataMiningLab.bPlusTree;

public class PointerKey {
	Node pointer;
	int value;
	public PointerKey(){
		this.pointer = null;
		this.value = Integer.MAX_VALUE;
	}
	
	public PointerKey(Node pointer, int value){
		this.pointer = pointer;
		this.value = value;
	}
	public void setPointerKey(Node pointer, int value){
		this.pointer = pointer;
		this.value = value;
	}
	public Node getPointer(){ return pointer; }
	public int getValue() { return value; }
}
