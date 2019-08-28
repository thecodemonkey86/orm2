package cpp.lib;

import codegen.CodeUtil;
import cpp.CoreTypes;
import cpp.core.TplCls;
import cpp.core.Type;
import cpp.core.expression.IArrayAccessible;

public class ClsQHash extends TplCls implements IArrayAccessible {
	protected Type valType;
	
	public ClsQHash(Type key, Type val) {
		super("QHash", key);
		if (val == null) {
			throw new NullPointerException();
		}
		valType= val;
		addMethod(new LibMethod(CoreTypes.Void, "insert"));
		addMethod(new LibMethod(CoreTypes.Bool, "contains"));
	}
	
	@Override
	public String toUsageString() {
		String str= constness ? CodeUtil.sp("const",type,isPtr()?"*":"") : isPtr()?type+ CodeUtil.abr(CodeUtil.commaSep(element.toUsageString(),valType.toUsageString()))+"*":type+ CodeUtil.abr(CodeUtil.commaSep(element.toUsageString(),valType.toUsageString()));
		return (useNamespace != null) ? useNamespace+"::"+ str : str;
		
	}
	
	@Override
	public String toString() {
		return type + CodeUtil.abr(CodeUtil.commaSep(element.toString(),valType.toString()));
	}

	@Override
	public Type getAccessType() {
		return valType;
	}

}
