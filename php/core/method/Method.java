package php.core.method;

import java.util.ArrayList;
import codegen.CodeUtil;
import php.core.AbstractPhpCls;
import php.core.Param;
import php.core.Type;
import php.core.Types;
import php.core.expression.Expression;
import php.core.expression.MethodCall;
import php.core.expression.Var;
import php.core.instruction.Comment;
import php.core.instruction.Instruction;
import php.core.instruction.InstructionBlock;
import php.core.instruction.ReturnInstruction;
import util.CodeUtil2;

public abstract class Method extends InstructionBlock{
	
	protected ArrayList<Param> params;

	protected boolean isStatic;
	protected boolean isAbstract;
	protected String visibility;
	protected Type returnType;
	protected String name;
	
	public static final String Protected = "protected";
	public static final String Private = "private";
	public static final String Public = "public";
	
	
	
	public boolean isStatic() {
		return isStatic;
	}

	public Method(String visibility, Type returnType, String name) {
		super();
		this.params = new ArrayList<>();
		this.visibility = visibility;
		this.returnType = returnType;
		this.name = name;
		
	}
	
	
	
	public Param addParam(Param a) {
		params.add(a);
		return a;
	}
	
	private String retType() {
		return  getReturnType().toDeclarationString();
	}
	
	@Override
	public String toString() {
		ArrayList<String> params=new ArrayList<>();
		for(Param p:this.params) {
			params.add(p.toDeclarationString());
		}
		
		StringBuilder sb=new StringBuilder();
		
		sb.append(CodeUtil.sp(visibility,(isStatic() ? "static" : null),"function" ,name,CodeUtil.parentheses(CodeUtil.commaSep(params)), (!getReturnType().equals(Types.Void)? ": "+retType():null)));
		
		sb.append(" {\n");
		for(Instruction i:instructions) {
			if(enableStacktrace)
				CodeUtil.writeLine(sb,new Comment(CodeUtil2.traceComment(i.getStackTrace())));
			CodeUtil.writeLine(sb,i);
		}
		
		sb.append('}');
		
		
		return sb.toString();
	}
	
	public void setReturnType(Type returnType) {
		this.returnType = returnType;
	}

	public Instruction getInstr(int i) {
		return instructions.get(i);
	}
	
	public Method setStatic(boolean isStatic) {
		
		this.isStatic = isStatic;
		return this;
	}
	
	public String getName() {
		return name;
	}

	public String getLastInstruction() {
		return instructions.get(instructions.size()-1).toString();
		
	}
	
	public Param paramByName(String name) {
		for(Param p:params) {
			if (p.getName().equals(name)) {
				return p;
			}
		}
		throw new RuntimeException("no such param "+name);
	}

	public ArrayList<Param> getParams() {
		return params;
	}

	public void addParams(ArrayList<Param> p) {
		params.addAll(p);
	}
	
	public ArrayList<Instruction> getInstructions() {
		return instructions;
	}
	
	public Expression call(Var var, Expression...args) {
		return new MethodCall(var, this, args);
	}

	public Type getReturnType() {
		return returnType;
	}
	
	
	public String getVisibility() {
		return visibility;
	}
	
	public AbstractPhpCls getParent() {
		return parent;
	}
	
	public abstract void addImplementation();
	
	
	
	public Param getParam(String name) {
		for(Param p:params) {
			if(p.getName().equals(name)) {
				return p;
			}
		}
		throw new RuntimeException("Param not found: "+name);
	}

	public boolean includeIfEmpty() {
		return false;
	}
	
	@Override
	public Expression _this() {
		if (isStatic())
			throw new RuntimeException("this in static method");
		return super._this();
	}

	public boolean isAbstract() {
		return isAbstract;
	}
	
	public void setAbstract(boolean isAbstract) {
		this.isAbstract = isAbstract;
	}
	
	
	public void _return() {
		addInstr(new ReturnInstruction());
	}
	@Override
	public void _return(Expression ret) {
		if (returnType != null && returnType.equals(Types.Void)) {
			throw new RuntimeException("Void methods cannot return a value");
		}
		super._return(ret);
	}

	
}
