package cpp.jsonentityrepository.method;

import java.util.ArrayList;

import cpp.core.Method;
import cpp.core.Param;
import cpp.core.SharedPtr;
import cpp.core.TplCls;
import cpp.core.Type;
import cpp.core.expression.BoolExpression;
import cpp.core.expression.MakeSharedExpression;
import cpp.core.expression.Var;
import cpp.core.instruction.IfBlock;
import cpp.entity.Nullable;
import cpp.entity.method.MethodColumnAttrSetNull;
import cpp.jsonentity.JsonEntity;
import database.column.Column;
import util.StringUtil;

public class MethodCreateNew extends Method {

	JsonEntity cls;
	boolean initializeFields,initializeFieldsWithNullable;
	ArrayList<Param> initializeFieldsParams;
	
	public MethodCreateNew(JsonEntity cls) {
		this(cls,false,false);
		setStatic(true);
	}
	
	public MethodCreateNew(JsonEntity cls,boolean initializeFields,boolean initializeFieldsWithNullable) {
		super(Public, cls.toSharedPtr(), "createNew" + cls.getName());
		this.cls=cls;
		this.initializeFields = initializeFields;
		this.initializeFieldsWithNullable = initializeFieldsWithNullable;
		if(initializeFields) {
			initializeFieldsParams = new ArrayList<>();
			if(!cls.getTbl().getPrimaryKey().isAutoIncrement()) {
				for(Column pkCol : cls.getTbl().getPrimaryKey().getColumns()) {
					Type t = JsonEntity.getDatabaseMapper().columnToType(pkCol);
					if(initializeFieldsWithNullable) {
						initializeFieldsParams.add(addParam(new Param((t.isPrimitiveType() 
										? t 
										: t.toConstRef()), pkCol.getCamelCaseName())));
					} else {
					initializeFieldsParams.add(addParam(new Param(pkCol.isNullable() 
							? (((TplCls) t).getElementType().isPrimitiveType() 
									? ((TplCls) t).getElementType()
								    : ((TplCls) t).getElementType().toConstRef()) 
							: (t.isPrimitiveType() 
									? t 
									: t.toConstRef()), pkCol.getCamelCaseName())));
					}
				}
			}
			for(Column col : cls.getTbl().getFieldColumns()) {
				if(!col.isFileImportEnabled()) {
					Type t = JsonEntity.getDatabaseMapper().columnToType(col);
					if(initializeFieldsWithNullable) {
						initializeFieldsParams.add(addParam(new Param((t.isPrimitiveType() 
										? t 
										: t.toConstRef()), col.getCamelCaseName())));
					} else {
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
		setStatic(true);
	}

	@Override
	public void addImplementation() {
		Var bean = _declare(returnType, "entity", new MakeSharedExpression((SharedPtr) returnType));
		_callMethodInstr(bean, "setInsertNew");
		addInstr(bean.callMethodInstruction("setLoaded", BoolExpression.TRUE));
		
		if(initializeFields) {
			if(this.initializeFieldsWithNullable) {
				int i=0;
				if(!cls.getTbl().getPrimaryKey().isAutoIncrement()) {
				for(i=0;i< cls.getTbl().getPrimaryKey().getColumnCount();i++) {
					Column pkCol =  cls.getTbl().getPrimaryKey().getColumn(i);
					Param param = initializeFieldsParams.get(i);
					if(pkCol.isNullable()) {
						IfBlock ifIsNull= _if(param.callMethod(Nullable.isNull));
						ifIsNull.thenBlock().addInstr(bean.callMethodInstruction(MethodColumnAttrSetNull.getMethodName(bean.getClassType().getAttrByName(param.getName()))));
						ifIsNull.elseBlock().addInstr(bean.callMethodInstruction("set" +StringUtil.ucfirst(param.getName())+"Internal",param.callMethod(Nullable.val)));
					} else {
						addInstr(bean.callMethodInstruction("set" +StringUtil.ucfirst(param.getName())+"Internal",param));
					}
				}
				}
				for(Column col : cls.getTbl().getFieldColumns()) {
					if(!col.isFileImportEnabled()) {
						Param param = initializeFieldsParams.get(i++);
						if(col.isNullable()) {
							IfBlock ifIsNull= _if(param.callMethod(Nullable.isNull));
							ifIsNull.thenBlock().addInstr(bean.callMethodInstruction(MethodColumnAttrSetNull.getMethodName(bean.getClassType().getAttrByName(param.getName()))));
							ifIsNull.elseBlock().addInstr(bean.callSetterMethodInstruction(param.getName(),param.callMethod(Nullable.val)));
						} else {
							addInstr(bean.callSetterMethodInstruction(param.getName(),param));
						}
					}
				}
			} else {
				for (Param param : initializeFieldsParams) {
					addInstr(bean.callMethodInstruction("set" +StringUtil.ucfirst(param.getName())+"Internal",param));
				}
			}
			
		}
		_return(bean);
		
	}

}
