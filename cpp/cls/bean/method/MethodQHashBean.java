package cpp.cls.bean.method;

import model.PrimaryKey;
import cpp.Struct;
import cpp.Types;
import cpp.cls.NonMemberMethod;
import cpp.cls.Param;
import cpp.cls.bean.BeanCls;
import cpp.cls.expression.Expression;
import cpp.cls.expression.NonMemberMethodCallExpression;
import cpp.cls.expression.PlusOperatorExpression;
import cpp.lib.MethodDefaultQHash;

public class MethodQHashBean extends NonMemberMethod {
	PrimaryKey pk;
	BeanCls cls;
	
	public MethodQHashBean(BeanCls cls, PrimaryKey pk) {
		super(Types.Uint, "qHash");
		this.pk = pk;
		addParam(new Param(cls.toRef(), "bean"));
		this.cls = cls;
//		setInlineQualifier(true);
	}
	
	public MethodQHashBean(Struct structPk, PrimaryKey pk) {
		super(Types.Uint, "qHash");
		this.pk = pk;
		addParam(new Param(structPk.toRef(), "pk"));
//		setInlineQualifier(true);
	}

	@Override
	public void addImplementation() {
		Expression paramBean = getParam("bean");
//				.constCast();
//		MethodDefaultQHash mQhash = new MethodDefaultQHash();
		
		Expression hash = 
				pk.getColumns().get(0).hasOneRelation() 
						? paramBean.callMethod("get"+ pk.getColumns().get(0).getOneRelation().getDestTable().getUc1stCamelCaseName()).callMethod("get"+pk.getColumns().get(0).getOneRelationMappedColumn().getUc1stCamelCaseName())  
								:  new NonMemberMethodCallExpression(new MethodDefaultQHash(),  paramBean.callMethod("get"+ pk.getColumns().get(0).getUc1stCamelCaseName()));
		for(int i=1;i<pk.getColumns().size();i++) {
			hash = new PlusOperatorExpression(hash, pk.getColumns().get(i).hasOneRelation() 
					? paramBean.callMethod("get"+ pk.getColumns().get(i).getOneRelation().getDestTable().getUc1stCamelCaseName()).callMethod("get"+pk.getColumns().get(i).getOneRelationMappedColumn().getUc1stCamelCaseName())  
					:  new NonMemberMethodCallExpression(new MethodDefaultQHash(),  paramBean.callMethod("get"+ pk.getColumns().get(i).getUc1stCamelCaseName())));
		}
		//_return(new NonMemberMethodCallExpression(mQhash,hash));
		_return(hash);
	}
}
