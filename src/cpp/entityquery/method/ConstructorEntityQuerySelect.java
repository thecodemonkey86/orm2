package cpp.entityquery.method;

import cpp.Types;
import cpp.core.Constructor;
import cpp.core.Param;
import cpp.core.expression.BoolExpression;
import cpp.core.expression.IntExpression;
import cpp.core.expression.StaticMethodCall;
import cpp.core.instruction.IfBlock;
import cpp.entity.EntityCls;
import cpp.entity.method.MethodAddRelatedTableJoins;
import cpp.entityquery.ClsEntityQuerySelect;

public class ConstructorEntityQuerySelect extends Constructor {
	Param pLazy;
	EntityCls entity;
	
	public ConstructorEntityQuerySelect(EntityCls entity) {
		super();
		this.entity = entity;
//		pTable = addParam(new Param(Types.QString.toConstRef(), "table"));
		if(entity.hasRelations())
			pLazy = addParam(new Param(Types.Bool, "loadLazy",BoolExpression.FALSE));
	}

	@Override
	public void addImplementation() {
		//addInstr( _this().assignAttr(pTable.getName(), pTable));
		addInstr( _this().assignAttr("limitResults", new IntExpression(0)));
		addInstr( _this().assignAttr("resultOffset", new IntExpression(-1)));
//		addInstr( _this().assignAttr(ClsBeanQuery.queryMode, EnumQueryMode.INSTANCE.constant(EnumQueryMode.Select)));
		//Expression attrMainBeanAlias =  _this().accessAttr(ClsBeanQuerySelect.mainBeanAlias);
		
		
		if(entity.hasRelations()) {
			_assign(_this().accessAttr(ClsEntityQuerySelect.lazyLoading),pLazy);
			IfBlock ifNotLazyLoading = _ifNot(pLazy);
			
			//ifNotLazyLoading.thenBlock()._assign(_this().accessAttr(ClsBeanQuerySelect.selectFields),  this.entity.callStaticMethod(MethodGetAllSelectFields.getMethodName(), attrMainBeanAlias ));
			//ifNotLazyLoading.elseBlock()._assign(_this().accessAttr(ClsBeanQuerySelect.selectFields),  this.entity.callStaticMethod(MethodGetSelectFields.getMethodName(), attrMainBeanAlias ));
			ifNotLazyLoading.thenBlock().addInstr(new StaticMethodCall(entity,entity.getMethod(MethodAddRelatedTableJoins.getMethodName()), _this().dereference()).asInstruction());

		}
	}

	
	
}
