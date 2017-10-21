package cpp.bean.method;

//public class MethodCreateNew extends Method {
//
//	BeanCls cls;
//	
//	public MethodCreateNew(BeanCls cls) {
//		super(Public, cls.toSharedPtr(), "createNew");
//		addParam(new Param(Types.Sql.toRawPointer(), "sqlCon"));
//		setStatic(true);
//		this.cls=cls;
//	}
//
//	@Override
//	public void addImplementation() {
//		Var bean = _declare(returnType, "bean", new MakeSharedExpression((SharedPtr) returnType,getParam("sqlCon")));
//		_callMethodInstr(bean, "setInsertNew");
//		_assign(bean.accessAttr("loaded"), BoolExpression.TRUE);
//		_return(bean);
//		
//	}
//
//}
