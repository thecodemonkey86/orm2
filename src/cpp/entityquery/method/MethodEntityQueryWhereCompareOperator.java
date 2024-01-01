package cpp.entityquery.method;

import cpp.Types;
import cpp.core.Cls;
import cpp.core.Method;
import cpp.core.Param;
import cpp.core.QString;
import cpp.entity.EntityCls;
import cpp.entityquery.EntityQueryType;
import cpp.lib.ClsAbstractBeanQuery;
import cpp.lib.ClsQVariant;
import database.column.Column;
import cpp.core.Type;

public class MethodEntityQueryWhereCompareOperator extends Method{
	public enum Operator{Lt("<","LessThan"),LtEq("<=","LessThanOrEqual"),Gt(">","GreaterThan"),GtEq(">=","GreaterThanOrEqual");
		
		String op;
		String methodNameSuffix;
		
		private Operator(String op,String methodNameSuffix) {
			this.op = op;
			this.methodNameSuffix = methodNameSuffix;
		}
	@Override
	public String toString() {
		return op;
	}
	}
	EntityCls entity;
	Param pValue ;
	Column c;
	EntityQueryType beanQueryType;
	Operator operator;
	
	public MethodEntityQueryWhereCompareOperator(Cls query,EntityQueryType beanQueryType, EntityCls entity,Column c,Operator operator) {
		super(Public, query.toRef(), "where"+c.getUc1stCamelCaseName()+operator.methodNameSuffix);
		this.entity=entity;
		Type t = EntityCls.getDatabaseMapper().columnToType(c);
		pValue = addParam(new Param(t.isPrimitiveType() ? t : t.toConstRef(), "value"));
		this.c = c;
		this.beanQueryType = beanQueryType;
		this.operator = operator;
	}

	@Override
	public void addImplementation() {
		//new InlineIfExpression(Expressions.not(_this().accessAttr(ClsBeanQuery.selectFields).callMethod(ClsQString.isEmpty)),
		
		if(beanQueryType == EntityQueryType.Select) {
			_return( _this().callMethod(ClsAbstractBeanQuery.where,  QString.fromStringConstant("e1." +  c.getEscapedName()+operator.toString()+"?"), Types.QVariant.callStaticMethod(ClsQVariant.fromValue, pValue) ));
		} else {
			_return( _this().callMethod(ClsAbstractBeanQuery.where, QString.fromStringConstant( c.getEscapedName()+operator.toString()+"?"), Types.QVariant.callStaticMethod(ClsQVariant.fromValue, pValue) ));
		}
		
		
		
	}
	@Override
	public boolean includeIfEmpty() {
		return true;
	}
}
