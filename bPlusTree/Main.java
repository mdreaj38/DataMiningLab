package DataMiningLab.bPlusTree;

import java.util.Scanner;

public class Main {
	public static void main(String args[]){
		BPlusTree bpt = new BPlusTree(4);
		/*bpt.insert(11, null);
		bpt.insert(12, null);
		bpt.insert(9, null);
		bpt.insert(4, null);
		bpt.insert(5, null);
		bpt.insert(6, null);
		bpt.insert(7, null);
		bpt.insert(2, null);
		bpt.insert(10, null);
		bpt.insert(3, null);
		bpt.insert(1, null);
		bpt.insert(8, null);*/
		
		Scanner s = new Scanner(System.in);
		
		System.out.println("1 for int , 2 for string");
		
		int option = s.nextInt();
		if(option == 2){
		
		System.out.println("commands");
		System.out.println("insert: 1 value");
		System.out.println("delete: 2 value");
		System.out.println("show tree : 3");
		
		while(s.hasNextInt()){
			int command = s.nextInt();
			if(command == 3) bpt.print();
			else if(command == 1){
				String value = s.next();
				bpt.insert(value, null);
			}
			else if(command == 2){
				String value = s.next();
				bpt.delete(value, null);
			}
			System.out.println();
		}
		}
		else{
			System.out.println("commands");
			System.out.println("insert: 1 value");
			System.out.println("delete: 2 value");
			System.out.println("show tree : 3");
			
			while(s.hasNextInt()){
				int command = s.nextInt();
				if(command == 3) bpt.print();
				else if(command == 1){
					int value = s.nextInt();
					bpt.insert(value, null);
				}
				else if(command == 2){
					int value = s.nextInt();
					bpt.delete(value, null);
				}
				System.out.println();
			}
		}
		System.out.println("starting find()");
		//bpt.find(12);
		bpt.print();
		s.close();
	}
}
