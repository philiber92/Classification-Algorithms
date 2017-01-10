package de.ovgu.classification.util;

/**
 * @author Philipp Bergt
 */
public class Condition {

    private ConditionType _conditionType;
    private int _dimensionIndex;
    private double _value;

    enum ConditionType {
        EQUAL,
        GREATER,
        LESS,
        GREATER_THAN,
        LESS_THAN
    }

    public Condition(double value, int dimensionIndex, ConditionType conditionType) {
        _value = value;
        _dimensionIndex = dimensionIndex;
        _conditionType = conditionType;
    }

    public boolean check(double arg) {
        switch (_conditionType) {
            case GREATER:
                return _value > arg;
            case LESS:
                return _value < arg;
            case GREATER_THAN:
                return _value >= arg;
            case LESS_THAN:
                return _value <= arg;
            default:
                return _value == arg;
        }
    }

    public int getDimension() {
        return _dimensionIndex;
    }


}
