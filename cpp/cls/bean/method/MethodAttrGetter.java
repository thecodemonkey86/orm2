package cpp.cls.bean.method;

import model.ManyRelation;
import model.OneRelation;
import model.OneToManyRelation;
import pg.PgCppUtil;
import util.StringUtil;
import cpp.cls.Attr;
import cpp.cls.Method;
import cpp.cls.bean.BeanCls;
import cpp.cls.expression.Expressions;

public class MethodAttrGetter extends Method{

	Attr a;
	boolean loadIfNotLoaded;
	
	public MethodAttrGetter(Attr a,boolean loadIfNotLoaded) {
		super(Public, 
				a.getType()
//				a.getType().isPrimitiveType() ? a.getType()	: a.getType().toRef()
						, getMethodName(a));
		this.a=a;
//		setConstQualifier(true);
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
