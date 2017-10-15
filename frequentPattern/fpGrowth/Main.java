package DataMiningLab.frequentPattern.fpGrowth;

import java.io.FileNotFoundException;

public class Main {
	public static void main(String args[]) throws FileNotFoundException{
		long start_time = System.nanoTime();
		new FPGrowth("c20d10k.txt");
		long end_time = System.nanoTime();
		System.out.println("time needed: " + (end_time - start_time) / 1e6 + " miliseconds");
	}
}
