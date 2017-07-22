package DataMiningLab.bPlusTree;

public class BPlusTree {
	int n;
	Node root;
	public BPlusTree(int n){
		this.n = n;
		root = new Node(n);
		root.isLeaf = true;
	}
	public Node search(Node node, int value){
		if(!node.getLeaf()){
			for(int i = 0 ; i < n - 1 ; i++){
				if(node.getKey(i) > value){
					return search(node.pk[i].pointer, value);
				}
				if(node.getKey(i) < value && node.getKey(i+1) >= value){
					return search(node.pk[i+1].pointer, value);
				}
			}
		}
		return node;
		
	}
	public extraNode insert(int value){
		return insert(root,value);
	}
	public extraNode insert(Node node, int value){
		extraNode en = null;
		if(!node.getLeaf()){
			for(int i = 0 ; i < n - 1 ; i++){
				if(node.getKey(i) == 0) break;
				if(node.getKey(i) > value){
					//return search(node.pk[i].pointer, value);
					en = insert(node.pk[i].pointer, value);
					node = en.node1;
					
				}
				if(node.getKey(i) < value && node.getKey(i+1) >= value){
					//return search(node.pk[i+1].pointer, value);
					en = insert(node.pk[i+1].pointer, value);
					node = en.node1;
				}
			}
			if(en.temp){
				
			}
		}
		//return node;
		en = insertInLeaf(node, value);
		if(en.temp){
			Node tempnode = new Node(n);
			tempnode.pk[0].key = en.node2.pk[0].key;
			tempnode.pk[0].pointer = en.node1;
			tempnode.pk[1].pointer = en.node2;
		}
		node.pk = en.node1.pk;
		return en;
	}
	//public extraNode insertInParent(Node node,extraNode en){
		
	//}
	public extraNode insertInLeaf(Node node, int value){
		Node temp = new Node(n+1);
		int i = 0 , j = 0 ;
		boolean found = false;
		int count = 0;
		for(; i < n; ){
			if(node.getKey(i) < value && node.getKey(i) != 0){
				temp.pk[j++].key = node.getKey(i);
				i++ ;
				count++;
			}
			else if(!found){
				temp.pk[j++].key = value;
				found = true;
				count++;
			}
			else if(found && node.getKey(i) != 0){
				temp.pk[j++].key = node.getKey(i);
				i++ ;
				count++;
			}
			else if(found ){
				temp.pk[j++].key = node.getKey(i);
				i++ ;
			}
		}
		if(count < n){
			Node node1 = new Node(n);
			for(int x = 0 ; x < n; x++){
				node1.pk[x] = temp.pk[x];
			}
			extraNode en = new extraNode(false, node1, null);
			return en;
		}
		int range = (int) Math.ceil((n-1.0)/2.0);
		int k = 0;
		Node node1 = new Node(n);
		Node node2 = new Node(n);
		node1.isLeaf = true;
		node2.isLeaf = true;
		for(int x = 0; k < range; k++){
			node1.pk[x++].key = temp.pk[k].key;
		}
		for(int y = 0 ; k < j ; k++){
			node2.pk[y++].key = temp.pk[k].key;
		}
		extraNode en = new extraNode(true, node1, node2);
		return en;
	}
	class extraNode{
		boolean temp;
		Node node1;
		Node node2;
		public extraNode(boolean temp, Node node1, Node node2){
			this.temp = temp;
			this.node1 = node1;
			this.node2 = node2;
		}
	}
	class pointerKey{
		Node pointer;
		int key;
		public pointerKey(Node pointer, int key){
			this.pointer = pointer;
			this.key = key;
		}
	}
	class Node{
		boolean isLeaf;
		int range;
		pointerKey[] pk;
		public Node(int n){
			range = n-1;
			pk = new pointerKey[n];
			for(int i = 0 ; i < n ; i++){
				pk[i] = new pointerKey(null, 0);
			}
										
		}
		public int getKey(int i) { return pk[i].key; }
		public void setLeaf(boolean leaf) { this.isLeaf = leaf; }
		public boolean getLeaf() { return isLeaf; }
	}
	
}
