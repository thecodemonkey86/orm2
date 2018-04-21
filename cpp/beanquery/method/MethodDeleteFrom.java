package cpp.beanquery.method;

import cpp.bean.BeanCls;
import cpp.beanquery.ClsBeanQuery;
import cpp.core.Method;
import cpp.core.instruction.Instruction;

public class MethodDeleteFrom extends Method{
	BeanCls bean;
	
	public MethodDeleteFrom(BeanCls bean,ClsBeanQuery parentType) {
		super(Public, parentType.toRef(), "deleteFrom");
		this.bean = bean;
	}

	@Override
	public void addImplementation() {
		addInstr(new Instruction() {
			@Override
			public String toString() {
				return "this->deleteFromTable = "+ MethodDeleteFrom.this.bean.getName() +"::getTableName();\r\n" + 
						"        return *this;";
			}
		});
	}

}
