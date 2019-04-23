package cpp.beanquery.method;

import cpp.Types;
import cpp.bean.BeanCls;
import cpp.bean.method.MethodAddRelatedTableJoins;
import cpp.beanquery.ClsBeanQuerySelect;
import cpp.core.Constructor;
import cpp.core.Param;
import cpp.core.expression.BoolExpression;
import cpp.core.expression.IntExpression;
import cpp.core.expression.StaticMethodCall;
import cpp.core.instruction.IfBlock;

public class ConstructorBeanQuerySelect extends Constructor {
	Param pSqlCon;
	Param pRepository;
//	Param pTable;
	Param pLazy;
	BeanCls bean;
	
	public ConstructorBeanQuerySelect(BeanCls bean) {
		super();
		this.bean = bean;
		pSqlCon = addParam( new Param(Types.Sql.toRawPointer(), "sqlCon"));
		pRepository = addParam(new Param(Types.BeanRepository.toSharedPtr(), "repository"));
//		pTable = addParam(new Param(Types.QString.toConstRef(), "table"));
		if(bean.hasRelations())
			pLazy = addParam(new Param(Types.Bool, "loadLazy",BoolExpression.FALSE));
	}

	@Override
	public void addImplementation() {
		addInstr( _this().assignAttr(pRepository.getName(), pRepository));
		//addInstr( _this().assignAttr(pTable.getName(), pTable));
		addInstr( _this().assignAttr(pSqlCon.getName(), pSqlCon));
		addInstr( _this().assignAttr("limitResults", new IntExpression(0)));
		addInstr( _this().assignAttr("resultOffset", new IntExpression(-1)));
//		addInstr( _this().assignAttr(ClsBeanQuery.queryMode, EnumQueryMode.INSTANCE.constant(EnumQueryMode.Select)));
		//Expression attrMainBeanAlias =  _this().accessAttr(ClsBeanQuerySelect.mainBeanAlias);
		
		
		if(bean.hasRelations()) {
			_assign(_this().accessAttr(ClsBeanQuerySelect.lazyLoading),pLazy);
			IfBlock ifNotLazyLoading = _ifNot(pLazy);
			
			//ifNotLazyLoading.thenBlock()._assign(_this().accessAttr(ClsBeanQuerySelect.selectFields),  this.bean.callStaticMethod(MethodGetAllSelectFields.getMethodName(), attrMainBeanAlias ));
			//ifNotLazyLoading.elseBlock()._assign(_this().accessAttr(ClsBeanQuerySelect.selectFields),  this.bean.callStaticMethod(MethodGetSelectFields.getMethodName(), attrMainBeanAlias ));
			ifNotLazyLoading.thenBlock().addInstr(new StaticMethodCall(bean,bean.getMethod(MethodAddRelatedTableJoins.getMethodName()), _this().dereference()).asInstruction());

		}
	}

	
	
}
