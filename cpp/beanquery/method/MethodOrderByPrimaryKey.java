package cpp.beanquery.method;

import cpp.Types;
import cpp.bean.BeanCls;
import cpp.core.Method;
import cpp.core.QStringLiteral;

public class MethodOrderByPrimaryKey extends Method{

	BeanCls cls;
	
	public MethodOrderByPrimaryKey(BeanCls cls) {
		super(Protected, Types.QString, "orderByPrimaryKey");
		setVirtualQualifier(true);
		setConstQualifier(true);
		setOverrideQualifier(true);
		this.cls = cls;
	}

	@Override
	public void addImplementation() {
		
		StringBuilder orderBy = new StringBuilder("b1.")
				   .append(cls.getTbl().getPrimaryKey().getFirstColumn().getEscapedName())
					.append(" ASC");
		for(int i=1;i<cls.getTbl().getPrimaryKey().getColumnCount();i++) {
			orderBy.append(",b1.").append(cls.getTbl().getPrimaryKey().getColumn(i).getEscapedName()).append(" ASC");
		}
		
		
		_return(QStringLiteral.fromStringConstant(orderBy.toString()));
	}

}
