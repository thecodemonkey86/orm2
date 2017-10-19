package cpp.cls.expression;

import util.StringUtil;
import codegen.CodeUtil;
import cpp.Types;
import cpp.cls.Type;

public class StringConstant extends Expression {

	protected String str;
	
	public StringConstant(String str) {
		this.str = str;
	}
	
	@Override
	public Type getType() {
		return Types.ConstCharPtr;
	}
	
	@Override
	public String toString() {
		return CodeUtil.quote(StringUtil.replaceAll(str,"\"", "\\\""));
	}

}
