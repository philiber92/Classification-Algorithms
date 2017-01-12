package de.ovgu.classification.tree;

import java.util.Vector;

import de.ovgu.classification.boosting.LTBoost;
import de.ovgu.classification.parser.Instance;
import de.ovgu.classification.parser.Instances;

/**
 * Represents an adaption of classic
 *
 * @author Philipp Bergt
 */
public class MultiClassADTree extends BoostableADTree<Vector<Double>, Vector<Double>> {

    /**
     *
     */
    public MultiClassADTree() {
        super();
        boostStrategy = new LTBoost();
    }

	@Override
	public Instances<Vector<Double>> classify(Instances<Vector<Double>> instances) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Instance<Vector<Double>> classify(Instance<Vector<Double>> instance) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Vector<Double> simulate(Instance<Vector<Double>> instance) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void train(Instances<Vector<Double>> instances, int iterations) {
		// TODO Auto-generated method stub
		
	}


}
