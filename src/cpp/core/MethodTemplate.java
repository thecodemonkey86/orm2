package cpp.core;

import java.util.ArrayList;
import java.util.List;

import codegen.CodeUtil;
import cpp.core.expression.Expression;
import cpp.core.instruction.Comment;
import cpp.core.instruction.Instruction;
import cpp.core.method.TplMethod;
import util.CodeUtil2;

public abstract class MethodTemplate {
	protected ArrayList<TplSymbol> tplTypes;
	protected String visibility;
	protected Type returnType;
	protected String name;
	protected boolean inlineQualifier;
	protected boolean constQualifier;
	protected Cls parent;
	protected ArrayList<Param> params;
	protected boolean isStatic;
	
	public ArrayList<Param> getParams() {
		return params;
	}
	
	public Param addParam(Param a) {
		params.add(a);
		return a;
	}
	
	public Param addParam(Type type, String name) {
		return addParam(new Param(type, name));
	}
	
	public Param addParam(Type type, String name, Expression defaultValue) {
		return addParam(new Param(type, name, defaultValue));
	}
	
	public void setParent(Cls parent) {
		this.parent = parent;
	}
	
	public void addTplType(TplSymbol e) {
		tplTypes.add(e);
	}

	public MethodTemplate(String visibility, Type returnType, String name,boolean isStatic) {
		tplTypes = new ArrayList<>();
		this.returnType = returnType;
		this.name = name;
		this.visibility = visibility;
		params = new ArrayList<>();
		this.isStatic = isStatic;
	}
	public TplMethod getConcreteMethod(Type...types) {
		TplMethod m=getConcreteMethodImpl(types);
		m.setParent(parent);
		m.setStatic(isStatic);
		return m;
	}
	
	protected abstract TplMethod getConcreteMethodImpl(Type...types) ;
	public <T> TplMethod getConcreteMethod(List<T> types) {
		Type[] t = new Type[types.size()];
		types.toArray(t);
		return getConcreteMethodImpl(t);
	}

	public String getName() {
		return name;
	}
	
	public ArrayList<TplSymbol> getTplTypes() {
		return tplTypes;
	}
	
	public Type getReturnType() {
		return returnType;
	}
	
	public String getVisibility() {
		return visibility;
	}
	
	public String toHeaderString() {
		String[] declTypeSymbols = new String[tplTypes.size()];
		for(int i=0;i<tplTypes.size();i++) {
			declTypeSymbols[i] = tplTypes.get(i).toDeclarationString();
		}
		
		ArrayList<String> params = new ArrayList<>();
		
		TplMethod concreteMethod = getConcreteMethod(tplTypes);
		
		for(Param p:concreteMethod.getParams()) {
			params.add(p.toDeclarationString());
		}
		
		concreteMethod.setParent(parent);
		concreteMethod.addImplementation();
		StringBuilder sb=new StringBuilder(CodeUtil.sp(getVisibility()+":","template"+CodeUtil.abr(CodeUtil.commaSep(tplTypes, "typename ")))+ CodeUtil.sp((inlineQualifier?"inline":null),(isStatic?"static":null), getReturnType().toDeclarationString(),getName(),CodeUtil.parentheses(CodeUtil.commaSep(params)),(constQualifier?"const":null)));
		CodeUtil.writeLine(sb, "{");
	
		for(Instruction i:concreteMethod.getInstructions()) {
			if(Instruction.isStackTraceEnabled())
				CodeUtil.writeLine(sb,new Comment(CodeUtil2.traceComment(i.getStackTrace())));
			CodeUtil.writeLine(sb,i);
		}
		sb.append("}");
		return sb.toString();
	}
}
