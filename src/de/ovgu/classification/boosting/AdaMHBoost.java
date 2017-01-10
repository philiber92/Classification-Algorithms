package de.ovgu.classification.boosting;

import java.util.Vector;

import de.ovgu.classification.parser.Instances;
import de.ovgu.classification.tree.BinaryClassADTree;

/**
 * Represents and adaption of basic AdaBoost algorithm, which is able to handle multi-class problems.
 *
 * @author Philipp Bergt
 */
public class AdaMHBoost implements Boosting<Vector, Vector>{
    @Override
    public void boost(BinaryClassADTree tree, Instances<Vector> instances, int iterations) {

    }
}
