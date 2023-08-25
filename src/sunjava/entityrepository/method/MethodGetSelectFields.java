package sunjava.entityrepository.method;

import java.util.ArrayList;

import codegen.CodeUtil;
import database.column.Column;
import sunjava.core.JavaString;
import sunjava.core.Method;
import sunjava.core.Param;
import sunjava.core.Types;
import sunjava.core.expression.CharExpression;
import sunjava.core.expression.Expression;
import sunjava.core.expression.Expressions;
import sunjava.core.expression.JavaStringPlusOperatorExpression;
import sunjava.entity.EntityCls;

public class MethodGetSelectFields extends Method  {

	protected EntityCls bean;
	
	public MethodGetSelectFields(EntityCls bean) {
		super(Public, Types.String, getMethodName(bean));
		setStatic(true);
		addParam(new Param(Types.String, "alias"));
		this.bean = bean;
	}

	@Override
	public void addImplementation() {
		ArrayList<Expression> l=new ArrayList<>();
		ArrayList<Column> cols = bean.getTbl().getAllColumns(); 
		for(Column col:cols) {
			JavaStringPlusOperatorExpression e= new JavaStringPlusOperatorExpression(getParam("alias"), JavaString.stringConstant('.' + CodeUtil.sp(col.getEscapedName(),"as ") ));
			l.add(e.concat(getParam("alias")).concat(JavaString.stringConstant("__" + col.getName() + ' ')));
		}
		
		_return(Expressions.concat(CharExpression.fromChar(','), l));		
	}

	public static String getMethodName(EntityCls bean) {
		return "getSelectFields"+ bean.getName();
	}
}
