package sunjava.bean.method;

import java.util.ArrayList;
import java.util.List;

import codegen.CodeUtil;
import database.column.Column;
import database.relation.AbstractRelation;
import database.relation.OneRelation;
import sunjava.bean.BeanCls;
import sunjava.core.JavaString;
import sunjava.core.Method;
import sunjava.core.Param;
import sunjava.core.Types;
import sunjava.core.expression.CharExpression;
import sunjava.core.expression.Expression;
import sunjava.core.expression.Expressions;
import sunjava.core.expression.JavaStringPlusOperatorExpression;

public class MethodGetAllSelectFields extends Method  {

	protected List<Column> cols;
	
	public MethodGetAllSelectFields(List<Column> cols) {
		super(Public, Types.String, "getAllSelectFields");
		setStatic(true);
		addParam(new Param(Types.String, "alias"));
		this.cols = cols;
	}

	@Override
	public void addImplementation() {
		BeanCls bean=(BeanCls) parent;
		List<OneRelation> oneRelations =bean.getOneRelations();
		List<AbstractRelation> relations = new ArrayList<>(bean.getOneToManyRelations().size()+bean.getManyToManyRelations().size());
		relations.addAll(bean.getOneToManyRelations());
		relations.addAll(bean.getManyToManyRelations());
		
		ArrayList<Expression> l=new ArrayList<>();
		for(Column col:cols) {
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
