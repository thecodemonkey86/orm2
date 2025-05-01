package sunjava.entity.method;

import database.relation.OneRelation;
import sunjava.core.Method;
import sunjava.core.Types;
import sunjava.core.expression.BinaryOperatorExpression;
import sunjava.core.expression.Expressions;
import sunjava.lib.LibEqualsOperator;
import util.StringUtil;
import util.pg.PgCppUtil;

public class MethodOneRelationEntityIsNull extends Method{
	OneRelation r;
	
	public MethodOneRelationEntityIsNull(OneRelation r ) {
		super(Public, Types.Bool, getMethodName(r));
		this.r=r;
	}

	@Override
	public void addImplementation() {
		_return(
				new BinaryOperatorExpression(
						_this().accessAttr(PgCppUtil.getOneRelationDestAttrName(r)),
						new LibEqualsOperator(),
						Expressions.Null)
				
				);
		
	}

	public static String getMethodName(OneRelation r) {
		return "is"+StringUtil.ucfirst(PgCppUtil.getOneRelationDestAttrName(r))+"Null";
	}
}
