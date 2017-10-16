package DataMiningLab.weka;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;

import weka.attributeSelection.InfoGainAttributeEval;
import weka.classifiers.Evaluation;
import weka.classifiers.bayes.NaiveBayes;
import weka.core.Instances;

public class Shift {
	public Shift() throws Exception{
		BufferedReader reader = new BufferedReader(new FileReader("data/weather.nominal.arff"));
		Instances data = new Instances(reader);
		reader.close();
		//setting class attributes
		data.setClassIndex(data.numAttributes() -1 );
		
		/*NaiveBayes nb = new NaiveBayes();
		nb.buildClassifier(data);
		
		Evaluation eval = new Evaluation(data);
		eval.evaluateModel(nb, data);
		System.out.println(eval.toSummaryString("\nResults\n======\n", false));*/
		
		InfoGainAttributeEval entropy = new InfoGainAttributeEval();
		entropy.buildEvaluator(data);
		
		double maxGain = entropy.evaluateAttribute(0);
		int splitIndex = 0;
		for(int i = 1 ; i < data.numAttributes() - 1 ; i++){
			double curGain = entropy.evaluateAttribute(i);
			if(curGain > maxGain){
				maxGain = curGain;
				splitIndex = i;
			}
		}
		System.out.println("splitting attributes: " + splitIndex);
	}
}
