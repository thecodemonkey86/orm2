package cpp.entityquery.method;

import cpp.Types;
import cpp.core.Method;
import cpp.core.instruction.Instruction;
import util.CodeUtil2;

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
				return "QString result(toString());" + CodeUtil2.NEWLINE + 
						"    for(int i = 0; i < params.size(); i++) {" + CodeUtil2.NEWLINE + 
						"        //       qDebug()<<params.at(i).typeName();" + CodeUtil2.NEWLINE + 
						"        QString v = QString(params.at(i).typeName()) != QStringLiteral(\"QByteArray\") ? params.at(i).toString() : QString(params.at(i).toByteArray().toHex());" + CodeUtil2.NEWLINE + 
						"        QRegularExpression e(\"^[0-9][0-9]*$\");" + CodeUtil2.NEWLINE + 
						"        result.replace(result.indexOf(QChar('?')), 1," + CodeUtil2.NEWLINE + 
						"                       v.isNull() ? QStringLiteral(\"NULL\") : e.match(v).hasMatch() ? v : QStringLiteral(\"'\") + v + QStringLiteral(\"'\"));" + CodeUtil2.NEWLINE + 
						"    }" + CodeUtil2.NEWLINE + 
						"    return result;";
			}
		});
		
	}

}
