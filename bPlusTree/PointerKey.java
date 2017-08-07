package DataMiningLab.bPlusTree;

public class PointerKey<T extends Comparable<T>> {
	private Node pointer;
	private T value;
	private Node parent;
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
	public void setPointer(Node pointer){this.pointer = pointer; }
	public T getValue() { return value; }
	public void setValue(T value) { this.value = value ; }
	public void setParent(Node parent){ this.parent = parent;}
}
