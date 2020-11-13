package cpp.entityrepository.method;

import cpp.Types;
import cpp.core.IAttributeContainer;
import cpp.core.Method;
import cpp.core.Param;
import cpp.core.Type;
import cpp.core.expression.Var;
import cpp.core.instruction.IfBlock;
import cpp.core.method.MethodAttributeSetter;
import cpp.entity.EntityCls;
import database.column.Column;

public class MethodGetByIdOrCreateNew extends Method {

	protected EntityCls bean;
	protected boolean addSortingParam;
	protected Param pOrderBy;
	
	public MethodGetByIdOrCreateNew(EntityCls cls,boolean addSortingParams) {
		this(cls);
		this.addSortingParam = addSortingParams;
		
		if(addSortingParams) {
			pOrderBy = addParam(Types.QString.toConstRef(), "orderBy");
		}
		setStatic(true);
	}
	public MethodGetByIdOrCreateNew(EntityCls cls) {
		super(Public, cls.toSharedPtr(), "get"+cls.getName()+"ByIdOrCreateNew");
		for(Column col:cls.getTbl().getPrimaryKey().getColumns()) {
			Type colType =  EntityCls.getDatabaseMapper().columnToType(col);
			addParam(new Param(colType.isPrimitiveType() ? colType : colType.toConstRef(), col.getCamelCaseName()));
		}
		this.bean=cls;
		setStatic(true);
	}

	
	@Override
	public void addImplementation() {
		Var e1 = _declare(returnType, "e1", parent.callStaticMethod(MethodGetById.getMethodName(bean),getParamsAsArray()));
		
		IfBlock ifBlock = _if(e1.isNull()) ;
		ifBlock.thenBlock()._assign(e1, Types.EntityRepository.callStaticMethod("createNew"+bean.getName()));
		

		if(!bean.getTbl().isAutoIncrement()) {
			for(Column colPk:bean.getTbl().getPrimaryKey()) {
				ifBlock.thenBlock()._callMethodInstr(e1, MethodAttributeSetter.getMethodName( ((IAttributeContainer)e1.getType()).getAttrByName(colPk.getCamelCaseName())),getParam(colPk.getCamelCaseName()));
			}
			
		}
		
		_return(e1);
	}

}
