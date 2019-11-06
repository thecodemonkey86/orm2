package cpp.entityquery;

import cpp.CoreTypes;
import cpp.Types;
import cpp.core.Attr;
import cpp.core.Cls;
import cpp.core.Param;
import cpp.core.Type;
import cpp.entity.EntityCls;
import cpp.entityquery.method.ConstructorEntityQuerySelect;
import cpp.entityquery.method.MethodAddQueryParameter;
import cpp.entityquery.method.MethodAndWhere1;
import cpp.entityquery.method.MethodAndWhere10;
import cpp.entityquery.method.MethodAndWhere11;
import cpp.entityquery.method.MethodAndWhere2;
import cpp.entityquery.method.MethodAndWhere3;
import cpp.entityquery.method.MethodAndWhere4;
import cpp.entityquery.method.MethodAndWhere5;
import cpp.entityquery.method.MethodAndWhere6;
import cpp.entityquery.method.MethodAndWhere7;
import cpp.entityquery.method.MethodAndWhere8;
import cpp.entityquery.method.MethodAndWhere9;
import cpp.entityquery.method.MethodEntityQueryFetch;
import cpp.entityquery.method.MethodEntityQueryFetchOne;
import cpp.entityquery.method.MethodEntityQueryWhereEquals;
import cpp.entityquery.method.MethodEntityQueryWhereIn;
import cpp.entityquery.method.MethodEntityQueryWhereIsNotNull;
import cpp.entityquery.method.MethodEntityQueryWhereIsNull;
import cpp.entityquery.method.MethodEntityQueryWhereNotEquals;
import cpp.entityquery.method.MethodExecQuery;
import cpp.entityquery.method.MethodExecute;
import cpp.entityquery.method.MethodGetDebugString;
import cpp.entityquery.method.MethodJoin1;
import cpp.entityquery.method.MethodJoin2;
import cpp.entityquery.method.MethodJoin3;
import cpp.entityquery.method.MethodJoin4;
import cpp.entityquery.method.MethodJoin5;
import cpp.entityquery.method.MethodJoin6;
import cpp.entityquery.method.MethodLeftJoin1;
import cpp.entityquery.method.MethodLeftJoin2;
import cpp.entityquery.method.MethodLeftJoin3;
import cpp.entityquery.method.MethodLeftJoin4;
import cpp.entityquery.method.MethodLeftJoin5;
import cpp.entityquery.method.MethodLeftJoin6;
import cpp.entityquery.method.MethodLimit;
import cpp.entityquery.method.MethodLimitAndOffset;
import cpp.entityquery.method.MethodOffset;
import cpp.entityquery.method.MethodOrderBy;
import cpp.entityquery.method.MethodOrderByPrimaryKey;
import cpp.entityquery.method.MethodPrintQDebug;
import cpp.entityquery.method.MethodSqlFieldEquals;
import cpp.entityquery.method.MethodToStringSelect;
import cpp.entityquery.method.MethodWhere1;
import cpp.entityquery.method.MethodWhere10;
import cpp.entityquery.method.MethodWhere11;
import cpp.entityquery.method.MethodWhere2;
import cpp.entityquery.method.MethodWhere3;
import cpp.entityquery.method.MethodWhere4;
import cpp.entityquery.method.MethodWhere5;
import cpp.entityquery.method.MethodWhere6;
import cpp.entityquery.method.MethodWhere7;
import cpp.entityquery.method.MethodWhere8;
import cpp.entityquery.method.MethodWhere9;
import cpp.entityrepository.ClsEntityRepository;
import cpp.lib.ClsQVector;
import database.column.Column;

public class ClsEntityQuerySelect extends Cls {

	//public static final String selectFields = "selectFields";
	public static final String params = "params";
	public static final String lazyLoading = "lazyLoading";
	//public static final String table = "table";
	//public static final String mainBeanAlias = "mainBeanAlias";
	
	public ClsEntityQuerySelect(EntityCls cls) {
		super(cls.getName()+ "EntityQuerySelect");
		addConstructor(new ConstructorEntityQuerySelect(cls));
		addMethod(new MethodEntityQueryFetch(cls));
		addMethod(new MethodEntityQueryFetchOne(cls));
		addMethod(new MethodOrderByPrimaryKey(cls));
		
		for(Column c : cls.getTbl().getAllColumns()) {
			addMethod(new MethodSqlFieldEquals(c,false));
			addMethod(new MethodSqlFieldEquals(c,true));
			addMethod(new MethodEntityQueryWhereEquals(this,EntityQueryType.Select, cls, c));
			addMethod(new MethodEntityQueryWhereNotEquals(this,EntityQueryType.Select, cls, c));
			Type colType = EntityCls.getDatabaseMapper().columnToType(c);
			addMethod(new MethodEntityQueryWhereIn(this,EntityQueryType.Select, cls, c,CoreTypes.qvector(colType)));
			addMethod(new MethodEntityQueryWhereIn(this,EntityQueryType.Select, cls, c,CoreTypes.qset(colType)));
			addMethod(new MethodEntityQueryWhereIn(this,EntityQueryType.Select, cls, c,CoreTypes.qlist(colType)));
			
			if(c.isNullable()) {
				addMethod(new MethodEntityQueryWhereIsNull(this,EntityQueryType.Select, cls, c));
				addMethod(new MethodEntityQueryWhereIsNotNull(this,EntityQueryType.Select, cls, c));
			}
		}
		
		addIncludeLib(ClsQVector.CLSNAME);
		addIncludeHeader(EntityCls.getModelPath() + "entities/"+cls.getIncludeHeader());
		addIncludeHeader("../"+ ClsEntityRepository.CLSNAME.toLowerCase());
		addIncludeHeader(Types.SqlUtil.getIncludeHeader());
		addIncludeHeader(Types.SqlQuery.getIncludeHeader());
//		addIncludeHeader(EnumQueryMode.INSTANCE.getName().toLowerCase());
		addIncludeLib("QSqlError",true);
		addIncludeLib("QSqlDriver");
		addIncludeLib(Types.QVariant.getName());
		addAttr(new Attr(Types.EntityRepository.toSharedPtr(), "repository"));
//		addAttr(new Attr(Types.QString,mainBeanAlias));
//		addAttr(new Attr(Types.QString,selectFields));
//		addAttr(new Attr(Types.QString,table));
		addAttr(new Attr(Types.QStringList,"orderByExpressions"));
		addAttr(new Attr(Types.QStringList,"joinTables"));
		addAttr(new Attr(Types.QStringList,"conditions"));
		addAttr(new Attr(Types.QStringList,"group"));
		addAttr(new Attr(Types.Int64,"limitResults"));
		addAttr(new Attr(Types.Int64,"resultOffset"));
		addAttr(new Attr(Types.QString,"limitOffsetCondition"));
		addAttr(new Attr(Types.QString,"limitOffsetOrderBy"));
		addAttr(new Attr(Types.Bool,lazyLoading));
		addAttr(new Attr(Types.QVariantList,params));
		addAttr(new Attr(Types.QSqlDatabase,"sqlCon"));
//		addAttr(new Attr(EnumQueryMode.INSTANCE,queryMode));
		
		addForwardDeclaredClass(Types.EntityRepository);
		
		addMethod(new MethodToStringSelect(cls));
		addMethod(new MethodJoin1(this));
		addMethod(new MethodJoin2(this));
		addMethod(new MethodJoin3(this));
		addMethod(new MethodJoin4(this));
		addMethod(new MethodJoin5(this));
		addMethod(new MethodJoin6(this));
		/*boolean[] booleanValues = new boolean[] {true,false};
		for(boolean qlatin1Literal1 : booleanValues) {
			for(boolean qlatin1Literal2 : booleanValues) {
				for(boolean qlatin1Literal3 : booleanValues) {
					addMethod(new MethodLeftJoin1(this,qlatin1Literal1,qlatin1Literal2,qlatin1Literal3));
				}
			}
		}*/
		addMethod(new MethodLeftJoin1(this,false,true,true));
		addMethod(new MethodLeftJoin1(this,false,false,false));
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
		addMethod(new MethodWhere8(this,true));
		addMethod(new MethodWhere8(this,false));
		addMethod(new MethodWhere9(this));
		addMethod(new MethodWhere10(this));
		addMethod(new MethodWhere11(this));
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
		addMethod(new MethodAndWhere11(this));
		addMethod(new MethodLimit(this,EntityQueryType.Select));
		addMethod(new MethodLimitAndOffset(this,EntityQueryType.Select,null,true));
		addMethod(new MethodLimitAndOffset(this,EntityQueryType.Select,new Param(Types.QString.toConstRef(), "param"),true));
		addMethod(new MethodLimitAndOffset(this,EntityQueryType.Select,new Param(Types.Int, "param"),true));
		addMethod(new MethodLimitAndOffset(this,EntityQueryType.Select,new Param(Types.Bool, "param"),true));
		addMethod(new MethodLimitAndOffset(this,EntityQueryType.Select,new Param(Types.Double, "param"),true));
		addMethod(new MethodLimitAndOffset(this,EntityQueryType.Select,new Param(Types.QVariant.toConstRef(), "param"),true));
		addMethod(new MethodLimitAndOffset(this,EntityQueryType.Select,new Param(Types.QVariantList.toConstRef(), "params"),true));
		addMethod(new MethodLimitAndOffset(this,EntityQueryType.Select,null,false));
		addMethod(new MethodOffset(this,EntityQueryType.Select));
		addMethod(new MethodOrderBy(this));
//		addMethod(new MethodPrintDebug());
		addMethod(new MethodPrintQDebug());
		addMethod(new MethodExecQuery());
//		addMethod(new MethodSelect1(cls,this));
//		addMethod(new MethodSelect2(cls,this));
//		addMethod(new MethodSelect3(cls,this));
//		addMethod(new MethodSelectLazy(cls,this));
		addMethod(new MethodExecute());
		addMethod(new MethodGetDebugString());
		addMethod(new MethodAddQueryParameter(Types.Int));
		addMethod(new MethodAddQueryParameter(Types.Int64));
		addMethod(new MethodAddQueryParameter(Types.QString));
		addMethod(new MethodAddQueryParameter(Types.QVariant));
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
