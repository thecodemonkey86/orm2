package cpp.entityhelper.method;

import util.StringUtil;
import cpp.Types;
import cpp.core.Method;
import cpp.core.Param;
import cpp.core.QString;
import cpp.core.Type;
import cpp.entity.EntityCls;
import cpp.entity.Nullable;
import cpp.entity.method.MethodGetFieldName;
import cpp.lib.ClsWebAppCommonForm;
import database.column.Column;

public class MethodFillFromForm extends Method{

	protected EntityCls entity;
	protected boolean prefix;
	public MethodFillFromForm(EntityCls entity) {
		this(entity,false);
	}
	public MethodFillFromForm(EntityCls entity, boolean prefix) {
		super(Method.Public, Types.Void, "fill"+StringUtil.ucfirst(entity.getName())+"FormData");
		setStatic(true);
		addParam(new Param(entity.toSharedPtr().toConstRef(),"entity"));
		addParam(new Param(new ClsWebAppCommonForm().toConstRef(), "form"));
		
		this.entity = entity;
		this.prefix = prefix;
		
		if (prefix) {
			addParam(new Param(Types.QString.toConstRef(), "prefix"));
		}
	}
	
	private static String getFormGetterMethod(Column col) {
		Type type = EntityCls.getDatabaseMapper().getTypeFromDbDataType(col.getDbType(), col.isNullable()) ;
		if (type instanceof Nullable) {
			type = ((Nullable) type).getElementType();
		}
		if (type.equals(Types.Int)) {
			return "intValue";
		} else if (type.equals(Types.Int64)) {
			return "int64Value";
		} else if(type.equals(Types.QString)) {
			return "stringValue";
		} else if(type.equals(Types.Double)) {
			return "doubleValue";
		} else if(type.equals(Types.Bool)) {
			return "boolValue";
		} else if(type.equals(Types.QDateTime)) {
			return "dateTimeValue";
		} else if(type.equals(Types.QDate)) {
			return "dateValue";
		}
		return null;
		//throw new RuntimeException("type not implemented: "+type);
	}
	

	@Override
	public void addImplementation() {
		if (prefix) {
			for(Column col:entity.getTbl().getColumnsWithoutPrimaryKey()) {
				if (!col.hasOneRelation()) {
					String formGetterMethod = getFormGetterMethod(col);
					if(formGetterMethod!=null)
						addInstr( getParam("entity").callMethod("set"+ col.getUc1stCamelCaseName(), getParam("form").callMethod(formGetterMethod,QString.fromStringConstant("%1_%2").callMethod("arg", getParam("prefix"), entity.callStaticMethod(MethodGetFieldName.getMethodName(col))))).asInstruction());
				}
						
			}
		} else {
			for(Column col:entity.getTbl().getColumnsWithoutPrimaryKey()) {
				if (!col.hasOneRelation()) {
					String formGetterMethod = getFormGetterMethod(col);
					if(formGetterMethod!=null)
						addInstr( getParam("entity").callMethod("set"+ col.getUc1stCamelCaseName(), getParam("form").callMethod(formGetterMethod,entity.callStaticMethod(MethodGetFieldName.getMethodName(col)))).asInstruction());
				}
						
			}
		}
		
	}

}
