package cpp.bean.method;

import java.util.ArrayList;
import java.util.List;

import cpp.Types;
import cpp.bean.BeanCls;
import cpp.core.Method;
import cpp.core.Param;
import cpp.core.QStringLiteral;
import cpp.lib.ClsQString;
import database.column.Column;
import database.relation.AbstractRelation;
import database.relation.OneRelation;

public class MethodGetAllSelectFields extends Method  {

	protected List<Column> cols;
	Param pAlias;
	
	public MethodGetAllSelectFields(List<Column> cols) {
		super(Public, Types.QString, getMethodName());
		setStatic(true);
		pAlias = addParam(new Param(Types.QString.toConstRef(), "alias"));
		this.cols = cols;
	}
	
	public static String getMethodName() {
		return "getAllSelectFields";
	}

	@Override
	public void addImplementation() {
		BeanCls bean=(BeanCls) parent;
		List<OneRelation> oneRelations =bean.getOneRelations();
		List<AbstractRelation> manyRelations = new ArrayList<>(bean.getOneToManyRelations().size()+bean.getManyToManyRelations().size());
		manyRelations.addAll(bean.getOneToManyRelations());
		manyRelations.addAll(bean.getManyToManyRelations());
		
		/*ArrayList<Expression> l=new ArrayList<>();
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
		*/
		String sprintfTmpl = "%1." + cols.get(0).getEscapedName() + " as %1__" + cols.get(0).getName();

		
		for(int i=1;i<cols.size();i++) {
			sprintfTmpl = sprintfTmpl + "," + "%1." + cols.get(i).getEscapedName() + " as %1__" + cols.get(i).getName();
		}
		for(OneRelation r:oneRelations) {
			for(Column col:r.getDestTable().getAllColumns()) {
				sprintfTmpl = sprintfTmpl + "," + r.getAlias()+"." + col.getEscapedName() + " as "+r.getAlias()+"__" + col.getName();
			}
		}
		for(AbstractRelation r:manyRelations) {
			for(Column col:r.getDestTable().getAllColumns()) {
				sprintfTmpl = sprintfTmpl + "," + r.getAlias()+"." + col.getEscapedName() + " as "+r.getAlias()+"__" + col.getName();
			}
		}
		_return (new QStringLiteral(sprintfTmpl).callMethod(ClsQString.arg,pAlias ));
		
		
	}

}
