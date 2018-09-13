package cpp.core.expression;

import util.StringUtil;

import java.util.List;

import cpp.core.Attr;
import cpp.core.Cls;
import cpp.core.IAttributeContainer;
import cpp.core.Method;
import cpp.core.MethodCall;
import cpp.core.Operator;
import cpp.core.RawPtr;
import cpp.core.SharedPtr;
import cpp.core.Type;
import cpp.core.instruction.AssignInstruction;
import cpp.core.instruction.Instruction;
import cpp.core.instruction.MethodCallInstruction;
import cpp.core.instruction.SemicolonTerminatedInstruction;
import cpp.lib.LibEqualsOperator;
import cpp.lib.LibOperator;

public abstract class Expression {
	public abstract Type getType();
	
	
	public Expression callMethod(Method m, Expression...args) {
		if(m == null)
			return this;
		return m.call(this, args);
	}
	

	public MethodCall callAttrGetter(String attrname) {
		return callMethod("get" + StringUtil.ucfirst( attrname));
	}
	
	public MethodCallInstruction callMethodInstruction(String m, Expression...args) {
		return new MethodCallInstruction(callMethod(m, args));
	}
	
	public MethodCallInstruction callSetterMethodInstruction(String attrName, Expression...args) {
		Type type = getType();
		if (type instanceof IAttributeContainer) {
			IAttributeContainer c=(IAttributeContainer) type;
			return new MethodCallInstruction(callMethod("set"+StringUtil.ucfirst(c.getAttrByName(attrName).getName()), args));
		}
		throw new RuntimeException("type "+type+" type is not a class or a struct"); 
	}
	
	public AccessExpression accessAttr(Attr prototype) {
		Type type = getType();
		if (type instanceof IAttributeContainer) {
			IAttributeContainer c=(IAttributeContainer) type;
			if (c.getAttr(prototype) == null) {
				c.getAttr(prototype);
				System.out.println();
			}
			if (type instanceof RawPtr) {
				RawPtr ptr = (RawPtr) type;
				if (ptr.getElementType().getName().equals("Order") ) {
					System.out.println(c.getAttr(prototype));
				}
			}
			return new AccessExpression(this, c.getAttr(prototype)) ;
		} 
		throw new RuntimeException("type "+type+" type is not a class or a struct"); 
	}
	
	public Expression accessAttr(String name) {
		return accessAttr(name, false);
	}
	
	public Expression accessAttr(String name, boolean forWrite) {
		Type type = getType();
		if (type instanceof IAttributeContainer) {
			IAttributeContainer c=(IAttributeContainer) type;
			
			Attr attr = c.getAttrByName(name);
//			if (!(attr.getType() instanceof SharedPtr) || !((SharedPtr)attr.getType() ).isWeak()) {
				return new AccessExpression(this, attr,forWrite) ;
//			} else {
//				return new WeakPtrLock(new AccessExpression(this, attr));
//			}
			
		} 
		throw new RuntimeException("type "+type+" type is not a class or a struct"); 
	}
	
	public MethodCall callMethod(String m, Expression...args) {
		if (!(getType() instanceof Cls)) {
			System.out.println(getType());
		}
		try{
			return new MethodCall(this, ((Cls)getType()).getMethod(m),args);
		} catch (Exception e) {
			System.out.println(e);
			throw e;
		}
	}
	
	public MethodCall callMethod(String m, List<Expression>args) {
		Expression[] argsArray = new Expression[args.size()];
		args.toArray(argsArray);
		return callMethod( m,argsArray);
	}
	
	public String getWriteAccessString() {
		return toString();
	}
	
	public String getReadAccessString() {
		return getType() instanceof SharedPtr && ((SharedPtr)getType()).isWeak() ? toString()+".lock()" :  toString();
	}
	
	@Override
	public abstract String toString() ;
	
	@Deprecated
	public Expression constCast() {
		return new ConstCastExpression(this);
	}
	
	public SemicolonTerminatedInstruction asInstruction() {
		return new SemicolonTerminatedInstruction(toString());
	}
	
	public MethodCall callAttrGetter(String attrName, Expression...args) {
		return callMethod("get"+StringUtil.ucfirst(attrName), args);
	}
	
	public MethodCall callAttrGetter(Attr attr, Expression...args) {
		return callMethod("get"+StringUtil.ucfirst(attr.getName()), args);
	}
	
	public Expression isNull() {
		return new BinaryOperatorExpression(this, new LibEqualsOperator(), Expressions.Nullptr); 
	}


	public Instruction assignAttr(String name, Expression assign) {
		return new AssignInstruction(accessAttr(name), assign);
	}
	
	public Expression _equals(Expression e) {
		return new BinaryOperatorExpression(this, new LibEqualsOperator(), e);
	}
	
	public Expression _notEquals(Expression e) {
		return new BinaryOperatorExpression(this, new NotEqualOperator(), e);
	}
	
	public Expression binOp(Operator op, Expression arg) {
		BinaryOperatorExpression e=new BinaryOperatorExpression(this, op, arg);
		return e ;
	}
	
	public Expression binOp(String symbol, Expression arg) {
		BinaryOperatorExpression e=new BinaryOperatorExpression(this, new LibOperator(symbol, arg.getType(), false), arg);
		return e ;
	}

	
	public Expression greaterThan(Expression other) {
		return binOp(">", other);
	}


	public DereferenceExpression dereference() {
		if(this.getType() instanceof RawPtr) {
			return new DereferenceExpression(this);
		} else {
			throw new RuntimeException("type is not a raw pointer");
		}
	}
	
//	public Expression callMethod(Cls cls, String m) {
//		return new MethodCall(this, cls.getMethod(m));
//	}
	
}
