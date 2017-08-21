package DataMiningLab.dynamicHashing;

import java.util.Scanner;

public class Main {
	public static void main(String args[]){
		DynamicHashing dh = new DynamicHashing(4,3);
		Scanner s=  new Scanner(System.in);
		System.out.println("insert : 1 value\nshow: 2");
		while(s.hasNextInt()){
			int command = s.nextInt();
			if(command == 1){
				int value = s.nextInt();
				dh.insert(value);
			}
			else if(command == 2){
				//System.out.println("Bucket so far");
				dh.show();
			}
		}
		s.close();
	}
}
