package sunjava.entity.method;

import java.util.ArrayList;

import codegen.CodeUtil;
import database.column.Column;
import database.relation.PrimaryKey;
import database.table.Table;
import sunjava.core.Method;
import sunjava.core.PrimitiveType;
import sunjava.core.Type;
import sunjava.core.Types;
import sunjava.core.expression.Expression;
import sunjava.core.expression.StaticMethodCall;
import sunjava.entity.EntityCls;

public class MethodHashCode extends Method {
	protected Table tbl;

	public MethodHashCode(Table tbl) {
		super(Method.Public, Types.Int, "hashCode");
		this.tbl = tbl;
		setOverrideAnnotation(true);
	}

	@Override
	public void addImplementation() {
		EntityCls entity = (EntityCls) parent;
		_return(new Expression() {

			@Override
			public String toString() {
				PrimaryKey pk = tbl.getPrimaryKey();
				ArrayList<String> expr=new ArrayList<>();
				if (pk.isMultiColumn()) {
					for(Column colPk:pk.getColumns()) {
						expr.add(entity.accessThisAttrByColumn(colPk).toString());
					}
					return "java.util.Objects.hash" + CodeUtil.parentheses(CodeUtil.commaSep(expr));
				} else {
					Column colPk = pk.getFirstColumn();
					Type type = EntityCls.getTypeMapper().columnToType(colPk);
					if (type.isPrimitiveType()) {
						if(type.equals(Types.Int)) {
							return entity.accessThisAttrByColumn(colPk).toString();
						}
						PrimitiveType primitiveType = (PrimitiveType) type;
						return new StaticMethodCall( primitiveType.getAutoBoxingClass(),  primitiveType.getAutoBoxingClass().getStaticMethod("hashCode"), entity.accessThisAttrByColumn(colPk)).toString();
					} else {
						return entity.accessThisAttrByColumn(colPk).callMethod("hashCode").toString();
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
