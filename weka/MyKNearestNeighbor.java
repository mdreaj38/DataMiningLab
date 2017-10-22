package DataMiningLab.weka;

import java.io.BufferedReader;
import java.io.FileReader;

import weka.classifiers.Evaluation;
import weka.classifiers.lazy.IBk;
import weka.core.Instances;

public class MyKNearestNeighbor {
	public MyKNearestNeighbor(String filename) throws Exception{
		BufferedReader reader = new BufferedReader(new FileReader(filename));
		Instances data = new Instances(reader);
		reader.close();
		//setting class attributes
		data.setClassIndex(data.numAttributes() -1 );
		
		IBk kNearest = new IBk();
		kNearest.buildClassifier(data);
		//kmeans.buildClusterer(data);
		
		Evaluation eval = new Evaluation(data);
		eval.evaluateModel(kNearest, data);
		System.out.println(eval.toSummaryString("\nResults\n======\n", false));
	}
}
