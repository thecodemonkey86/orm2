package cpp.bean.method;

import util.StringUtil;
import util.pg.PgCppUtil;
import cpp.bean.BeanCls;
import cpp.core.Attr;
import cpp.core.Method;
import cpp.core.expression.Expressions;
import database.relation.ManyRelation;
import database.relation.OneRelation;
import database.relation.OneToManyRelation;

public class MethodAttrGetter extends Method{

	Attr a;
	boolean loadIfNotLoaded;
	
	public MethodAttrGetter(Attr a,boolean loadIfNotLoaded) {
		super(Public, 
				a.getType()
//				a.getType().isPrimitiveType() ? a.getType()	: a.getType().toRef()
						, getMethodName(a));
		this.a=a;
		setConstQualifier(true);
		this.loadIfNotLoaded= loadIfNotLoaded;
//		if (loadIfNotLoaded) {
//			addParam(new Param(Types.Bool , "noLoading", BoolExpression.FALSE));
//		}
	}

	@Override
	public void addImplementation() {
		if ( loadIfNotLoaded) {
			_if(Expressions.and(
					Expressions.not(parent.getAttrByName("loaded"))
//					Expressions.not(paramByName("noLoading"))
				)
					
					
			).thenBlock()._callMethodInstr(_this().accessAttr(BeanCls.repository), "load", _this());
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
