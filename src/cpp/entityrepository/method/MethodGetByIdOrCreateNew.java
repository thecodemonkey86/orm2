package cpp.entityrepository.method;

import cpp.Types;
import cpp.core.Method;
import cpp.core.Param;
import cpp.core.Type;
import cpp.core.expression.Var;
import cpp.core.instruction.IfBlock;
import cpp.entity.EntityCls;
import cpp.util.ClsDbPool;
import database.column.Column;

public class MethodGetByIdOrCreateNew extends Method {

	protected EntityCls entity;
	protected boolean addSortingParam;
	protected Param pOrderBy;
	protected Param pSqlCon;
	
	public MethodGetByIdOrCreateNew(EntityCls entity,boolean addSortingParams) {
		super(Public, entity.toSharedPtr(), "get"+entity.getName()+"ByIdOrCreateNew");
		this.addSortingParam = addSortingParams;

		for(Column col:entity.getTbl().getPrimaryKey().getColumns()) {
			Type colType =  EntityCls.getDatabaseMapper().columnToType(col);
			addParam(new Param(colType.isPrimitiveType() ? colType : colType.toConstRef(), col.getCamelCaseName()));
		}

		if(addSortingParams) {
			pOrderBy = addParam(Types.QString.toConstRef(), "orderBy");
		}
		pSqlCon = addParam(Types.QSqlDatabase.toConstRef(),"sqlCon",ClsDbPool.instance.callStaticMethod(ClsDbPool.getDatabase));
		this.entity=entity;
		setStatic(true);
	}
	public MethodGetByIdOrCreateNew(EntityCls entity) {
		this(entity,false);
	}

	
	@Override
	public void addImplementation() {
		Var e1 = _declare(returnType, "e1", parent.callStaticMethod(MethodGetById.getMethodName(entity),getParamsAsArray()));
		
		IfBlock ifBlock = _if(e1.isNull()) ;
		ifBlock.thenBlock()._assign(e1, Types.EntityRepository.callStaticMethod("createNew"+entity.getName()));
		

		if(!entity.getTbl().isAutoIncrement()) {
			for(Column colPk:entity.getTbl().getPrimaryKey()) {
				ifBlock.thenBlock()._callMethodInstr(e1, "set"+ colPk.getUc1stCamelCaseName()+"Internal",getParam(colPk.getCamelCaseName()));
			}
			
		}
		
		_return(e1);
	}

}
