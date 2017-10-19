package sunjava.cls.expression;

import sunjava.IAttributeContainer;
import sunjava.cls.AbstractJavaCls;
import sunjava.cls.Attr;
import sunjava.cls.JavaCls;
import sunjava.cls.Method;
import sunjava.cls.MethodCall;
import sunjava.cls.Type;
import sunjava.cls.instruction.AssignInstruction;
import sunjava.cls.instruction.Instruction;
import sunjava.cls.instruction.MethodCallInstruction;
import sunjava.cls.instruction.ScClosedInstruction;
import sunjava.lib.LibEqualsOperator;
import sunjava.lib.LibOperator;
import util.StringUtil;

public abstract class Expression {
	public abstract Type getType();
	
	public Expression callMethod(Method m, Expression...args) {
		return new MethodCall(this, m,args);
	}
	
	public MethodCallInstruction callMethodInstruction(String m, Expression...args) {
		return new MethodCallInstruction(callMethod(m, args));
	}
	
	public MethodCallInstruction callAttrSetterMethodInstr(String attrName, Expression...args) {
		Type type = getType();
		if (type instanceof IAttributeContainer) {
			IAttributeContainer c=(IAttributeContainer) type;
			return new MethodCallInstruction(callMethod("set"+StringUtil.ucfirst(c.getAttrByName(attrName).getName()), args));
		}
		throw new RuntimeException("type "+type+" type is not a class"); 
	}
	
	public AccessExpression accessAttr(Attr prototype) {
		Type type = getType();
		if (type instanceof IAttributeContainer) {
			IAttributeContainer c=(IAttributeContainer) type;
			return new AccessExpression(this, c.getAttr(prototype)) ;
		} 
		throw new RuntimeException("type "+type+" type is not a class"); 
	}
	
	public Expression accessAttr(String name) {
		Type type = getType();
		if (type instanceof IAttributeContainer) {
			IAttributeContainer c=(IAttributeContainer) type;
			
			Attr attr = c.getAttrByName(name);
				return new AccessExpression(this, attr) ;
			
		} 
		throw new RuntimeException("type "+getType()+" type is not a class or a struct");
	}
	
	
	public MethodCall callMethod(String m, Expression...args) {
		if (!(getType() instanceof JavaCls)) {
			System.out.println(getType());
		}
		try{
			return new MethodCall(this, ((AbstractJavaCls)getType()).getMethod(m),args);
		} catch (Exception e) {
			System.out.println(getType());
			e.printStackTrace();
			throw e;
		}
	}
	
	public String getWriteAccessString() {
		return toString();
	}
	
	@Override
	public abstract String toString() ;
	
	
	public ScClosedInstruction asInstruction() {
		final Expression ex = this;
		return new ScClosedInstruction(toString()) {
			@Override
			public void collectImports(AbstractJavaCls cls) {
				ex.collectImports(cls);
			}
		};
	}
	
	public MethodCall callAttrGetter(String attrName, Expression...args) {
		return callMethod("get"+StringUtil.ucfirst(attrName), args);
	}
	
	public MethodCall callAttrSetter(String attrName, Expression...args) {
		return callMethod("set"+StringUtil.ucfirst(attrName), args);
	}
	
	public MethodCall callAttrGetter(Attr attr, Expression...args) {
		return callMethod("get"+StringUtil.ucfirst(attr.getName()), args);
	}
	
	public Expression isNull() {
		return new BinaryOperatorExpression(this, new LibEqualsOperator(), Expressions.Null); 
	}
	public Expression isNotNull() {
		return new BinaryOperatorExpression(this, Operators.NEQ, Expressions.Null); 
	}


	public Instruction assignAttr(String name, Expression assign) {
		return new AssignInstruction(accessAttr(name), assign);
	}
	
	public Expression equalsOp(Expression e) {
		return new BinaryOperatorExpression(this, new LibEqualsOperator(), e);
	}
	public Expression binOp(String symbol, Expression arg) {
		BinaryOperatorExpression e=new BinaryOperatorExpression(this, new LibOperator(symbol, arg.getType(), false), arg);
		return e ;
	}

	
	public Expression greaterThan(Expression other) {
		return binOp(">", other);
	}
	
	public Expression cast(Type castType) {
		return new JavaCast(castType, this);
	}


	public Expression _equals(Expression other) {
		if (other.getType().isPrimitiveType()) {
			return equalsOp(other);
		} else {
			return callMethod("equals", other);
		}
		
	}
	
	public Expression _instanceof(Type type) {
		return new JavaInstanceOfExpression(this, type);
	}
	
	public void collectImports(AbstractJavaCls cls) {
		
	}

	public Expression accessAttrGetter(Attr prototype) {
		Type type = getType();
		if (type instanceof IAttributeContainer) {
			IAttributeContainer c=(IAttributeContainer) type;
			return new MethodCall(this,((AbstractJavaCls) getType()).getMethod("get" + StringUtil.ucfirst(c.getAttr(prototype).getName() )));
		} 
		throw new RuntimeException("type "+type+" type is not a class or a struct");
		
	}
	
//	public Expression callMethod(Cls cls, String m) {
//		return new MethodCall(this, cls.getMethod(m));
//	}
	
}
