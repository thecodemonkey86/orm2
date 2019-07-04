package cpp.entityquery.method;

import cpp.Types;
import cpp.core.Method;
import cpp.core.instruction.Instruction;

public class MethodPrintQDebug extends Method{

	public MethodPrintQDebug() {
		super(Method.Public, Types.Void, "printQDebug");
		setConstQualifier(true);
	}

	@Override
	public void addImplementation() {
		addInstr(new Instruction() {
			@Override
			public String toString() {
				return "qDebug() << getDebugString();";
			}
		});
	}
	
	@Override
	public String toString() {
		return "#ifdef QT_DEBUG\n"+ super.toString()+"\n#endif\n";
	}
	
	@Override
	public String toHeaderString() {
		return "#ifdef QT_DEBUG\n"+ super.toHeaderString()+"\n#endif\n";
	}

}
