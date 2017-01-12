package de.ovgu.classification.boosting;

import java.util.Vector;

import de.ovgu.classification.parser.Instances;
import de.ovgu.classification.tree.BoostableADTree;

/**
 * Represents and adaption of basic AdaBoost algorithm, which is able to handle multi-class problems.
 *
 * @author Philipp Bergt
 */
public class AdaMHBoost implements Boosting<Vector<Double>, Vector<Double>>{

	@Override
	public void boost(BoostableADTree<Vector<Double>, Vector<Double>> tree, Instances<Vector<Double>> instances,
			int iterations) {
		// TODO Auto-generated method stub
		
	}
}
