package de.ovgu.classification.boosting;

import java.util.Vector;

import de.ovgu.classification.parser.Instance;
import de.ovgu.classification.parser.Instances;
import de.ovgu.classification.tree.BinaryClassADTree;

/**
 * Represents the basic AdaBoost algorithm, which it's able to classify binary problems.
 *
 * @author Philipp Bergt
 */
public class AdaBoost implements Boosting<Vector, Double>{

    private Instances<Vector> _instances;
    private BinaryClassADTree _adTree;
    private Vector<Double> _weights;
    private int _iterations;
    private int _positiveLabel, _negativeLabel;

    @Override
    public void boost(BinaryClassADTree tree, Instances<Vector> instances, int iterations) {
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
    }

    private void initWeights() {
        _weights = new Vector<>();
        int numInstances = _instances.size();
        _instances.forEach(vectorInstance -> _weights.add(1.0/numInstances));
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

    private void updateWeights() {
        for(int i = 0; i < _weights.size(); i++) {
            double value = _weights.get(i);
            final Instance instance = _instances.get(i);
            int y = (instance.getLabel() == _positiveLabel) ? 1 : -1;
            value = value * Math.exp(-y*_adTree.simulate(instance));
            _weights.set(i, value);
        }
    }

}

