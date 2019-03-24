package php.beanpk.method; 

import java.util.ArrayList;

import database.column.Column;
import database.relation.PrimaryKey;
import database.table.Table;
import php.core.AbstractPhpCls;
import php.core.Param;
import php.core.Types;
import php.core.expression.Expression;
import php.core.expression.Expressions;
import php.core.method.Method;
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
