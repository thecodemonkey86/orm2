package php.entityrepository.method;

import java.util.ArrayList;
import java.util.List;

import database.column.Column;
import database.relation.AbstractRelation;
import php.core.Param;
import php.core.PhpFunctions;
import php.core.Types;
import php.core.expression.PhpStringLiteral;
import php.core.method.Method;
import php.entity.EntityCls;

public class MethodGetAllSelectFields extends Method  {

	protected EntityCls entity;
	public MethodGetAllSelectFields(EntityCls entity) {
		super(Public, Types.String, "getAllSelectFields"+ entity.getName());
		setStatic(true);
		addParam(new Param(Types.String, "alias", new PhpStringLiteral("e1")));
		this.entity = entity;
	}

	@Override
	public void addImplementation() {
		List<AbstractRelation> relations = new ArrayList<>(entity.getOneRelations().size()+ entity.getOneToManyRelations().size()+entity.getManyToManyRelations().size());
		relations.addAll(entity.getOneRelations());
		relations.addAll(entity.getOneToManyRelations());
		relations.addAll(entity.getManyToManyRelations());
		
		List<Column> cols = entity.getTbl().getAllColumns();
		String sprintfTmpl = (cols.get(0).hasOverrideSelect()?cols.get(0).getOverrideSelect():  "%1$s." + cols.get(0).getEscapedName()) + " as %1$s__" + cols.get(0).getName();
		for(int i=1;i<cols.size();i++) {
			sprintfTmpl = sprintfTmpl + "," + (cols.get(i).hasOverrideSelect()?cols.get(i).getOverrideSelect():  "%1$s." + cols.get(i).getEscapedName()) + " as %1$s__" + cols.get(i).getName();
		}
		
		for(AbstractRelation r:relations) {
			for(Column col:r.getDestTable().getAllColumns()) {
				sprintfTmpl = sprintfTmpl + "," + r.getAlias()+"." + col.getEscapedName() + " as " + r.getAlias()+"__" + col.getName();
			}
		}
		_return (PhpFunctions.sprintf.call(new PhpStringLiteral(sprintfTmpl),getParam("alias")));	
	}

}
