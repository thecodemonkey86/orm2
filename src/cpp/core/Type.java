package cpp.core;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import codegen.CodeUtil;


public class Type {
	protected String type;
	protected boolean constness;
	protected List<Operator> operators;
	
	public Type(String type) {
		this.type =type;
	}

	public void setName(String type) {
		this.type = type;
	} 
	
	@Override
	public String toString() {
		return toDeclarationString();
	}
	
//	public String getQualifiedName() {
//		return type;
//	}
	
	public String toUsageString () {
		return constness ? CodeUtil.sp("const",type,isPtr()?"*":"") : isPtr()?type+"*":type;
	}
	
	public String toDeclarationString() {
		return toUsageString();
	}
	
	public String getConstructorName() {
		return type;
	}
	
	public boolean isPtr() {
		return false;
	}
	
	public boolean isPrimitiveType() {
		return false;
	}
	
	public void setConstness(boolean constness) {
		this.constness = constness;
	}
	
	public static Type nonPtr(String name) {
		return new Type(name);
	}
	
	public static Type constPtr(Type baseType) {
		Type t= new RawPtr(baseType);
		t.setConstness(true);
		return t;
	}
	
	public static RawPtr rawPtr(Type baseType) {
		return new RawPtr(baseType);
	}
	
	
//	public static Type constPtr(Type baseType) {
//		return constPtr(baseType.type);
//	}
	
	public static SharedPtr sharedPtr(Cls type) {
		return new SharedPtr(type);
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj instanceof Type) {
			Type t=(Type) obj;
			return type.equals(t.type) && t.constness == constness && isPtr() == t.isPtr();
		}
		return false;
	}

	
	@Override
	public int hashCode() {
		return Objects.hash(type, constness, isPtr());
	}
	
	public String getName() {
		return type;
	}

	public Type toRawPointer() {
		return rawPtr(this);
	}
	
	public void addOperator(Operator op) {
		if (operators == null)
			operators = new ArrayList<>();
		operators.add(op);
	}
	
	public Operator getOperator(String symbol) {
		if (operators!=null) {
			for(Operator op:operators) {
				if (op.getSymbol().equals(symbol)) {
					return op;
				}
			}
		}
		throw new RuntimeException("no such operator: "+type+"::"+symbol);
	}

	public Type toConstRef() {
		if(this instanceof RawPtr) {
			throw new RuntimeException("raw ptr");
		}
		return new ConstRef(this);
	}

	public Ref toRef() {
		return new Ref(this);
	}

	public String getForwardDeclaration() {
		throw new UnsupportedOperationException();
	}
	
	
}
