package cpp.bean.method;

import java.util.List;

import cpp.Types;
import cpp.core.Method;
import cpp.core.Param;
import cpp.core.QStringLiteral;
import cpp.lib.ClsQString;
import database.column.Column;

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
		
		String sprintfTmpl = "%1." + cols.get(0).getEscapedName() + " as %1__" + cols.get(0).getName();

		for(int i=1;i<cols.size();i++) {
			sprintfTmpl = sprintfTmpl + "," + "%1." + cols.get(i).getEscapedName() + " as %1__" + cols.get(i).getName();
		}
		
		_return (new QStringLiteral(sprintfTmpl).callMethod(ClsQString.arg, getParam("alias")));
	}

}
