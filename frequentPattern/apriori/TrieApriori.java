package DataMiningLab.frequentPattern.apriori;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Scanner;

public class TrieApriori {
	private TrieNode root;
	private ArrayList <ArrayList<Integer>> transactions;
	private int MIN_SUP = 2;
	private String filename;
	public TrieApriori(String filename){
		this.filename = filename;
		root = new TrieNode(null,0,null);
		transactions = new ArrayList <ArrayList<Integer>> ();
		init();
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
		ArrayList <Integer> temp_nodes = new ArrayList<Integer>();
		while(s.hasNextLine()){
			Scanner s_temp = new Scanner(s.nextLine());
			ArrayList<Integer> temp_trans = new ArrayList <Integer>();
			while(s_temp.hasNextInt()){
				int temp_int = s_temp.nextInt();
				temp_trans.add(temp_int);
				if(!temp_nodes.contains(temp_int)){
					temp_nodes.add(temp_int);
				}
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
		temp_nodes.sort(new Comparator<Integer>(){
			@Override
			public int compare(Integer o1, Integer o2) {
				return o1-o2;
			}
			
		});
		for(int x : temp_nodes){
			root.addChild(new TrieNode(x+"",0,root));
		}
		
	}
	private int triepriori(int k){
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
			ArrayList<ArrayList<Integer>> combination = createCombination(k+1, path);
			for(ArrayList <Integer> x : combination){
				if(!traverseTrie(x)){
					removableIndex.add(i);
				}
			}
		}
		for(int i = removableIndex.size() -1 ; i>= 0 ; i--){
			node.removeChild(i);
		}
		return node.getChildCount();
	}
	private boolean traverseTrie(ArrayList <Integer> x){
		TrieNode tempRoot = root;
		int index = 0;
		while(tempRoot.getChildCount() != 0){
			for(int i = 0 ; i < tempRoot.getChildCount(); i++){
				if(index == x.size()) return true;
				if(tempRoot.getChild(i).getID().equals(x.get(index)+"")){
					index++;
					tempRoot = tempRoot.getChild(i);
				}
			}
		}
		return false;
	}
	
	private void traverse_database(int k){
		for(ArrayList<Integer> x : transactions){		//read the database
			ArrayList <ArrayList <Integer>> combination = createCombination(k,x); //create subsets of length k
			for(ArrayList <Integer> y : combination){		//iterate the TRIE for all the combinations to increase count
				TrieNode temp_root = root;
				int depth = 0;
				for(int z : y){
					for(int i = 0 ; i < temp_root.getChildCount(); i++){
						if(temp_root.getChild(i).getID().equals(z+"")){
							temp_root = temp_root.getChild(i);
							depth++;
						}
					}
				}
				if(depth == k)
					temp_root.setCount(temp_root.getCount() + 1);
			}
		}
		//run a dfs for level k, and remove any node that is less than min_sup
		removeUnworthyNode(k, root, 0);
	}
	private TrieNode removeUnworthyNode(int k, TrieNode node,int depth){
		if(depth == k){
			if(node.getCount() < MIN_SUP){
				//node.getParent().removeChild(node);
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
	private ArrayList<ArrayList <Integer>> createCombination(int k, ArrayList <Integer> trans_slice){
		ArrayList<ArrayList <Integer>> combi = new ArrayList<ArrayList <Integer>>();
		int max = (1 << trans_slice.size());
		for(int i = 1 ; i < max ; i++){
			int temp = i;
			int index  = 0;
			ArrayList<Integer> c = new ArrayList<Integer>();
			while(temp != 0){
				if((temp&1)!=0){
					c.add(trans_slice.get(index));
				}
				index++;
				temp = temp >> 1;
			}
			
			//combi.add(new GeneralPair<String,Integer>(c, min));
			if(c.size()==k)
				combi.add(c);
		}
		Collections.reverse(combi);
		return combi;
	}
}
