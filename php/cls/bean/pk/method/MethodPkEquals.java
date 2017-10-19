package php.cls.bean.pk.method; 

import java.util.ArrayList;

import model.Column;
import model.PrimaryKey;
import model.Table;
import php.Types;
import php.cls.AbstractPhpCls;
import php.cls.Method;
import php.cls.Param;
import php.cls.expression.Expression;
import php.cls.expression.Expressions;
public class MethodPkEquals extends Method {
	protected Table tbl;
	
	public MethodPkEquals(Table tbl, AbstractPhpCls parent) {
		super(Method.Public, Types.Bool, "equals");
		this.tbl = tbl;
		addParam(new Param(parent, "other"));
	}

	@Override
	public void addImplementation() {
		Param other = getParam("other");
		PrimaryKey pk = tbl.getPrimaryKey();
		ArrayList<Expression> expr=new ArrayList<>();
		for(Column colPk:pk.getColumns()) {
			expr.add(_this().accessAttr(colPk.getCamelCaseName())._equals(  
				
					other.accessAttr(colPk.getCamelCaseName())));
		}
		_return(Expressions.and(expr));
		
	}

}
