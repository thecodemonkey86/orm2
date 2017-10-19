package cpp.cls.bean.method;

import model.PrimaryKey;
import cpp.Struct;
import cpp.Types;
import cpp.cls.NonMemberMethod;
import cpp.cls.Param;
import cpp.cls.Type;
import cpp.cls.expression.Expression;
import cpp.cls.expression.NonMemberMethodCallExpression;
import cpp.cls.expression.PlusOperatorExpression;
import cpp.lib.MethodDefaultQHash;

public class MethodQHashPkStruct extends NonMemberMethod {
	PrimaryKey pk;
	
	public MethodQHashPkStruct(Type singleColumnPkType, PrimaryKey pk) {
		super(Types.Uint, "qHash");
		this.pk = pk;
		addParam(new Param(singleColumnPkType, "pk"));
//		setInlineQualifier(true);
	}
	
	public MethodQHashPkStruct(Struct structPk, PrimaryKey pk) {
		super(Types.Uint, "qHash");
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
