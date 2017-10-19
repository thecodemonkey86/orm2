package cpp.cls.bean.helper.method;

import util.StringUtil;
import model.Column;
import cpp.Types;
import cpp.cls.Method;
import cpp.cls.Param;
import cpp.cls.QString;
import cpp.cls.Type;
import cpp.cls.bean.BeanCls;
import cpp.cls.bean.Nullable;
import cpp.cls.bean.method.MethodGetFieldName;
import cpp.lib.ClsWebAppCommonForm;

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
		throw new RuntimeException("type not implemented: "+type);
	}
	

	@Override
	public void addImplementation() {
		if (prefix) {
			for(Column col:bean.getTbl().getColumnsWithoutPrimaryKey()) {
				if (!col.hasOneRelation()) {
					addInstr( getParam("bean").callMethod("set"+ col.getUc1stCamelCaseName(), getParam("form").callMethod(getFormGetterMethod(col),QString.fromStringConstant("%1_%2").callMethod("arg", getParam("prefix"), bean.callStaticMethod(MethodGetFieldName.getMethodName(col))))).asInstruction());
				}
						
			}
		} else {
			for(Column col:bean.getTbl().getColumnsWithoutPrimaryKey()) {
				if (!col.hasOneRelation()) {
					addInstr( getParam("bean").callMethod("set"+ col.getUc1stCamelCaseName(), getParam("form").callMethod(getFormGetterMethod(col),bean.callStaticMethod(MethodGetFieldName.getMethodName(col)))).asInstruction());
				}
						
			}
		}
		
	}

}
