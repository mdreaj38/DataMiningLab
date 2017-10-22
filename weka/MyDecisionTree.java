package DataMiningLab.weka;

import java.io.BufferedReader;
import java.io.FileReader;

import weka.classifiers.Evaluation;
import weka.classifiers.trees.J48;
import weka.core.Instances;

public class MyDecisionTree {
	
	public MyDecisionTree(String filename) throws Exception {
		BufferedReader reader = new BufferedReader(new FileReader(filename));
		Instances data = new Instances(reader);
		reader.close();
		//setting class attributes
		data.setClassIndex(data.numAttributes() -1 );
		
		J48 decisionTree = new J48();
		decisionTree.buildClassifier(data);
		
		Evaluation eval = new Evaluation(data);
		eval.evaluateModel(decisionTree, data);
		System.out.println(eval.toSummaryString("\nResults\n======\n", false));
	}
}
