package sunjava.core;

import java.util.ArrayList;
import java.util.List;

import codegen.CodeUtil;
import util.CodeUtil2;

public class JavaInterface extends AbstractJavaCls {

	protected List<JavaInterface> superInterfaces;
	
	public JavaInterface(String name, String pkg) {
		super(name, pkg);
	}
	
	public String toSourceString() {
		StringBuilder sb=new StringBuilder();
		
		if (pkg != null && pkg.length() > 0) {
			CodeUtil2.writeLine(sb , CodeUtil.sp("package",pkg)+";");
		}
		sb.append(CodeUtil2.NEWLINE);
		for(String imp:imports) {
			CodeUtil2.writeLine(sb, CodeUtil.sp("import",imp+";"));
		}
		
		sb.append(CodeUtil.sp("public", "interface",type)).append(' ');
		
		if (superInterfaces != null) {
			sb.append(CodeUtil.sp("extends", superInterfaces.get(0).toDeclarationString()));
			for(int i= 1; i< superInterfaces.size(); i++) {
				sb.append(',').append( superInterfaces.get(i).toDeclarationString());
			}
			
		}
		
		sb.append("{\n");
		
		
		for(Method m:methods) {
			if (!m.isAbstract()) {
				throw new RuntimeException("method is not abstract: " + m.getName());
			}
			if (m.getInstructions().size()>0 || m.includeIfEmpty()) {
				CodeUtil2.writeLine(sb, m);
				sb.append(CodeUtil2.NEWLINE);
			}
		}
		
		sb.append('}');
		
		return sb.toString();
	}

	public void addSuperInterface(JavaInterface i) {
		if (this.superInterfaces == null) {
			this.superInterfaces = new ArrayList<>();
		}
		this.superInterfaces.add(i);
	}
}
