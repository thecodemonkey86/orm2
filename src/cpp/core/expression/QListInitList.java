package cpp.core.expression;

import java.util.ArrayList;

import codegen.CodeUtil;
import cpp.Types;
import cpp.core.Type;

public class QListInitList extends Expression {

	Type elemType;
	ArrayList<Expression> expressions;
	
	public QListInitList(Type elemType) {
		this.elemType = elemType;
		this.expressions = new ArrayList<>();
	}
	
	public void addExpression(Expression e) {
		this.expressions.add(e);
	}
	
	@Override
	public Type getType() {
		return Types.qlist(elemType);
	}

	@Override
	public String toString() {
		return '{' + CodeUtil.commaSep(expressions) + '}';
	}

}
