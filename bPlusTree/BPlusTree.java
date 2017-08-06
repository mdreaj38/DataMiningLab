package DataMiningLab.bPlusTree;

import java.util.LinkedList;

public class BPlusTree<T extends Comparable<T>> {
	
	int n;
	Node root;
	
	public BPlusTree(int n){
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
					arr[i].pointer.parent = C;
					C = arr[i].pointer;
					
					break;
				}
				//if(value < arr[i].value ){
				if(value.compareTo((T)arr[i].value) < 0){
					arr[i].pointer.parent = C;
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
		for(int i = 0 ; i < Math.ceil(n/2.0) ; i++){
			L.arr[i].pointer = T.arr[i].pointer;
			L.arr[i].value = T.arr[i].value;
			L.setNumberOfValue(i + 1);
		}
		T K_ = null;
		for(int i = (int)Math.ceil(n/2.0), j = 0 ; i < n ; i++, j++){
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
			P.arr[i].value = null;
			P.arr[i].pointer = null;
		}
		Node P_ = new Node(n);
		for(int i = 0 ; i < Math.ceil((n+1.0)/2.0) ; i++){
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
		T K__ = (T)T.arr[(int)Math.ceil((n+1.0)/2.0)-1].value;
		int j = 0;
		for(int i = (int)Math.ceil((n+1.0)/2.0); i < n ; i++, j++){
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
	
	public void delete(T K, Node P){
		Node X = find(K);
		if(X == null) return;
		delete_entry(X, K, P);
	}
	public void delete_entry(Node N, T K, Node P){
		N.delete(new PointerKey(P,K));
		if(N == root && N.numberOfValue == 0 && N.arr[0].pointer != null){
			root = N.arr[0].pointer;
			root.setParent(null);
		}
		
		else if(N.numberOfValue < Math.ceil((n-1)/2.0)){
			int pos = 0;
			for(; N.parent.arr[pos].pointer != N; pos++);
			//PointerKey left = new PointerKey(null,null);
			//PointerKey right = new PointerKey(null, null);
			Node LeftN = null, RightN = null;
			T LeftK = null, RightK = null;
			if(pos != 0){
				//left = new PointerKey(, );
				LeftN = N.parent.arr[pos-1].pointer;
				LeftK = (T) N.parent.arr[pos-1].value;
				LeftN.setParent(N.parent);
			}
			if(pos != N.parent.numberOfValue){
				RightN = N.parent.arr[pos+1].pointer;
				RightK = (T) N.parent.arr[pos].value;
				RightN.setParent(N.parent);
			}
			//coalesce
			if(LeftN!=null&&((N.isLeafNode&&LeftN.numberOfValue+N.numberOfValue<n)||
					(!N.isLeafNode&&LeftN.numberOfValue+N.numberOfValue+2<=n))) {
				coalesce(N,LeftN, (T) LeftK);
				return;
			}
			else if(RightN!=null&&((N.isLeafNode&&RightN.numberOfValue+N.numberOfValue<n)||
					(!N.isLeafNode&&RightN.numberOfValue+N.numberOfValue+2<=n))){
				coalesce(RightN, N, (T) RightK);
				return;
			}
			//redistribution
			else if(LeftN!=null&&LeftN.numberOfValue>(int)Math.ceil((n-1)/2.0)) {
				redistFromLeft(N,LeftN,(T) LeftK,pos-1);
				return;
			}
			else if(RightN!=null&&RightN.numberOfValue>(int)Math.ceil((n-1)/2.0)) {
				redistFromRight(N,RightN,(T) RightK,pos);
				return;
			}
		}
	}
	public void coalesce(Node N, Node NN, T KK){
		if(!N.isLeafNode) {
			NN.arr[NN.numberOfValue].value = KK;
			NN.numberOfValue++;
			for(int i=0;i<N.numberOfValue;i++) {
				NN.arr[NN.numberOfValue].pointer = N.arr[i].pointer;
				NN.arr[NN.numberOfValue].value = N.arr[i].value;
				NN.numberOfValue++;
			}
			NN.arr[NN.numberOfValue].pointer = N.arr[N.numberOfValue].pointer;
		}
		else {
			for(int i=0;i<N.numberOfValue;i++) {
				NN.arr[NN.numberOfValue].pointer = N.arr[i].pointer;
				NN.arr[NN.numberOfValue].value = N.arr[i].value;
				NN.numberOfValue++;
			}
			NN.arr[n-1].pointer = N.arr[n-1].pointer;
		}
		delete_entry(N.parent,KK,N);
		N.parent = null;
		
		for(int i = 0 ; i < n; i++){
			N.arr[i] = new PointerKey(null, null);
		}
	}
	
	public void redistFromLeft(Node N,Node NN,T KK,int pos) {
		if(!N.isLeafNode) {
			int m = NN.numberOfValue;		//Final pointer of NN
			T lastKey = (T) NN.arr[m-1].value;
			N.insert(0, KK , NN.arr[m].pointer);
			Node temp = N.arr[0].pointer;
			N.arr[0].pointer = N.arr[1].pointer;
			N.arr[1].pointer = temp;
			NN.delete(new PointerKey(NN.arr[m].pointer,lastKey) );
			N.parent.arr[pos].value = lastKey;
			
		}
		else {
			int m = NN.numberOfValue-1;
			T lastKey = (T) NN.arr[m].value;
			N.insert(0, lastKey, NN.arr[m].pointer);
			NN.delete(new PointerKey(NN.arr[m].pointer, lastKey));
			N.parent.arr[pos].value = lastKey;
		}
	}
	
	public void redistFromRight(Node N,Node NN,T KK,int pos) {
		if(!N.isLeafNode) {
			int m = NN.numberOfValue;		//Final pointer of NN
			T firstKey = (T) NN.arr[0].value;
			N.insert(m, KK, NN.arr[0].pointer);
			NN.delete(new PointerKey(NN.arr[0].pointer,firstKey) );
			N.parent.arr[pos].value = firstKey;
		}
		else {
			int m = N.numberOfValue;
			T firstKey = (T) NN.arr[0].pointer;
			N.insert(m, firstKey, NN.arr[0].pointer);
			NN.delete(new PointerKey(NN.arr[0].pointer, firstKey));
			N.parent.arr[pos].value = firstKey;
		}
	}
}
