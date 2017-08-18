package DataMiningLab.dynamicHashing;

import java.util.ArrayList;

public class Bucket {
	private String header;
	private ArrayList <Integer> list;
	
	public Bucket(String header){
		this.header = header;
		list = new ArrayList<Integer>();
	}
	public int getSize(){ return list.size(); }
	public int getI() { return header.length(); }
	public String getHeader() { return header; }
	public int getBucketContent(int index) { return list.get(index); }
	public void insertInBucket(int num){ list.add(num); }
	public void empty() { list.clear(); }
	public boolean isEmpty() { return list.isEmpty(); }
	public void setHeader(String suffix){ header += suffix; }
	public void remove(int value) {
		for(int i = 0; i < list.size(); i++){
			if(list.get(i) == value){
				list.remove(i);
			}
		}
	}
	
}
