package php.core.expression;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import codegen.CodeUtil;
import php.core.Type;
import php.core.Types;

public class ArrayInitExpression extends Expression{

	private List<Expression> elems;
	
	public ArrayInitExpression(Expression ...elems) {
		this.elems = Arrays.asList(elems);
	}
	
	public ArrayInitExpression(List<Expression>elems) {
		this.elems = elems;
	}
	
	public ArrayInitExpression() {
		elems = new ArrayList<>();
	}
	
	public void addElement(Expression s) {
		elems.add(s);
	}
	
	@Override
	public Type getType() {
		return Types.array(elems.get(0).getType());
	}

	@Override
	public String toString() {
		return "array" + CodeUtil.parentheses( CodeUtil.commaSep(elems) );
	}

}
