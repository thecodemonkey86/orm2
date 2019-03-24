package cpp.beanquery.method;

import cpp.Types;
import cpp.bean.BeanCls;
import cpp.beanquery.ClsBeanQuery;
import cpp.core.Method;
import cpp.core.instruction.Instruction;

public class MethodSelect3 extends Method{
	BeanCls bean;
	
	public MethodSelect3(BeanCls bean,ClsBeanQuery parentType) {
		super(Public, parentType.toRef(), "select");
		addParam(Types.QString.toConstRef(), "mainBeanAlias");
		addParam(Types.QString.toConstRef(), "selectFields");
		this.bean = bean;
	}

	@Override
	public void addImplementation() {
		addInstr(new Instruction() {
			@Override
			public String toString() {
				return "this->mainBeanAlias = mainBeanAlias;\r\n" + 
						"        this->selectFields = selectFields;\r\n" + 
						"        this->fromTable = "+ MethodSelect3.this.bean.getName() +"::getTableName(mainBeanAlias);\r\n" + 
						"        return *this;";
			}
		});
	}

}
