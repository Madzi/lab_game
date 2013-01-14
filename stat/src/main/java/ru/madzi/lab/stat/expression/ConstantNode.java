package ru.madzi.lab.stat.expression;

/**
 *
 */
public class ConstantNode extends AbstractNode {

    private double value;

    public ConstantNode(double value) {
        this.value = value;
    }

    public double getValue() {
        return value;
    }

    public ConstantNode setValue(double value) {
        this.value = value;
        return this;
    }

}
