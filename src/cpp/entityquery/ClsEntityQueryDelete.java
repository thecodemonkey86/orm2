package cpp.entityquery;

import cpp.CoreTypes;
import cpp.Types;
import cpp.core.Attr;
import cpp.core.Cls;
import cpp.core.Type;
import cpp.entity.EntityCls;
import cpp.entityquery.method.ConstructorEntityQueryDelete;
import cpp.entityquery.method.MethodAddQueryParameter;
import cpp.entityquery.method.MethodAndWhere1;
import cpp.entityquery.method.MethodAndWhere10;
import cpp.entityquery.method.MethodAndWhere11;
import cpp.entityquery.method.MethodAndWhere2;
import cpp.entityquery.method.MethodAndWhere3;
import cpp.entityquery.method.MethodAndWhere4;
import cpp.entityquery.method.MethodAndWhere5;
import cpp.entityquery.method.MethodAndWhere6;
import cpp.entityquery.method.MethodAndWhere8;
import cpp.entityquery.method.MethodAndWhere9;
import cpp.entityquery.method.MethodEntityQueryBoolWhere;
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
import cpp.entityquery.method.MethodToStringDelete;
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
import cpp.entityquery.method.MethodWhere8;
import cpp.entityquery.method.MethodWhere9;
import cpp.entityrepository.ClsEntityRepository;
import cpp.lib.ClsQList;
import database.column.Column;

public class ClsEntityQueryDelete extends Cls {

	public static final String params = "params";
	public static final String table = "table";
	
	public ClsEntityQueryDelete(EntityCls cls) {
		super(cls.getName()+ "EntityQueryDelete");
		addConstructor(new ConstructorEntityQueryDelete());
		
		for(Column c : cls.getTbl().getAllColumns()) {
			addMethod(new MethodSqlFieldEquals(c,false));
			addMethod(new MethodSqlFieldEquals(c,true));
			addMethod(new MethodEntityQueryWhereEquals(this,EntityQueryType.Delete,  cls, c));
			if( EntityCls.getDatabaseMapper().columnToType(c).equals(Types.Bool)) {
				addMethod(new MethodEntityQueryBoolWhere(this,EntityQueryType.Delete,  c, true));
				addMethod(new MethodEntityQueryBoolWhere(this,EntityQueryType.Delete,  c, false));
			}
			addMethod(new MethodEntityQueryWhereNotEquals(this,EntityQueryType.Delete, cls, c));
			Type colType = EntityCls.getDatabaseMapper().columnToType(c);
			addMethod(new MethodEntityQueryWhereIn(this,EntityQueryType.Delete, cls, c,CoreTypes.qlist(colType)));
			addMethod(new MethodEntityQueryWhereIn(this,EntityQueryType.Delete, cls, c,CoreTypes.qset(colType)));
			
			
			if(c.isNullable()) {
				addMethod(new MethodEntityQueryWhereIsNull(this,EntityQueryType.Delete, cls, c));
				addMethod(new MethodEntityQueryWhereIsNotNull(this,EntityQueryType.Delete, cls, c));
			} else {
				for(MethodEntityQueryWhereCompareOperator.Operator o : MethodEntityQueryWhereCompareOperator.Operator.values()) {
					addMethod(new MethodEntityQueryWhereCompareOperator(this, EntityQueryType.Delete, cls, c, o));
				}		
			}
		}
		
		addIncludeLib(ClsQList.CLSNAME);
		addIncludeHeader(EntityCls.getModelPath() + "entities/"+cls.getIncludeHeader());
		addIncludeHeader("../"+ ClsEntityRepository.CLSNAME.toLowerCase());
		addIncludeHeader(Types.SqlUtil.getIncludeHeader());
		addIncludeHeader(Types.SqlQuery.getIncludeHeader());
//		addIncludeHeader(EnumQueryMode.INSTANCE.getName().toLowerCase());
		addIncludeLib("QDebug");
		addIncludeLib("QSqlError",true);
		addIncludeLib("QSqlDriver");
		addIncludeLib(Types.QVariant.getName());
		addIncludeLibInSource(Types.QRegularExpression);
		addAttr(new Attr(Types.EntityRepository.toRawPointer(), "repository"));
		addAttr(new Attr(Types.QString,table));
		addAttr(new Attr(Types.QStringList,"conditions"));
		//addAttr(new Attr(Types.Int64,"limitResults"));
		//addAttr(new Attr(Types.Int64,"resultOffset"));
		addAttr(new Attr(Types.QVariantList,params));
		addAttr(new Attr(Types.QSqlDatabase,"sqlCon"));
//		addAttr(new Attr(EnumQueryMode.INSTANCE,queryMode));
		
		addForwardDeclaredClass(Types.EntityRepository);
		
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
//		addMethod(new MethodPrintDebug());
		addMethod(new MethodPrintQDebug());
//		addMethod(new MethodDeleteFrom(cls, this));
		addMethod(new MethodExecute());
		addMethod(new MethodGetDebugString());
		addMethod(new MethodAddQueryParameter(Types.Int));
		addMethod(new MethodAddQueryParameter(Types.Int64));
		addMethod(new MethodAddQueryParameter(Types.QString));
		addMethod(new MethodAddQueryParameter(Types.QVariant));
		addMethod(new MethodWhere1(this));
		addMethod(new MethodWhere2(this));
		addMethod(new MethodWhere3(this));
		addMethod(new MethodWhere4(this));
		addMethod(new MethodWhere5(this));
		addMethod(new MethodWhere6(this));
//		addMethod(new MethodWhere7(this));
		addMethod(new MethodWhere8(this));
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
//		addMethod(new MethodAndWhere7(this));
		addMethod(new MethodAndWhere8(this));
		addMethod(new MethodAndWhere9(this));
		addMethod(new MethodAndWhere10(this));
		addMethod(new MethodAndWhere11(this));
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
