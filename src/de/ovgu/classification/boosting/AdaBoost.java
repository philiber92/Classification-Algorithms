package de.ovgu.classification.boosting;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import de.ovgu.classification.parser.Instance;
import de.ovgu.classification.parser.Instances;
import de.ovgu.classification.tree.BinaryClassADTree;
import de.ovgu.classification.tree.BoostableADTree;
import de.ovgu.classification.tree.BoostableADTree.SplitterNode;
import de.ovgu.classification.tree.PredictionNode;
import de.ovgu.classification.util.*;
import de.ovgu.classification.util.Condition.ConditionType;

/**
 * Represents the basic AdaBoost algorithm, which it's able to classify binary problems.
 *
 * @author Philipp Bergt
 */
public class AdaBoost implements Boosting<Vector<Double>, Double>{

    private Instances<Vector<Double>> _instances;
    private BinaryClassADTree _adTree;
    private Map<Instance, Double> _weights;
    private List<Condition> _conditions;
    private int _iterations;
    private int _positiveLabel, _negativeLabel;

    @Override
    public void boost(BinaryClassADTree tree, Instances<Vector<Double>> instances, int iterations) {
        if(instances.countClasses() != 2)
            throw new RuntimeException("AdaBoost is only able two handle binary class problems!");
        if(iterations <= 0)
            throw new RuntimeException("Number of iterations must be greater or equal than one!");
        _instances = instances;
        _adTree = tree;
        _iterations = iterations;
        init();
    }

    private void init() {
        _positiveLabel = _adTree.getPositiveLabel();
        _negativeLabel = _adTree.getNegativeLabel();
        initWeights();
        initRootNode();
        initConditions();
    }

    private void initWeights() {
        _weights = new HashMap<>();
        int numInstances = _instances.size();
        _instances.forEach(vectorInstance -> _weights.put(vectorInstance, 1.0/numInstances));
    }

    private void initRootNode() {
        double positiveWeight = 0.0;
        double factor = 1.0/_instances.size();
        for(int i = 0; i < _instances.size(); i++) {
            if(_instances.get(i).getLabel() == _positiveLabel) {
                positiveWeight += factor;
            }
        }
        double negativeWeight = 1.0 - positiveWeight;
        double alpha = 0.5 * Math.log(positiveWeight/negativeWeight);
        _adTree.setRootPrediction(alpha);
    }
    
    private void initConditions() {
    	int dimensions = _instances.countDimensions();
    	for(Instance<Vector<Double>> instance : _instances) {
    		Vector<Double> instanceData = instance.getData();
    		for(int i = 0; i < dimensions; i++) {
    			_conditions.add(Condition.create(instanceData.get(i), i, Condition.ConditionType.GREATER));
    			_conditions.add(Condition.create(instanceData.get(i), i, Condition.ConditionType.LESS));
    			_conditions.add(Condition.create(instanceData.get(i), i, Condition.ConditionType.GREATER_THAN));
    			_conditions.add(Condition.create(instanceData.get(i), i, Condition.ConditionType.LESS_THAN));
    		}
    	}
    }
    
    private void boost() {
    	for(int i = 0; i < _iterations; i++) {
    		List<BoostableADTree<Vector<Double>, Double>.PredictionNode> leaves = _adTree.getAllLeaves();
    	}
    }
    
    private void checkLeave(de.ovgu.classification.tree.BoostableADTree.PredictionNode node) {
    	final Condition condition = new Condition(0, 0, ConditionType.GREATER);
    	//final SplitterNode splitter = new SplitterNode(condition, truePrediction, falsePrediction);
    }

    private void updateWeights() {
        for(Instance instance : _instances) {
        	double value = _weights.get(instance);
        	int y = (instance.getLabel() == _positiveLabel) ? 1 : -1;
        	value = value * Math.exp(-y*_adTree.simulate(instance));
        	_weights.put(instance, value);
        }
    }
    
    private double getPositiveWeightSum(Instances<Vector<Double>> instances) {
    	double value = 0.0;
    	for(Instance instance : instances) {
    		if(instance.getLabel() == _positiveLabel) {
    			value += _weights.get(instance);
    		}
    	}
    	return value;
    }
    
    private double getNegativeWeightSum(Instances<Vector<Double>> instances) {
    	double value = 0.0;
    	for(Instance instance : instances) {
    		if(instance.getLabel() == _negativeLabel) {
    			value += _weights.get(instance);
    		}
    	}
    	return value;
    }
    
    private double getWeightSum(Instances<Vector<Double>> instances) {
    	return getPositiveWeightSum(instances) + getNegativeWeightSum(instances);
    }
    
    

}

