package de.ovgu.classification.tree;

import java.util.Optional;

/**
 * Represents basic {@link PredictionNode} which is able to hold an
 * value of type {@link #PredictionType}.
 * 
 * @author Philipp Bergt
 */
public interface PredictionNode<PredictionType> {

    /**
     * Sets {@link SplitterNode} to {@link PredictionNode}.
     * 
     * @param splitter
     */
    void setSplitter(SplitterNode<PredictionType> splitter);

    /**
     * @return true if {@link SplitterNode}, otherwise false
     */
    boolean hasSplitter();

    /**
     * @return currently set {@link SplitterNode}
     */
    Optional<SplitterNode<PredictionType>> getSplitter();

    /**
     * @return currently set value
     */
    PredictionType getValue();
    
    /**
     * Removes {@link SplitterNode} from {@link PredictionNode}.
     */
    void removeSplitter();
}
