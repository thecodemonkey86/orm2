package php.entity.method;

import database.relation.ManyRelation;
import database.relation.OneRelation;
import database.relation.OneToManyRelation;
import php.core.Attr;
import php.core.PhpCls;
import php.core.Types;
import php.core.expression.Expressions;
import php.core.method.Method;
import util.StringUtil;
import util.pg.PgCppUtil;

public class MethodAttrGetter extends Method{

	Attr a;
	boolean loadIfNotLoaded;
	
	public MethodAttrGetter(Attr a,boolean loadIfNotLoaded) {
		super(Public, 
				a.getType()
//				a.getType().isPrimitiveType() ? a.getType()	: a.getType()
						, getMethodName(a));
		this.a=a;
		this.loadIfNotLoaded= loadIfNotLoaded;
//		if (loadIfNotLoaded) {
//			addParam(new Param(Types.Bool , "noLoading", BoolExpression.FALSE));
//		}
	}

	@Override
	public void addImplementation() {
		PhpCls parent = (PhpCls) this.parent;
		if ( loadIfNotLoaded) {
			_if(Expressions.and(
					Expressions.not(parent.getAttrByName("loaded"))
//					Expressions.not(paramByName("noLoading"))
				)
					
					
			).thenBlock().addInstr( Types.BeanRepository.callStaticMethod("load"+parent.getName(), _this()).asInstruction());
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
