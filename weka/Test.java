package DataMiningLab.weka;

import java.io.BufferedReader;
import java.io.FileReader;

import weka.classifiers.bayes.NaiveBayes;
import weka.classifiers.Evaluation;
import weka.core.Instances;

public class Test {
	
	public Test() throws Exception{
		BufferedReader reader = new BufferedReader(new FileReader("data/weather.nominal.arff"));
		Instances data = new Instances(reader);
		reader.close();
		//setting class attributes
		data.setClassIndex(data.numAttributes() -1 );
		
		NaiveBayes nb = new NaiveBayes();
		nb.buildClassifier(data);
		
		Evaluation eval = new Evaluation(data);
		eval.evaluateModel(nb, data);
		System.out.println(eval.toSummaryString("\nResults\n======\n", false));
		
	}
}
