package cpp.bean.method;

import java.util.ArrayList;
import java.util.List;

import codegen.CodeUtil;
import cpp.Types;
import cpp.bean.BeanCls;
import cpp.core.Method;
import cpp.core.Param;
import cpp.core.QString;
import cpp.core.expression.Expression;
import cpp.core.expression.Expressions;
import cpp.core.expression.QChar;
import cpp.core.expression.QStringPlusOperatorExpression;
import database.column.Column;
import database.relation.AbstractRelation;
import database.relation.OneRelation;

public class MethodGetAllSelectFields extends Method  {

	protected List<Column> cols;
	
	public MethodGetAllSelectFields(List<Column> cols) {
		super(Public, Types.QString, getMethodName());
		setStatic(true);
		addParam(new Param(Types.QString.toConstRef(), "alias"));
		this.cols = cols;
	}
	
	public static String getMethodName() {
		return "getAllSelectFields";
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
			QStringPlusOperatorExpression e= new QStringPlusOperatorExpression(getParam("alias"), QString.fromStringConstant('.' + CodeUtil.sp(col.getEscapedName(),"as ") ));
			QStringPlusOperatorExpression colExpression = e.concat(getParam("alias")).concat(QString.fromStringConstant("__" + col.getName()));
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
			l.add(QString.fromStringConstant(CodeUtil.concat(l2, ",")));
		_return(Expressions.concat(QChar.fromChar(','), l));		
	}

}
