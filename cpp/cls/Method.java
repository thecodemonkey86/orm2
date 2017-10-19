package cpp.cls;

import generate.CodeUtil2;

import java.util.ArrayList;

import codegen.CodeUtil;
import cpp.cls.expression.Expression;
import cpp.cls.expression.Var;
import cpp.cls.instruction.Comment;
import cpp.cls.instruction.InstructionBlock;
import cpp.cls.instruction.Instruction;

public abstract class Method extends InstructionBlock{
	
	protected ArrayList<Param> params;

	protected boolean isStatic;
	protected boolean constQualifier;
	protected boolean inlineQualifier;
	protected boolean virtualQualifier;
	
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
	
	public Method setConstQualifier(boolean constQualifier) {
		this.constQualifier = constQualifier;
		return this;
	}
	
	public Method setInlineQualifier(boolean inlineQualifier) {
		this.inlineQualifier = inlineQualifier;
		return this;
	}
	
	public Method setVirtualQualifier(boolean virtualQualifier) {
		if (isStatic && virtualQualifier) {
			throw new RuntimeException("Static methods cannot be virtual");
		}
		this.virtualQualifier = virtualQualifier;
		return this;
	}
	
	public Param addParam(Param a) {
		params.add(a);
		return a;
	}
	
	private String retType() {
		return getReturnType() instanceof SharedPtr ? "shared_ptr"+CodeUtil.abr(((SharedPtr)getReturnType()).getElementType().toUsageString()) : getReturnType().toDeclarationString();
	}
	
	
	
	@Override
	public String toString() {
		ArrayList<String> params=new ArrayList<>();
		for(Param p:this.params) {
			params.add(p.toSourceString());
		}
		
		StringBuilder sb=new StringBuilder(CodeUtil.sp(retType(),getParent().getName()+"::"+name,CodeUtil.parentheses(CodeUtil.commaSep(params)),(constQualifier?"const":null),"{\n"));
		for(Instruction i:instructions) {
			CodeUtil.writeLine(sb,new Comment(CodeUtil2.traceComment(i.getStackTrace())));
			CodeUtil.writeLine(sb,i);
		}
		sb.append("}");
		
		
		return sb.toString();
	}
	

	public String toHeaderString() {
		ArrayList<String> params=new ArrayList<>();
		for(Param p:this.params) {
			params.add(p.toDeclarationString());
		}
		return CodeUtil.sp(getVisibility()+":",(inlineQualifier?"inline":null), (isStatic?"static":(virtualQualifier?"virtual":null)),retType() ,getName(),CodeUtil.parentheses(CodeUtil.commaSep(params)),(constQualifier?"const":null), ";");
	}
	
	public void setReturnType(Type returnType) {
		this.returnType = returnType;
	}

	public Instruction getInstr(int i) {
		return instructions.get(i);
	}
	
	public Method setStatic(boolean isStatic) {
		if (virtualQualifier && isStatic) {
			throw new RuntimeException("virtual methods cannot be static");
		}
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
	
	public void setParent(Cls parent) {
		this.parent = parent;
	}
	
	public String getVisibility() {
		return visibility;
	}
	
	public Cls getParent() {
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
	
	
}
