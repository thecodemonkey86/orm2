package cpp.beanquery.method;

import cpp.bean.BeanCls;
import cpp.core.Cls;
import cpp.core.Method;
import cpp.core.instruction.Instruction;

public class MethodUpdate extends Method{
	BeanCls bean;
	
	public MethodUpdate(BeanCls bean,Cls parentType) {
		super(Public, parentType.toRef(), "deleteFrom");
		this.bean = bean;
	}

	@Override
	public void addImplementation() {
		addInstr(new Instruction() {
			@Override
			public String toString() {
				return "this->deleteFromTable = "+ MethodUpdate.this.bean.getName() +"::getTableName();\r\n" + 
						"        return *this;";
			}
		});
	}

}
