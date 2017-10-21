package sunjava.core;

import generate.CodeUtil2;

import java.util.ArrayList;
import java.util.List;

import codegen.CodeUtil;
import sunjava.core.expression.Expression;
import sunjava.core.expression.Var;
import sunjava.core.instruction.Comment;
import sunjava.core.instruction.Instruction;
import sunjava.core.instruction.InstructionBlock;
import sunjava.core.instruction.ReturnInstruction;
import sunjava.lib.ClsException;

public abstract class Method extends InstructionBlock{
	
	protected ArrayList<Param> params;

	protected boolean isStatic;
	protected boolean isAbstract;
	protected String visibility;
	protected Type returnType;
	protected String name;
	protected boolean overrideAnnotation;
	protected List<ClsException> throwsExceptions;
	
	public static final String Protected = "protected";
	public static final String Private = "private";
	public static final String Public = "public";

	@Override
	public void collectImports(AbstractJavaCls cls) {
		for(Param p : params) {
			p.getType().collectImports(cls);
		}
		if (throwsExceptions != null) {
			for(ClsException e : throwsExceptions) {
				e.collectImports(cls);
			}
		}
		returnType.collectImports(cls);
		super.collectImports(cls);
	}
	
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
	
	public void setOverrideAnnotation(boolean overrideAnnotation) {
		this.overrideAnnotation = overrideAnnotation;
	}
	
	@Override
	public String toString() {
		ArrayList<String> params=new ArrayList<>();
		for(Param p:this.params) {
			params.add(p.toSourceString());
		}
		
		StringBuilder sb=new StringBuilder();
		
		if(overrideAnnotation) {
			CodeUtil.writeLine(sb,"@Override");
		}
		sb.append(CodeUtil.sp(visibility,(isStatic() ? "static" : null), retType(),name,CodeUtil.parentheses(CodeUtil.commaSep(params))));
		
		if (throwsExceptions()) {
			sb.append(' ').append(CodeUtil.sp("throws", throwsExceptions.get(0).toDeclarationString()));
			for(int i=1;i<throwsExceptions.size();i++) {
				sb.append(',').append(throwsExceptions.get(i));
			}
		}
		sb.append(" {\n");
		for(Instruction i:instructions) {
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
	
	public AbstractJavaCls getParent() {
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
	
	public void addThrowsException(ClsException ex) {
		if (this.throwsExceptions == null) {
			this.throwsExceptions = new ArrayList<>();
		}
		this.throwsExceptions.add(ex);
	}
	
	public List<ClsException> getThrowsExceptions() {
		return throwsExceptions;
	}
	
	public boolean throwsExceptions() {
		return throwsExceptions != null && !throwsExceptions.isEmpty();
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

//	public void addImport(Type type ) {
//		if (type == null) {
//			throw new NullPointerException();
//		}
//		if (type.hasImport()) {
//			if (imports == null) {
//				imports = new ArrayList<>();
//			}
//			imports.add(type.getImport());
//		}
//	}
//	
//	public void addImport(String i ) {
//		if (imports == null) {
//			imports = new ArrayList<>();
//		}
//		imports.add(i);
//	}
//	
//	public List<String> getImports() {
//		return imports;
//	}
//	
//	public boolean hasImports() {
//		return imports != null && !imports.isEmpty();
//	}
	
}
