package php.cls.expression;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import codegen.CodeUtil;
import php.Types;
import php.cls.Type;

public class ArrayInitExpression extends Expression{

	private List<Expression> elems;
	
	public ArrayInitExpression(Expression ...elems) {
		this.elems = Arrays.asList(elems);
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
