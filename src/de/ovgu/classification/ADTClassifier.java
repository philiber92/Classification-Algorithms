package de.ovgu.classification;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Vector;

import de.ovgu.classification.tree.BinaryClassADTree;
import weka.core.Instance;
import weka.core.Instances;
import weka.core.converters.ArffLoader.ArffReader;

/**
 * @author Philipp Bergt
 */
public class ADTClassifier {

    public static void main(String... args) throws IOException {
        System.out.print(args[0]);
        System.out.println(args[1]);
        System.out.println(args[2]);
        BufferedReader reader =
        		new BufferedReader(new FileReader(args[0]));
        ArffReader arff = new ArffReader(reader);
        Instances train =  arff.getData();
        BufferedReader readerTest =
        		new BufferedReader(new FileReader(args[1]));
        train.setClassIndex(train.numAttributes() - 1);
        ArffReader arffTest = new ArffReader(readerTest);
        Instances test =  arffTest.getData();
        test.setClassIndex(test.numAttributes() - 1);
        BinaryClassADTree tree = new BinaryClassADTree();
        tree.train(getInstances(train, true), Integer.valueOf(args[2]));
        de.ovgu.classification.parser.Instances<Vector<Double>> labels = getInstances(test, false);
        tree.classify(labels);
        for(de.ovgu.classification.parser.Instance<Vector<Double>> label : labels) {
        	System.out.println(label.getLabel());
        }
        
    }
    
    public static de.ovgu.classification.parser.Instances<Vector<Double>> getInstances(Instances instances, boolean isLabeled) {
    	de.ovgu.classification.parser.Instances<Vector<Double>> copyInstances = new de.ovgu.classification.parser.Instances<>();
    	int numAttributes = instances.numAttributes() - 1;
    	for(int i = 0; i < instances.numInstances(); i++) {
    		final Instance instance = instances.instance(i);
    		final Vector<Double> attributes = getAttributes(instance);
    		attributes.remove(numAttributes);
    		de.ovgu.classification.parser.Instance<Vector<Double>> copyInstance = new de.ovgu.classification.parser.Instance<Vector<Double>>(attributes, numAttributes);
    		if(isLabeled) {
    			Double label =  new Double(instance.classValue());
    			copyInstance.setLabel(label.intValue());
    		}
    		copyInstances.add(copyInstance);
    	}
    	return copyInstances;
    }
    
    public static Vector<Double> getAttributes(Instance instance) {
    	final Vector<Double> attr = new Vector<>();
    	for(double value : instance.toDoubleArray()) {
    		attr.add(value);
    	}
    	return attr;
    }
}
