package cpp.entityquery.method;

import cpp.Types;
import cpp.core.Cls;
import cpp.core.Method;
import cpp.core.Param;
import cpp.core.QString;
import cpp.core.instruction.IfBlock;
import cpp.entity.EntityCls;
import cpp.entity.Nullable;
import cpp.entityquery.EntityQueryType;
import cpp.lib.ClsAbstractBeanQuery;
import cpp.lib.ClsQVariant;
import database.column.Column;
import cpp.core.Type;

public class MethodEntityQueryWhereEquals extends Method{
	EntityCls bean;
	Param pValue ;
	Column c;
	EntityQueryType beanQueryType;
	
	public MethodEntityQueryWhereEquals(Cls query,EntityQueryType beanQueryType, EntityCls bean,Column c) {
		super(Public, query.toRef(), "where"+c.getUc1stCamelCaseName()+"Equals");
		this.bean=bean;
		Type t = EntityCls.getDatabaseMapper().columnToType(c);
		pValue = addParam(new Param(t.isPrimitiveType() ? t : t.toConstRef(), "value"));
		this.c = c;
		this.beanQueryType = beanQueryType;
	}

	@Override
	public void addImplementation() {
		//new InlineIfExpression(Expressions.not(_this().accessAttr(ClsBeanQuery.selectFields).callMethod(ClsQString.isEmpty)),
		
		if(beanQueryType == EntityQueryType.Select) {
			if(c.isNullable()) {
				IfBlock ifNull = _if(pValue.callMethod(Nullable.isNull));
				ifNull.thenBlock()._return( _this().callMethod(ClsAbstractBeanQuery.where, QString.fromStringConstant("e1." + c.getEscapedName()+" is null")) );
				ifNull.elseBlock()._return( _this().callMethod(ClsAbstractBeanQuery.where, QString.fromStringConstant("e1." + c.getEscapedName()+"=?"),Types.QVariant.callStaticMethod(ClsQVariant.fromValue,  pValue.callMethod(Nullable.val) )) );
			} else {
				_return( _this().callMethod(ClsAbstractBeanQuery.where,  QString.fromStringConstant("e1." +  c.getEscapedName()+"=?"), Types.QVariant.callStaticMethod(ClsQVariant.fromValue, pValue) ));
			}
		} else {
			if(c.isNullable()) {
				IfBlock ifNull = _if(pValue.callMethod(Nullable.isNull));
				ifNull.thenBlock()._return( _this().callMethod(ClsAbstractBeanQuery.where, QString.fromStringConstant(c.getEscapedName()+" is null"))) ;
				ifNull.elseBlock()._return( _this().callMethod(ClsAbstractBeanQuery.where, QString.fromStringConstant(c.getEscapedName()+"=?"), Types.QVariant.callStaticMethod(ClsQVariant.fromValue,  pValue.callMethod(Nullable.val) )) );
			} else {
				_return( _this().callMethod(ClsAbstractBeanQuery.where, QString.fromStringConstant( c.getEscapedName()+"=?"), Types.QVariant.callStaticMethod(ClsQVariant.fromValue, pValue) ));
			}
		}
		
		
		
	}
	@Override
	public boolean includeIfEmpty() {
		return true;
	}
}
