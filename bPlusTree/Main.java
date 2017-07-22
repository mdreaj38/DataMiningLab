package DataMiningLab.bPlusTree;

public class Main {
	public static void main(String args[]){
		BPlusTree bpt = new BPlusTree(4);
		bpt.insert(4);
		bpt.insert(3);
		bpt.insert(1);
		bpt.insert(2);
	}
}
