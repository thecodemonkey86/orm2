package cpp.entityquery.method;

import cpp.Types;
import cpp.core.Method;
import cpp.core.QStringLiteral;
import cpp.entity.EntityCls;

public class MethodOrderByPrimaryKey extends Method{

	EntityCls cls;
	
	public MethodOrderByPrimaryKey(EntityCls cls) {
		super(Protected, Types.QString, "orderByPrimaryKey");
		setConstQualifier(true);
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
