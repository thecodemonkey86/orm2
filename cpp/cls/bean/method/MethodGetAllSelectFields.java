package cpp.cls.bean.method;

import java.util.ArrayList;
import java.util.List;

import codegen.CodeUtil;
import model.AbstractRelation;
import model.Column;
import model.OneRelation;
import cpp.Types;
import cpp.cls.Method;
import cpp.cls.Param;
import cpp.cls.QString;
import cpp.cls.bean.BeanCls;
import cpp.cls.expression.Expression;
import cpp.cls.expression.Expressions;
import cpp.cls.expression.QChar;
import cpp.cls.expression.QStringPlusOperatorExpression;

public class MethodGetAllSelectFields extends Method  {

	protected List<Column> cols;
	
	public MethodGetAllSelectFields(List<Column> cols) {
		super(Public, Types.QString, "getAllSelectFields");
		setStatic(true);
		addParam(new Param(Types.QString.toConstRef(), "alias"));
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
