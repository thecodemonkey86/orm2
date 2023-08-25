package sunjava.entitypk.method;

import java.util.ArrayList;

import codegen.CodeUtil;
import database.column.Column;
import database.relation.PrimaryKey;
import database.table.Table;
import sunjava.core.Method;
import sunjava.core.Type;
import sunjava.core.Types;
import sunjava.core.expression.Expression;

public class MethodPkHashCode extends Method {
	protected Table tbl;

	public MethodPkHashCode(Table tbl) {
		super(Method.Public, Types.Int, "hashCode");
		this.tbl = tbl;
		setOverrideAnnotation(true);
	}


	@Override
	public void addImplementation() {
		_return(new Expression() {

			@Override
			public String toString() {
				PrimaryKey pk = tbl.getPrimaryKey();
				ArrayList<String> expr=new ArrayList<>();
				for(Column colPk:pk.getColumns()) {
					expr.add(_this().accessAttr(colPk.getCamelCaseName()).toString());
				}
				return "java.util.Objects.hash" + CodeUtil.parentheses(CodeUtil.commaSep(expr));
			}

			@Override
			public Type getType() {
				return Types.Int;
			}
		});


	}

}
