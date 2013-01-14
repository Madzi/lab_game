package ru.madzi.lab.stat;

import ru.madzi.lab.stat.expression.AbstractNode;
import ru.madzi.lab.stat.expression.ConstantNode;
import ru.madzi.lab.stat.expression.Expression;
import ru.madzi.lab.stat.expression.Operator;
import ru.madzi.lab.stat.expression.OperatorNode;
import ru.madzi.lab.stat.expression.Relation;
import ru.madzi.lab.stat.expression.RelationNode;
import ru.madzi.lab.stat.expression.VariableNode;

/**
 * Hello world!
 *
 */
public class App {

    private static AbstractNode node = new RelationNode(
            Relation.EQL,
            new VariableNode("f"),
            new OperatorNode(
                Operator.MUL,
                new VariableNode("m"),
                new OperatorNode(
                    Operator.ADD,
                    new VariableNode("a"),
                    new ConstantNode(2.0)
                )
            )
        );

    public static void main( String[] args ) {
        System.out.println( Expression.rpn(node) );
        System.out.println( Expression.npr(node) );
    }

}
