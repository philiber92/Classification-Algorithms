package de.ovgu.classification.util;

/**
 * Represents small wrapper class which provides functionality to
 * build and check simple conditions.
 * 
 * @author Philipp Bergt
 */
public class Condition {

    private ConditionType _conditionType;
    private int _dimensionIndex;
    private double _value;

    public enum ConditionType {
        EQUAL,
        GREATER,
        LESS,
        GREATER_THAN,
        LESS_THAN
    }

    /**
     * Constructs new instance of {@link Condition}.
     * 
     * @param value
     * @param dimensionIndex
     * @param conditionType
     */
    public Condition(double value, int dimensionIndex, ConditionType conditionType) {
        _value = value;
        _dimensionIndex = dimensionIndex;
        _conditionType = conditionType;
    }

    /**
     * Runs conditions checks based on given double value.
     * 
     * @param arg
     * @return
     */
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

    /**
     * @return dimension index
     */
    public int getDimension() {
        return _dimensionIndex;
    }
    
    /**
     * Creates new {@link Condition} 
     * 
     * @param value
     * @param dimensionIndex
     * @param conditionType
     * @return
     */
    public static Condition create(double value, int dimensionIndex, ConditionType conditionType) {
    	return new Condition(value, dimensionIndex, conditionType);
    }
    
    @Override
    public String toString() {
    	return String.format("x%s %s", _dimensionIndex, _value);
    }


}
