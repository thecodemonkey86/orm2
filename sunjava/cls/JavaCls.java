package sunjava.cls;

import java.util.ArrayList;
import java.util.List;

import codegen.CodeUtil;
import sunjava.IAttributeContainer;
import sunjava.Types;
import sunjava.cls.expression.Expression;
import sunjava.cls.expression.NewOperator;
import sunjava.cls.expression.StaticAccessExpression;
import sunjava.cls.expression.StaticMethodCall;
import sunjava.lib.LibMethod;


public class JavaCls extends AbstractJavaCls implements IAttributeContainer{
	
	protected ArrayList<Constructor> constructors;
	
	protected ArrayList<Attr> attrs;
	
	
	protected JavaCls superclass;
	protected List<JavaInterface> interfaces;
	protected boolean isAbstract;
	
	public void setAbstract(boolean isAbstract) {
		this.isAbstract = isAbstract;
	}
	
	public boolean isAbstract() {
		return isAbstract;
	}
	
	public void setSuperclass(JavaCls superclass) {
		this.superclass = superclass;
		superclass.collectImports(this);
	}
	
	public JavaCls getSuperclass() {
		return superclass;
	}
	
	
	
	public JavaCls(String name,String pkg) {
		super(name,pkg);
		
		this.attrs=new ArrayList<>();
		this.constructors=new ArrayList<>();
		this.operators = new ArrayList<>(); 
		
		addMethod(new LibMethod(Types.Bool, "equals"));
		addMethod(new LibMethod(Types.Int, "hashCode"));
		
	}
	
	
	public void addConstructor(Constructor c) {
		constructors.add(c);
		c.setParent(this);
	}
	public Attr addAttr(Attr a) {		
		attrs.add(a);
		a.getType().collectImports(this);
		return a;
	}
	
	
	@Override
	public Attr getAttrByName(String name) {
		if (superclass!=null) {
			for(Attr a:superclass.attrs ) {
				if (a.getName().equals(name)) {
					return a;
				}
			}
		}
		for(Attr a:attrs ) {
			if (a.getName().equals(name)) {
				return a;
			}
		}
		System.out.println(getName()+"::"+ name);
		throw new IllegalArgumentException("no such attribute: " +name);
	}
	
	
	protected void addHeaderCodeBeforeClassDefinition(StringBuilder sb){
		
	}
	
	protected void addClassHeaderCode(StringBuilder sb){
		
	}
	
	
	public String toSourceString() {
		StringBuilder sb=new StringBuilder();
		
		if (pkg != null && pkg.length() > 0) {
			CodeUtil.writeLine(sb , CodeUtil.sp("package",pkg)+";");
		}
		sb.append('\n');
		for(String imp:imports) {
			CodeUtil.writeLine(sb, CodeUtil.sp("import",imp)+";");
		}
		
		sb.append(CodeUtil.sp("public", "class",getName())).append(' ');
		
		if (superclass != null) {
			sb.append(CodeUtil.sp("extends" ,superclass.toDeclarationString()));
		}
		
		if (interfaces != null) {
			sb.append(CodeUtil.sp("implements", interfaces.get(0).toDeclarationString()));
			for(int i= 1; i< interfaces.size(); i++) {
				sb.append(',').append( interfaces.get(i).toDeclarationString());
			}
			
		}
		
		sb.append("{\n");
		
		for(Attr a:attrs) {
			CodeUtil.writeLine(sb, a.toDeclarationString());
		}
		
		for(Constructor c:constructors) {
			CodeUtil.writeLine(sb, c);
		}
		
		for(Method m:methods) {
			if (m.getInstructions().size()>0 || m.includeIfEmpty()) {
				CodeUtil.writeLine(sb, m);
				sb.append('\n');
			}
		}
		
		sb.append('}');
		
		return sb.toString();
	}
	
	public ArrayList<Constructor> getConstructors() {
		return constructors;
	}
	
	public Method getStaticMethod(String name) {
		for(Method m:methods) {
			if (m.getName().equals(name) && m.isStatic()) {
				return m;
			}
		}
		if (superclass !=null)
		for(Method m:superclass.methods) {
			if (m.getName().equals(name) && m.isStatic()) {
				return m;
			}
		}
		throw new RuntimeException("no such method "+getClass().getName()+"|"+ getName()+"::"+name);
	}
	
	@Override
	public Method getMethod(String name) {
		for(Method m:methods) {
			if (m.getName().equals(name)) {
				return m;
			}
		}
		if (superclass !=null) {
			return superclass.getMethod(name);
		}
		
		throw new RuntimeException("no such method "+getClass().getName()+"|"+ getName()+"::"+name);
	}
	
	public Expression newInstance(Expression...args) {
		return new NewOperator(this, args);
	}
	
	
	public Expression callStaticMethod(String methodname,Expression...args) {
		Method m= getMethod(methodname);
		if (m.isStatic()) {
			return new StaticMethodCall(this,m,args);
		}
		throw new RuntimeException("method "+methodname+" is not static");
	}
	
	
	public Attr getStaticAttribute(String name) {
		if (superclass!=null)
		for(Attr a:superclass.attrs) {
			if (a.getName().equals(name)) {
				if (!a.isStatic()) {
					throw new RuntimeException("attribute is not static: "+name);
				}
//				if (!a.isPublic()) {
//					throw new RuntimeException("attribute is not public: "+name);
//				}
				return a;
			}
		}
		for(Attr a:attrs) {
			if (a.getName().equals(name)) {
				if (!a.isStatic()) {
					throw new RuntimeException("attribute is not static: "+name);
				}
//				if (!a.isPublic()) {
//					throw new RuntimeException("attribute is not public: "+name);
//				}
				return a;
			}
		}
		throw new RuntimeException("no such attribute: "+name);
	}
	
	
	public void addMethodImplementations() {
		
		for(Constructor c:constructors) {
			c.setParent(this);
			c.addImplementation();
			c.collectImports(this);
		}
		
		
		for (Method m : methods) {
			if(m.getName().equals("setEntity1Internal")) {
				System.out.println();
			}
			m.setParent(this);
			m.addImplementation();
			
			if(m.getName().equals("getCartEntryById")) {
				System.out.println();
			}
			
			m.collectImports(this);
		}
		
		for (Operator o : operators) {
			o.addImplementation();
		}
	}

	@Override
	public Attr getAttr(Attr prototype) {
		if (superclass!=null) {
			for(Attr a:superclass.attrs ) {
				if (a.getName().equals(prototype.getName())) {
					return a;
				}
			}
		}
		for(Attr a:attrs ) {
			if (a.getName().equals(prototype.getName())) {
				return a;
			}
		}
		return null;
	}
	
	public Expression _this() {
		return new Expression() {
			
			@Override
			public String toString() {
				return "this";
			}
			
			@Override
			public Type getType() {
				return JavaCls.this;
			}
		};
	}

	public StaticAccessExpression accessStaticAttribute(Attr prototype) {
		for(Attr a:attrs ) {
			if (a.getName().equals(prototype.getName())) {
				if (!a.isStatic()) {
					throw new RuntimeException("attribute " + a.getName()+" is not static");
				}
				return new StaticAccessExpression(this, a);
			}
		}
		if (superclass!=null) {
			return superclass.accessStaticAttribute(prototype);
		} else {
			throw new RuntimeException("no such attribute " + prototype.getName());
		}
	}

}
