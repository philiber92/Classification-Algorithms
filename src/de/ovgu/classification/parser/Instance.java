package de.ovgu.classification.parser;

import java.util.Optional;

/**
 * Represents a data set, which contains both data and label.
 *
 * @author Philipp Bergt
 */
public class Instance<T> {

    private T _data;
    private int _dimension;
    private Optional<Integer> _label;

    /**
     * Constructs new {@link Instance} by given data, dimension and label.
     *
     * @param data data
     * @param dimension dimensions of data
     * @param label data label
     */
    public Instance(final T data, final int dimension, final int label) {
        init(data, dimension, Optional.of(label));
    }

    /**
     * Constructs new unlabeled {@link Instance} by given data and dimension.
     *
     * @param data data
     * @param dimension dimensions of data
     */
    public Instance(final T data, final int dimension) {
        init(data, dimension, Optional.empty());
    }

    private void init(final T data, final int dimension, final Optional<Integer> label) {
        _data = data;
        _label = label;
        _dimension = dimension;
    }

    /**
     * Returns related label of instance. If the instance is
     * unlabeled -1 is returned.
     *
     * @return related label or -1
     */
    public int getLabel() {
        return _label.orElse(-1);
    }

    /**
     *
     * @param label label
     */
    public void setLabel(int label) {
        _label = Optional.of(label);
    }

    /**
     * @return related data
     */
    public T getData() {
        return _data;
    }

    /**
     * @return number of dimensions
     */
    public int countDimensions() {
        return _dimension;
    }

    /**
     * Checks if instance is labeled.
     *
     * @return true, if instance is labeled, otherwise false
     */
    public boolean isLabeled() {
        return _label.isPresent();
    }

}
