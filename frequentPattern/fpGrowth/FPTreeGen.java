package DataMiningLab.frequentPattern.fpGrowth;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

public class FPTreeGen {
	
	private FPNode root;
	private ArrayList<FPNode> L;
	private ArrayList <ArrayList<Integer>> Transaction;
	private HashMap<String , ArrayList<generatedFP>> genFP;
	int MIN_SUP = 2;
	
	
	public FPTreeGen(ArrayList<FPNode> L, ArrayList <ArrayList<Integer>> Transaction){
		this.L = L;
		this.Transaction = Transaction;
		root = new FPNode(null,0,null);
		genFP = new HashMap <String, ArrayList<generatedFP>>();
		for(FPNode x : L){
			genFP.put(x.getID(), new ArrayList <generatedFP>());
		}
		fpGen();
		FP_Growth(root,new ArrayList <FPNode> () , L);
	}
	
	public void FP_Growth(FPNode root, ArrayList<FPNode> alpha, ArrayList <FPNode> L){
		
		if(!hasMultiPath(root)){ //Tree contains a single Path.
			ArrayList <String> nodes = new ArrayList<String>();
			int sup = Integer.MAX_VALUE;
			do{
				root = root.getChild(0);
				nodes.add(root.getID());
				if(sup > root.getCount()) sup = root.getCount();
			}while(root.getChildrenCount() != 0);
			ArrayList <String> combi = createCombination(nodes);
			for(String y : combi){
				String temp = y;
				for(FPNode x : alpha){
					temp += ", " + x.getID();
				}
				System.out.println("{"+temp + ":" + sup +"}");
			}
		}
		else{
			for(int i = L.size()-1; i >= 0 ; i--){
				ArrayList<FPNode> beta = new ArrayList<FPNode>();
				beta.add(L.get(i));
				beta.addAll(alpha);
				int supCount = L.get(i).getCount();
				
				if(beta.size() > 1){
					String temp = "";
					boolean first = false;
					for(FPNode x : beta){
						if(!first){
							temp += x.getID();
							first = true;
						}
						else
							temp += ", " + x.getID();
					}
					System.out.println("{"+temp + ":" + supCount +"}");
				}
				
				ArrayList <FPNode> tempL = new ArrayList <FPNode>();
				ArrayList <Pair> tempTrans = createPatternBase(i,L, tempL);
				//ajaira code starts
				/*System.out.println("header table of " + beta.get(0));
				for(FPNode x : tempL) System.out.println(x);
				System.out.println("\ndone header table");
				System.out.println("conditional pattern base of " + beta.get(0));
				for(Pair x : tempTrans) System.out.print(x + " ");
				System.out.println("\ndone conditional pattern base");*/
				//ajaira code ends
				FPNode TreeBeta = new FPNode(null,0,null);
				fpGen(TreeBeta, tempTrans, tempL);
				if(TreeBeta.getChildrenCount() != 0){
					FP_Growth(TreeBeta, beta,tempL);
				}
			}
		}
	}
	private ArrayList<String> createCombination(ArrayList<String> nodes){
		ArrayList <String> combi = new ArrayList<String>();
		int max = (1 << nodes.size());
		for(int i = 1 ; i < max ; i++){
			int temp = i;
			int index  = 0;
			boolean first = false;
			String c = "";
			while(temp != 0){
				if((temp&1)!=0){
					if(!first){
						c += nodes.get(index);
						first = true;
					}
					else 
						c += ", " + nodes.get(index);
				}
				index++;
				temp = temp >> 1;
			}
			
			combi.add(c);
		}
		return combi;
	}
	private boolean hasMultiPath(FPNode root){
		while(root.getChildrenCount() != 0) {
			if(root.getChildrenCount() > 1) return true;
			root = root.getChild(0);
		}
		return false;
	}
	private ArrayList <Pair> createPatternBase(int index,ArrayList <FPNode> L,
			ArrayList <FPNode> tempL){
		ArrayList <Pair> patternTransaction = new ArrayList<Pair>();
		HashMap<String, Integer> hm = new HashMap<String,Integer>();
		for(int i = 0 ; i < L.get(index).getChildrenCount(); i++){ //get all cases of index
			FPNode temp = L.get(index).getChild(i);		//Node that needs to be traversed
			int supCount = temp.getCount();				//Support count of the current node
			ArrayList<String> forPair = new ArrayList<String>();
			while(temp != root){		//get to the root and get the path along the way
				temp = temp.getParent();
				if(temp == null || temp == root || temp.getParent() == null) break;
				forPair.add(temp.getID());
				//L.add(new FPNode(temp.getID(), supCount, null));
				if(hm.containsKey(temp.getID())){
					hm.put(temp.getID(), hm.get(temp.getID())+supCount);
				}
				else{
					hm.put(temp.getID(), supCount);
				}
			}
			if(forPair.size() != 0)
				patternTransaction.add(new Pair(forPair, supCount));
		}
		for (Map.Entry<String, Integer> entry : hm.entrySet()) {
			if(!(entry.getValue() < MIN_SUP))
				tempL.add(new FPNode(entry.getKey()+"",entry.getValue(),null));
		}
		tempL.sort(new Comparator<FPNode>(){

			@Override
			public int compare(FPNode o1, FPNode o2) {
				return o2.getCount() - o1.getCount();
			}
			
		});
		return patternTransaction;
	}
	
	public void fpGen(){
		for(int i = 0 ; i < Transaction.size() ; i++){
			FPNode currentNode = root;
			for(FPNode x : L){
				if(Transaction.get(i).contains(Integer.parseInt(x.getID()))){
					int index = insert(currentNode, x.getID());
					currentNode = currentNode.getChild(index);
					
					x.add(currentNode);
				}
			}
		}
		//show(root);
	}
	public void fpGen(FPNode root, ArrayList <Pair> transPair, ArrayList<FPNode> L){
		
		for(int i = 0 ; i < transPair.size() ; i++){
			FPNode currentNode = root;
			for(FPNode x : L){
				//if(Transaction.get(i).contains(Integer.parseInt(x.getID()))){
				if(transPair.get(i).getItems().contains(x.getID())){
					int index = insert(currentNode, x.getID(), transPair.get(i).getCount());
					currentNode = currentNode.getChild(index);
					x.add(currentNode);
				}
			}
		}
		//System.out.println("sub-tree");
		//show(root);
	}
	public void show(FPNode root){
		
		System.out.println(root);
		for(int i = 0 ; i < root.getChildrenCount(); i++){
			show(root.getChild(i));
		}
	}
	public int insert(FPNode cuRoot, String ID){
		int index = cuRoot.search(ID);
		if(index == -1){
			cuRoot.add(new FPNode(ID,1,cuRoot));
			return cuRoot.search(ID);
		}
		cuRoot.incCountOfChildBy(index, 1);
		return index;
	}
	public int insert(FPNode cuRoot, String ID, int supCount){
		int index = cuRoot.search(ID);
		if(index == -1){
			cuRoot.add(new FPNode(ID,supCount,cuRoot));
			return cuRoot.search(ID);
		}
		cuRoot.incCountOfChildBy(index, supCount);
		return index;
	}
	private class generatedFP{
		private ArrayList <Integer> nodes;
		private int supportCount;
		public generatedFP(ArrayList <Integer> nodes, int supportCount){
			this.nodes = nodes;
			this.supportCount = supportCount;
		}
		public int getSize(){ return nodes.size();}
		public int getSupportCount() { return supportCount; }
		public int getNode(int index) { return nodes.get(index); }
		
	}
	private class Pair{
		private ArrayList<String> items;
		private int count;
		public Pair(ArrayList<String> items, int count){
			this.items = items;
			this.count = count;
		}
		public ArrayList<String> getItems() { return items; }
		public int getCount() { return count; }
		public String toString(){
			String returnString = "{";
			for(String x : items) returnString += " " + x + " ";
			returnString += ":"+count+"} ";
			return returnString;
		}
	}
	
	
}
