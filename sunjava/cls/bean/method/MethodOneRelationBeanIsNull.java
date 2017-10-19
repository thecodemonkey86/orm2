package sunjava.cls.bean.method;

import pg.PgCppUtil;
import sunjava.Types;
import sunjava.cls.Method;
import sunjava.cls.expression.BinaryOperatorExpression;
import sunjava.cls.expression.Expressions;
import sunjava.lib.LibEqualsOperator;
import util.StringUtil;
import model.OneRelation;

public class MethodOneRelationBeanIsNull extends Method{
	OneRelation r;
	
	public MethodOneRelationBeanIsNull(OneRelation r ) {
		super(Public, Types.Bool, getMethodName(r));
		this.r=r;
	}

	@Override
	public void addImplementation() {
		if (parent.getName().equals("Track")) {
			System.out.println("");
		}
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
