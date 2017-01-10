package de.ovgu.classification.boosting;

import java.util.Vector;

import de.ovgu.classification.parser.Instances;
import de.ovgu.classification.tree.BinaryClassADTree;

/**
 * @author Philipp Bergt
 */
public class LT1PCBoost implements Boosting<Vector, Vector> {

    @Override
    public void boost(BinaryClassADTree tree, Instances<Vector> instances, int iterations) {

    }
}
