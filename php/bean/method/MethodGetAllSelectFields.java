package php.bean.method;
//package php.cls.bean.method;
//
//import java.util.ArrayList;
//import java.util.List;
//
//import codegen.CodeUtil;
//import model.AbstractRelation;
//import model.Column;
//import model.OneRelation;
//import php.Types;
//import php.cls.Method;
//import php.cls.Param;
//import php.cls.bean.BeanCls;
//
//import php.cls.expression.Expression;
//import php.cls.expression.Expressions;
//
//import php.cls.expression.PhpStringLiteral;
//import php.cls.expression.PhpStringPlusOperatorExpression;
//
//public class MethodGetAllSelectFields extends Method  {
//
//	protected List<Column> cols;
//	
//	public MethodGetAllSelectFields(List<Column> cols) {
//		super(Public, Types.String, "getAllSelectFields");
//		setStatic(true);
//		addParam(new Param(Types.String, "alias"));
//		this.cols = cols;
//	}
//
//	@Override
//	public void addImplementation() {
//		BeanCls bean=(BeanCls) parent;
//		List<OneRelation> oneRelations =bean.getOneRelations();
//		List<AbstractRelation> relations = new ArrayList<>(bean.getOneToManyRelations().size()+bean.getManyToManyRelations().size());
//		relations.addAll(bean.getOneToManyRelations());
//		relations.addAll(bean.getManyToManyRelations());
//		
//		ArrayList<Expression> l=new ArrayList<>();
//		for(Column col:cols) {
//			PhpStringPlusOperatorExpression e= new PhpStringPlusOperatorExpression(getParam("alias"), new PhpStringLiteral('.' + CodeUtil.sp(col.getEscapedName(),"as ") ));
//			PhpStringPlusOperatorExpression colExpression = e.concat(getParam("alias")).concat(new PhpStringLiteral("__" + col.getName()));
//			if (colExpression.toString().isEmpty()) {
//				throw new RuntimeException("col expression empty");
//			}
//			l.add(colExpression);
//		}
//		//int //bCount = 2;
//		ArrayList<String> l2=new ArrayList<>();
//		for(OneRelation r:oneRelations) {
//			for(Column col:r.getDestTable().getAllColumns()) {
//				l2.add(r.getAlias()+"."+col.getEscapedName() + " as "+ r.getAlias() +"__" + col.getName());
//			}
//			//bCount++;
//		}
//		for(AbstractRelation r:relations) {
//			for(Column col:r.getDestTable().getAllColumns()) {
//				l2.add(r.getAlias()+"."+col.getEscapedName() + " as "+ r.getAlias() +"__" + col.getName());
//			}
//			//bCount++;
//		}
//		if (!l2.isEmpty())
//			l.add(new PhpStringLiteral(CodeUtil.concat(l2, ",")));
//		_return(Expressions.concat(CharExpression.fromChar(','), l));		
//	}
//
//}
