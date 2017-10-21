package cpp;

import java.util.ArrayList;

import codegen.CodeUtil;
import cpp.core.Attr;
import cpp.core.Type;

public class Struct extends Type implements IAttributeContainer  {
	protected ArrayList<Attr> attrs;
//	protected String scope;
	
	public Struct(String name) {
		super(name);
		attrs = new ArrayList<>();
	}
	
	public boolean addAttr(Attr e) {
		return attrs.add(e);
	}
	public ArrayList<Attr> getAttrs() {
		return attrs;
	}
	
	@Override
	public String toUsageString() {
		return type;
	}
		
	@Override
	public String toDeclarationString() {
		return CodeUtil.sp("struct",type+';');
	}
	
	public String toSourceString() {
		StringBuilder sb=new StringBuilder(CodeUtil.sp("struct",type,"{\n"));
		for(Attr a:attrs) {
			sb.append(CodeUtil.sp(a.getType().toUsageString(), a.getName())).append(";\n");
		}
		return sb.append("};").toString();
	}
	
	@Override
	public String toString() {
		return toDeclarationString();
	}
	
//	public void setScope(String scope) {
//		this.scope = scope;
//	}

	@Override
	public Attr getAttrByName(String name) {
		for(Attr a:attrs) {
			if (a.getName().equals(name)) {
				return a;
			}
		}
		throw new RuntimeException("not such attribute "+name);
	}

	@Override
	public Attr getAttr(Attr prototype) {
		for(Attr a:attrs) {
			if (a.getName().equals(prototype.getName())) {
				return a;
			}
		}
		throw new RuntimeException("not such attribute "+prototype.getName());
	} 
	
}
