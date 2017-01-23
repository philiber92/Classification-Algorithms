package de.ovgu.classification.tree;

import de.ovgu.classification.util.Condition;

/**
 * Represents basic {@link SplitterNode} which holds a
 * {@link Condition} and a pair of {@link PredictionNode}.
 * 
 * @author Philipp Bergt
 */
public interface SplitterNode<PredictionType> {

    /**
     * @return {@link Condition}
     */
    Condition getCondition();

    /**
     * @return true {@link PredictionNode}
     */
    PredictionNode<PredictionType> getTruePrediction();

    /**
     * @return false {@link PredictionNode}
     */
    PredictionNode<PredictionType> getFalsePrediction();
    
    /**
     * Sets value of true {@link PredictionNode}.
     * 
     * @param value
     */
    void setTrueValue(PredictionType value);
    
    /**
     * Sets value of false {@link PredictionNode}.
     * 
     * @param value
     */
    void setFalseValue(PredictionType value);
}
