package cpp.beanquery;

import cpp.Types;
import cpp.bean.BeanCls;
import cpp.beanquery.method.ConstructorBeanQuery;
import cpp.beanquery.method.MethodAndWhere1;
import cpp.beanquery.method.MethodAndWhere2;
import cpp.beanquery.method.MethodAndWhere3;
import cpp.beanquery.method.MethodAndWhere4;
import cpp.beanquery.method.MethodAndWhere5;
import cpp.beanquery.method.MethodAndWhere6;
import cpp.beanquery.method.MethodAndWhere7;
import cpp.beanquery.method.MethodAndWhere8;
import cpp.beanquery.method.MethodAndWhere9;
import cpp.beanquery.method.MethodBeanQueryFetch;
import cpp.beanquery.method.MethodBeanQueryFetchOne;
import cpp.beanquery.method.MethodBeanQueryWhereEquals;
import cpp.beanquery.method.MethodDeleteFrom;
import cpp.beanquery.method.MethodExecQuery;
import cpp.beanquery.method.MethodExecute;
import cpp.beanquery.method.MethodGetDebugString;
import cpp.beanquery.method.MethodJoin1;
import cpp.beanquery.method.MethodJoin2;
import cpp.beanquery.method.MethodJoin3;
import cpp.beanquery.method.MethodJoin4;
import cpp.beanquery.method.MethodJoin5;
import cpp.beanquery.method.MethodJoin6;
import cpp.beanquery.method.MethodLeftJoin1;
import cpp.beanquery.method.MethodLeftJoin2;
import cpp.beanquery.method.MethodLeftJoin3;
import cpp.beanquery.method.MethodLeftJoin4;
import cpp.beanquery.method.MethodLeftJoin5;
import cpp.beanquery.method.MethodLeftJoin6;
import cpp.beanquery.method.MethodLimit;
import cpp.beanquery.method.MethodLimitAndOffset;
import cpp.beanquery.method.MethodOffset;
import cpp.beanquery.method.MethodOrderBy;
import cpp.beanquery.method.MethodOrderByPrimaryKey;
import cpp.beanquery.method.MethodPrintDebug;
import cpp.beanquery.method.MethodPrintQDebug;
import cpp.beanquery.method.MethodSelect1;
import cpp.beanquery.method.MethodSelect2;
import cpp.beanquery.method.MethodSelect3;
import cpp.beanquery.method.MethodSqlFieldEquals;
import cpp.beanquery.method.MethodToString;
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
import cpp.core.Param;
import cpp.lib.ClsQVariant;
import cpp.lib.ClsQVector;
import database.column.Column;

public class ClsBeanQuery extends Cls {

	
	public ClsBeanQuery(BeanCls cls) {
		super(cls.getName()+ "BeanQuery");
		addConstructor(new ConstructorBeanQuery());
		addMethod(new MethodBeanQueryFetch(cls));
		addMethod(new MethodBeanQueryFetchOne(cls));
		addMethod(new MethodOrderByPrimaryKey(cls));
		
		
		for(Column c : cls.getTbl().getAllColumns()) {
			addMethod(new MethodSqlFieldEquals(c,false));
			addMethod(new MethodSqlFieldEquals(c,true));
			addMethod(new MethodBeanQueryWhereEquals(this, cls, c));
		}
		
		addIncludeLib(ClsQVector.CLSNAME);
		addIncludeHeader(BeanCls.getModelPath() + "beans/"+cls.getIncludeHeader());
		addIncludeHeader("../"+ ClsBeanRepository.CLSNAME.toLowerCase());
		addIncludeLib("QSqlError",true);
		addIncludeLib("QSqlDriver");
		addIncludeLib(Types.QVariant.getName());
		addAttr(new Attr(Types.BeanRepository.toSharedPtr(), "repository"));
		addAttr(new Attr(Types.QString,"mainBeanAlias"));
		addAttr(new Attr(Types.QString,"selectFields"));
		addAttr(new Attr(Types.QString,"fromTable"));
		addAttr(new Attr(Types.QStringList,"orderByExpressions"));
		addAttr(new Attr(Types.QStringList,"joinTables"));
		addAttr(new Attr(Types.QStringList,"conditions"));
		addAttr(new Attr(Types.QStringList,"group"));
		addAttr(new Attr(Types.Int64,"limitResults"));
		addAttr(new Attr(Types.Int64,"resultOffset"));
		addAttr(new Attr(Types.QString,"limitOffsetCondition"));
		addAttr(new Attr(Types.QString,"limitOffsetOrderBy"));
		addAttr(new Attr(Types.QString,"deleteFromTable"));
		addAttr(new Attr(Types.QVariantList,"params"));
		addAttr(new Attr(Types.Sql.toRawPointer(),"sqlCon"));
		
		addForwardDeclaredClass(Types.BeanRepository);
		
		addMethod(new MethodToString(cls));
		addMethod(new MethodJoin1(this));
		addMethod(new MethodJoin2(this));
		addMethod(new MethodJoin3(this));
		addMethod(new MethodJoin4(this));
		addMethod(new MethodJoin5(this));
		addMethod(new MethodJoin6(this));
		addMethod(new MethodLeftJoin1(this));
		addMethod(new MethodLeftJoin2(this));
		addMethod(new MethodLeftJoin3(this));
		addMethod(new MethodLeftJoin4(this));
		addMethod(new MethodLeftJoin5(this));
		addMethod(new MethodLeftJoin6(this));
	//	addMethod(new MethodLeftJoin7(this)); // TODO
		addMethod(new MethodWhere1(this));
		addMethod(new MethodWhere2(this));
		addMethod(new MethodWhere3(this));
		addMethod(new MethodWhere4(this));
		addMethod(new MethodWhere5(this));
		addMethod(new MethodWhere6(this));
		addMethod(new MethodWhere7(this));
		addMethod(new MethodWhere8(this));
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
		addMethod(new MethodLimit(this));
		addMethod(new MethodLimitAndOffset(this,null,true));
		addMethod(new MethodLimitAndOffset(this,new Param(Types.QString.toConstRef(), "param"),true));
		addMethod(new MethodLimitAndOffset(this,new Param(Types.Int, "param"),true));
		addMethod(new MethodLimitAndOffset(this,new Param(Types.Bool, "param"),true));
		addMethod(new MethodLimitAndOffset(this,new Param(Types.Double, "param"),true));
		addMethod(new MethodLimitAndOffset(this,new Param(Types.QVariant.toConstRef(), "param"),true));
		addMethod(new MethodLimitAndOffset(this,null,false));
		addMethod(new MethodOffset(this));
		addMethod(new MethodOrderBy(this));
		addMethod(new MethodPrintDebug());
		addMethod(new MethodPrintQDebug());
		addMethod(new MethodExecQuery());
		addMethod(new MethodSelect1(cls,this));
		addMethod(new MethodSelect2(cls,this));
		addMethod(new MethodSelect3(cls,this));
		addMethod(new MethodDeleteFrom(cls, this));
		addMethod(new MethodExecute());
		addMethod(new MethodGetDebugString());
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
