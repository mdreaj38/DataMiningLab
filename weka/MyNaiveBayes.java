package DataMiningLab.weka;

import java.io.BufferedReader;
import java.io.FileReader;

import weka.classifiers.bayes.NaiveBayes;
import weka.classifiers.Evaluation;
import weka.core.Instances;

public class MyNaiveBayes {
	
	public MyNaiveBayes(String filename) throws Exception{
		BufferedReader reader = new BufferedReader(new FileReader(filename));
		Instances data = new Instances(reader);
		reader.close();
		//setting class attributes
		data.setClassIndex(data.numAttributes() -1 );
		
		NaiveBayes nb = new NaiveBayes();
		nb.buildClassifier(data);
		
		Evaluation eval = new Evaluation(data);
		eval.evaluateModel(nb, data);
		System.out.println(eval.toSummaryString("\nResults\n======\n", false));
		
		// Print Confusion Matrix
        double conMat[][]= eval.confusionMatrix();
        for (int i=0;i<conMat.length;++i) {
            for (int j=0;j<conMat[i].length;++j) {
                System.out.print(conMat[i][j]+" ");
            }
            System.out.println();
        }
        weka.core.SerializationHelper.write("naiveBayes.model", nb);
	}
}
