package php.cls.bean.repo.method;

import java.util.ArrayList;
import java.util.List;

import model.AbstractRelation;
import model.Column;
import php.PhpFunctions;
import php.Types;
import php.cls.Method;
import php.cls.Param;
import php.cls.bean.BeanCls;
import php.cls.expression.PhpStringLiteral;

public class MethodGetAllSelectFields extends Method  {

	protected BeanCls bean;
	public MethodGetAllSelectFields(BeanCls bean) {
		super(Public, Types.String, "getAllSelectFields"+ bean.getName());
		setStatic(true);
		addParam(new Param(Types.String, "alias", new PhpStringLiteral("b1")));
		this.bean = bean;
	}

	@Override
	public void addImplementation() {
		List<AbstractRelation> relations = new ArrayList<>(bean.getOneRelations().size()+ bean.getOneToManyRelations().size()+bean.getManyToManyRelations().size());
		relations.addAll(bean.getOneRelations());
		relations.addAll(bean.getOneToManyRelations());
		relations.addAll(bean.getManyToManyRelations());
		
		List<Column> cols = bean.getTbl().getAllColumns();
		String sprintfTmpl = "%1$s." + cols.get(0).getEscapedName() + " as %1$s__" + cols.get(0).getName();
		for(int i=1;i<cols.size();i++) {
			sprintfTmpl = sprintfTmpl + "," + "%1$s." + cols.get(i).getEscapedName() + " as %1$s__" + cols.get(i).getName();
		}
		
		for(AbstractRelation r:relations) {
			for(Column col:r.getDestTable().getAllColumns()) {
				sprintfTmpl = sprintfTmpl + "," + r.getAlias()+"." + col.getEscapedName() + " as " + r.getAlias()+"__" + col.getName();
			}
		}
		_return (PhpFunctions.sprintf.call(new PhpStringLiteral(sprintfTmpl),getParam("alias")));	
	}

}
