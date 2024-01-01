package cpp.entityquery.method;

import cpp.Types;
import cpp.core.Cls;
import cpp.core.ConstRef;
import cpp.core.Method;
import cpp.core.Param;
import cpp.core.QString;
import cpp.core.TplCls;
import cpp.core.expression.CreateObjectExpression;
import cpp.core.expression.InlineIfExpression;
import cpp.core.expression.Var;
import cpp.core.instruction.ForeachLoop;
import cpp.entity.EntityCls;
import cpp.entity.Nullable;
import cpp.entityquery.EntityQueryType;
import cpp.lib.ClsAbstractBeanQuery;
import cpp.lib.ClsQVariant;
import cpp.lib.ClsQVariantList;
import cpp.lib.ClsQList;
import cpp.lib.ClsSqlUtil;
import database.column.Column;


public class MethodEntityQueryWhereIn extends Method {
	EntityCls entity;
	Param pValue ;
	Column c;
	EntityQueryType beanQueryType;
	public MethodEntityQueryWhereIn(Cls query,EntityQueryType beanQueryType, EntityCls entity,Column c, TplCls pValueType) {
		super(Public, query.toRef(), "where"+c.getUc1stCamelCaseName()+"In");
		this.entity=entity;
		this.pValue = addParam(pValueType.toConstRef(), "value");
		this.c = c;
		this.beanQueryType = beanQueryType;
	}


	@Override
	public void addImplementation() {
		ForeachLoop foreachParams = _foreach(new Var(((TplCls) ((ConstRef) pValue.getType()).getBase()).getElementType(), "_p"),pValue);
		
		if(c.isNullable()) {
			foreachParams._callMethodInstr(_this().accessAttr("params"), ClsQVariantList.append	,new InlineIfExpression( foreachParams.getVar().callMethod(Nullable.isNull), new CreateObjectExpression(Types.QVariant), Types.QVariant.callStaticMethod(ClsQVariant.fromValue, foreachParams.getVar().callMethod(Nullable.val))));
			
		} else {
			foreachParams._callMethodInstr(_this().accessAttr("params"), ClsQVariantList.append	, !foreachParams.getVar().getType().equals(Types.QVariant) ? Types.QVariant.callStaticMethod(ClsQVariant.fromValue, foreachParams.getVar()) :foreachParams.getVar()  );
			
		}
		if(beanQueryType == EntityQueryType.Select) {
			_return( _this().callMethod(ClsAbstractBeanQuery.where,QString.fromStringConstant("e1." + c.getEscapedName()+" in ").concat(Types.SqlUtil.callStaticMethod(ClsSqlUtil.getPlaceholders, pValue.callMethod(ClsQList.size)))));
		} else {
			_return( _this().callMethod(ClsAbstractBeanQuery.where,QString.fromStringConstant(c.getEscapedName()+" in ").concat(Types.SqlUtil.callStaticMethod(ClsSqlUtil.getPlaceholders, pValue.callMethod(ClsQList.size)))));
		}
		
		
	}
	@Override
	public boolean includeIfEmpty() {
		return true;
	}
}
