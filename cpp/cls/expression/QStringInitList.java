package cpp.cls.expression;

import java.util.ArrayList;

import codegen.CodeUtil;
import cpp.Types;
import cpp.cls.QString;
import cpp.cls.Type;

public class QStringInitList extends Expression{

	private ArrayList<QString> strings;
	
	public QStringInitList() {
		strings = new ArrayList<>();
	}
	
	public void addString(QString s) {
		strings.add(s);
	}
	
	@Override
	public Type getType() {
		return Types.QStringList;
	}

	@Override
	public String toString() {
		return '{' + CodeUtil.commaSep(strings) + '}';
	}

}
