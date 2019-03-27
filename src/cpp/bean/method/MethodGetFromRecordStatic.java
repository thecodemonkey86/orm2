package cpp.bean.method;

/*public class MethodGetFromRecordStatic extends Method {
	protected List<Column> columns;
	
	public MethodGetFromRecordStatic(List<Column> columns,BeanCls cls) {
		super(Public, cls.toSharedPtr(), "getFromRecord");
		setStatic(true);
		addParam(new Param(Types.Sql.toRawPointer(), "sqlCon"));
		addParam(new Param(Types.QSqlRecord.toConstRef(), "record"));
		addParam(new Param(Types.QString.toConstRef(), "alias"));
		this.columns = columns;
	}

	@Override
	public boolean includeIfEmpty() {
		return true;
	}
	
	@Override
	public void addImplementation() {
		Var bean = _declareMakeShared(parent, "bean", getParam("sqlCon"));
//		Var bean = _declareNewRaw(returnType, "bean", parent.getAttrByName("sqlCon"));
		for(Column col:columns) {
			try{
//				if (!col.hasOneRelation()) {
					addInstr(bean.assignAttr(col.getCamelCaseName(),getParam("record").callMethod("value", new QStringPlusOperatorExpression(getParam("alias"), QString.fromStringConstant("__"+ col.getName()))).callMethod(BeanCls.getDatabaseMapper().getQVariantConvertMethod(col))));
//				}
//					_callMethodInstr(bean, "set"+col.getUc1stCamelCaseName(), getParam("record").callMethod("value", new QStringPlusOperatorExpression(getParam("alias"), QString.fromStringConstant("__"+ col.getName()))).callMethod(BeanCls.getDatabaseMapper().getQVariantConvertMethod(col)));
			} catch (Exception e) {
				e.printStackTrace();
				System.out.println(parent);
			}
		}
		addInstr(bean.assignAttr("insert",BoolExpression.FALSE));
		_return(bean);
	}

}
*/