package cpp.core.instruction;

import java.util.ArrayList;
import java.util.Iterator;

import cpp.core.Attr;
import cpp.core.Cls;
import cpp.core.Method;
import cpp.core.MethodCall;
import cpp.core.SharedPtr;
import cpp.core.Type;
import cpp.core.expression.AccessExpression;
import cpp.core.expression.Expression;
import cpp.core.expression.Expressions;
import cpp.core.expression.InlineIfExpression;
import cpp.core.expression.MakeSharedExpression;
import cpp.core.expression.NewOperator;
import cpp.core.expression.StdMoveExpression;
import cpp.core.expression.ThisExpression;
import cpp.core.expression.Var;

public class InstructionBlock extends Instruction implements Iterable<Instruction> {
	protected ArrayList<Instruction> instructions;
	protected Cls parent;
	static int indent=0;
	
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
	
	public Var _declareInitConstructor(Type type, String varName, Expression init ) {
		Var var = new Var(type, varName);
		addInstr(new DeclareInitConstructInstruction(var, init));
		return var;
	}
	
	public Var _declare(Type type, String varName) {
		Var var = new Var(type, varName);
		addInstr(new DeclareInstruction(var, null));
		return var;
	}
	
	public void _declare(Var var,	Expression init) {
		addInstr(new DeclareInstruction(var, init));
	}
	
	public Var _declareNewRaw(Type type, String varName, Expression...args) {
		Var var = new Var(type, varName);
		addInstr(new DeclareInstruction(var, new NewOperator(type,args)));
		return var;
	}
	
	public Var _declareMakeShared(Cls type, String varName,Expression...args) {
		SharedPtr sp = type.toSharedPtr();
		return _declare(sp, varName, new MakeSharedExpression(sp, args));
	}
	
	public void _callMethodInstr(Expression expression, Method method,Expression...args) {
		addInstr(new MethodCallInstruction(new MethodCall(expression, method, args)));
	}
	
	public void _callMethodInstr(Expression expression, String method,Expression...args) {
		addInstr(new MethodCallInstruction(new MethodCall(expression, ((Cls) expression.getType()).getMethod(method), args)));
	}
	
	public Expression _inlineIf(Expression condition, Expression ifExpression,
			Expression elseExpression) {
		return new InlineIfExpression(condition, ifExpression, elseExpression);
	}
	
	public StdMoveExpression _stdMove(Expression ex) {
		return new StdMoveExpression(ex);
	}
	
	public IfBlock _if(Expression condition) {
		IfBlock ifBlock = IfBlock.create().setCondition(condition);
		addInstr(ifBlock);
		return ifBlock;
	}
	
	public Expression _nullptr() {
		return Expressions.Nullptr;
	}
	
	public Expression _this() {
		return new ThisExpression(InstructionBlock.this.parent);
	}

	@Override
	public Iterator<Instruction> iterator() {
		return instructions.iterator();
	}

	public boolean isEmpty() {
		return instructions.isEmpty();
	}
	


}
