package de.ovgu.classification.parser;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Simple wrapper class which is able to hold multiple elements of type {@link Instance}.
 *
 * @param <T> underlying data structure
 * @author Philipp Bergt
 */
public class Instances<T> implements Iterable<Instance<T>>{

    final List<Instance<T>> _instances = new ArrayList<>();

    /**
     * Adds given instance to collection.
     *
     * @param instance
     */
    public void add(final Instance<T> instance) {
        _instances.add(instance);
    }

    /**
     * Adds given instance constructed by given arguments and a weight of 1.
     *
     * @param data x data type or dimension
     * @param label data label
     */
    public void add(T data, int label) {
        _instances.add(new Instance<>(data, label));
    }

    /**
     * Counts all class labels.
     *
     * @return number of labels
     */
    public int countClasses() {
        if(_instances.isEmpty())
            throw new RuntimeException("No instances added!");
        return getLabels().size();
    }

    /**
     * Counts dimension(s) of instances' data.
     *
     * @return dimensions
     */
    public int countDimensions() {
        if(_instances.isEmpty())
            throw new RuntimeException("No instances added!");
        return _instances.get(0).countDimensions();
    }

    /**
     * Counts all contained {@link Instance}.
     *
     * @return number of {@link Instance} elements.
     */
    public int size() {
        return _instances.size();
    }

    public Instance<T> get(int index) {
        return _instances.get(index);
    }

    public List<Integer> getLabels() {
        return _instances
                .stream()
                .filter(Instance::isLabeled)
                .map(Instance::getLabel)
                .collect(Collectors.toCollection(HashSet::new))
                .stream()
                .collect(Collectors.toCollection(ArrayList::new));
    }

    @Override
    public Iterator<Instance<T>> iterator() {
        return _instances.iterator();
    }

}
