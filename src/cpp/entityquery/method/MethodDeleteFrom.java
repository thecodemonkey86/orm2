package cpp.entityquery.method;
//package cpp.beanquery.method;
//
//import cpp.bean.BeanCls;
//import cpp.core.Cls;
//import cpp.core.Method;
//import cpp.core.instruction.Instruction;
//
//public class MethodDeleteFrom extends Method{
//	BeanCls bean;
//	
//	public MethodDeleteFrom(BeanCls bean,Cls parentType) {
//		super(Public, parentType.toRef(), "deleteFrom");
//		this.bean = bean;
//	}
//
//	@Override
//	public void addImplementation() {
//		addInstr(new Instruction() {
//			@Override
//			public String toString() {
//				return "this->queryMode=Delete;\r\nthis->table = "+ MethodDeleteFrom.this.bean.getName() +"::getTableName();\r\n" + 
//						"        return *this;";
//			}
//		});
//	}
//
//}
