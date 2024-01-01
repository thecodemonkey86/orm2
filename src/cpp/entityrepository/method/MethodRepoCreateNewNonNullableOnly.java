package cpp.entityrepository.method;

import java.util.ArrayList;

import cpp.core.Method;
import cpp.core.Param;
import cpp.core.SharedPtr;
import cpp.core.Type;
import cpp.core.expression.BoolExpression;
import cpp.core.expression.MakeSharedExpression;
import cpp.core.expression.Var;
import cpp.entity.EntityCls;
import database.column.Column;
import util.StringUtil;

public class MethodRepoCreateNewNonNullableOnly extends Method {

	EntityCls cls;
	ArrayList<Param> initializeFieldsParams;
	
	public MethodRepoCreateNewNonNullableOnly(EntityCls cls) {
		super(Public, cls.toSharedPtr(), "createNew" + cls.getName());
		this.cls=cls;
		initializeFieldsParams = new ArrayList<>();
		if(!cls.getTbl().getPrimaryKey().isAutoIncrement()) {
			for(Column pkCol : cls.getTbl().getPrimaryKey().getColumns()) {
				if(!pkCol.isNullable()) {
					Type t = EntityCls.getDatabaseMapper().columnToType(pkCol);
					initializeFieldsParams.add(addParam(new Param(t.isPrimitiveType() 
							? t 
							: t.toConstRef(), pkCol.getCamelCaseName())));
				}
				
			}
		}
		for(Column col : cls.getTbl().getFieldColumns()) {
			
				Type t = EntityCls.getDatabaseMapper().columnToType(col);
				if(!col.isNullable()) {
					initializeFieldsParams.add(addParam(new Param(t.isPrimitiveType() 
									? t 
									: t.toConstRef(), col.getCamelCaseName())));
					
				}
				
				
			
		}
		setStatic(true);
	}

	@Override
	public void addImplementation() {
		Var entity = _declare(returnType, "entity", new MakeSharedExpression((SharedPtr) returnType));
		_callMethodInstr(entity, "setInsertNew");
		addInstr(entity.callMethodInstruction("setLoaded", BoolExpression.TRUE));
		
		for (Param param : initializeFieldsParams) {
			addInstr(entity.callMethodInstruction("set" +StringUtil.ucfirst(param.getName())+"Internal",param));
		}
		_return(entity);
		
	}

}
