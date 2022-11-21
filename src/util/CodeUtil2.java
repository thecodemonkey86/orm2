package util;

import codegen.CodeUtil;

public class CodeUtil2 extends CodeUtil {
	public static String uc1stCamelCase(String s) {
		String[] parts = s.split("_");
		String name="";
		for(String p:parts) {
			name+=StringUtil.ucfirst(p);
		}
		return name;
	}

	public static String camelCase(String s) {
		String[] parts = s.split("_");
		String name="";
		for(String p:parts) {
			name+=StringUtil.ucfirst(p);
		}
		return Character.toLowerCase(name.charAt(0))+ name.substring(1);
	}
	
	public static String plural(String var) {
		if (var.endsWith("s")||var.endsWith("x") ) {
			return var+"es";
		} else if (var.endsWith("y")) {
			return var.substring(0, var.length()-1)+"ies";
		}
		
		return var+"s";
	}
	
	public static String strMultiply(String str,String sep,int count) {
		StringBuilder sb=new StringBuilder(str);
		for(int i=1;i<count;i++) {
			sb.append(sep);
			sb.append(str);
		}
		return sb.toString();
	}

	public static String singleQuote(char c) {
		return "\'" + c +"\'";
	}

	public static String singleQuote(String s) {
		return "\'" + s +"\'";
	}
	
	public static String traceComment(StackTraceElement[] stackTrace) {
		if (stackTrace==null) 
			return "";
		StringBuilder sb=new StringBuilder();
		for(int i=2;i<stackTrace.length;i++) {
			String s=stackTrace[i].toString();
			if(s.startsWith("sun.reflect.NativeMethodAccessorImpl")) 
				return sb.toString();
			sb.append(s);
			sb.append(" | ");
		}
		return sb.toString();
	}
}
