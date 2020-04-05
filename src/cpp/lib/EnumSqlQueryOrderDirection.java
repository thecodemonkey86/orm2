package cpp.lib;

import cpp.Types;
import cpp.core.EnumConstant;

public class EnumSqlQueryOrderDirection extends cpp.core.Enum{

	public static final EnumConstant ORDER_ASC = new EnumConstant("ORDER_ASC");
	public static final EnumConstant ORDER_DESC = new EnumConstant("ORDER_DESC");
	
	public static final EnumSqlQueryOrderDirection INSTANCE = new EnumSqlQueryOrderDirection();
	
	public EnumSqlQueryOrderDirection() {
		super(Types.SqlQuery, "OrderDirection",ORDER_ASC,ORDER_DESC);
	}

}
