package cpp.beanquery;

import cpp.Types;
import cpp.bean.BeanCls;
import cpp.beanquery.method.ConstructorBeanQueryDelete;
import cpp.beanquery.method.MethodAddQueryParameter;
import cpp.beanquery.method.MethodAndWhere1;
import cpp.beanquery.method.MethodAndWhere10;
import cpp.beanquery.method.MethodAndWhere2;
import cpp.beanquery.method.MethodAndWhere3;
import cpp.beanquery.method.MethodAndWhere4;
import cpp.beanquery.method.MethodAndWhere5;
import cpp.beanquery.method.MethodAndWhere6;
import cpp.beanquery.method.MethodAndWhere7;
import cpp.beanquery.method.MethodAndWhere8;
import cpp.beanquery.method.MethodAndWhere9;
import cpp.beanquery.method.MethodBeanQueryWhereEquals;
import cpp.beanquery.method.MethodBeanQueryWhereIsNull;
import cpp.beanquery.method.MethodBeanQueryWhereNotEquals;
import cpp.beanquery.method.MethodExecute;
import cpp.beanquery.method.MethodGetDebugString;
import cpp.beanquery.method.MethodPrintDebug;
import cpp.beanquery.method.MethodPrintQDebug;
import cpp.beanquery.method.MethodSqlFieldEquals;
import cpp.beanquery.method.MethodToStringDelete;
import cpp.beanquery.method.MethodWhere1;
import cpp.beanquery.method.MethodWhere2;
import cpp.beanquery.method.MethodWhere3;
import cpp.beanquery.method.MethodWhere4;
import cpp.beanquery.method.MethodWhere5;
import cpp.beanquery.method.MethodWhere6;
import cpp.beanquery.method.MethodWhere7;
import cpp.beanquery.method.MethodWhere8;
import cpp.beanquery.method.MethodWhere9;
import cpp.beanrepository.ClsBeanRepository;
import cpp.core.Attr;
import cpp.core.Cls;
import cpp.lib.ClsQVector;
import database.column.Column;

public class ClsBeanQueryDelete extends Cls {

	public static final String params = "params";
	public static final String table = "table";
	
	public ClsBeanQueryDelete(BeanCls cls) {
		super(cls.getName()+ "BeanQueryDelete");
		addConstructor(new ConstructorBeanQueryDelete());
		
		for(Column c : cls.getTbl().getAllColumns()) {
			addMethod(new MethodSqlFieldEquals(c,false));
			addMethod(new MethodSqlFieldEquals(c,true));
			addMethod(new MethodBeanQueryWhereEquals(this,BeanQueryType.Delete,  cls, c));
			addMethod(new MethodBeanQueryWhereNotEquals(this,BeanQueryType.Delete, cls, c));
			
			if(c.isNullable()) {
				addMethod(new MethodBeanQueryWhereIsNull(this,BeanQueryType.Delete, cls, c));
			}
		}
		
		addIncludeLib(ClsQVector.CLSNAME);
		addIncludeHeader(BeanCls.getModelPath() + "beans/"+cls.getIncludeHeader());
		addIncludeHeader("../"+ ClsBeanRepository.CLSNAME.toLowerCase());
//		addIncludeHeader(EnumQueryMode.INSTANCE.getName().toLowerCase());
		addIncludeLib("QSqlError",true);
		addIncludeLib("QSqlDriver");
		addIncludeLib(Types.QVariant.getName());
		addAttr(new Attr(Types.BeanRepository.toSharedPtr(), "repository"));
		addAttr(new Attr(Types.QString,table));
		addAttr(new Attr(Types.QStringList,"conditions"));
		//addAttr(new Attr(Types.Int64,"limitResults"));
		//addAttr(new Attr(Types.Int64,"resultOffset"));
		addAttr(new Attr(Types.QVariantList,params));
		addAttr(new Attr(Types.Sql.toRawPointer(),"sqlCon"));
//		addAttr(new Attr(EnumQueryMode.INSTANCE,queryMode));
		
		addForwardDeclaredClass(Types.BeanRepository);
		
		addMethod(new MethodToStringDelete(cls));
//		addMethod(new MethodLimit(this,BeanQueryType.Delete));
//		addMethod(new MethodLimitAndOffset(this,BeanQueryType.Delete,null,true));
//		addMethod(new MethodLimitAndOffset(this,BeanQueryType.Delete,new Param(Types.QString.toConstRef(), "param"),true));
//		addMethod(new MethodLimitAndOffset(this,BeanQueryType.Delete,new Param(Types.Int, "param"),true));
//		addMethod(new MethodLimitAndOffset(this,BeanQueryType.Delete,new Param(Types.Bool, "param"),true));
//		addMethod(new MethodLimitAndOffset(this,BeanQueryType.Delete,new Param(Types.Double, "param"),true));
//		addMethod(new MethodLimitAndOffset(this,BeanQueryType.Delete,new Param(Types.QVariant.toConstRef(), "param"),true));
//		addMethod(new MethodLimitAndOffset(this,BeanQueryType.Delete,null,false));
//		addMethod(new MethodOffset(this,BeanQueryType.Delete));
		addMethod(new MethodPrintDebug());
		addMethod(new MethodPrintQDebug());
//		addMethod(new MethodDeleteFrom(cls, this));
		addMethod(new MethodExecute());
		addMethod(new MethodGetDebugString());
		addMethod(new MethodAddQueryParameter(Types.Int));
		addMethod(new MethodAddQueryParameter(Types.QString));
		addMethod(new MethodAddQueryParameter(Types.QVariant));
		addMethod(new MethodWhere1(this));
		addMethod(new MethodWhere2(this));
		addMethod(new MethodWhere3(this));
		addMethod(new MethodWhere4(this));
		addMethod(new MethodWhere5(this));
		addMethod(new MethodWhere6(this));
		addMethod(new MethodWhere7(this));
		addMethod(new MethodWhere8(this,true));
		addMethod(new MethodWhere8(this,false));
		addMethod(new MethodWhere9(this));
		addMethod(new MethodAndWhere1(this));
		addMethod(new MethodAndWhere2(this));
		addMethod(new MethodAndWhere3(this));
		addMethod(new MethodAndWhere4(this));
		addMethod(new MethodAndWhere5(this));
		addMethod(new MethodAndWhere6(this));
		addMethod(new MethodAndWhere7(this));
		addMethod(new MethodAndWhere8(this));
		addMethod(new MethodAndWhere9(this));
		addMethod(new MethodAndWhere10(this));
	}
	
	@Override
	public String toHeaderString() {
		// TODO Auto-generated method stub
		return super.toHeaderString();
	}
	
	@Override
	protected void addBeforeSourceCode(StringBuilder sb){
		super.addBeforeSourceCode(sb);
	}


}
