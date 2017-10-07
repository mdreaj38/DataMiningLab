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
	private double min_sup_perc = 0.6;
	private String filename;
	private int fp = 0;
	private int actual[] = new int[100];
	private int afterPruning[] = new int[100];
	private int apparent[] = new int[100];
	public TrieApriori(String filename){
		this.filename = filename;
		root = new TrieNode(-1,0,null);
		transactions = new ArrayList <ArrayList<Integer>> ();
		init();
		MIN_SUP = (int)Math.ceil((transactions.size() * min_sup_perc));
		preProcess();
		int depth = triepriori(1);
		for(int i = 1 ; i <= depth; i++){
			mine(i,"",0,root);
		}
		System.out.println("frequent pattern: " + fp);
		for(int i = 0 ; i < depth; i++){
			System.out.println(apparent[i] + " " + afterPruning[i] + " " + actual[i]);
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
		if(k != 1){
			traverse_database(k);
		}
		if(candidateGeneration(k) != 0){
			return triepriori(k+1);
		}
		return k;
	}
	private void mine(int k, String show, int depth, TrieNode node){
		if(depth == k){
			fp++;
			actual[k-1]++;
			//System.out.println(show + " " + node.getID() + " : " + node.getCount());
			return;
		}
		for(int i = 0 ; i < node.getChildCount(); i++){
			if(node.getID()==-1)
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
			int count = 0;
			TrieNode parent = node.getParent();
			ArrayList <TrieNode> toBeAdded = new ArrayList<TrieNode>();
			for(int i = index+1; i < parent.getChildCount(); i++){
				//node.addChild(new TrieNode(parent.getChild(i).getID(),0,node));
				if(addOrPrune(node, parent.getChild(i).getID())){
					count++;
					//node.addChild(new TrieNode(parent.getChild(i).getID(),0,node));
					toBeAdded.add(new TrieNode(parent.getChild(i).getID(),0,node));
				}
				apparent[k]++;
			}
			afterPruning[k] += toBeAdded.size();
			//System.out.println("to be added: " + toBeAdded);
			for(TrieNode x : toBeAdded) node.addChild(x);
			return count;
		}
		int count = 0;
		for(int i = 0 ; i < node.getChildCount(); i++){
			count += subsetGeneration(k, node.getChild(i), depth+1, i);
		}
		return count;
	}
	private boolean addOrPrune(TrieNode node, int ID){
		ArrayList<Integer> path = new ArrayList<Integer>();
		TrieNode temp = node;
		while(temp != root){
			path.add(temp.getID());
			temp = temp.getParent();
		}
		Collections.reverse(path);
		path.add(ID);
		for(int i = 0 ; i < path.size(); i++){
			//System.out.println(path);
			if(!checkTrie(path, i, root, 0))return false;
		}
		return true;
	}
	private boolean checkTrie(ArrayList<Integer> path, int skip, TrieNode node, int index){
		if(node.getChildCount() == 0 && index == path.size()){
			//System.out.println(path);
			return true;
		}
		if(index == skip) return checkTrie(path,skip,node,index+1);
		for(int i = 0 ; i < node.getChildCount(); i++){
			if(path.get(index) == node.getChild(i).getID()){
				//System.out.println(node.getChild(i).getID());
				return checkTrie(path,skip,node.getChild(i), index+1);
			}
		}
		return false;
	}
	
	private void traverse_database(int k){
		for(ArrayList<Integer> x : transactions){		//read the database
			addTransaction(x,k,root,0,0);
		}
		//run a dfs for level k, and remove any node that is less than min_sup
		removeUnworthyNode(k, root, 0);
	}
	
	private void addTransaction(ArrayList<Integer> transaction, int k, TrieNode node, int index, int depth){
		if(k == depth){
			node.setCount(node.getCount()+1);
			return;
		}
		int i = index, j = 0;
		while(i < transaction.size() - k + depth + 1 && j < node.getChildCount()){
			int comp = node.getChild(j).getID() - transaction.get(i);
			if(comp == 0){
				addTransaction(transaction,k,node.getChild(j),i+1, depth+1);
				i++;
			}
			else if(comp > 0) i++;
			else if(comp < 0) j++;
		}
		//addTransaction(transaction,k,node,index+1,depth);
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
				L.add(new TrieNode(entry.getKey(),entry.getValue(),root));
			apparent[0]++;
		}
		L.sort(new Comparator<TrieNode>(){

			@Override
			public int compare(TrieNode o1, TrieNode o2) {
				return o1.getID() - o2.getID();
			}
			
		});
		for(TrieNode x : L){
			root.addChild(x);
		}
		afterPruning[0] = apparent[0];
		//for(FPNode x : L) System.out.println(x);
	}
}