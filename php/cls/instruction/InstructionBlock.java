package php.cls.instruction;

import java.util.ArrayList;
import java.util.Iterator;

import php.cls.method.MethodAttributeSetter;
import php.cls.expression.BinaryOperatorExpression;
import php.PhpArray;
import php.Types;
import php.cls.AbstractPhpCls;
import php.cls.Attr;
import php.cls.PhpCls;
import php.cls.Method;
import php.cls.MethodCall;
import php.cls.Type;
import php.cls.expression.AccessExpression;
import php.cls.expression.ArrayInitExpression;
import php.cls.expression.Expression;
import php.cls.expression.Expressions;
import php.cls.expression.InlineIfExpression;
import php.cls.expression.NewOperator;
import php.cls.expression.Operators;
import php.cls.expression.StaticMethodCall;
import php.cls.expression.ParentExpression;
import php.cls.expression.ThisExpression;
import php.cls.expression.Var;

public class InstructionBlock extends Instruction implements Iterable<Instruction> {
	protected ArrayList<Instruction> instructions;
	protected AbstractPhpCls parent;
	protected static boolean enableStacktrace;
	
	public static void setEnableStacktrace(boolean enableStacktrace) {
		Method.enableStacktrace = enableStacktrace;
	}
	/*private static int indent=0;
	
	public static int getIndent() {
		return indent;
	}
	
	public static void increaseIndent() {
		indent++;
	}
	public static void decreaseIndent() {
		indent--;
	}*/
	
	
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
	 * @deprecated declaration without initialisation is useful only in some cases in Php. Recommended to use _declareInitDefaultConstructor instead
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
		addInstr(new MethodCallInstruction(new MethodCall(expression, ((AbstractPhpCls) expression.getType()).getMethod(method), args)));
	}
	
	public Expression _inlineIf(Expression condition, Expression ifExpression,
			Expression elseExpression) {
		return new InlineIfExpression(condition, ifExpression, elseExpression);
	}
	
	public void _arrayPush(Expression array, Expression expression) {
		addInstr(new BinaryOperatorExpression(array, Operators.ARRAY_PUSH, expression).asInstruction());
	}
	public void _arraySet(Expression array, Expression arrayIndex, Expression expression) {
		addInstr(new AssignInstruction(array.arrayIndex(arrayIndex),expression));
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
		return new ParentExpression((PhpCls) InstructionBlock.this.parent);
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
	

	public void callStaticMethodInstr(PhpCls cls,	String methodName, Expression ...args) {
		
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

	public void setParent(AbstractPhpCls parent) {
		this.parent = parent;
	}

	public Var _declareNewArray(PhpArray array, String varName, Expression...expressions) {
		return _declare(array, varName, new ArrayInitExpression(expressions));
	}
	public Var _declareNewArray(String varName, Expression...expressions) {
		return _declare(Types.array(Types.Mixed), varName, new ArrayInitExpression(expressions));
	}
}
