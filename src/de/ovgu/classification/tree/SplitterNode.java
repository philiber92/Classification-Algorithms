package de.ovgu.classification.tree;

import de.ovgu.classification.util.Condition;

/**
 * @author Philipp Bergt
 */
public interface SplitterNode<PredictionType> {

    /**
     *
     * @return
     */
    Condition getCondition();

    /**
     *
     * @return
     */
    PredictionNode<PredictionType> getTruePrediction();

    /**
     *
     * @return
     */
    PredictionNode<PredictionType> getFalsePrediction();
}
