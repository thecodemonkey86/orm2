package cpp.core;

import java.util.ArrayList;
import java.util.LinkedHashSet;

import codegen.CodeUtil;
import cpp.core.expression.Expression;
import cpp.core.expression.NewOperator;
import cpp.core.expression.StaticMethodCall;
import cpp.core.expression.ThisExpression;
import util.StringUtil;


public class Cls extends Type implements IAttributeContainer{
	protected String name;
	protected ArrayList<Method> methods;
	protected ArrayList<Constructor> constructors;
	
	protected Destructor destructor;
	protected ArrayList<Attr> attrs;
	protected LinkedHashSet<String> includes;
	protected ArrayList<Type> forwardDeclaredTypes;
	protected ArrayList<String> preprocessorInstructions;
	
	protected ArrayList<Cls> superclasses;
	protected ArrayList<NonMemberMethod> nonMemberMethods;
	protected ArrayList<Operator> nonMemberOperators;
	protected String useNamespace;
	protected String headerInclude;
	
	public void setUseNamespace(String useNamespace) {
		this.useNamespace = useNamespace;
	}
	
	public void addSuperclass(Cls superclass) {
		if(superclasses==null) {
			this.superclasses = new ArrayList<>();
		}
		this.superclasses.add(superclass);
	}
	
	public Cls getSuperclass() {
		return superclasses.get(0);
	}
	
	public Cls getSuperclass(int index) {
		return superclasses.get(index);
	}
	
	public Cls(String name) {
		super(name);
		this.methods=new ArrayList<>();
		this.attrs=new ArrayList<>();
		this.constructors=new ArrayList<>();
		this.includes=new LinkedHashSet<>();
		this.operators = new ArrayList<>(); 
		this.name=name;
		this.forwardDeclaredTypes = new ArrayList<>(); 
	}
	
	public void addInclude(String i) {
		includes.add(i);
	}
	
	public void addIncludeHeader(String i) {
		if(i==null) {
			throw new NullPointerException();
		}
		includes.add(CodeUtil.quote(i+".h"));
	}
	
	public void addIncludeLib(String i) {
		includes.add(CodeUtil.abr(i));
	}
	
	public void addIncludeLib(Type i) {
		includes.add(CodeUtil.abr(i.getName()));
	}
	
	public void addMethod(Method m) {
		methods.add(m);
		m.setParent(this);
	}
	
	
	public void addNonMemberMethod(NonMemberMethod m) {
		if (nonMemberMethods==null) nonMemberMethods=new ArrayList<>();
		nonMemberMethods.add(m);
		m.setParent(this);
	}
	
	public void addConstructor(Constructor c) {
		constructors.add(c);
		c.setParent(this);
	}
	public void addAttr(Attr a) {
		attrs.add(a);
	}
	
	
	@Override
	public Attr getAttrByName(String name) {
		if (superclasses!=null) {
			for (Cls superclass : superclasses) {
				for(Attr a:superclass.attrs ) {
					if (a.getName().equals(name)) {
						return a;
					}
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
	
	@Override
	public String getName() {
		return name;
	}
	
	protected void addHeaderCodeBeforeClassDeclaration(StringBuilder sb){
		
	}
	
	protected void addClassHeaderCode(StringBuilder sb){
		
	}
	
	protected void addBeforeSourceCode(StringBuilder sb){
		CodeUtil.writeLine(sb, "#include "+CodeUtil.quote(name.toLowerCase()+".h"));
		sb.append('\n');
	}
	
	public String toHeaderString() {
		StringBuilder sb=new StringBuilder();
		
		CodeUtil.writeLine(sb, "#ifndef "+name.toUpperCase()+"_H");
		CodeUtil.writeLine(sb, "#define "+name.toUpperCase()+"_H");
		for(Type predef:forwardDeclaredTypes) {
			CodeUtil.writeLine(sb, predef.getForwardDeclaration()+";");
		}
		for(String incl:includes) {
			CodeUtil.writeLine(sb, "#include "+incl);
		}
		if (this.preprocessorInstructions != null) {
			for(String pp:this.preprocessorInstructions) {
				CodeUtil.writeLine(sb, pp);
			}
		}
		addHeaderCodeBeforeClassDeclaration(sb);
		sb.append( "class " +name);
		if (superclasses!=null) {
			ArrayList<String> superClassDecl = new ArrayList<>();
			for (Cls superclass : superclasses) {
				superClassDecl.add("public "+superclass.getConstructorName());
			}
			
			
			sb.append(": ");
			sb.append(CodeUtil.commaSep(superClassDecl));
		}
		sb.append("{\n");
		addClassHeaderCode(sb);
		for(Attr a:attrs) {
			CodeUtil.writeLine(sb, a.toDeclarationString());
		}
		for(Constructor c:constructors) {
			CodeUtil.writeLine(sb, c.toHeaderString());
		}
		if (destructor !=null)
			CodeUtil.writeLine(sb, destructor.toHeaderString());
		for(Method m:methods) {
			if (m.getInstructions().size()>0 || m.includeIfEmpty())
				CodeUtil.writeLine(sb, m.toHeaderString());
		}
		for(Operator o:operators) {
			CodeUtil.writeLine(sb, o.toHeaderString());
		}
		
		CodeUtil.writeLine(sb, "};");
		
		if (nonMemberMethods!=null) {
			for(Method m:nonMemberMethods) {
				CodeUtil.writeLine(sb, m.toHeaderString());
			}
		} 
//			else {
//			System.out.println();
//		}
		if (nonMemberOperators!=null) {
			for(Operator op:nonMemberOperators) {
				CodeUtil.writeLine(sb, op.toHeaderString());
			}
		}
		sb.append("#endif");
		return sb.toString();
	}
	
	
	public String toSourceString() {
		StringBuilder sb=new StringBuilder();
		
		
//		for(String incl:includes) {
//			CodeUtil.writeLine(sb, "#include "+incl);
//		}
		addBeforeSourceCode(sb);
		for(Constructor c:constructors) {
			CodeUtil.writeLine(sb, c);
		}
		if (destructor !=null)
			CodeUtil.writeLine(sb, destructor);
		
		for(Method m:methods) {
			if (m.getInstructions().size()>0 || m.includeIfEmpty()) {
				CodeUtil.writeLine(sb, m);
				sb.append('\n');
			}
		}
		
		for(Operator o:operators) {
			CodeUtil.writeLine(sb, o);
		}
		
		for(Attr a:attrs) {
			if (a.isStatic())
				CodeUtil.writeLine(sb, CodeUtil.sp(a.getType().toDeclarationString(),getName()+"::"+a.getName(),"=",a.getInitValue(),";"));
		}
		if (nonMemberMethods!=null) {
			for(Method m:nonMemberMethods) {
				CodeUtil.writeLine(sb, m.toString());
			}
		} 
		if (nonMemberOperators!=null) {
			for(Operator op:nonMemberOperators) {
				CodeUtil.writeLine(sb, op.toString());
			}
		}
		return sb.toString();
	}
	
	public ArrayList<Constructor> getConstructors() {
		return constructors;
	}
	
	public void addForwardDeclaredClass(Cls cls) {
//		if(cls instanceof TplCls) {
//			throw new IllegalArgumentException();
//		}
		this.forwardDeclaredTypes.add(cls);
	}
	
	public void addForwardDeclaredClass(Struct struct) {
		this.forwardDeclaredTypes.add(struct);
	}
	
	public void setDestructor(Destructor destructor) {
		this.destructor = destructor;
		destructor.setParent(this);
	}
	
	protected Method getMethodInternal(String name) {
		for(Method m:methods) {
			if (m.getName().equals(name)) {
				return m;
			}
		}
		return null;
	}
	public Method getMethod(String name) {
		for(Method m:methods) {
			if (m.getName().equals(name)) {
				return m;
			}
		}
		if (superclasses !=null) {
			for (Cls superclass : superclasses) {
				Method m = superclass.getMethodInternal(name);
				if(m != null) {
					return m;
				}
			}
		}
		
		throw new RuntimeException("no such method "+getClass().getName()+"|"+ getName()+"::"+name);
	}
	
	public Expression newInstance(Expression...args) {
		return new NewOperator(this, args);
	}
	
	public SharedPtr toSharedPtr() {
		return sharedPtr(this);
	}
	
	public Type toUniquePointer() {
		return new UniquePtr(this);
	}
	
	public Expression callStaticMethod(String methodname,Expression...args) {
		Method m= getMethod(methodname);
		if (m.isStatic()) {
			return new StaticMethodCall(this,m,args);
		}
		throw new RuntimeException("method "+methodname+" is not static");
	}
	
	@Override
	public String toUsageString() {
		return useNamespace !=null ? useNamespace+"::"+  super.toUsageString() :  super.toUsageString();
	}

	@Deprecated
	public String getIncludeHeader() {
		return name.toLowerCase();
	}
	
	protected Attr getStaticAttributeInternal(String name) {
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
		return null;
	}
	
	public Attr getStaticAttribute(String name) {
		if (superclasses!=null) {
			for (Cls superclass : superclasses) {
				Attr a = superclass.getStaticAttributeInternal(name);
				if(a!= null) {
					return a;
				}
			}
		}
		Attr a = getStaticAttributeInternal(name);
		if(a!= null) {
			return a;
		} else {
			throw new RuntimeException("no such attribute: "+name);
		}
	}
	
	public void addNonMemberOperator(Operator op) {
		if(nonMemberOperators==null)
			nonMemberOperators = new ArrayList<>();
		nonMemberOperators.add(op);
	}
	
	public void addMethodImplementations() {
		
		for(Constructor c:constructors) {
			c.addImplementation();
		}
		
		if (destructor!=null)
			destructor.addImplementation();
		
		for (Method m : methods) {
			m.addImplementation();
		}
		
		for (Operator o : operators) {
			o.addImplementation();
		}
	}

	protected Attr getAttrInternal(Attr prototype) {
		for(Attr a:attrs ) {
			if (a.getName().equals(prototype.getName())) {
				return a;
			}
		}
		return null;
	}
	
	@Override
	public Attr getAttr(Attr prototype) {
		if (superclasses!=null) {
			for (Cls superclass : superclasses) {
				Attr a = superclass.getAttrInternal(prototype);
				if(a!=null) {
					return a;
				}
			}
		}
		return getAttrInternal(prototype);
	}
	
	public Expression _this() {
		return new ThisExpression(this);
	}
	
	public void addPreprocessorInstruction(String s) {
		if (this.preprocessorInstructions == null) {
			this.preprocessorInstructions = new ArrayList<>();
		}
		this.preprocessorInstructions.add(s);
	}
	
	public String getHeaderInclude() {
		return headerInclude;
	}
	
	public boolean hasHeaderInclude() {
		return headerInclude != null;
	}
	
	public void collectIncludes(Cls cls) {
		if(hasHeaderInclude() ) {
			cls.addInclude(getHeaderInclude());
		}
	}
	
	@Override
	public String getForwardDeclaration() {
		return CodeUtil.sp("class",getName());
	}
}
