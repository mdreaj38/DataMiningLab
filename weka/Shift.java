package DataMiningLab.weka;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

import weka.attributeSelection.InfoGainAttributeEval;
import weka.classifiers.Evaluation;
import weka.classifiers.evaluation.output.prediction.PlainText;
import weka.classifiers.functions.MultilayerPerceptron;
import weka.core.Attribute;
import weka.core.Instance;
import weka.core.Instances;

public class Shift {
	private String filename;
	private Attribute splitValues;
	public Shift(String filename) throws Exception{
		this.filename = filename;
		BufferedReader reader = new BufferedReader(new FileReader(filename+".arff"));
		Instances data = new Instances(reader);
		reader.close();
		//setting class attributes
		data.setClassIndex(data.numAttributes() -1 );
		
		int splitIndex = getSplitIndex(data);
		splitValues = data.attribute(splitIndex);
		splitFiles(data, splitValues);
		for(int i = 0; i < splitValues.numValues(); i++){
			trainModel(splitValues.value(i));
		}
		boolean ran[][] = new boolean[splitValues.numValues()][splitValues.numValues()];
		for(int i = 0 ; i < splitValues.numValues(); i++)
			for(int j = 0; j < splitValues.numValues(); j++)
				ran[i][j] = false;
		for(int i = 0 ; i < splitValues.numValues(); i++){
			for(int j = 0 ; j < splitValues.numValues(); j++){
				if(!splitValues.value(i).equals(splitValues.value(j)) && !ran[i][j]){
					ran[i][j] = ran[j][i] = true;
					testEachOther(splitValues.value(i),splitValues.value(j));
				}
			}
		}
	}
	public void testEachOther(String test, String testWith) throws Exception{
		BufferedReader reader = new BufferedReader(new FileReader(filename+"_"+test+".arff"));
		Instances testData = new Instances(reader);
		reader.close();
		testData.setClassIndex(testData.numAttributes() -1 );
		
		StringBuffer predsBuffer = new StringBuffer();
        PlainText plainText = new PlainText();
        plainText.setHeader(testData);
        plainText.setBuffer(predsBuffer);
        
        MultilayerPerceptron classifier = (MultilayerPerceptron) weka.core.SerializationHelper
                .read(new FileInputStream(new File(filename+"_"+testWith+".model")));
        
        Evaluation eval = new Evaluation(testData);
        eval.evaluateModel(classifier, testData,plainText);
        
        System.out.println("Testing between "+test + " and " + testWith + ":");
        System.out.println(predsBuffer);
		
	}
	public void trainModel(String attributeValue) throws Exception{
		BufferedReader reader = new BufferedReader(new FileReader(filename+"_"+attributeValue+".arff"));
		Instances data = new Instances(reader);
		reader.close();
		data.setClassIndex(data.numAttributes() -1 );
		
		MultilayerPerceptron mlp = new MultilayerPerceptron();
        //NaiveBayes n= new NaiveBayes();
        //Setting Parameters
        mlp.setLearningRate(0.1);
        mlp.setTrainingTime(500);
        mlp.setHiddenLayers("3");
        mlp.buildClassifier(data);
        
        Evaluation eval = new Evaluation(data);
        eval.evaluateModel(mlp, data);
        System.out.println(eval.errorRate()); //Printing Training Mean root squared Error
        System.out.println(eval.toSummaryString()); //Summary of Training
        
        weka.core.SerializationHelper.write(filename+"_"+attributeValue+".model", mlp);
	}
	public void splitFiles(Instances data, Attribute splitValues) throws IOException{
		
		String header = createHeader();
		HashMap<String, ArrayList<Instance>> hm = new HashMap<String, ArrayList<Instance>>();
		HashMap<String, FileWriter> fileWrite = new HashMap<String, FileWriter>();
		
		for(int i = 0; i < splitValues.numValues(); i++){
			System.out.println(splitValues.value(i));
			if(!hm.containsKey(splitValues.value(i)))
				hm.put(splitValues.value(i), new ArrayList<Instance>());
			if(!fileWrite.containsKey(splitValues.value(i))){
				fileWrite.put(splitValues.value(i), new FileWriter(
						new File(filename+"_"+splitValues.value(i))+".arff"));
				fileWrite.get(splitValues.value(i)).write(header);
			}
		}
		for(int i = 0; i < data.size(); i++){
			//System.out.println(data.instance(i));
			hm.get(data.instance(i).stringValue(splitValues)).add(data.instance(i));
		}
		for(int i = 0; i < splitValues.numValues(); i++){
			//System.out.println(hm.get(splitValues.value(i)));
			for(Instance x : hm.get(splitValues.value(i))){
				fileWrite.get(splitValues.value(i)).write(x.toString()+"\n");
			}
			fileWrite.get(splitValues.value(i)).close();
		}
		
	}
	public String createHeader() throws FileNotFoundException{
		Scanner s = new Scanner(new File(filename+".arff"));
		String attributeList = "";
		while(s.hasNextLine()){
			String str = s.nextLine();
			if(str.contains("@data")){
				attributeList += str + "\n";
				break;
			}
			attributeList += str + "\n";
		}
		s.close();
		return attributeList;
	}
	public int getSplitIndex(Instances data) throws Exception{
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
		return splitIndex;
	}
}
