package sunjava.cls.expression;

import java.util.ArrayList;

import codegen.CodeUtil;
import sunjava.Types;
import sunjava.cls.AbstractJavaCls;
import sunjava.cls.Type;

public class ArrayInitList extends Expression{

	private ArrayList<Expression> elems;
	
	public ArrayInitList() {
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
		return '{' + CodeUtil.commaSep(elems) + '}';
	}

	@Override
	public void collectImports(AbstractJavaCls cls) {
		for(Expression e : elems)
			e.collectImports(cls);
	}

}
