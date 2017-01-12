package de.ovgu.classification.boosting;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import de.ovgu.classification.parser.Instance;
import de.ovgu.classification.parser.Instances;
import de.ovgu.classification.tree.BinaryClassADTree;
import de.ovgu.classification.tree.BoostableADTree;
import de.ovgu.classification.tree.PredictionNode;
import de.ovgu.classification.tree.SplitterNode;
import de.ovgu.classification.util.*;

/**
 * Represents the basic AdaBoost algorithm, which it's able to classify binary problems.
 *
 * @author Philipp Bergt
 */
public class AdaBoost implements Boosting<Vector<Double>, Double>{

    private Instances<Vector<Double>> _instances;
    private BinaryClassADTree _adTree;
    private Map<Instance<Vector<Double>>, Double> _weights;
    private List<Condition> _conditions;
    private int _iterations;
    private int _positiveLabel, _negativeLabel;

    @Override
    public void boost(BoostableADTree<Vector<Double>, Double> tree, Instances<Vector<Double>> instances, int iterations) {
        if(instances.countClasses() != 2)
            throw new RuntimeException("AdaBoost is only able to handle binary class problems!");
        if(iterations <= 0)
            throw new RuntimeException("Number of iterations must be greater than or equal to one!");
        _instances = instances;
        _adTree = (BinaryClassADTree) tree;
        _iterations = iterations;
        _conditions = new ArrayList<>();
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
    	int test = _instances.size();
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
    		addLeafToTree();
    	}
    }
    
    private void addLeafToTree() {
		List<BoostableADTree<Vector<Double>, Double>.BoostPredictionNode> leaves = _adTree.getAllLeaves();
		double z = Double.MAX_VALUE;
		Condition minCondition = null;
		PredictionNode<Double> minPrediction = null;
		for(BoostableADTree<Vector<Double>, Double>.BoostPredictionNode leaf : leaves) {
	    	for(Condition condition : _conditions) {
	    		leaf.setSplitter(_adTree.new BoostSplitterNode(condition, 0.0, 0.0));
	    		if(calculateRating(leaf.getSplitter().get()) < z) {
	    			minCondition = condition;
	    			minPrediction = leaf;
	    		}
	    		
	    	}
	    	leaf.removeSplitter();
		}
		setSplitterByCondition(minPrediction, minCondition);
    }
    
    private void setSplitterByCondition(PredictionNode<Double> node, Condition condition) {
		node.setSplitter(_adTree.new BoostSplitterNode(condition, 0.0, 0.0));
		SplitterNode<Double> splitter = node.getSplitter().get();
    	Tuple<Instances<Vector<Double>>, Instances<Vector<Double>>> result = _adTree.simulateSplitter(_instances, (BoostableADTree<Vector<Double>, Double>.BoostSplitterNode) splitter);
    	double positiveTrue = getPositiveWeightSum(result.getFirst());
    	double negativeTrue = getNegativeWeightSum(result.getSecond());
    	double positiveFalse = getPositiveWeightSum(result.getSecond());
    	double negativeFalse =  getNegativeWeightSum(result.getSecond());
    	double a1 = 0.5 * Math.exp((positiveTrue+1.0)/(negativeTrue+1.0));
    	double a2 = 0.5 * Math.exp((positiveFalse+1.0)/(negativeFalse+1.0));
    	splitter.setTrueValue(a1);
    	splitter.setFalseValue(a2);
    }
    
    private double calculateRating(SplitterNode<Double> splitter) {
    	Tuple<Instances<Vector<Double>>, Instances<Vector<Double>>> result = _adTree.simulateSplitter(_instances, (BoostableADTree<Vector<Double>, Double>.BoostSplitterNode) splitter);
    	double positiveTrue = getPositiveWeightSum(result.getFirst());
    	double negativeTrue = getNegativeWeightSum(result.getSecond());
    	double positiveFalse = getPositiveWeightSum(result.getSecond());
    	double negativeFalse =  getNegativeWeightSum(result.getSecond());
    	Instances<Vector<Double>> remaining = new Instances<>(_instances);
    	remaining.removeAll(result.getFirst());
    	remaining.removeAll(result.getSecond());
    	return 2 * (Math.sqrt(positiveTrue*negativeTrue) + Math.sqrt(positiveFalse*negativeFalse) + getWeightSum(remaining));
    }

    private void updateWeights() {
        for(Instance<Vector<Double>> instance : _instances) {
        	double value = _weights.get(instance);
        	int y = (instance.getLabel() == _positiveLabel) ? 1 : -1;
        	value = value * Math.exp(-y*_adTree.simulate(instance));
        	_weights.put(instance, value);
        }
    }
    
    private double getPositiveWeightSum(Instances<Vector<Double>> instances) {
    	double value = 0.0;
    	for(Instance<Vector<Double>> instance : instances) {
    		if(instance.getLabel() == _positiveLabel) {
    			value += _weights.get(instance);
    		}
    	}
    	return value;
    }
    
    private double getNegativeWeightSum(Instances<Vector<Double>> instances) {
    	double value = 0.0;
    	for(Instance<Vector<Double>> instance : instances) {
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

