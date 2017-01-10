package de.ovgu.classification.tree;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import de.ovgu.classification.boosting.Boosting;
import de.ovgu.classification.parser.Instances;
import de.ovgu.classification.util.Condition;

/**
 * @author Philipp Bergt
 */
public abstract class BoostableADTree<Input, PredictionType> implements ADTree<Input, PredictionType>{

    protected Optional<de.ovgu.classification.tree.PredictionNode> rootNode;
    protected Boosting boostStrategy;

    public BoostableADTree() {
        rootNode = Optional.empty();
    }

    public BoostableADTree(PredictionNode predictionNode) {
        rootNode = Optional.of(predictionNode);
    }

    public abstract void train(Instances instances, int iterations);

    public List<PredictionNode> getAllLeaves() {
        PredictionNode predictionNode = (PredictionNode) rootNode
                .orElseThrow(() -> new RuntimeException("Tree isn't set!"));
        return predictionNode.getAllLeaves();
    }

    public void setBoostStrategy(Boosting boosting) {
        boostStrategy = boosting;
    }

    @Override
    public void setRoot(de.ovgu.classification.tree.PredictionNode predictionNode) {
        rootNode = Optional.of(predictionNode);
    }

    @Override
    public void setRootPrediction(PredictionType prediction) {
        setRoot(new PredictionNode(prediction));
    }

    @Override
    public Optional<de.ovgu.classification.tree.PredictionNode> getRootPrediction() {
        return rootNode;
    }

    class PredictionNode implements de.ovgu.classification.tree.PredictionNode<PredictionType> {

        private final PredictionType _prediction;
        private Optional<de.ovgu.classification.tree.SplitterNode<PredictionType>> _splitter;

        PredictionNode(PredictionType prediction) {
            this(prediction, null);
        }

        PredictionNode(PredictionType prediction, SplitterNode splitterNode) {
            _prediction = prediction;
            setSplitter(splitterNode);
        }

        @Override
        public void setSplitter(de.ovgu.classification.tree.SplitterNode<PredictionType> splitter) {
            _splitter = Optional.ofNullable(splitter);
        }

        @Override
        public boolean hasSplitter() {
            return _splitter.isPresent();
        }

        @Override
        public Optional<de.ovgu.classification.tree.SplitterNode<PredictionType>> getSplitter() {
            return _splitter;
        }

        @Override
        public PredictionType getValue() {
            return _prediction;
        }

        /**
         * Collects all predictions which are leaves.
         *
         * @return list containing all currently set leaves
         */
        public List<PredictionNode> getAllLeaves() {
            return collectFreePredictions(this);
        }

        /**
         * Recursively search for all predictions in which the splitter isn't set.
         *
         * @param predictionNode start node
         * @return list containing all 'splitter-free' predictions
         */
        private List<PredictionNode> collectFreePredictions(final PredictionNode predictionNode) {
            final ArrayList<PredictionNode> list = new ArrayList<>();
            if(hasSplitter()) {
                final SplitterNode splitter = (SplitterNode) _splitter.get();
                list.addAll(collectFreePredictions((PredictionNode) splitter.getTruePrediction()));
                list.addAll(collectFreePredictions((PredictionNode) splitter.getFalsePrediction()));
                return list;
            }
            list.add(predictionNode);
            return list;
//            only jdk 9 can do this :(
//            splitterNode.ifPresent(sp -> {
//                list.addAll(search((PredictionNode) sp.getFalsePrediction()));
//                list.addAll(search((PredictionNode) sp.getTruePrediction()));
//            }).ifNotPresent(...);
        }
    }

    class SplitterNode implements de.ovgu.classification.tree.SplitterNode<PredictionType> {

        private final Condition _condition;
        private final PredictionNode _truePrediction, _falsePrediction;

        SplitterNode(Condition condition, PredictionNode truePrediction, PredictionNode falsePrediction) {
            _condition = condition;
            _truePrediction = truePrediction;
            _falsePrediction = falsePrediction;
        }

        @Override
        public Condition getCondition() {
            return _condition;
        }

        @Override
        public de.ovgu.classification.tree.PredictionNode<PredictionType> getTruePrediction() {
            return _truePrediction;
        }

        @Override
        public de.ovgu.classification.tree.PredictionNode<PredictionType> getFalsePrediction() {
            return _falsePrediction;
        }
    }
}
