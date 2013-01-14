package ru.madzi.lab.stat.expression;

/**
 *
 */
public class RelationNode extends AbstractDoNode {

    private Relation relation;

    public RelationNode(Relation relation, AbstractNode left, AbstractNode right) {
        this.relation = relation;
        setLeft(left);
        setRight(right);
    }

    public Relation getRelation() {
        return relation;
    }

    public RelationNode setRelation(Relation relation) {
        this.relation = relation;
        return this;
    }

}
