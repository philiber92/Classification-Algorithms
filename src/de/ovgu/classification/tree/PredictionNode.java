package de.ovgu.classification.tree;

import java.util.Optional;

/**
 * @author Philipp Bergt
 */
public interface PredictionNode<PredictionType> {

    /**
     *
     * @param splitter
     */
    void setSplitter(SplitterNode<PredictionType> splitter);

    /**
     *
     * @return
     */
    boolean hasSplitter();

    /**
     *
     * @return
     */
    Optional<SplitterNode<PredictionType>> getSplitter();

    /**
     *
     * @return
     */
    PredictionType getValue();
}
