package ru.madzi.lab.stat.expression;

/**
 *
 */
public class OperatorNode extends AbstractDoNode {

    private Operator operator;

    public OperatorNode(Operator operator, AbstractNode left, AbstractNode right) {
        this.operator = operator;
        setLeft(left);
        setRight(right);
    }

    public Operator getOperator() {
        return operator;
    }

    public OperatorNode setOperator(Operator operator) {
        this.operator = operator;
        return this;
    }

}
