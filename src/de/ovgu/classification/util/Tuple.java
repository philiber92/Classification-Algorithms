package de.ovgu.classification.util;

/**
 * Simple tuple class.
 * 
 * @author Philipp Bergt
 */
public class Tuple<First, Second> {

    First _first;
    Second _second;

    public Tuple(First first, Second second) {
        _first = first;
        _second = second;
    }

    /**
     * @return first element
     */
    public First getFirst() {
        return _first;
    }

    /**
     * @return second element
     */
    public Second getSecond() {
        return _second;
    }

    /**
     * Sets object as first element.
     * @param _first
     */
    public void setFirst(First _first) {
        this._first = _first;
    }

    /**
     * Sets object as second element.
     * @param _second
     */
    public void setSecond(Second _second) {
        this._second = _second;
    }

}
