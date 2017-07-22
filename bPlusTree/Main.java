package DataMiningLab.bPlusTree;

public class Main {
	public static void main(String args[]){
		//BPlusTree bpt = new BPlusTree(4);
		AlternateBPlusTree bpt = new AlternateBPlusTree(4);
		/*bpt.insert(4,null);
		bpt.insert(3,null);
		bpt.insert(1,null);
		bpt.insert(2,null);*/
		
		bpt.insert(11, null);
		bpt.insert(12, null);
		bpt.insert(9, null);
		bpt.insert(4, null);
		bpt.insert(5, null);
		bpt.insert(6, null);
		bpt.insert(7, null);
		bpt.insert(2, null);
		bpt.insert(10, null);
		bpt.insert(3, null);
		bpt.insert(1, null);
		bpt.insert(8, null);
		
		
		System.out.println("starting find()");
		bpt.find(12);
	}
}
