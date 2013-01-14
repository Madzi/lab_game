package ru.madzi.lab.stat.expression;

/**
 *
 */
public enum Relation {

    EQL("="),
    NEQ("#"),
    LSS("<"),
    LEQ("<="),
    GTR(">"),
    GEQ(">=");

    private String name;

    Relation(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

}
