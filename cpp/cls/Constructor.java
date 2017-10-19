package cpp.cls;

import java.util.ArrayList;

import codegen.CodeUtil;
import cpp.cls.expression.Expression;

public abstract class Constructor extends Method {
	
	protected ArrayList<Expression> passToSuperConstructor;
	
	public Constructor() {
		super(Public,null,null);
		params = new ArrayList<>();
		instructions = new ArrayList<>();
		
	}
	
	public void addPassToSuperConstructor(Expression p) {
		if (passToSuperConstructor==null)
			passToSuperConstructor=new ArrayList<>();
		this.passToSuperConstructor.add(p);
	}
	
	@Override
	public String toString() {
		ArrayList<String> params=new ArrayList<>();
		for(Param p:this.params) {
			params.add(p.toDeclarationString());
		}
		
		StringBuilder sb=new StringBuilder(CodeUtil.sp(parent.getName()+"::"+parent.getName(),CodeUtil.parentheses(CodeUtil.commaSep(params))
				));
		
		if (passToSuperConstructor!=null) {
			
			sb.append(CodeUtil.sp(':',parent.getSuperclass().getName()+CodeUtil.parentheses(CodeUtil.commaSep(passToSuperConstructor))));
		}
			
			sb.append("{\n");
		for(Object i:instructions) {
			CodeUtil.writeLine(sb,i);
		}
		sb.append("}");
		
		
		return sb.toString();
	}
	
	@Override
	public String toHeaderString() {
		ArrayList<String> params=new ArrayList<>();
		for(Param p:this.params) {
			params.add(p.toDeclarationString());
		}
		return CodeUtil.sp("public:", parent.getName(),CodeUtil.parentheses(CodeUtil.commaSep(params)),";");
	}

}
