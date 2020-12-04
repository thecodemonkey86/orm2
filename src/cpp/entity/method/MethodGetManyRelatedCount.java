package cpp.entity.method;

import cpp.Types;
import cpp.core.Attr;
import cpp.core.Method;
import cpp.core.Param;
import cpp.core.expression.BoolExpression;
import cpp.core.expression.Expressions;
import cpp.core.instruction.IfBlock;
import cpp.entity.ManyAttr;
import cpp.entityrepository.method.MethodEntityLoad;
import cpp.lib.ClsQVector;
import cpp.orm.OrmUtil;
import cpp.util.ClsDbPool;
import database.relation.IManyRelation;
import util.StringUtil;

public class MethodGetManyRelatedCount extends Method{

	protected Attr a;
	protected Param pSqlCon;
	
	public MethodGetManyRelatedCount(ManyAttr a, IManyRelation r) {
		super(Public, Types.Int ,"get"+ StringUtil.ucfirst(OrmUtil.getManyRelationDestAttrName(r)+"Count" ));
		this.a = a;
		pSqlCon = addParam(Types.QSqlDatabase.toConstRef(),"sqlCon",ClsDbPool.instance.callStaticMethod(ClsDbPool.getDatabase));
		//setConstQualifier(true);
	}

	@Override
	public void addImplementation() {
		IfBlock ifNotLoaded = _if(Expressions.not(parent.getAttrByName("loaded")));
		
		ifNotLoaded.thenBlock().addInstr(Types.EntityRepository.callStaticMethod(MethodEntityLoad.getMethodName(), _this().dereference(),pSqlCon).asInstruction());
		ifNotLoaded.thenBlock()._assign(parent.getAttrByName("loaded"), BoolExpression.TRUE);
		_return(a.callMethod(ClsQVector.size));
		
	}

}
