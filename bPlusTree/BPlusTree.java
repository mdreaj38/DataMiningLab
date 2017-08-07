package DataMiningLab.bPlusTree;

import java.util.LinkedList;

public class BPlusTree<T extends Comparable<T>> {
	
	private int n;
	private Node root;
	
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
			for(int i = 0 ; i < cur.getNumberOfValue()+1 ; i++){
				if(cur.getNumberOfValue() != i)
					System.out.print(cur.getArrPointerKey(i).getValue() + "|");
				if(!cur.getIsLeafNode()){
					depth.add(d+1);
					queue.add(cur.getArrPointerKey(i).getPointer());
				}
				
			}
			
		}
	}
	
	public Node find(T value){
		Node C = root;
		while(!C.getIsLeafNode()){
			
			for(int i = 0 ; i < n ; i++){
				if(C.getArrPointerKey(i).getValue() == null){
					//getArrPointerKey(i).pointer.parent = C;
					C.getArrPointerKey(i).getPointer().setParent(C);
					//C = getArrPointerKey(i).pointer;
					C = C.getArrPointerKey(i).getPointer();
					
					break;
				}
				//if(value < getArrPointerKey(i).value ){
				if(value.compareTo((T)C.getArrPointerKey(i).getValue()) < 0){
					//getArrPointerKey(i).pointer.parent = C;
					C.getArrPointerKey(i).getPointer().setParent(C);
					C = C.getArrPointerKey(i).getPointer();
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
			T.getArrPointerKey(i).setValue(L.getArrPointerKey(i).getValue());
			T.getArrPointerKey(i).setPointer(L.getArrPointerKey(i).getPointer());
		}
		insertInLeaf(T,K,P);
		L_.getArrPointerKey(n-1).setPointer(L.getArrPointerKey(n-1).getPointer()); 
		L.getArrPointerKey(n-1).setPointer(L_);
		for(int i = 0 ; i < n-1; i++){
			//L.getArrPointerKey(i) = new PointerKey();
			L.setArrPointerKey(i, new PointerKey(null,null));
		}
		for(int i = 0 ; i < Math.ceil(n/2.0) ; i++){
			L.getArrPointerKey(i).setPointer(T.getArrPointerKey(i).getPointer());
			L.getArrPointerKey(i).setValue(T.getArrPointerKey(i).getValue());
			L.setNumberOfValue(i + 1);
		}
		T K_ = null;
		for(int i = (int)Math.ceil(n/2.0), j = 0 ; i < n ; i++, j++){
			if(j == 0) K_ = (T)T.getArrPointerKey(i).getValue();
			L_.getArrPointerKey(j).setPointer(T.getArrPointerKey(i).getPointer());
			L_.getArrPointerKey(j).setValue(T.getArrPointerKey(i).getValue());
			L_.setNumberOfValue(j + 1);
		}
		insertInParent(L,K_,L_);
	}
	
	public void insertInLeaf(Node L, T K, Node P){
		for(int i = 0 ; i < n ; i++){
			if(L.getArrPointerKey(i).getValue() == null){
				L.getArrPointerKey(i).setValue(K);
				L.getArrPointerKey(i).setPointer(P);
				break;
			}
			if(K.compareTo((T)L.getArrPointerKey(i).getValue()) < 0){
				for(int j = n-1; j > i ; j--){
					L.getArrPointerKey(j).setValue(L.getArrPointerKey(j-1).getValue());
					L.getArrPointerKey(j).setPointer(L.getArrPointerKey(j-1).getPointer());
				}
				L.getArrPointerKey(i).setValue(K);
				L.getArrPointerKey(i).setPointer(P);
				break;
			}
		}
		//L.setArr(arr);
		L.setArr(L.getArr());
		
	}
	public void insertInParent(Node N, T K_, Node N_){
		if(N == root){	//root of the tree
			Node R = new Node(n);
			R.getArrPointerKey(0).setPointer(N);
			R.getArrPointerKey(0).setValue(K_);
			R.getArrPointerKey(1).setPointer(N_);
			
			R.setNumberOfValue(1);
			N.setParent(R);
			N_.setParent(R);
			root = R;
			return;
		}
		Node P = N.getParent();
		if(P.getNumberOfValue() < n - 1){
			for(int i = 0 ; i < n ; i++){
				if(P.getArrPointerKey(i).getPointer() == N){
					for(int j = n-1; j > i ; j--){
						P.getArrPointerKey(j).setValue(P.getArrPointerKey(j-1).getValue());
						P.getArrPointerKey(j).setPointer(P.getArrPointerKey(j-1).getPointer());
					}
					P.getArrPointerKey(i).setValue(K_);
					P.getArrPointerKey(i+1).setPointer(N_);
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
			T.getArrPointerKey(i).setPointer(P.getArrPointerKey(i).getPointer());
			T.getArrPointerKey(i).setValue(P.getArrPointerKey(i).getValue());
		}
		
		for(int i = 0 ; i < n + 1 ; i++){
			if(T.getArrPointerKey(i).getPointer() == N){
				for(int j = n; j > i ; j--){
					T.getArrPointerKey(j).setValue(T.getArrPointerKey(j-1).getValue());
					T.getArrPointerKey(j).setPointer(T.getArrPointerKey(j-1).getPointer());
				}
				T.getArrPointerKey(i).setValue( K_);
				T.getArrPointerKey(i+1).setPointer(N_);
				break;
			}
		}
		//P = new Node(n);
		for(int i = 0 ; i < n ; i++){
			P.getArrPointerKey(i).setValue(null);
			P.getArrPointerKey(i).setPointer(null);
		}
		Node P_ = new Node(n);
		for(int i = 0 ; i < Math.ceil((n+1.0)/2.0) ; i++){
			if(i == Math.ceil((n+1.0)/2) - 1){
				P.getArrPointerKey(i).setPointer(T.getArrPointerKey(i).getPointer());
				P.getArrPointerKey(i).getPointer().setParent(P);
				continue;
			}
			P.getArrPointerKey(i).setPointer(T.getArrPointerKey(i).getPointer());
			P.getArrPointerKey(i).getPointer().setParent(P);
			P.getArrPointerKey(i).setValue(T.getArrPointerKey(i).getValue());
			P.setNumberOfValue(i + 1);
		}
		T K__ = (T)T.getArrPointerKey((int)Math.ceil((n+1.0)/2.0)-1).getValue();
		int j = 0;
		for(int i = (int)Math.ceil((n+1.0)/2.0); i < n ; i++, j++){
			if(j == 0) K_ = (T)T.getArrPointerKey(i).getValue();
			P_.getArrPointerKey(j).setPointer(T.getArrPointerKey(i).getPointer());
			P_.getArrPointerKey(j).getPointer().setParent(P_);
			P_.getArrPointerKey(j).setValue(T.getArrPointerKey(i).getValue());
			P_.setNumberOfValue(j + 1);
		}
		P_.getArrPointerKey(j).setPointer(T.getArrPointerKey(n).getPointer());
		P_.getArrPointerKey(j).getPointer().setParent(P_);
		insertInParent(P,K__,P_);
	}
	
	public void delete(T K, Node P){
		Node X = find(K);
		if(X == null) return;
		delete_entry(X, K, P);
	}
	public void delete_entry(Node N, T K, Node P){
		N.delete(new PointerKey(P,K));
		if(N == root && N.getNumberOfValue() == 0 && N.getArrPointerKey(0).getPointer() != null){
			root = N.getArrPointerKey(0).getPointer();
			root.setParent(null);
		}
		
		else if(N != root && ((N.getIsLeafNode() && N.getNumberOfValue() < Math.ceil((n-1)/2.0))
				 || (!N.getIsLeafNode() && N.getNumberOfValue()+1 < Math.ceil((n)/2.0)))){
			int pos = 0;
			for(; N.getParent().getArrPointerKey(pos).getPointer() != N; pos++);
			//PointerKey left = new PointerKey(null,null);
			//PointerKey right = new PointerKey(null, null);
			Node LeftN = null, RightN = null;
			T LeftK = null, RightK = null;
			if(pos != 0){
				//left = new PointerKey(, );
				LeftN = N.getParent().getArrPointerKey(pos-1).getPointer();
				LeftK = (T) N.getParent().getArrPointerKey(pos-1).getValue();
				LeftN.setParent(N.getParent());
			}
			if(pos != N.getParent().getNumberOfValue()){
				RightN = N.getParent().getArrPointerKey(pos+1).getPointer();
				RightK = (T) N.getParent().getArrPointerKey(pos).getValue();
				RightN.setParent(N.getParent());
			}
			//coalesce
			if(LeftN!=null&&((N.getIsLeafNode()&&LeftN.getNumberOfValue()+N.getNumberOfValue()<n)||
					(!N.getIsLeafNode()&&LeftN.getNumberOfValue()+N.getNumberOfValue()+2<=n))) {
				coalesce(N,LeftN, (T) LeftK);
				return;
			}
			else if(RightN!=null&&((N.getIsLeafNode()&&RightN.getNumberOfValue()+N.getNumberOfValue()<n)||
					(!N.getIsLeafNode()&&RightN.getNumberOfValue()+N.getNumberOfValue()+2<=n))){
				coalesce(RightN, N, (T) RightK);
				return;
			}
			//redistribution
			else if(LeftN!=null&&((LeftN.getIsLeafNode() && LeftN.getNumberOfValue()>(int)Math.ceil((n-1)/2.0))
					|| (!LeftN.getIsLeafNode() && LeftN.getNumberOfValue()+1>(int)Math.ceil((n)/2.0)))) {
				redistFromLeft(N,LeftN,(T) LeftK,pos-1);
				return;
			}
			else if(RightN!=null&&((RightN.getIsLeafNode() && RightN.getNumberOfValue()>(int)Math.ceil((n-1)/2.0))
					|| (!RightN.getIsLeafNode() && RightN.getNumberOfValue()+1>(int)Math.ceil((n)/2.0)))) {
				redistFromRight(N,RightN,(T) RightK,pos);
				return;
			}
		}
	}
	public void coalesce(Node N, Node n_, T k_){
		if(!N.getIsLeafNode()) {
			n_.getArrPointerKey(n_.getNumberOfValue()).setValue(k_);
			n_.setNumberOfValue(n_.getNumberOfValue() + 1);
			for(int i=0;i<N.getNumberOfValue();i++) {
				n_.getArrPointerKey(n_.getNumberOfValue()).setPointer(N.getArrPointerKey(i).getPointer());
				n_.getArrPointerKey(n_.getNumberOfValue()).setValue(N.getArrPointerKey(i).getValue());
				n_.setNumberOfValue(n_.getNumberOfValue() + 1);
			}
			n_.getArrPointerKey(n_.getNumberOfValue()).setPointer(N.getArrPointerKey(N.getNumberOfValue()).getPointer());
		}
		else {
			for(int i=0;i<N.getNumberOfValue();i++) {
				n_.getArrPointerKey(n_.getNumberOfValue()).setPointer(N.getArrPointerKey(i).getPointer());
				n_.getArrPointerKey(n_.getNumberOfValue()).setValue(N.getArrPointerKey(i).getValue());
				n_.setNumberOfValue(n_.getNumberOfValue() + 1);
			}
			n_.getArrPointerKey(n-1).setPointer(N.getArrPointerKey(n-1).getPointer());
		}
		delete_entry(N.getParent(),k_,N);
		N.setParent(null);
		
		for(int i = 0 ; i < n; i++){
			N.setArrPointerKey(i,new PointerKey(null, null));
		}
	}
	
	public void redistFromLeft(Node N,Node n_,T k_,int pos) {
		if(!N.getIsLeafNode()) {
			int m = n_.getNumberOfValue();		
			T lastKey = (T) n_.getArrPointerKey(m-1).getValue();
			N.insert(0, k_ , n_.getArrPointerKey(m).getPointer());
			Node temp = N.getArrPointerKey(0).getPointer();
			N.getArrPointerKey(0).setPointer( N.getArrPointerKey(1).getPointer());
			N.getArrPointerKey(1).setPointer(temp);
			n_.delete(new PointerKey(n_.getArrPointerKey(m).getPointer(),lastKey) );
			N.getParent().getArrPointerKey(pos).setValue(lastKey);
			
		}
		else {
			int m = n_.getNumberOfValue()-1;
			T lastKey = (T) n_.getArrPointerKey(m).getValue();
			N.insert(0, lastKey, n_.getArrPointerKey(m).getPointer());
			n_.delete(new PointerKey(n_.getArrPointerKey(m).getPointer(), lastKey));
			N.getParent().getArrPointerKey(pos).setValue(lastKey);
		}
	}
	
	public void redistFromRight(Node N,Node n_,T k_,int pos) {
		if(!N.getIsLeafNode()) {
			int m = n_.getNumberOfValue();		
			T firstKey = (T) n_.getArrPointerKey(0).getValue();
			N.insert(m, k_, n_.getArrPointerKey(0).getPointer());
			n_.delete(new PointerKey(n_.getArrPointerKey(0).getPointer(),firstKey) );
			N.getParent().getArrPointerKey(pos).setValue(firstKey);
		}
		else {
			int m = N.getNumberOfValue();
			//T firstKey = (T) n_.arr[0].pointer;
			N.insert(m, (T) n_.getArrPointerKey(0).getValue(), n_.getArrPointerKey(0).getPointer());
			n_.delete(new PointerKey(n_.getArrPointerKey(0).getPointer(), (T) n_.getArrPointerKey(0).getValue()));
			N.getParent().getArrPointerKey(pos).setValue((T) n_.getArrPointerKey(0).getValue());
		}
	}
}
