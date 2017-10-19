package cpp.cls.bean.method;

import java.util.List;

import model.Column;
import cpp.Types;
import cpp.cls.Method;
import cpp.cls.Param;
import cpp.cls.QStringLiteral;
import cpp.lib.ClsQString;

public class MethodGetSelectFields extends Method  {

	protected List<Column> cols;
	
	public MethodGetSelectFields(List<Column> cols) {
		super(Public, Types.QString, "getSelectFields");
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
			sprintfTmpl = sprintfTmpl + "," + "%1." + cols.get(i).getEscapedName() + " as %1 __" + cols.get(i).getName();
		}
		
		_return (new QStringLiteral(sprintfTmpl).callMethod(ClsQString.arg, getParam("alias")));
	}

}
