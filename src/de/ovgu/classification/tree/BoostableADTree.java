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

    protected Optional<PredictionNode<PredictionType>> rootNode;
    protected Boosting<Input, PredictionType> boostStrategy;

    public BoostableADTree() {
        rootNode = Optional.empty();
    }

    public BoostableADTree(BoostPredictionNode predictionNode) {
        rootNode = Optional.of(predictionNode);
    }

    public abstract void train(Instances<Input> instances, int iterations);

    public List<BoostPredictionNode> getAllLeaves() {
        BoostPredictionNode predictionNode = (BoostPredictionNode) rootNode
                .orElseThrow(() -> new RuntimeException("Tree isn't set!"));
        return predictionNode.getAllLeaves();
    }

    public void setBoostStrategy(Boosting<Input, PredictionType> boosting) {
        boostStrategy = boosting;
    }

    @Override
    public void setRoot(PredictionNode<PredictionType> predictionNode) {
        rootNode = Optional.of(predictionNode);
    }

    @Override
    public void setRootPrediction(PredictionType prediction) {
        setRoot(new BoostPredictionNode(prediction));
    }

    @Override
    public Optional<PredictionNode<PredictionType>> getRootPrediction() {
        return rootNode;
    }

    public class BoostPredictionNode implements PredictionNode<PredictionType> {

        private final PredictionType _prediction;
        private Optional<SplitterNode<PredictionType>> _splitter;

        public BoostPredictionNode(PredictionType prediction) {
            this(prediction, null);
        }

        BoostPredictionNode(PredictionType prediction, BoostSplitterNode splitterNode) {
            _prediction = prediction;
            setSplitter(splitterNode);
        }

        @Override
        public void setSplitter(SplitterNode<PredictionType> splitter) {
            _splitter = Optional.ofNullable(splitter);
        }

        @Override
        public boolean hasSplitter() {
            return _splitter.isPresent();
        }

        @Override
        public Optional<SplitterNode<PredictionType>> getSplitter() {
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
        public List<BoostPredictionNode> getAllLeaves() {
            return collectFreePredictions(this);
        }

        /**
         * Recursively search for all predictions in which the splitter isn't set.
         *
         * @param predictionNode start node
         * @return list containing all 'splitter-free' predictions
         */
        private List<BoostPredictionNode> collectFreePredictions(final BoostPredictionNode predictionNode) {
            final ArrayList<BoostPredictionNode> list = new ArrayList<>();
            if(predictionNode.hasSplitter()) {
                final BoostSplitterNode splitter = (BoostSplitterNode) predictionNode.getSplitter().get();
                list.addAll(collectFreePredictions((BoostPredictionNode) splitter.getTruePrediction()));
                list.addAll(collectFreePredictions((BoostPredictionNode) splitter.getFalsePrediction()));
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

		@Override
		public void removeSplitter() {
			_splitter = Optional.empty();
		}
    }

    public class BoostSplitterNode implements SplitterNode<PredictionType> {

        private final Condition _condition;
        private BoostPredictionNode _truePrediction, _falsePrediction;

        public BoostSplitterNode(Condition condition, BoostPredictionNode truePrediction, BoostPredictionNode falsePrediction) {
            _condition = condition;
            _truePrediction = truePrediction;
            _falsePrediction = falsePrediction;
        }
        
        public BoostSplitterNode(Condition condition, PredictionType truePrediction, PredictionType falsePrediction) {
        	this(condition, new BoostPredictionNode(truePrediction), new BoostPredictionNode(falsePrediction));
        }

        @Override
        public Condition getCondition() {
            return _condition;
        }

        @Override
        public PredictionNode<PredictionType> getTruePrediction() {
            return _truePrediction;
        }

        @Override
        public PredictionNode<PredictionType> getFalsePrediction() {
            return _falsePrediction;
        }

		@Override
		public void setTrueValue(PredictionType value) {
			_truePrediction = new BoostPredictionNode(value);
		}

		@Override
		public void setFalseValue(PredictionType value) {
			_falsePrediction = new BoostPredictionNode(value);
		}
    }
}
