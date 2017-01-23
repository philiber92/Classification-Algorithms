package de.ovgu.classification.tree;

import java.util.List;
import java.util.Optional;
import java.util.Vector;

import de.ovgu.classification.boosting.AdaBoost;
import de.ovgu.classification.parser.Instance;
import de.ovgu.classification.parser.Instances;
import de.ovgu.classification.util.Condition;
import de.ovgu.classification.util.Tuple;

/**
 * Represents a simple alternating decision tree, which is only able to handle binary class problems.
 *
 * @author Philipp Bergt
 */
public class BinaryClassADTree extends BoostableADTree<Vector<Double>, Double> {

    private int _positiveLabel, _negativeLabel;

    public BinaryClassADTree() {
        boostStrategy = new AdaBoost();
    }

    @Override
    public Instances<Vector<Double>> classify(Instances<Vector<Double>> instances) {
        for(Instance<Vector<Double>> instance : instances) {
            classify(instance);
        }
        return instances;
    }

    @Override
    public Instance<Vector<Double>> classify(Instance<Vector<Double>> instance) {
        double value = simulate(instance);
        if(value >= 0.0) {
            instance.setLabel(_positiveLabel);
        } else {
            instance.setLabel(_negativeLabel);
        }
        return instance;
    }

    @Override
    public Double simulate(Instance<Vector<Double>> instance) {
        final BoostPredictionNode root = (BoostPredictionNode) rootNode.get();
        double value = root.getValue();
        return value + simulateSplitter(root.getSplitter(), instance);
    }
    
    /**
     * Simulates given splitter based on a set of {@link Instances}. 
     * Returns all true {@link Instance} and false ones as {@link Tuple}.
     * 
     * @param instances
     * @param splitter
     * @return
     */
    public Tuple<Instances<Vector<Double>>, Instances<Vector<Double>>> simulateSplitter(Instances<Vector<Double>> instances, BoostSplitterNode splitter) {
    	final Instances<Vector<Double>> trueInstances = new Instances<>();
    	final Instances<Vector<Double>> falseInstances = new Instances<>();
    	for(Instance<Vector<Double>> instance : instances) {
    		final BoostableADTree<Vector<Double>, Double>.BoostPredictionNode prediction = (BoostableADTree<Vector<Double>, Double>.BoostPredictionNode) simulatePrediction(instance);
    		if(prediction == splitter.getTruePrediction()) {
    			trueInstances.add(instance);
    		} else if(prediction == splitter.getFalsePrediction()) {
    			falseInstances.add(instance);
    		}
    	}
    	return new Tuple<Instances<Vector<Double>>, Instances<Vector<Double>>>(trueInstances, falseInstances);
    }

    @Override
    public void train(Instances<Vector<Double>> instances, int iterations) {
        final List<Integer> labels = instances.getLabels();
        _positiveLabel = labels.get(0);
        _negativeLabel = labels.get(1);
        boostStrategy.boost(this, instances, iterations);
    }
    
    public PredictionNode<Double> simulatePrediction(Instance<Vector<Double>> instance) {
    	PredictionNode<Double> currentNode = rootNode.get();
        while(true) {
        	if(!currentNode.hasSplitter()) {
        		return currentNode;
        	}
        	SplitterNode<Double> splitter = currentNode.getSplitter().get();
            final Condition condition = splitter.getCondition();
            final int dimension = condition.getDimension();
            currentNode = (condition.check(instance.getData().get(dimension)))
            		? (PredictionNode<Double>) splitter.getTruePrediction() : (PredictionNode<Double>) splitter.getFalsePrediction();
        }
    }

    /**
     * @return label which was set as positive one
     */
    public int getPositiveLabel() {
        return _positiveLabel;
    }

    /**
     * @return label which was set as negative one
     */
    public int getNegativeLabel() {
        return _negativeLabel;
    }

    /**
     * Simulates given {@link SplitterNode} by sums up all related values of passed {@link PredictionNode}.
     * 
     * @param splitter
     * @param instance
     * @return sum of passed {@link PredictionNode}
     */
    private double simulateSplitter(Optional<SplitterNode<Double>> splitter, Instance<Vector<Double>> instance) {
        if(!splitter.isPresent()) {
            return 0.0;
        }
        final SplitterNode<Double> splitterNode = splitter.get();
        final Condition condition = splitterNode.getCondition();
        final int dimension = condition.getDimension();
        final PredictionNode<Double> truePrediction = splitterNode.getTruePrediction();
        final PredictionNode<Double> falsePrediction = splitterNode.getFalsePrediction();

        return (condition.check(instance.getData().get(dimension)))
                ? truePrediction.getValue() + simulateSplitter(truePrediction.getSplitter(), instance)
                : falsePrediction.getValue() + simulateSplitter(falsePrediction.getSplitter(), instance);
    }
}
