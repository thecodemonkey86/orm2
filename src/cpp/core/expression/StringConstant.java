package cpp.core.expression;

import util.StringUtil;
import codegen.CodeUtil;
import cpp.Types;
import cpp.core.Type;

public class StringConstant extends Expression {

	protected String str;
	
	public StringConstant(String str) {
		if(str == null) {
			throw new NullPointerException();
		}
		this.str = str;
	}
	
	@Override
	public Type getType() {
		return Types.ConstCharPtr;
	}
	
	@Override
	public String toString() {
		return CodeUtil.quote(StringUtil.replaceAll(StringUtil.replaceAll(str,"\\", "\\\\"),"\"","\\\""));
	}

}
