package ru.madzi.lab.stat.expression;

/**
 *
 */
public abstract class AbstractDoNode extends AbstractNode {

    private AbstractNode left;

    private AbstractNode right;

    public AbstractNode getLeft() {
        return left;
    }

    public AbstractDoNode setLeft(AbstractNode left) {
        this.left = left;
        return this;
    }

    public AbstractNode getRight() {
        return right;
    }

    public AbstractDoNode setRight(AbstractNode right) {
        this.right = right;
        return this;
    }

}
