package cpp.beanrepository.method;

import java.util.ArrayList;

import cpp.bean.BeanCls;
import cpp.core.Method;
import cpp.core.Param;
import cpp.core.SharedPtr;
import cpp.core.TplCls;
import cpp.core.Type;
import cpp.core.expression.BoolExpression;
import cpp.core.expression.MakeSharedExpression;
import cpp.core.expression.Var;
import cpp.lib.EnableSharedFromThis;
import database.column.Column;

public class MethodRepoCreateNew extends Method {

	BeanCls cls;
	boolean initializeFields;
	ArrayList<Param> initializeFieldsParams;
	
	public MethodRepoCreateNew(BeanCls cls,boolean initializeFields) {
		super(Public, cls.toSharedPtr(), "createNew" + cls.getName());
		this.cls=cls;
		this.initializeFields = initializeFields;
		if(initializeFields) {
			initializeFieldsParams = new ArrayList<>();
			if(!cls.getTbl().getPrimaryKey().isAutoIncrement()) {
				for(Column pkCol : cls.getTbl().getPrimaryKey().getColumns()) {
					Type t = BeanCls.getDatabaseMapper().columnToType(pkCol);
					initializeFieldsParams.add(addParam(new Param(pkCol.isNullable() 
							? (((TplCls) t).getElementType().isPrimitiveType() 
									? ((TplCls) t).getElementType()
								    : ((TplCls) t).getElementType().toConstRef()) 
							: (t.isPrimitiveType() 
									? t 
									: t.toConstRef()), pkCol.getCamelCaseName())));
				}
			}
			for(Column col : cls.getTbl().getFieldColumns()) {
				// FIXME getFieldColumns enthält auch die PK-Felder, da isPartOfPk derzeit nicht funktioniert
				boolean found = false;
				for(Column pkCol : cls.getTbl().getPrimaryKey().getColumns()) {
					if(pkCol.equals(col)) {
						found = true;
						break;
					}
				}
				if(!found) {
					Type t = BeanCls.getDatabaseMapper().columnToType(col);
					initializeFieldsParams.add(addParam(new Param(col.isNullable() 
							? (((TplCls) t).getElementType().isPrimitiveType() 
									? ((TplCls) t).getElementType()
									: ((TplCls) t).getElementType().toConstRef())
							: (t.isPrimitiveType() 
									? t 
									: t.toConstRef()), col.getCamelCaseName())));
					
				}
			}
		}
	}

	@Override
	public void addImplementation() {
		Var bean = _declare(returnType, "bean", new MakeSharedExpression((SharedPtr) returnType,_this().callMethod(EnableSharedFromThis.SHARED_FROM_THIS)));
		_callMethodInstr(bean, "setInsertNew");
		addInstr(bean.callMethodInstruction("setLoaded", BoolExpression.TRUE));
		
		if(initializeFields) {
			for (Param param : initializeFieldsParams) {
				addInstr(bean.callSetterMethodInstruction(param.getName(),param));
			}
		}
		_return(bean);
		
	}

}
