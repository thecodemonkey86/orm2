package php.cls;

import java.util.List;


public class Type {
	protected String type;
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
	
	/*@Override
	public String toString() {
		return toDeclarationString();
	}
	
//	public String getQualifiedName() {
//		return type;
//	}
	
	public String toUsageString () {
		return type;
	}*/
	
	public String toDeclarationString() {
		return type;
	}
	
	public String getConstructorName() {
		return type;
	}
	
	public boolean isPrimitiveType() {
		return false;
	}
	
	
	
	@Override
	public boolean equals(Object obj) {
		if (obj instanceof Type) {
			Type t=(Type) obj;
			return type.equals(t.type) ;
		}
		return false;
	}

	
	@Override
	public int hashCode() {
		return type.hashCode();
	}
	
	public String getName() {
		return type;
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

	public Ref toRef() {
		return new Ref(this);
	}

	public String getSprintfType() {
		return "%s";
	}
	
}
