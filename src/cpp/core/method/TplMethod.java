package cpp.core.method;

import java.util.ArrayList;

import codegen.CodeUtil;
import cpp.core.Method;
import cpp.core.MethodTemplate;
import cpp.core.Param;
import cpp.core.TplMethodCall;
import cpp.core.TplSymbol;
import cpp.core.Type;
import cpp.core.expression.Expression;
import cpp.core.instruction.Comment;
import cpp.core.instruction.Instruction;
import util.CodeUtil2;

public abstract class TplMethod extends Method{

	protected MethodTemplate template;
	
	protected Type[] concreteTypes;
	
	
	public TplMethod(MethodTemplate template,String visibility, Type returnType, String name,Type[] concreteTypes) {
		super(visibility, returnType, name);
		this.concreteTypes = concreteTypes;
		this.template = template;
	}

	
	@Override
	public Expression call(Expression expression, Expression...args) {
		return new TplMethodCall(expression, this, args, concreteTypes);
	}
	
	
	@Override
	public String toHeaderString() {
		ArrayList<String> params=new ArrayList<>();
		for(Param p:this.params) {
			if(p.getType() instanceof TplSymbol)
			{
				for(int i=0;i< template.getTplTypes().size();i++) {
					if(((TplSymbol)p.getType()).equals(template.getTplTypes().get(i))) {
						params.add(new Param(concreteTypes[i], p.getName()).toDeclarationString());
						break;
					}
				}
			} else {
				params.add(p.toDeclarationString());
			}
		}
		ArrayList<String> typeSymbols=new ArrayList<>();
		for(TplSymbol t : template.getTplTypes()) {
			typeSymbols.add(t.getName());
		}
		
		StringBuilder sb=new StringBuilder(CodeUtil.sp(getVisibility()+":","template"+CodeUtil.abr(CodeUtil.commaSep(typeSymbols, "typename ")))+ CodeUtil.sp((inlineQualifier?"inline":null), getReturnType().toDeclarationString(),getName(),CodeUtil.parentheses(CodeUtil.commaSep(params)),(constQualifier?"const":null)));
		CodeUtil.writeLine(sb, "{");
		for(Instruction i:instructions) {
			if(Instruction.isStackTraceEnabled())
				CodeUtil.writeLine(sb,new Comment(CodeUtil2.traceComment(i.getStackTrace())));
			CodeUtil.writeLine(sb,i);
		}
		sb.append("}");
		
		
		
		return sb.toString();
		
	}
	
	@Override
	public boolean isHeaderOnly() {
		return true;
	}
}
