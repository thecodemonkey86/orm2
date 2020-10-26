package cpp.entity.method;

import java.util.ArrayList;
import java.util.List;

import cpp.Types;
import cpp.core.Method;
import cpp.core.Param;
import cpp.core.QString;
import cpp.entity.EntityCls;
import cpp.lib.ClsQString;
import database.column.Column;
import database.relation.AbstractRelation;
import database.relation.OneRelation;
import util.CodeUtil2;

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
		EntityCls bean=(EntityCls) parent;
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
		ArrayList<String> sprintfTmpl = new ArrayList<>();// "%1." + cols.get(0).getEscapedName() + " as %1__" + cols.get(0).getName();

		
		for(Column col:cols) {
//			sprintfTmpl = sprintfTmpl + "," + "%1." + cols.get(i).getEscapedName() + " as %1__" + cols.get(i).getName();
			if(!col.isFileImportEnabled())
				sprintfTmpl.add( "%1." + col.getEscapedName() + " as %1__" + col.getName());
		}
		for(OneRelation r:oneRelations) {
			for(Column col:r.getDestTable().getAllColumns()) {
				sprintfTmpl.add( r.getAlias()+"." + col.getEscapedName() + " as "+r.getAlias()+"__" + col.getName());
			}
		}
		for(AbstractRelation r:manyRelations) {
			for(Column col:r.getDestTable().getAllColumns()) {
				sprintfTmpl.add( r.getAlias()+"." + col.getEscapedName() + " as "+r.getAlias()+"__" + col.getName());
			}
		}
		_return (QString.fromLatin1StringConstant(CodeUtil2.commaSep(sprintfTmpl) ).callMethod(ClsQString.arg,pAlias ));
		
		
	}

}
