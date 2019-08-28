package sunjava.core.instruction;

import java.util.ArrayList;
import java.util.Iterator;

import sunjava.core.AbstractJavaCls;
import sunjava.core.Attr;
import sunjava.core.JavaCls;
import sunjava.core.Method;
import sunjava.core.MethodCall;
import sunjava.core.Type;
import sunjava.core.expression.AccessExpression;
import sunjava.core.expression.Expression;
import sunjava.core.expression.Expressions;
import sunjava.core.expression.InlineIfExpression;
import sunjava.core.expression.NewOperator;
import sunjava.core.expression.StaticMethodCall;
import sunjava.core.expression.SuperExpression;
import sunjava.core.expression.ThisExpression;
import sunjava.core.expression.Var;
import sunjava.core.method.MethodAttributeSetter;

public class InstructionBlock extends Instruction implements Iterable<Instruction> {
	protected ArrayList<Instruction> instructions;
	protected AbstractJavaCls parent;
	
	
	public InstructionBlock() {
		instructions = new ArrayList<>();
	}

	public boolean addInstr(Instruction e) {
		return instructions.add(e);
	}
	
	public ForeachLoop _foreach(Var var, Expression collection) {
		ForeachLoop f = new ForeachLoop(var, collection);
		this.addInstr(f);
		return f;
	}
	
	public DoWhile _doWhile() {
		DoWhile doWhile = new DoWhile();
		addInstr(doWhile);
		return doWhile; 
	}
	
	public While _while(Expression condition) {
		While whileBlock = While.create().setCondition(condition);
		addInstr(whileBlock);
		return whileBlock; 
	}
	
	public AccessExpression _accessThis(Attr a) {
		return new AccessExpression(_this(), a);
	}
	
	public Instruction _assignInstruction(Expression assign, Expression value) {
		return new AssignInstruction(assign, value);
	}
	
	public void _assign(Expression var, Expression value) {
		addInstr(new AssignInstruction(var, value));
	}
	
	public void _return(Expression ret) {
		addInstr(Instructions._return(ret));
	}
	
	public Var _declare(Type type, String varName, Expression init ) {
		Var var = new Var(type, varName);
		addInstr(new DeclareInstruction(var, init));
		return var;
	}
	
	
	/**
	 * @deprecated declaration without initialisation is useful only in some cases in Java. Recommended to use _declareInitDefaultConstructor instead
	 * @param type
	 * @param varName
	 * @return
	 */
	public Var _declare(Type type, String varName) {
		Var var = new Var(type, varName);
		addInstr(new DeclareInstruction(var, null));
		return var;
	}
	
	/**
	 * @param type
	 * @param varName
	 * @return
	 */
	public Var _declareInitDefaultConstructor(Type type, String varName) {
		Var var = new Var(type, varName);
		addInstr(new DeclareInstruction(var, new NewOperator(type)));
		return var;
	}
	
	public void _declare(Var var,	Expression init) {
		addInstr(new DeclareInstruction(var, init));
	}
	
	public Var _declareNew(Type type, String varName, Expression...args) {
		Var var = new Var(type, varName);
		addInstr(new DeclareInstruction(var, new NewOperator(type,args)));
		return var;
	}
	
	
	public void _callMethodInstr(Expression expression, Method method,Expression...args) {
		addInstr(new MethodCallInstruction(new MethodCall(expression, method, args)));
	}
	
	public void _callMethodInstr(Expression expression, String method,Expression...args) {
		addInstr(new MethodCallInstruction(new MethodCall(expression, ((AbstractJavaCls) expression.getType()).getMethod(method), args)));
	}
	
	public Expression _inlineIf(Expression condition, Expression ifExpression,
			Expression elseExpression) {
		return new InlineIfExpression(condition, ifExpression, elseExpression);
	}
	
	
	public IfBlock _if(Expression condition) {
		IfBlock ifBlock = IfBlock.create().setCondition(condition);
		ifBlock.thenBlock().setParent(parent);
		addInstr(ifBlock);
		return ifBlock;
	}
	public Expression _not(Expression expression) {
		return Expressions.not(expression);
	}
	public Expression _null() {
		return Expressions.Null;
	}
	
	public Expression _this() {
		return new ThisExpression(InstructionBlock.this.parent);
	}

	public Expression _super() {
		return new SuperExpression((JavaCls) InstructionBlock.this.parent);
	}
	
	@Override
	public Iterator<Instruction> iterator() {
		return instructions.iterator();
	}

	public boolean isEmpty() {
		return instructions.isEmpty();
	}


	public Var getDeclaredVariable(String name) {
		for(Instruction i : instructions) {
			if(i instanceof DeclareInstruction) {
				Var v = ((DeclareInstruction)i).getVar();
				if(v.getName().equals(name))
					return v;
			}
		}
		throw new RuntimeException("no such variable declared: "+name);
	}
	
	@Override
	public void collectImports(AbstractJavaCls cls) {
		for(Instruction i : instructions) {
			i.collectImports(cls);
		}
	}

	public void callStaticMethodInstr(JavaCls cls,	String methodName, Expression ...args) {
		
		addInstr(new StaticMethodCall(cls, cls.getMethod(methodName), args).asInstruction());
		
	}
	
	public MethodCall callAttrSetter(Attr a, Expression ...args) {
		return new MethodCall(_this(),  new MethodAttributeSetter(a), args);
	}
	
	public TryCatchBlock _tryCatch() {
		TryCatchBlock tryCatch = new TryCatchBlock();
		this.addInstr(tryCatch);
		return tryCatch;
	}

	public void setParent(AbstractJavaCls parent) {
		this.parent = parent;
	}
}
