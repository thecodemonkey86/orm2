package sunjava.bean.method;

import java.util.ArrayList;

import codegen.CodeUtil;
import database.column.Column;
import database.relation.PrimaryKey;
import database.table.Table;
import sunjava.bean.BeanCls;
import sunjava.core.Method;
import sunjava.core.PrimitiveType;
import sunjava.core.Type;
import sunjava.core.Types;
import sunjava.core.expression.Expression;
import sunjava.core.expression.StaticMethodCall;

public class MethodHashCode extends Method {
	protected Table tbl;

	public MethodHashCode(Table tbl) {
		super(Method.Public, Types.Int, "hashCode");
		this.tbl = tbl;
		setOverrideAnnotation(true);
	}

	@Override
	public void addImplementation() {
		BeanCls bean = (BeanCls) parent;
		_return(new Expression() {

			@Override
			public String toString() {
				PrimaryKey pk = tbl.getPrimaryKey();
				ArrayList<String> expr=new ArrayList<>();
				if (pk.isMultiColumn()) {
					for(Column colPk:pk.getColumns()) {
						expr.add(bean.accessThisAttrByColumn(colPk).toString());
					}
					return "java.util.Objects.hash" + CodeUtil.parentheses(CodeUtil.commaSep(expr));
				} else {
					Column colPk = pk.getFirstColumn();
					Type type = BeanCls.getTypeMapper().columnToType(colPk);
					if (type.isPrimitiveType()) {
						if(type.equals(Types.Int)) {
							return bean.accessThisAttrByColumn(colPk).toString();
						}
						PrimitiveType primitiveType = (PrimitiveType) type;
						return new StaticMethodCall( primitiveType.getAutoBoxingClass(),  primitiveType.getAutoBoxingClass().getStaticMethod("hashCode"), bean.accessThisAttrByColumn(colPk)).toString();
					} else {
						return bean.accessThisAttrByColumn(colPk).callMethod("hashCode").toString();
					}
				}

			}

			@Override
			public Type getType() {
				return Types.Int;
			}
		});

	}

}
