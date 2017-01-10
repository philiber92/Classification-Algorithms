package de.ovgu.classification.util;

/**
 * @author Philipp Bergt
 */
public class Tuple<First, Second> {

    First _first;
    Second _second;

    public Tuple(First first, Second second) {
        _first = first;
        _second = second;
    }

    public First getFirst() {
        return _first;
    }

    public Second getSecond() {
        return _second;
    }

    public void setFirst(First _first) {
        this._first = _first;
    }

    public void setSecond(Second _second) {
        this._second = _second;
    }

}
