package sunjava.cls.bean.repo.method;

import java.util.ArrayList;

import codegen.CodeUtil;
import model.Column;
import sunjava.Types;
import sunjava.cls.JavaString;
import sunjava.cls.Method;
import sunjava.cls.Param;
import sunjava.cls.bean.BeanCls;
import sunjava.cls.expression.CharExpression;
import sunjava.cls.expression.Expression;
import sunjava.cls.expression.Expressions;
import sunjava.cls.expression.JavaStringPlusOperatorExpression;

public class MethodGetSelectFields extends Method  {

	protected BeanCls bean;
	
	public MethodGetSelectFields(BeanCls bean) {
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

	public static String getMethodName(BeanCls bean) {
		return "getSelectFields"+ bean.getName();
	}
}
