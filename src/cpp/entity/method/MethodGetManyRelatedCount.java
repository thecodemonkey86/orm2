package cpp.entity.method;

import cpp.Types;
import cpp.core.Attr;
import cpp.core.Method;
import cpp.core.Param;
import cpp.entity.ManyAttr;
import cpp.lib.ClsQList;
import cpp.orm.OrmUtil;
import cpp.util.ClsDbPool;
import database.relation.AbstractRelation;
import database.relation.IManyRelation;
import util.StringUtil;

public class MethodGetManyRelatedCount extends Method{

	protected Attr a;
	protected Param pSqlCon;
	protected IManyRelation relation;
	
	public MethodGetManyRelatedCount(ManyAttr a, IManyRelation r) {
		super(Public, Types.SizeT ,"get"+ StringUtil.ucfirst(OrmUtil.getManyRelationDestAttrName(r)+"Count" ));
		this.a = a;
		this.relation = r;
		pSqlCon = addParam(Types.QSqlDatabase.toConstRef(),"sqlCon",ClsDbPool.instance.callStaticMethod(ClsDbPool.getDatabase));
		//setConstQualifier(true);
	}

	@Override
	public void addImplementation() {
		_callMethodInstr(_this(), parent.getMethod(MethodEnsureLoaded.getMethodName((AbstractRelation) relation)),pSqlCon);
		_return(a.callMethod(ClsQList.size));
		
	}

}
