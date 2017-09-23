package DataMiningLab.frequentPattern.apriori;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class TrieApriori {
	private TrieNode root;
	private ArrayList <ArrayList<Integer>> transactions;
	private int MIN_SUP = 2;
	private double min_sup_perc = 0.001;
	private String filename;
	public TrieApriori(String filename){
		this.filename = filename;
		root = new TrieNode(null,0,null);
		transactions = new ArrayList <ArrayList<Integer>> ();
		init();
		MIN_SUP = (int)(transactions.size() * min_sup_perc);
		preProcess();
		int depth = triepriori(1);
		for(int i = 2 ; i < depth; i++){
			mine(i,"",0,root);
		}
	}
	private void init(){
		Scanner s = null;
		try {
			s = new Scanner(new File(filename));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		while(s.hasNextLine()){
			Scanner s_temp = new Scanner(s.nextLine());
			ArrayList<Integer> temp_trans = new ArrayList <Integer>();
			while(s_temp.hasNextInt()){
				int temp_int = s_temp.nextInt();
				temp_trans.add(temp_int);
			}
			s_temp.close();
			transactions.add(temp_trans);
		}
		//sort the transactions
		for(ArrayList <Integer> x : transactions){
			x.sort(new Comparator<Integer>(){
				@Override
				public int compare(Integer x, Integer y) {
					return x - y;
				}
				
			});
		}
	}
	private int triepriori(int k){
		System.out.println("running: "+k);
		if(k != 1)
			traverse_database(k);
		if(candidateGeneration(k) != 0)
			return triepriori(k+1);
		return k;
	}
	private void mine(int k, String show, int depth, TrieNode node){
		if(depth == k){
			System.out.println(show + " " + node.getID() + " : " + node.getCount());
			return;
		}
		for(int i = 0 ; i < node.getChildCount(); i++){
			if(node.getID()==null)
				mine(k,show,depth+1,node.getChild(i));
			else
				mine(k,show+" "+node.getID(),depth+1,node.getChild(i));
		}
	}
	private int candidateGeneration(int k){
		return subsetGeneration(k,root,0,0);
	}
	private int subsetGeneration(int k,TrieNode node, int depth, int index){
		if(k == depth){
			TrieNode parent = node.getParent();
			for(int i = index+1; i < parent.getChildCount(); i++){
				node.addChild(new TrieNode(parent.getChild(i).getID(),0,node));
			}
			return prune(node,k);
		}
		int count = 0;
		for(int i = 0 ; i < node.getChildCount(); i++){
			count += subsetGeneration(k, node.getChild(i), depth+1, i);
		}
		return count;
	}
	private int prune(TrieNode node,int k){
		ArrayList <Integer> removableIndex = new ArrayList <Integer> ();
		for(int i = 0 ; i < node.getChildCount(); i++){
			ArrayList<Integer> path = new ArrayList<Integer>();
			TrieNode tempNode = node;
			while(tempNode != root){
				path.add(Integer.parseInt(tempNode.getID()));
				tempNode = tempNode.getParent();
			}
			Collections.reverse(path);
			if(path.size() != 1)
				for(int index = 0 ; index < path.size(); index++){
					if(!traverseTrie(path,index)) 
						removableIndex.add(i);
				}
		}
		for(int i = removableIndex.size() -1 ; i>= 0 ; i--){
			node.removeChild(i);
		}
		return node.getChildCount();
	}
	private boolean traverseTrie(ArrayList <Integer> x,int skip){
		TrieNode tempRoot = root;
		int count = 0;
		for(int i = 0; i < x.size(); i++){
			if(i == skip) continue;
			for(int j = 0 ; j < tempRoot.getChildCount(); j++){
				if(tempRoot.getChild(j).getID().equals(x.get(i)+"")){
					tempRoot = tempRoot.getChild(j);
					count++;
				}
			}
		}
		if(count == x.size() - 1)
			return true;
		return false;
	}
	
	private void traverse_database(int k){
		//int row = 0;
		for(ArrayList<Integer> x : transactions){		//read the database
			addTransaction(x,k,root,0,0);
			//System.out.println(row++);
		}
		//run a dfs for level k, and remove any node that is less than min_sup
		removeUnworthyNode(k, root, 0);
	}
	private void addTransaction(ArrayList<Integer> x, int k, TrieNode node, int index, int depth){
		if(k == depth){
			node.setCount(node.getCount()+1);
			return;
		}
		if(index == x.size()) return;
		for(int i = 0; i < node.getChildCount(); i++){
			if(node.getChild(i).getID().equals(x.get(index)+"")){
				addTransaction(x,k,node.getChild(i),index+1, depth+1);
			}
		}
		addTransaction(x,k,node,index+1,depth);
	}
	private TrieNode removeUnworthyNode(int k, TrieNode node,int depth){
		if(depth == k){
			if(node.getCount() < MIN_SUP){
				return node;
			}
			return null;
		}
		if(node.getChildCount() == 0) return null;
		ArrayList<TrieNode> toBeRemoved = new ArrayList<TrieNode>();
		for(int i = 0 ; i < node.getChildCount(); i++){
			TrieNode temp_node = removeUnworthyNode(k,node.getChild(i),depth+1);
			if(temp_node != null) toBeRemoved.add(temp_node);
		}
		for(TrieNode x: toBeRemoved) node.removeChild(x);
		return null;
	}
	private void preProcess(){
		HashMap<Integer, Integer> hm = new HashMap<Integer,Integer>();
		ArrayList <TrieNode> L = new ArrayList<TrieNode>();
		for(ArrayList<Integer> x : transactions){
			ArrayList <Integer> temp = new ArrayList<Integer>();
			for(int y : x){
				if(temp.contains(y)) continue;
				if(hm.containsKey(y)){
					hm.put(y, hm.get(y)+1);
				}
				else{
					hm.put(y, 1);
				}
				temp.add(y);
			}
		}
		for (Map.Entry<Integer, Integer> entry : hm.entrySet()) {
			if(!(entry.getValue() < MIN_SUP))
				L.add(new TrieNode(entry.getKey()+"",entry.getValue(),root));
		}
		L.sort(new Comparator<TrieNode>(){

			@Override
			public int compare(TrieNode o1, TrieNode o2) {
				return Integer.parseInt(o1.getID()) - Integer.parseInt(o2.getID());
			}
			
		});
		for(TrieNode x : L){
			root.addChild(x);
		}
		//for(FPNode x : L) System.out.println(x);
	}
}