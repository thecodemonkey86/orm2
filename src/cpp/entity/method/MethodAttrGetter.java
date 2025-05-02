package cpp.entity.method;

import util.StringUtil;
import util.pg.PgCppUtil;
import cpp.Types;
import cpp.core.Attr;
import cpp.core.Method;
import cpp.core.Param;
import cpp.util.ClsDbPool;
import database.relation.AbstractRelation;
import database.relation.ManyRelation;
import database.relation.OneRelation;
import database.relation.OneToManyRelation;

public class MethodAttrGetter extends Method{

	protected Attr a;
	protected AbstractRelation relation;
	protected Param pSqlCon;
	
	public MethodAttrGetter(Attr a,AbstractRelation relation) {
		super(Public, 
				a.getType()
//				a.getType().isPrimitiveType() ? a.getType()	: a.getType().toRef()
						, getMethodName(a));
		this.a=a;
		setConstQualifier(relation==null);
		this.relation= relation;
		if ( relation!=null) {
			pSqlCon = addParam(Types.QSqlDatabase.toConstRef(),"sqlCon",ClsDbPool.instance.callStaticMethod(ClsDbPool.getDatabase));
		}
//		if (loadIfNotLoaded) {
//			addParam(new Param(Types.Bool , "noLoading", BoolExpression.FALSE));
//		}
	}

	@Override
	public void addImplementation() {
		
		
		if ( relation!=null) {
			_callMethodInstr(_this(), parent.getMethod(MethodEnsureLoaded.getMethodName((AbstractRelation) relation)),pSqlCon);
		}
		_return(a);
		
	}

	public static String getMethodName(Attr a) {
		return  "get"+StringUtil.ucfirst(a.getName());
	}
	
	public static String getMethodName(OneRelation r) {
		return "get"+StringUtil.ucfirst(PgCppUtil.getOneRelationDestAttrName(r));
	}
	
	public static String getMethodName(OneToManyRelation r) {
		return "get"+StringUtil.ucfirst(PgCppUtil.getOneToManyRelationDestAttrName(r));
	}
	
	public static String getMethodName(ManyRelation r) {
		return "get"+StringUtil.ucfirst(PgCppUtil.getManyRelationDestAttrName(r));
	}
}
