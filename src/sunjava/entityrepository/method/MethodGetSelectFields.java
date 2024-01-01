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

	protected EntityCls entity;
	
	public MethodGetSelectFields(EntityCls entity) {
		super(Public, Types.String, getMethodName(entity));
		setStatic(true);
		addParam(new Param(Types.String, "alias"));
		this.entity = entity;
	}

	@Override
	public void addImplementation() {
		ArrayList<Expression> l=new ArrayList<>();
		ArrayList<Column> cols = entity.getTbl().getAllColumns(); 
		for(Column col:cols) {
			JavaStringPlusOperatorExpression e= new JavaStringPlusOperatorExpression(getParam("alias"), JavaString.stringConstant('.' + CodeUtil.sp(col.getEscapedName(),"as ") ));
			l.add(e.concat(getParam("alias")).concat(JavaString.stringConstant("__" + col.getName() + ' ')));
		}
		
		_return(Expressions.concat(CharExpression.fromChar(','), l));		
	}

	public static String getMethodName(EntityCls entity) {
		return "getSelectFields"+ entity.getName();
	}
}
