package cpp.entityquery.method;

import cpp.Types;
import cpp.core.Method;
import cpp.core.instruction.Instruction;

public class MethodGetDebugString extends Method{

	public MethodGetDebugString() {
		super(Method.Public, Types.QString, "getDebugString");
		setConstQualifier(true);
	}

	@Override
	public void addImplementation() {
		addInstr(new Instruction() {
			@Override
			public String toString() {
				return " QString result(toString());\r\n" + 
						"       for(int i=0;i<params.size();i++) {\r\n" + 
						"    //       qDebug()<<params.at(i).typeName();\r\n" + 
						"           QString v= QString(params.at(i).typeName())!= QString( \"QByteArray\") ? params.at(i).toString() :QString(params.at(i).toByteArray().toHex());\r\n" + 
						"           QRegExp e(\"^[0-9][0-9]*$\");\r\n" + 
						"            result.replace(result.indexOf(QChar('?')),1,\r\n" + 
						"                          v.isNull()?QString(\"NULL\"): e.exactMatch(v)?v:QString(\"'\")+ v+ QString(\"'\"));\r\n" + 
						"       }\r\n" + 
						"       return result;";
			}
		});
		
	}

}
