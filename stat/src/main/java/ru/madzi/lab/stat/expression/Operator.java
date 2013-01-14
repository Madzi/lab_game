package ru.madzi.lab.stat.expression;

/**
 *
 */
public enum Operator {

    ADD(1, "+"),
    SUB(1, "-"),
    MUL(2, "*"),
    DIV(2, "/");

    private int priority;

    private String name;

    Operator(int priority, String name) {
        this.priority = priority;
        this.name = name;
    }

    public int getPriority() {
        return priority;
    }

    public String getName() {
        return name;
    }

    public boolean highPriority(Operator op) {
        return priority > op.priority;
    }

    public boolean lowPriority(Operator op) {
        return priority < op.priority;
    }

}
