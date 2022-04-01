package cpp.entityquery;

import cpp.CoreTypes;
import cpp.QtCoreTypes;
import cpp.QtSqlTypes;
import cpp.Types;
import cpp.core.Attr;
import cpp.core.Cls;
import cpp.core.Type;
import cpp.entity.EntityCls;
import cpp.entityquery.method.ConstructorEntityQueryUpdate;
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
import cpp.entityquery.method.MethodEntityQueryWhereCompareOperator;
import cpp.entityquery.method.MethodEntityQueryWhereEquals;
import cpp.entityquery.method.MethodEntityQueryWhereIn;
import cpp.entityquery.method.MethodEntityQueryWhereIsNotNull;
import cpp.entityquery.method.MethodEntityQueryWhereIsNull;
import cpp.entityquery.method.MethodEntityQueryWhereNotEquals;
import cpp.entityquery.method.MethodExecute;
import cpp.entityquery.method.MethodGetDebugString;
import cpp.entityquery.method.MethodPrintQDebug;
import cpp.entityquery.method.MethodSqlFieldEquals;
import cpp.entityquery.method.MethodToStringUpdate;
import cpp.entityquery.method.MethodUpdateSet;
import cpp.entityquery.method.MethodUpdateSetExpression;
import cpp.entityquery.method.MethodWhere1;
import cpp.entityquery.method.MethodWhere10;
import cpp.entityquery.method.MethodWhere11;
import cpp.entityquery.method.MethodWhere12;
import cpp.entityquery.method.MethodWhere13;
import cpp.entityquery.method.MethodWhere2;
import cpp.entityquery.method.MethodWhere3;
import cpp.entityquery.method.MethodWhere4;
import cpp.entityquery.method.MethodWhere5;
import cpp.entityquery.method.MethodWhere6;
import cpp.entityquery.method.MethodWhere7;
import cpp.entityquery.method.MethodWhere8;
import cpp.entityquery.method.MethodWhere9;
import cpp.lib.ClsQVector;
import cpp.util.ClsDbPool;
import database.column.Column;

public class ClsEntityQueryUpdate extends Cls {

	public static final String selectFields = "selectFields";
	public static final String params = "params";
	public static final String lazyLoading = "lazyLoading";
	public static final String table = "table";
	public static final String updateFields = "updateFields";
	
	public ClsEntityQueryUpdate(EntityCls cls) {
		super(cls.getName()+ "EntityQueryUpdate");
		addConstructor(new ConstructorEntityQueryUpdate());
		
		for(Column c : cls.getTbl().getAllColumns()) {
			addMethod(new MethodSqlFieldEquals(c,false));
			addMethod(new MethodSqlFieldEquals(c,true));
			addMethod(new MethodEntityQueryWhereEquals(this, EntityQueryType.Update,cls, c));
			addMethod(new MethodEntityQueryWhereNotEquals(this,EntityQueryType.Update, cls, c));
			Type colType = EntityCls.getDatabaseMapper().columnToType(c);
			addMethod(new MethodEntityQueryWhereIn(this,EntityQueryType.Update, cls, c,CoreTypes.qvector(colType)));
			addMethod(new MethodEntityQueryWhereIn(this,EntityQueryType.Update, cls, c,CoreTypes.qset(colType)));
			addMethod(new MethodEntityQueryWhereIn(this,EntityQueryType.Update, cls, c,CoreTypes.qlist(colType)));
			
			if(c.isNullable()) {
				addMethod(new MethodEntityQueryWhereIsNull(this,EntityQueryType.Update, cls, c));
				addMethod(new MethodEntityQueryWhereIsNotNull(this,EntityQueryType.Update, cls, c));
			} else {
				for(MethodEntityQueryWhereCompareOperator.Operator o : MethodEntityQueryWhereCompareOperator.Operator.values()) {
					addMethod(new MethodEntityQueryWhereCompareOperator(this, EntityQueryType.Update, cls, c, o));
				}		
			}
			
			addMethod(new MethodUpdateSet(cls,this,c));
			addMethod(new MethodUpdateSetExpression(cls,this,c));
			
			colType.collectIncludes(this,false);
		}
		
		addIncludeLib(ClsQVector.CLSNAME);
		//addIncludeHeader(EntityCls.getModelPath() + "entities/"+cls.getIncludeHeader());
		addIncludeHeaderInSource("../"+ Types.EntityRepository.getName().toLowerCase());
		addIncludeHeader(Types.nullable(Types.Void).getHeaderInclude());
		addIncludeInSourceDefaultHeaderFileName(Types.SqlUtil);
		addIncludeLibInSource(Types.qset(Types.Void));
		addIncludeDefaultHeaderFileName(Types.SqlQuery);
		addIncludeHeader(ClsDbPool.instance.getHeaderInclude());
		addIncludeLibInSource(QtCoreTypes.QDebug,true);
		addIncludeLibInSource(QtSqlTypes.QSqlError,true);
		//addIncludeLib("QSqlDriver");
		addIncludeLib(Types.QVariant.getName());
		addAttr(new Attr(Types.QString,"mainBeanAlias"));
		addAttr(new Attr(Types.QString,selectFields));
		addAttr(new Attr(Types.QString,table));
		addAttr(new Attr(Types.QStringList,"joinTables"));
		addAttr(new Attr(Types.QStringList,"conditions"));
		addAttr(new Attr(Types.QStringList,"group"));
		addAttr(new Attr(Types.QStringList,updateFields));
		addAttr(new Attr(Types.Bool,lazyLoading));
		addAttr(new Attr(Types.QVariantList,params));
//		addAttr(new Attr(EnumQueryMode.INSTANCE,queryMode));
		
		//addForwardDeclaredClass(Types.EntityRepository);
		
		addMethod(new MethodToStringUpdate(cls));
		/*boolean[] booleanValues = new boolean[] {true,false};
		for(boolean qlatin1Literal1 : booleanValues) {
			for(boolean qlatin1Literal2 : booleanValues) {
				for(boolean qlatin1Literal3 : booleanValues) {
					addMethod(new MethodLeftJoin1(this,qlatin1Literal1,qlatin1Literal2,qlatin1Literal3));
				}
			}
		}*/
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
		addMethodTemplate(new MethodWhere12(this)); 
		addMethodTemplate(new MethodWhere13(this)); 
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
//		addMethod(new MethodLimit(this,BeanQueryType.Update));
//		addMethod(new MethodLimitAndOffset(this,BeanQueryType.Update,null,true));
//		addMethod(new MethodLimitAndOffset(this,BeanQueryType.Update,new Param(Types.QString.toConstRef(), "param"),true));
//		addMethod(new MethodLimitAndOffset(this,BeanQueryType.Update,new Param(Types.Int, "param"),true));
//		addMethod(new MethodLimitAndOffset(this,BeanQueryType.Update,new Param(Types.Bool, "param"),true));
//		addMethod(new MethodLimitAndOffset(this,BeanQueryType.Update,new Param(Types.Double, "param"),true));
//		addMethod(new MethodLimitAndOffset(this,BeanQueryType.Update,new Param(Types.QVariant.toConstRef(), "param"),true));
//		addMethod(new MethodLimitAndOffset(this,BeanQueryType.Update,null,false));
//		addMethod(new MethodOffset(this,BeanQueryType.Update));
//		addMethod(new MethodPrintDebug());
		addMethod(new MethodPrintQDebug());
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
