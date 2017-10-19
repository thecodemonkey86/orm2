package sunjava.cls.bean.method;

import java.util.ArrayList;

import codegen.CodeUtil;
import model.Column;
import model.PrimaryKey;
import model.Table;
import sunjava.Types;
import sunjava.cls.Method;
import sunjava.cls.PrimitiveType;
import sunjava.cls.Type;
import sunjava.cls.bean.BeanCls;
import sunjava.cls.expression.Expression;
import sunjava.cls.expression.StaticMethodCall;

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
