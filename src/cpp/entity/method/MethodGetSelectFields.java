package cpp.entity.method;

import java.util.ArrayList;
import java.util.List;

import cpp.Types;
import cpp.core.Method;
import cpp.core.Param;
import cpp.core.QString;
import cpp.lib.ClsQString;
import database.column.Column;
import util.CodeUtil2;

public class MethodGetSelectFields extends Method  {

	protected List<Column> cols;
	
	public static String getMethodName() {
		return "getSelectFields";
	}
	
	public MethodGetSelectFields(List<Column> cols) {
		super(Public, Types.QString, getMethodName());
		setStatic(true);
		addParam(new Param(Types.QString.toConstRef(), "alias"));
		this.cols = cols;
	}

	@Override
	public void addImplementation() {
		/*ArrayList<Expression> l=new ArrayList<>();
		for(Column col:cols) {
			QStringPlusOperatorExpression e= new QStringPlusOperatorExpression(getParam("alias"), QString.fromStringConstant('.' + CodeUtil.sp(col.getEscapedName(),"as ") ));
			l.add(e.concat(getParam("alias")).concat(QString.fromStringConstant("__" + col.getName() + ' ')));
		}
		
		_return(Expressions.concat(QChar.fromChar(','), l));*/
		
		ArrayList<String> sprintfTmpl = new ArrayList<>();// "%1." + cols.get(0).getEscapedName() + " as %1__" + cols.get(0).getName();

		for(Column col:cols) {
			if(!col.isFileImportEnabled())
				sprintfTmpl.add("%1." + col.getEscapedName() + " as %1__" + col.getName());
		}
		
		_return (QString.fromStringConstant(CodeUtil2.commaSep(sprintfTmpl)).callMethod(ClsQString.arg, getParam("alias")));
	}

}
