package cpp.jsonentity.method;

import cpp.Types;
import cpp.core.NonMemberMethod;
import cpp.core.Param;
import cpp.core.Struct;
import cpp.core.Type;
import cpp.core.expression.Expression;
import cpp.core.expression.NonMemberMethodCallExpression;
import cpp.core.expression.PlusOperatorExpression;
import cpp.lib.MethodDefaultQHash;
import database.relation.PrimaryKey;

public class MethodQHashPkStruct extends NonMemberMethod {
	PrimaryKey pk;
	
	public MethodQHashPkStruct(Type singleColumnPkType, PrimaryKey pk) {
		super(Types.SizeT, "qHash");
		this.pk = pk;
		addParam(new Param(singleColumnPkType, "pk"));
//		setInlineQualifier(true);
	}
	
	public MethodQHashPkStruct(Struct structPk, PrimaryKey pk) {
		super(Types.SizeT, "qHash");
		this.pk = pk;
		addParam(new Param(structPk.toConstRef(), "pk"));
//		setInlineQualifier(true);
	}

	@Override
	public void addImplementation() {
		Param paramPk = getParam("pk");
		Expression hash = new NonMemberMethodCallExpression(new MethodDefaultQHash(), 
				paramPk.accessAttr(pk.getColumns().get(0).getCamelCaseName()));
		for(int i=1;i<pk.getColumns().size();i++) {
			hash = new PlusOperatorExpression(hash, new NonMemberMethodCallExpression(new MethodDefaultQHash(), paramPk.accessAttr(pk.getColumns().get(i).getCamelCaseName())));
		}
		_return(hash);
		
	}
}
