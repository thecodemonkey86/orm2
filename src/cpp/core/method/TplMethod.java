package cpp.core.method;

import cpp.core.Method;
import cpp.core.MethodTemplate;
import cpp.core.Param;
import cpp.core.TplMethodCall;
import cpp.core.TplSymbol;
import cpp.core.Type;
import cpp.core.expression.Expression;

public abstract class TplMethod extends Method{

	protected MethodTemplate template;
	
	protected Type[] concreteTypes;
	
	
	public TplMethod(MethodTemplate template,Type[] concreteTypes) {
		super(template.getVisibility(), template.getReturnType(), template.getName());
		this.concreteTypes = concreteTypes;
		this.template = template;
		for(Param p:template.getParams()) {
			if(p.getType() instanceof TplSymbol)
			{
				for(int i=0;i< template.getTplTypes().size();i++) {
					if(((TplSymbol)p.getType()).equals(template.getTplTypes().get(i))) {
						params.add(new Param(concreteTypes[i], p.getName()));
						break;
					}
				}
			} else {
				params.add(p);
			}
		}
	}

	
	@Override
	public Expression call(Expression expression, Expression...args) {
		return new TplMethodCall(expression, this, args, concreteTypes);
	}
	
	
	/*@Override
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
		
	}*/
	

	public boolean hasOutputSourceCode() {
		return false;
	}
	
	public boolean hasOutputHeaderCode() {
		return false;
	}
}
