package cpp.lib;

import cpp.Types;
import cpp.core.EnumConstant;

public class EnumSqlQueryOrderDirection extends cpp.core.Enum{

	public static final EnumSqlQueryOrderDirection INSTANCE = new EnumSqlQueryOrderDirection();
	
	public static final EnumConstant ORDER_ASC = new EnumConstant(EnumSqlQueryOrderDirection.INSTANCE, "ORDER_ASC");
	public static final EnumConstant ORDER_DESC = new EnumConstant(EnumSqlQueryOrderDirection.INSTANCE, "ORDER_DESC");
	
	public EnumSqlQueryOrderDirection() {
		super(Types.SqlQuery, "OrderDirection",ORDER_ASC,ORDER_DESC);
	}

}
