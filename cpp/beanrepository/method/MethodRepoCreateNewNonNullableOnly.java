package cpp.beanrepository.method;

import java.util.ArrayList;

import cpp.bean.BeanCls;
import cpp.core.Method;
import cpp.core.Param;
import cpp.core.SharedPtr;
import cpp.core.Type;
import cpp.core.expression.BoolExpression;
import cpp.core.expression.MakeSharedExpression;
import cpp.core.expression.Var;
import cpp.lib.EnableSharedFromThis;
import database.column.Column;

public class MethodRepoCreateNewNonNullableOnly extends Method {

	BeanCls cls;
	ArrayList<Param> initializeFieldsParams;
	
	public MethodRepoCreateNewNonNullableOnly(BeanCls cls) {
		super(Public, cls.toSharedPtr(), "createNew" + cls.getName());
		this.cls=cls;
		initializeFieldsParams = new ArrayList<>();
		if(!cls.getTbl().getPrimaryKey().isAutoIncrement()) {
			for(Column pkCol : cls.getTbl().getPrimaryKey().getColumns()) {
				if(!pkCol.isNullable()) {
					Type t = BeanCls.getDatabaseMapper().columnToType(pkCol);
					initializeFieldsParams.add(addParam(new Param(t.isPrimitiveType() 
							? t 
							: t.toConstRef(), pkCol.getCamelCaseName())));
				}
				
			}
		}
		for(Column col : cls.getTbl().getFieldColumns()) {
			
				Type t = BeanCls.getDatabaseMapper().columnToType(col);
				if(!col.isNullable()) {
					initializeFieldsParams.add(addParam(new Param(t.isPrimitiveType() 
									? t 
									: t.toConstRef(), col.getCamelCaseName())));
					
				}
				
				
			
		}
	}

	@Override
	public void addImplementation() {
		Var bean = _declare(returnType, "bean", new MakeSharedExpression((SharedPtr) returnType,_this().callMethod(EnableSharedFromThis.SHARED_FROM_THIS)));
		_callMethodInstr(bean, "setInsertNew");
		addInstr(bean.callMethodInstruction("setLoaded", BoolExpression.TRUE));
		
		for (Param param : initializeFieldsParams) {
			addInstr(bean.callSetterMethodInstruction(param.getName(),param));
		}
		_return(bean);
		
	}

}
