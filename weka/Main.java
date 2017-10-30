package DataMiningLab.weka;

import java.util.Scanner;

public class Main {
	public static void main(String args[]) throws Exception{
		new Shift("data/hour");
	}
	public static void menu() throws Exception{
		Scanner s = new Scanner(System.in);
		boolean running = true;
		while(running){
			ClassifierStrategy cs = new ClassifierStrategy();
			System.out.print("enter 1 for Naive Bayes\n"
					+ "enter 2 for Decision Tree\n"
					+ "enter 3 for Neural Network\n"
					+ "enter 4 for K Nearest Neighbor\n"
					+ "enter 0 to exit\n"
					+ "Input: ");
			int decision = s.nextInt();
			if(decision == 0) break;
			System.out.println("Enter filename: ");
			String filename = "data/" + s.next();
			switch(decision){
				case 1:
					cs.runClassifier("NB", filename);
					break;
				case 2:
					cs.runClassifier("DT", filename);
					break;
				case 3:
					cs.runClassifier("NN", filename);
					break;
				case 4:
					cs.runClassifier("KNN", filename);
					break;
				default:
					running = false;
			}
		}
		s.close();
	}
}
