package ru.madzi.lab.stat.expression;

/**
 *
 */
public class Expression {

    public static String npr(AbstractNode node) {
        StringBuilder sb = new StringBuilder();
        if (node != null) {
            if (node instanceof VariableNode) {
                VariableNode variable = (VariableNode) node;
                sb.append(variable.getName());
            } else if (node instanceof ConstantNode) {
                ConstantNode constant = (ConstantNode) node;
                sb.append(constant.getValue());
            } else if (node instanceof OperatorNode) {
                OperatorNode operator = (OperatorNode) node;
                AbstractNode left = operator.getLeft();
                AbstractNode right = operator.getRight();
                if (left instanceof OperatorNode 
                        && operator.getOperator().highPriority(((OperatorNode) left).getOperator())) {
                    sb.append("(")
                      .append(npr(left))
                      .append(")");
                } else {
                    sb.append(npr(left));
                }
                sb.append(" ").append(operator.getOperator().getName()).append(" ");
                if (right instanceof OperatorNode 
                        && operator.getOperator().highPriority(((OperatorNode) right).getOperator())) {
                    sb.append("(")
                      .append(npr(right))
                      .append(")");
                } else {
                    sb.append(npr(right));
                }
            } else if (node instanceof RelationNode) {
                RelationNode relation = (RelationNode) node;
                sb.append(npr(relation.getLeft()));
                sb.append(" ");
                sb.append(relation.getRelation().getName());
                sb.append(" ");
                sb.append(npr(relation.getRight()));
            } else {
                throw new IllegalArgumentException("Unknown node type.");
            }
        }
        return sb.toString();
    }

    public static String rpn(AbstractNode node) {
        StringBuilder sb = new StringBuilder();
        if (node != null) {
            if (sb.length() > 0) {
                sb.append(" ");
            }
            if (node instanceof VariableNode) {
                VariableNode variable = (VariableNode) node;
                sb.append(variable.getName());
            } else if (node instanceof ConstantNode) {
                ConstantNode constant = (ConstantNode) node;
                sb.append(constant.getValue());
            } else if (node instanceof OperatorNode) {
                OperatorNode operator = (OperatorNode) node;
                sb.append(rpn(operator.getLeft()))
                  .append(" ")
                  .append(rpn(operator.getRight()))
                  .append(" ")
                  .append(operator.getOperator().getName());
            } else if (node instanceof RelationNode) {
                RelationNode relation = (RelationNode) node;
                sb.append(rpn(relation.getLeft()))
                  .append(" ")
                  .append(rpn(relation.getRight()))
                  .append(" ")
                  .append(relation.getRelation().getName());
            } else {
                throw new IllegalArgumentException("Unknown node type.");
            }
        }
        return sb.toString();
    }

}
