package cpp.jsonentity;

import cpp.Types;
import cpp.core.NonMemberMethod;
import cpp.core.Param;
import cpp.core.expression.Expression;
import cpp.core.expression.NonMemberMethodCallExpression;
import cpp.core.expression.PlusOperatorExpression;
import cpp.lib.MethodDefaultQHash;
import database.relation.PrimaryKey;

public class MethodQHashEntity extends NonMemberMethod {
	JsonEntity cls;
	
	public MethodQHashEntity(JsonEntity cls, boolean sp) {
		super(Types.SizeT, "qHash");
		addParam(new Param(sp ? cls.toSharedPtr().toConstRef() : cls.toConstRef(), "entity"));
		this.cls = cls;
//		setInlineQualifier(true);
	}
	

	@Override
	public void addImplementation() {
		PrimaryKey pk = cls.getTbl().getPrimaryKey();
		Expression paramEntity = getParam("entity");
//				.constCast();
//		MethodDefaultQHash mQhash = new MethodDefaultQHash();
		
		Expression hash = 
				pk.getColumns().get(0).hasOneRelation() 
						? paramEntity.callMethod("get"+ pk.getColumns().get(0).getOneRelation().getDestTable().getUc1stCamelCaseName()).callMethod("get"+pk.getColumns().get(0).getOneRelationMappedColumn().getUc1stCamelCaseName())  
								:  new NonMemberMethodCallExpression(new MethodDefaultQHash(),  paramEntity.callMethod("get"+ pk.getColumns().get(0).getUc1stCamelCaseName()));
		for(int i=1;i<pk.getColumns().size();i++) {
			hash = new PlusOperatorExpression(hash, pk.getColumns().get(i).hasOneRelation() 
					? paramEntity.callMethod("get"+ pk.getColumns().get(i).getOneRelation().getDestTable().getUc1stCamelCaseName()).callMethod("get"+pk.getColumns().get(i).getOneRelationMappedColumn().getUc1stCamelCaseName())  
					:  new NonMemberMethodCallExpression(new MethodDefaultQHash(),  paramEntity.callMethod("get"+ pk.getColumns().get(i).getUc1stCamelCaseName())));
		}
		//_return(new NonMemberMethodCallExpression(mQhash,hash));
		_return(hash);
	}
}
