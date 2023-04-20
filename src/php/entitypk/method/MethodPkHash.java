package php.entitypk.method;

import database.column.Column;
import database.relation.PrimaryKey;
import php.core.CoreTypes;
import php.core.PhpFunctions;
import php.core.Types;
import php.core.expression.Expression;
import php.core.expression.PhpStringLiteral;
import php.core.expression.Var;
import php.core.method.Method;
import php.lib.ClsDateTime;

public class MethodPkHash extends Method{
	PrimaryKey pk;
	public MethodPkHash(PrimaryKey pk) {
		super(Public, CoreTypes.String, getMethodName());
		this.pk = pk;
	}

//	@Override
//	public void addImplementation() {
//		Expression a=_this().accessAttr(pk.getFirstColumn().getCamelCaseName());
////		Expression expr = a.getType().equals(CoreTypes.String) ? a : a.cast(CoreTypes.String);
//		
//		Expression[] expr=new Expression[pk.getColumnCount()];
//		int i=0;
//		ArrayList<String> listSprintf=new ArrayList<>();
//		for(Column pkCol : pk) {
//			php.core.Type t=a.getType();
//			listSprintf.add(t.equals(CoreTypes.Int) ?"%d":(t.equals(CoreTypes.Float) ? "%f" : "%s"));
//			a=_this().accessAttr(pkCol.getCamelCaseName());
//			
//			expr[i++] =t.equals(CoreTypes.String) ? a :(
//					t.equals(CoreTypes.DateTime) 
//						? a.callMethod(ClsDateTime.format, ClsDateTime.ISO8601) 
//						: a.cast(CoreTypes.String));
//		}
//		_return(PhpFunctions.sprintf.call(new PhpStringLiteral(CodeUtil.concat(listSprintf, "") , expr));
//	}
	

	public static String getMethodName() {
		return "hash";
	}

	@Override
	public void addImplementation() {
		Expression a=_this().accessAttr(pk.getFirstColumn().getCamelCaseName());
//		Expression expr = a.getType().equals(CoreTypes.String) ? a : a.cast(CoreTypes.String);
		Var vMd5 = _declare(Types.Mixed, "_md5", PhpFunctions.hash_init.call(new PhpStringLiteral("md5")));
		for(Column pkCol : pk) {
			php.core.Type t=a.getType();
			a=_this().accessAttr(pkCol.getCamelCaseName());
			
			addInstr(PhpFunctions.hash_update.call(vMd5,t.equals(CoreTypes.String) ? a :(
					t.equals(CoreTypes.DateTime) 
						? a.callMethod(ClsDateTime.format, ClsDateTime.ISO8601) 
						: a.cast(CoreTypes.String))).asInstruction());
		}
		_return(PhpFunctions.hash_final.call(vMd5));
	}

}
