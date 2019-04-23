//package cpp.beanquery.method;
//
//import cpp.Types;
//import cpp.bean.BeanCls;
//import cpp.core.Cls;
//import cpp.core.Method;
//import cpp.core.instruction.Instruction;
//
//public class MethodSelect2 extends Method{
//	BeanCls bean;
//	
//	public MethodSelect2(BeanCls bean,Cls parentType) {
//		super(Public, parentType.toRef(), "select");
//		addParam(Types.QString.toConstRef(), "mainBeanAlias");
//		this.bean = bean;
//	}
//
//	@Override
//	public void addImplementation() {
//		addInstr(new Instruction() {
//			@Override
//			public String toString() {
//				return "this->mainBeanAlias = mainBeanAlias;\r\n" + 
//						"        this->selectFields = "+  MethodSelect2.this.bean.getName() +"::getAllSelectFields(mainBeanAlias);\r\n" + 
//						"        this->table = "+  MethodSelect2.this.bean.getName() +"::getTableName(mainBeanAlias);\r\n" + 
//						"        return *this;";
//			}
//		});
//	}
//
//}
