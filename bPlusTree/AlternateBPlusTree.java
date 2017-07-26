package DataMiningLab.bPlusTree;

import java.util.LinkedList;

public class AlternateBPlusTree<T extends Comparable<T>> {
	
	int n;
	Node root;
	
	public AlternateBPlusTree(int n){
		this.n = n;
		root = new Node(n);
		root.setIsLeafNode(true);
	}
	
	public void print(){
		LinkedList<Node> queue = new LinkedList<Node>();
		queue.add(root);
		LinkedList<Integer> depth = new LinkedList<Integer>();
		
		depth.add(0);
		int currentDepth = -1;
		while(!queue.isEmpty()){
			Node cur = queue.pop();
			int d = depth.pop();
			if(currentDepth < d){
				System.out.println();
				currentDepth++;
			}
			for(int i = 0 ; i < d; i++){
				System.out.print(" ");
			}
			System.out.print("|");
			for(int i = 0 ; i < cur.numberOfValue+1 ; i++){
				if(cur.numberOfValue != i)
					System.out.print(cur.arr[i].value + "|");
				if(!cur.isLeafNode){
					depth.add(d+1);
					queue.add(cur.arr[i].pointer);
				}
				
			}
			
		}
	}
	
	public Node find(T value){
		Node C = root;
		while(!C.getIsLeafNode()){
			PointerKey arr[] = C.getArr();
			
			for(int i = 0 ; i < n ; i++){
				if(arr[i].value == null){
					C = arr[i].pointer;
					break;
				}
				//if(value < arr[i].value ){
				if(value.compareTo((T)arr[i].value) < 0){
					C = arr[i].pointer;
					break;
				}
			}
			
			
		}
		return C;
	}
	public void insert(T K, Node P){
		Node L = find(K);
		if(L.getNumberOfValue() < n - 1 ){ // if there's space in the node
			insertInLeaf(L,K,P);
			L.setNumberOfValue(L.getNumberOfValue() + 1);
			return;
		}
		//if there's not enough space, create T for intermediate node
		Node L_ = new Node(n);
		L.setIsLeafNode(true);
		L_.setIsLeafNode(true);
		Node T = new Node(n+1);
		for(int i = 0 ; i < n ; i++){
			T.arr[i].value = L.arr[i].value;
			T.arr[i].pointer = L.arr[i].pointer;
		}
		insertInLeaf(T,K,P);
		L_.arr[n-1].pointer = L.arr[n-1].pointer; L.arr[n-1].pointer = L_;
		for(int i = 0 ; i < n-1; i++){
			L.arr[i] = new PointerKey();
		}
		for(int i = 0 ; i < Math.ceil(n/2) ; i++){
			L.arr[i].pointer = T.arr[i].pointer;
			L.arr[i].value = T.arr[i].value;
			L.setNumberOfValue(i + 1);
		}
		T K_ = null;
		for(int i = (int)Math.ceil(n/2), j = 0 ; i < n ; i++, j++){
			if(j == 0) K_ = (T)T.arr[i].value;
			L_.arr[j].pointer = T.arr[i].pointer;
			L_.arr[j].value = T.arr[i].value;
			L_.setNumberOfValue(j + 1);
		}
		insertInParent(L,K_,L_);
	}
	
	public void insertInLeaf(Node L, T K, Node P){
		PointerKey arr[] = L.getArr();
		for(int i = 0 ; i < n ; i++){
			if(arr[i].value == null){
				arr[i].value = K;
				arr[i].pointer = P;
				break;
			}
			if(K.compareTo((T)arr[i].value) < 0){
				for(int j = n-1; j > i ; j--){
					arr[j].value = arr[j-1].value;
					arr[j].pointer = arr[j-1].pointer;
				}
				arr[i].value = K;
				arr[i].pointer = P;
				break;
			}
		}
		L.setArr(arr);
		
	}
	public void insertInParent(Node N, T K_, Node N_){
		if(N == root){	//root of the tree
			Node R = new Node(n);
			R.arr[0].pointer = N;
			R.arr[0].value = K_;
			R.arr[1].pointer = N_;
			
			R.setNumberOfValue(1);
			N.setParent(R);
			N_.setParent(R);
			root = R;
			return;
		}
		Node P = N.getParent();
		if(P.getNumberOfValue() < n - 1){
			for(int i = 0 ; i < n ; i++){
				if(P.arr[i].pointer == N){
					for(int j = n-1; j > i ; j--){
						P.arr[j].value = P.arr[j-1].value;
						P.arr[j].pointer = P.arr[j-1].pointer;
					}
					P.arr[i].value = K_;
					P.arr[i+1].pointer = N_;
					P.setNumberOfValue(P.getNumberOfValue()+1);
					N_.setParent(P);
					break;
				}
			}
			return ;
		}
		//if not, splitting starts 
		Node T = new Node(n+1);
		for(int i = 0 ; i < n ; i++){
			T.arr[i].pointer = P.arr[i].pointer;
			T.arr[i].value = P.arr[i].value;
		}
		
		for(int i = 0 ; i < n + 1 ; i++){
			if(T.arr[i].pointer == N){
				for(int j = n; j > i ; j--){
					T.arr[j].value = T.arr[j-1].value;
					T.arr[j].pointer = T.arr[j-1].pointer;
				}
				T.arr[i].value = K_;
				T.arr[i+1].pointer = N_;
				break;
			}
		}
		//P = new Node(n);
		for(int i = 0 ; i < n ; i++){
			P.arr[i].value = Integer.MAX_VALUE;
			P.arr[i].pointer = null;
		}
		Node P_ = new Node(n);
		for(int i = 0 ; i < Math.ceil((n+1.0)/2) ; i++){
			if(i == Math.ceil((n+1.0)/2) - 1){
				P.arr[i].pointer = T.arr[i].pointer;
				P.arr[i].pointer.setParent(P);
				continue;
			}
			P.arr[i].pointer = T.arr[i].pointer;
			P.arr[i].pointer.setParent(P);
			P.arr[i].value = T.arr[i].value;
			P.setNumberOfValue(i + 1);
		}
		T K__ = (T)T.arr[(int)Math.ceil((n+1.0)/2)-1].value;
		int j = 0;
		for(int i = (int)Math.ceil((n+1.0)/2); i < n ; i++, j++){
			if(j == 0) K_ = (T)T.arr[i].value;
			P_.arr[j].pointer = T.arr[i].pointer;
			P_.arr[j].pointer.setParent(P_);
			P_.arr[j].value = T.arr[i].value;
			P_.setNumberOfValue(j + 1);
		}
		P_.arr[j].pointer = T.arr[n].pointer;
		P_.arr[j].pointer.setParent(P_);
		insertInParent(P,K__,P_);
	}
}
