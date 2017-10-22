package DataMiningLab.weka;

public class Main {
	public static void main(String args[]) throws Exception{
		new MyNaiveBayes("data/airline.arff");
		//new MyDecisionTree("data/weather.nominal.arff");
		//new MyNeuralNetwork("data/weather.nominal.arff");
		//new MyKNearestNeighbor("data/weather.nominal.arff");
	}
}
