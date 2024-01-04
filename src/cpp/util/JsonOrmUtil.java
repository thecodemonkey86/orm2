package cpp.util;

import cpp.CoreTypes;
import cpp.core.ConstRef;
import cpp.core.TplCls;
import cpp.core.Type;
import cpp.core.expression.Expression;
import cpp.entity.Nullable;
import cpp.lib.ClsQString;

public class JsonOrmUtil {
	public static Expression convertToQString(Expression e) {
		Type t=null;
		Expression ex=null;
		if(e.getType() instanceof Nullable) {
			t= ((TplCls) e.getType()).getElementType();
			ex=e.callMethod(Nullable.val);
		} else if(e.getType() instanceof ConstRef) {
			t=((ConstRef) e.getType()).getBase();
			ex=e;
		} else {
			t=e.getType();
			ex=e;
		}

		
		if(t.equals(CoreTypes.QString)) {
			return ex;
		} else if(t.equals(CoreTypes.Int)
				||t.equals(CoreTypes.Int16)
				||t.equals(CoreTypes.Int64)
				||t.equals(CoreTypes.Int32)
				||t.equals(CoreTypes.Int8)
				||t.equals(CoreTypes.UInt)
				||t.equals(CoreTypes.UInt16)
				||t.equals(CoreTypes.UInt32)
				||t.equals(CoreTypes.UInt8)
				||t.equals(CoreTypes.Double)
				||t.equals(CoreTypes.Short)
				) {
			return CoreTypes.QString.callStaticMethod(ClsQString.number, ex);
		} else {
			throw new RuntimeException("not impl: "+ex.getType());
		}
	}
}
