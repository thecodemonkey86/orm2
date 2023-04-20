package cpp.core.expression;

import codegen.CodeUtil;
import cpp.core.Cls;
import cpp.core.Type;
import cpp.core.instruction.AssignInstruction;
import cpp.core.instruction.DeclareInstruction;
import cpp.core.instruction.Instruction;

public class Var extends Expression{
	protected Type type;
	protected String name;
	protected String ifDef,ifNDef;
	
	public Var(Type type, String name) {
		this.type = type;
		this.name = name;
	}

	public String getName() {
		return name;
	}
	
//	@Deprecated 
//	public String toSourceString() {
//		return CodeUtil.sp(type.toDeclarationString(),getName());
//	}
	
	@Override
	public String toString() {
		return name;
	}
	
//	@Override
//	public String getWriteAccessString() {
//		return name;
//	}
//	
//	@Override
//	public String getReadAccessString() {
//		return type instanceof SharedPtr && ((SharedPtr)type).isWeak() ? name+".lock()" : name;
//	}
	
	public String toDeclarationString() {
		if(ifDef!=null) {
			return "#ifdef "+ifDef+"\r\n"+CodeUtil.sp(type.toDeclarationString(),name)+"\r\n#endif\r\n";
		} else if(ifNDef!=null) {
			return "#ifndef "+ifNDef+"\r\n"+CodeUtil.sp(type.toDeclarationString(),name)+"\r\n#endif\r\n";
		}
		return CodeUtil.sp(type.toDeclarationString(),name);
	}

	@Override
	public Type getType() {
		return type;
	}
	
	
	public Cls getClassType() {
		if (!(type instanceof Cls)) {
			throw new RuntimeException("type "+type+" is not a class"); 
		}
		return (Cls) type;
	}
	
	public DeclareInstruction toDeclareInstruction(Expression init) {
		return new DeclareInstruction(this, init);
	}
	
	public Expression invokeOperator(String symbol, Expression arg) {
		return new BinaryOperatorExpression(this,  type.getOperator(symbol), arg);
	}
	
	public ArrayAccessExpression arrayIndex(Expression arrayIndex) {
		return new ArrayAccessExpression(this, arrayIndex);
	}

	public Instruction assign(Expression val) {
		return new AssignInstruction(this, val);
	}
	

	public Expression deref() {
		return new DereferenceExpression(this);
	}

	public Expression pointer() {
		return new PointerExpression(this);
	}

	public Var setIfDef(String ifDef) {
		this.ifDef = ifDef;
		return this;
	}
	
	public Var setIfNDef(String ifNDef) {
		this.ifNDef = ifNDef;
		return this;
	}
	
	public boolean hasIfDef() {
		return ifDef!=null;
	}
	public boolean hasIfNDef() {
		return ifNDef!=null;
	}
	
	public String getIfDef() {
		return ifDef;
	}
	
	public String getIfNDef() {
		return ifNDef;
	}
	
	
}
