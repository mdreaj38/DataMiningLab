package DataMiningLab.weka;

public class ClassifierStrategy {
	
	public void runClassifier(String classifier, String filename)throws Exception{
		if(classifier.equals("NN")){
			new MyNeuralNetwork(filename);
		}
		else if(classifier.equals("KNN")){
			new MyKNearestNeighbor(filename);
		}
		else if(classifier.equals("NB")){
			new MyNaiveBayes(filename);
		}
		else if(classifier.equals("DT")){
			new MyDecisionTree(filename);
		}
	}
}
