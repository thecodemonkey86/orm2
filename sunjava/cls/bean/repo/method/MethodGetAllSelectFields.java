package sunjava.cls.bean.repo.method;

import java.util.ArrayList;
import java.util.List;

import codegen.CodeUtil;
import model.AbstractRelation;
import model.Column;
import model.OneRelation;
import sunjava.Types;
import sunjava.cls.JavaString;
import sunjava.cls.Method;
import sunjava.cls.Param;
import sunjava.cls.bean.BeanCls;
import sunjava.cls.expression.CharExpression;
import sunjava.cls.expression.Expression;
import sunjava.cls.expression.Expressions;
import sunjava.cls.expression.JavaStringPlusOperatorExpression;

public class MethodGetAllSelectFields extends Method  {

	protected BeanCls bean;
	public MethodGetAllSelectFields(BeanCls bean) {
		super(Public, Types.String, "getAllSelectFields"+ bean.getName());
		setStatic(true);
		addParam(new Param(Types.String, "alias"));
		this.bean = bean;
	}

	@Override
	public void addImplementation() {
		List<OneRelation> oneRelations =bean.getOneRelations();
		List<AbstractRelation> relations = new ArrayList<>(bean.getOneToManyRelations().size()+bean.getManyToManyRelations().size());
		relations.addAll(bean.getOneToManyRelations());
		relations.addAll(bean.getManyToManyRelations());
		
		ArrayList<Expression> l=new ArrayList<>();
		for(Column col:bean.getTbl().getAllColumns()) {
			JavaStringPlusOperatorExpression e= new JavaStringPlusOperatorExpression(getParam("alias"), JavaString.stringConstant('.' + CodeUtil.sp(col.getEscapedName(),"as ") ));
			JavaStringPlusOperatorExpression colExpression = e.concat(getParam("alias")).concat(JavaString.stringConstant("__" + col.getName()));
			if (colExpression.toString().isEmpty()) {
				throw new RuntimeException("col expression empty");
			}
			l.add(colExpression);
		}
		//int //bCount = 2;
		ArrayList<String> l2=new ArrayList<>();
		for(OneRelation r:oneRelations) {
			for(Column col:r.getDestTable().getAllColumns()) {
				l2.add(r.getAlias()+"."+col.getEscapedName() + " as "+ r.getAlias() +"__" + col.getName());
			}
			//bCount++;
		}
		for(AbstractRelation r:relations) {
			for(Column col:r.getDestTable().getAllColumns()) {
				l2.add(r.getAlias()+"."+col.getEscapedName() + " as "+ r.getAlias() +"__" + col.getName());
			}
			//bCount++;
		}
		if (!l2.isEmpty())
			l.add(JavaString.stringConstant(CodeUtil.concat(l2, ",")));
		_return(Expressions.concat(CharExpression.fromChar(','), l));		
	}

}
