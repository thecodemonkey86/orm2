package cpp.beanhelper.method;

import util.StringUtil;
import cpp.Types;
import cpp.bean.BeanCls;
import cpp.bean.Nullable;
import cpp.bean.method.MethodGetFieldName;
import cpp.core.Method;
import cpp.core.Param;
import cpp.core.QString;
import cpp.core.Type;
import cpp.lib.ClsWebAppCommonForm;
import database.column.Column;

public class MethodFillFromForm extends Method{

	protected BeanCls bean;
	protected boolean prefix;
	public MethodFillFromForm(BeanCls bean) {
		this(bean,false);
	}
	public MethodFillFromForm(BeanCls bean, boolean prefix) {
		super(Method.Public, Types.Void, "fill"+StringUtil.ucfirst(bean.getName())+"FormData");
		setStatic(true);
		addParam(new Param(bean.toSharedPtr().toConstRef(),"bean"));
		addParam(new Param(new ClsWebAppCommonForm().toConstRef(), "form"));
		
		this.bean = bean;
		this.prefix = prefix;
		
		if (prefix) {
			addParam(new Param(Types.QString.toConstRef(), "prefix"));
		}
	}
	
	private static String getFormGetterMethod(Column col) {
		Type type = BeanCls.getDatabaseMapper().getTypeFromDbDataType(col.getDbType(), col.isNullable()) ;
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
			for(Column col:bean.getTbl().getColumnsWithoutPrimaryKey()) {
				if (!col.hasOneRelation()) {
					String formGetterMethod = getFormGetterMethod(col);
					if(formGetterMethod!=null)
						addInstr( getParam("bean").callMethod("set"+ col.getUc1stCamelCaseName(), getParam("form").callMethod(formGetterMethod,QString.fromStringConstant("%1_%2").callMethod("arg", getParam("prefix"), bean.callStaticMethod(MethodGetFieldName.getMethodName(col))))).asInstruction());
				}
						
			}
		} else {
			for(Column col:bean.getTbl().getColumnsWithoutPrimaryKey()) {
				if (!col.hasOneRelation()) {
					String formGetterMethod = getFormGetterMethod(col);
					if(formGetterMethod!=null)
						addInstr( getParam("bean").callMethod("set"+ col.getUc1stCamelCaseName(), getParam("form").callMethod(formGetterMethod,bean.callStaticMethod(MethodGetFieldName.getMethodName(col)))).asInstruction());
				}
						
			}
		}
		
	}

}
