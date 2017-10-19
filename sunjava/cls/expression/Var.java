package sunjava.cls.expression;

import codegen.CodeUtil;
import sunjava.cls.AbstractJavaCls;
import sunjava.cls.JavaCls;
import sunjava.cls.Type;
import sunjava.cls.instruction.AssignInstruction;
import sunjava.cls.instruction.DeclareInstruction;
import sunjava.cls.instruction.Instruction;

public class Var extends Expression{
	protected Type type;
	protected String name;
	
	public Var(Type type, String name) {
		if(type == null) {
			throw new IllegalArgumentException("type is null");
		}
		this.type = type;
		this.name = name;
	}

	public String getName() {
		return name;
	}
	
//	@Deprecated 
//	public String toSourceString() {
//		return CodeUtil.sp(type.toDeclarationString(),getName());
//	}
	
	@Override
	public String toString() {
		return name;
	}
	
//	@Override
//	public String getWriteAccessString() {
//		return name;
//	}
//	
//	@Override
//	public String getReadAccessString() {
//		return type instanceof SharedPtr && ((SharedPtr)type).isWeak() ? name+".lock()" : name;
//	}
	
	public String toDeclarationString() {
		return CodeUtil.sp(type.toDeclarationString(),name);
	}

	@Override
	public Type getType() {
		return type;
	}
	
	
	public AbstractJavaCls getClassType() {
		if (!(type instanceof AbstractJavaCls)) {
			throw new RuntimeException("type "+type+" is not a class / interface"); 
		}
		return (AbstractJavaCls) type;
	}
	
	public JavaCls getClassConcreteType() {
		if (!(type instanceof AbstractJavaCls)) {
			throw new RuntimeException("type "+type+" is not a class"); 
		}
		return (JavaCls) type;
	}
	
	public DeclareInstruction toDeclareInstruction(Expression init) {
		return new DeclareInstruction(this, init);
	}
	
	public Expression invokeOperator(String symbol, Expression arg) {
		return new BinaryOperatorExpression(this,  type.getOperator(symbol), arg);
	}
	
	public ArrayAccessExpression arrayIndex(Expression arrayIndex) {
		return new ArrayAccessExpression(this, arrayIndex);
	}

	public Instruction assign(Expression val) {
		return new AssignInstruction(this, val);
	}
	
	
	
}
