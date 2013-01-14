package ru.madzi.lab.stat.expression;

/**
 *
 */
public class VariableNode extends AbstractNode {

    private String name;

    public VariableNode(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public VariableNode setName(String name) {
        this.name = name;
        return this;
    }

}
