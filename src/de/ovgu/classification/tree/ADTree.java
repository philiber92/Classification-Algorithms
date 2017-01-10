package de.ovgu.classification.tree;

import java.util.Optional;

import de.ovgu.classification.parser.Instance;
import de.ovgu.classification.parser.Instances;

/**
 * Represents an interface for constructing and simulating alternating decision trees.
 *
 * @author Philipp Bergt
 */
public interface ADTree<Input, PredictionType> {

    /**
     * Classifies multiples objects of type {@link Instance} by simulating {@link ADTree}.
     *
     * @param instances
     * @return related {@link Instances} with set {@link Instance#_label}
     */
    Instances<Input> classify(Instances<Input> instances);

    /**
     * Classifies one object of type {@link Instance} by simulating {@link ADTree}.
     *
     * @param instance
     * @return related {@link Instance} with set {@link Instance#_label}
     */
    Instance<Input> classify(Instance<Input> instance);

    /**
     * Sets root {@link PredictionNode} by {@link PredictionType}.
     *
     * @param prediction
     */
    void setRootPrediction(PredictionType prediction);

    /**
     * Sets root {@link PredictionNode} to pre-created one.
     *
     * @param predictionNode
     */
    void setRoot(PredictionNode predictionNode);

    /**
     * Simulates given {@link Instance} over the whole pre-trained adtree.
     *
     * @param instance
     * @return summed {@link PredictionType} from root to last leaf
     */
    PredictionType simulate(Instance<Input> instance);

    /**
     * Returns root {@link PredictionNode}.
     *
     * @return {@link PredictionNode}
     */
    Optional<PredictionNode> getRootPrediction();
}
