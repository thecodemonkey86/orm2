package cpp.core.method;

import java.util.ArrayList;

import codegen.CodeUtil;
import cpp.core.Method;
import cpp.core.MethodTemplate;
import cpp.core.Param;
import cpp.core.TplMethodCall;
import cpp.core.Type;
import cpp.core.expression.Expression;

public abstract class TplMethod extends Method{

	protected MethodTemplate template;
	
	protected Type[] concreteTypes;
	
	public TplMethod(String visibility, Type returnType, String name,Type[] concreteTypes) {
		super(visibility, returnType, name);
		this.concreteTypes = concreteTypes;
	}

	
	@Override
	public Expression call(Expression expression, Expression...args) {
		return new TplMethodCall(expression, this, args, concreteTypes);
	}
	
	@Override
	public String toHeaderString() {
		ArrayList<String> params=new ArrayList<>();
		for(Param p:this.params) {
			params.add(p.toSourceString());
		}
		ArrayList<String> tplTypes=new ArrayList<>();
		for(Type t:this.concreteTypes) {
			tplTypes.add(t.toDeclarationString());
		}
		return CodeUtil.sp(getVisibility()+":","template"+CodeUtil.abr(CodeUtil.commaSep(tplTypes, "typename ")))+ CodeUtil.sp((inlineQualifier?"inline":null), getReturnType().toDeclarationString(),getName(),CodeUtil.parentheses(CodeUtil.commaSep(params)),(constQualifier?"const":null), ";");
	}
}
