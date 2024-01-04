package php.core.expression;

import php.core.AbstractPhpCls;
import php.core.Attr;
import php.core.IAttributeContainer;
import php.core.Operator;
import php.core.PhpArray;
import php.core.PhpCls;
import php.core.PhpFunctions;
import php.core.Type;
import php.core.Types;
import php.core.instruction.AssignInstruction;
import php.core.instruction.Instruction;
import php.core.instruction.MethodCallInstruction;
import php.core.instruction.SemicolonTerminatedInstruction;
import php.core.method.Method;
import php.lib.LibEqualsOperator;
import php.lib.LibOperator;
import util.StringUtil;

public abstract class Expression {
	public abstract Type getType();
	
	public Expression callMethod(Method m, Expression...args) {
		return new MethodCall(this, m,args);
	}
	
	public MethodCallInstruction callMethodInstruction(String m, Expression...args) {
		return new MethodCallInstruction(callMethod(m, args));
	}
	
	public Instruction arrayPush(Expression arg) {
		if(!(getType() instanceof PhpArray)) {
			throw new RuntimeException("expression must be an array");
		}
		return new BinaryOperatorExpression(this, ArrayPushOperator.INSTANCE, arg).asInstruction(); 
	}
	
	public Instruction arrayUnset(Expression arg) {
		if(!(getType() instanceof PhpArray)) {
			throw new RuntimeException("expression must be an array");
		}
		return new PhpFunctionCall( PhpFunctions.unset, arrayIndex( arg)).asInstruction();
	}
	

	public Instruction arrayIndexSet(Expression arrayIndex, Expression arg) {
		return new AssignInstruction(this.arrayIndex(arrayIndex), arg);
	}
	
//	public Expression splObjectHash() {
//		return new PhpFunctionCall( PhpFunctions.spl_object_hash, this);
//	}
	public Expression count() {
		return new PhpFunctionCall( PhpFunctions.count, this);
	}
	public Expression arrayIndex(Expression arg) {
		return new ArrayAccessExpression(this, arg);
	}
	
	public Expression arrayIndexIsset(Expression arg) {
		return PhpFunctions.isset.call(this.arrayIndex(arg));
	}
	public Expression arrayKeyExists(Expression arg) {
		return PhpFunctions.array_key_exists.call(arg,this);
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
		if (!(getType() instanceof PhpCls)) {
			System.out.println(getType());
		}
		try{
			return new MethodCall(this, ((AbstractPhpCls)getType()).getMethod(m),args);
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
	
	
	public SemicolonTerminatedInstruction asInstruction() {
		return new SemicolonTerminatedInstruction(toString());
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
	
	public Expression notEqualsOp(Expression e) {
		return new BinaryOperatorExpression(this, NotEqualOperator.INSTANCE, e);
	}
	
	public Expression and(Expression e) {
		return new BinaryOperatorExpression(this, Operators.AND, e);
	}
	public Expression binOp(String symbol, Expression arg) {
		BinaryOperatorExpression e=new BinaryOperatorExpression(this, new LibOperator(symbol, arg.getType(), false), arg);
		return e ;
	}

	public Expression binOp(Operator op, Expression arg) {
		BinaryOperatorExpression e=new BinaryOperatorExpression(this, op, arg);
		return e ;
	}
	
	public Expression _or(Expression arg) {
		BinaryOperatorExpression e=new BinaryOperatorExpression(this, Operators.OR, arg);
		return e ;
	}
	
	public Expression _and(Expression arg) {
		BinaryOperatorExpression e=new BinaryOperatorExpression(this, Operators.AND, arg);
		return e ;
	}
	
	public Expression greaterThan(Expression other) {
		return binOp(">", other);
	}


	public Expression _equals(Expression other) {
		if (other.getType()==null || other.getType().isPrimitiveType() || other.getType().equals(Types.String)|| other.getType().equals(Types.DateTime)) {
			return equalsOp(other);
		} else {
			return callMethod("equals", other);
		}
		
	}
	public Expression _notEquals(Expression other) {
		if (other.getType().isPrimitiveType() || other.getType().equals(Types.String)|| other.getType().equals(Types.DateTime)) {
			return notEqualsOp(other);
		} else {
			return new NotExpression(callMethod("equals", other));
		}
		
	}
	
	public Expression _instanceof(Type type) {
		return new PhpInstanceOfExpression(this, type);
	}
	

	public Expression accessAttrGetter(Attr prototype) {
		Type type = getType();
		if (type instanceof IAttributeContainer) {
			IAttributeContainer c=(IAttributeContainer) type;
			return new MethodCall(this,((AbstractPhpCls) getType()).getMethod("get" + StringUtil.ucfirst(c.getAttr(prototype).getName() )));
		} 
		throw new RuntimeException("type "+type+" type is not a class or a struct");
		
	}

	public String getUsageString() {
		return toString();
	}
	
	public Expression concat(Expression e) {
		return new PhpStringPlusOperatorExpression(this, e);
	}
	
	public Expression cast(Type castType) {
		return new Cast(castType, this);
	}

//	public Expression callMethod(Cls cls, String m) {
//		return new MethodCall(this, cls.getMethod(m));
//	}
	
}
