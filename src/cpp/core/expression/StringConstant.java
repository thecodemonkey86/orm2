package cpp.core.expression;

import util.Pair;
import util.StringUtil;

import java.util.Arrays;

import codegen.CodeUtil;
import cpp.Types;
import cpp.core.Type;

public class StringConstant extends Expression {

	protected String str;
	public static String getCppEscapedString(String s) {
		String escapeSequences = StringUtil.replaceAll(s,Arrays.asList(
				new Pair<String, String>("\\", "\\\\"),
				new Pair<String, String>("\r", "\\r"),
				new Pair<String, String>("\n", "\\n"),
				new Pair<String, String>("\t", "\\t")
				
				
				));
		
		
		String escapeQuots = escapeSequences.replace("\"", "\\\""); 
		
		return escapeQuots;
	}
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
		return CodeUtil.quote(getCppEscapedString(str));
	}

}
