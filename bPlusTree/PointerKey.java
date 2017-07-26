package DataMiningLab.bPlusTree;

public class PointerKey<T extends Comparable<T>> {
	Node pointer;
	T value;
	public PointerKey(){
		this.pointer = null;
		this.value = null;
	}
	
	public PointerKey(Node pointer, T value){
		this.pointer = pointer;
		this.value = value;
	}
	public void setPointerKey(Node pointer, T value){
		this.pointer = pointer;
		this.value = value;
	}
	public Node getPointer(){ return pointer; }
	public T getValue() { return value; }
}
