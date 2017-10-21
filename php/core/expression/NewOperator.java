package php.core.expression;

import java.util.ArrayList;

import generate.CodeUtil2;
import php.core.PhpArray;
import php.core.PhpCls;
import php.core.PrimitiveType;
import php.core.Type;
import php.lib.PhpStringType;

public class NewOperator extends Expression {

	protected Type type;
	protected Expression[] args;
	
	public NewOperator(Type type, Expression...args) {
		if (type instanceof PhpStringType && (args.length == 0 || args[0].getType() instanceof PhpStringType)) {
			throw new RuntimeException("warning null string");
		}
		if(type instanceof PrimitiveType) {
			throw new RuntimeException("cannot instantiate primitive type");
		}
		if(type instanceof PhpArray) {
			throw new RuntimeException("cannot instantiate array type");
		}
		if(((PhpCls)type).isAbstract()) {
			throw new RuntimeException("cannot instantiate abstract class: " + type.getName());
		}
		this.type = type;		
		this.args = args;
		for (Expression a : args) {
			if(a==null) {
				throw new IllegalArgumentException();
			}
		}
	}
	
	@Override
	public String toString() {
		ArrayList<String> args = new ArrayList<>();
		for(Expression a : this.args) {
			args.add(a.getUsageString());
		}
		
		return CodeUtil2.sp("new",type.toDeclarationString()+CodeUtil2.parentheses(CodeUtil2.commaSep(args)));
	}
	
	@Override
	public Type getType() {
		return type;
	}

	

}
