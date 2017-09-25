package DataMiningLab.frequentPattern.apriori;

public class Main {
	public static void main(String args[]){
		long start_time = System.nanoTime();
		new TrieApriori("mushroom.txt");
		long end_time = System.nanoTime();
		System.out.println("time needed: " + (end_time - start_time) / 1e6 + " miliseconds");
	}
}
