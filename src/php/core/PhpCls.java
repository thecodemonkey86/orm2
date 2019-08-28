package php.core;

import java.util.ArrayList;
import java.util.List;

import codegen.CodeUtil;
import php.core.expression.ConstantAccessExpression;
import php.core.expression.Expression;
import php.core.expression.NewOperator;
import php.core.expression.StaticAccessExpression;
import php.core.expression.StaticMethodCall;
import php.core.expression.ThisExpression;
import php.core.method.Method;


public class PhpCls extends AbstractPhpCls implements IAttributeContainer{
	
	protected Constructor constructor;
	
	protected ArrayList<Attr> attrs;
	protected ArrayList<ClassConstant> constants;
	
	
	protected PhpCls superclass;
	protected List<PhpInterface> interfaces;
	protected boolean isAbstract;
	
	public void setAbstract(boolean isAbstract) {
		this.isAbstract = isAbstract;
	}
	
	public boolean isAbstract() {
		return isAbstract;
	}
	
	public void setSuperclass(PhpCls superclass) {
		this.superclass = superclass;
	}
	
	public PhpCls getSuperclass() {
		return superclass;
	}
	
	
	
	public PhpCls(String name,String namespace) {
		super(name,namespace);
		
		this.attrs=new ArrayList<>();
		this.operators = new ArrayList<>(); 
				
	}
	
	
	public void setConstructor(Constructor c) {
		this.constructor = c;
		c.setParent(this);
	}
	public Attr addAttr(Attr a) {		
		attrs.add(a);
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
		StringBuilder sb=new StringBuilder("<?php\n");
		
		if (namespace != null && namespace.length() > 0) {
			CodeUtil.writeLine(sb , CodeUtil.sp("namespace",namespace.substring(1))+";");
		}
		sb.append('\n');
		sb.append(CodeUtil.sp("class",getName())).append(' ');
		
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
		
		if(constants!=null)
		for(ClassConstant c:constants) {
			CodeUtil.writeLine(sb, c.toDeclarationString());
		}
		
		CodeUtil.writeLine(sb, constructor);
		
		for(Method m:methods) {
			if (m.getInstructions().size()>0 || m.includeIfEmpty()) {
				CodeUtil.writeLine(sb, m);
				sb.append('\n');
			}
		}
		sb.append('}');
		
		return sb.toString();
	}
	
	public Constructor getConstructor() {
		return constructor;
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
		
		constructor.setParent(this);
		constructor.addImplementation();
		
		
		for (Method m : methods) {
			if(m.getName().equals("setEntity1Internal")) {
				System.out.println();
			}
			m.setParent(this);
			m.addImplementation();
			
			if(m.getName().equals("getCartEntryById")) {
				System.out.println();
			}
			
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
		return new ThisExpression(this);
	}

	public void addConstant(ClassConstant c) {
		if(constants == null) {
			constants = new ArrayList<>();
		}
		constants.add(c);
	}
	
	public Expression accessConstant(String constant) {
		if(constants!=null) {
			for(ClassConstant c:constants) {
				if (c.getName().equals(constant)) {
					return new ConstantAccessExpression(this, c);
				}
			}
		}
		if(superclass != null) {
			return superclass.accessConstant(constant);
		} else {
			throw new RuntimeException("no such constant " + constant);
		}
		
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

	@Override
	public String toDeclarationString() {
		if(namespace == null) {
			return super.toDeclarationString();
		} else {
			String fullyQualifiedName = namespace.equals("\\") ? "" : namespace;
			if(!fullyQualifiedName.endsWith("\\")) {
				fullyQualifiedName = fullyQualifiedName + '\\'; 
			}
			fullyQualifiedName = fullyQualifiedName + type;
			return fullyQualifiedName;
		}
		 
	}

	@Override
	public NullableType toNullable() {
		return new NullableType(this, namespace);
	}

	
}
