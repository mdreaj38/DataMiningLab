package DataMiningLab.weka;

import java.io.BufferedReader;
import java.io.FileReader;

import weka.classifiers.Evaluation;
import weka.classifiers.functions.MultilayerPerceptron;
import weka.core.Instances;

public class MyNeuralNetwork {
	public MyNeuralNetwork(String filename) throws Exception {
		BufferedReader reader = new BufferedReader(new FileReader(filename));
		Instances data = new Instances(reader);
		reader.close();
		//setting class attributes
		data.setClassIndex(data.numAttributes() -1 );
		
		MultilayerPerceptron mlp = new MultilayerPerceptron();
		
		mlp.setLearningRate(0.1);
		mlp.setMomentum(0.2);
		mlp.setTrainingTime(2000);
		mlp.setHiddenLayers("3");
		
		mlp.buildClassifier(data);
		
		Evaluation eval = new Evaluation(data);
		eval.evaluateModel(mlp, data);
		System.out.println(eval.toSummaryString("\nResults\n======\n", false));
	}
}
