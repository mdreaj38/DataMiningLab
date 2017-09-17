package DataMiningLab.frequentPattern.fpGrowth;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class FPGrowth {
	ArrayList < ArrayList <Integer> > transaction;
	ArrayList <FPNode> L;
	int MIN_SUP = 2;
	
	public FPGrowth() throws FileNotFoundException{
		transaction = new ArrayList<ArrayList<Integer>>();
		L = new ArrayList<FPNode>();
		init();
		preProcess();
		new FPTreeGen(L,transaction);
	}
	private void preProcess(){
		HashMap<Integer, Integer> hm = new HashMap<Integer,Integer>();
		for(ArrayList<Integer> x : transaction){
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
				L.add(new FPNode(entry.getKey()+"",entry.getValue(),null));
		}
		L.sort(new Comparator<FPNode>(){

			@Override
			public int compare(FPNode o1, FPNode o2) {
				return o2.getCount() - o1.getCount();
			}
			
		});
		//for(FPNode x : L) System.out.println(x);
	}
	
	private void init() throws FileNotFoundException{
		Scanner s = new Scanner(new File("transaction.txt"));
		while(s.hasNextLine()){
			Scanner s_ind = new Scanner(s.nextLine());
			ArrayList <Integer> i_temp = new ArrayList<Integer>();
			while(s_ind.hasNextInt()){
				i_temp.add(s_ind.nextInt());
			}
			transaction.add(i_temp);
			s_ind.close();
		}
		s.close();
	}
}
