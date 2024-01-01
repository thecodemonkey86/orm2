package cpp.jsonentityrepository.method;

import java.util.ArrayList;

import cpp.core.Method;
import cpp.core.Param;
import cpp.core.SharedPtr;
import cpp.core.Type;
import cpp.core.expression.BoolExpression;
import cpp.core.expression.MakeSharedExpression;
import cpp.core.expression.Var;
import cpp.jsonentity.JsonEntity;
import cpp.lib.ClsBaseJsonEntity;
import database.column.Column;
import util.StringUtil;

public class MethodCreateNewNonNullableOnly extends Method {

	JsonEntity cls;
	ArrayList<Param> initializeFieldsParams;
	
	public MethodCreateNewNonNullableOnly(JsonEntity cls) {
		super(Public, cls.toSharedPtr(), "createNew" + cls.getName());
		this.cls=cls;
		initializeFieldsParams = new ArrayList<>();
		if(!cls.getTbl().getPrimaryKey().isAutoIncrement()) {
			for(Column pkCol : cls.getTbl().getPrimaryKey().getColumns()) {
				if(!pkCol.isNullable()) {
					Type t = JsonEntity.getDatabaseMapper().columnToType(pkCol);
					initializeFieldsParams.add(addParam(new Param(t.isPrimitiveType() 
							? t 
							: t.toConstRef(), pkCol.getCamelCaseName())));
				}
				
			}
		}
		for(Column col : cls.getTbl().getFieldColumns()) {
			
				Type t = JsonEntity.getDatabaseMapper().columnToType(col);
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
		_callMethodInstr(entity, ClsBaseJsonEntity.setInsertNew);
		addInstr(entity.callMethodInstruction("setLoaded", BoolExpression.TRUE));
		
		for (Param param : initializeFieldsParams) {
			addInstr(entity.callMethodInstruction("set" +StringUtil.ucfirst(param.getName())+"Internal",param));
		}
		_return(entity);
		
	}

}
