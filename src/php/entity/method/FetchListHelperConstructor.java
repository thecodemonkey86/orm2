package php.entity.method;

import java.util.ArrayList;
import java.util.List;

import database.relation.AbstractRelation;
import php.core.Attr;
import php.core.Constructor;
import php.core.Param;
import php.core.PhpCls;
import php.core.expression.ArrayInitExpression;
import php.entity.EntityCls;

public class FetchListHelperConstructor extends Constructor{

	EntityCls entity;
	
	public FetchListHelperConstructor(EntityCls entity) {
		this.entity = entity;
		addParam(new Param(entity, "e1"));
	}
	
	@Override
	public void addImplementation() {
		addInstr(_this().assignAttr("e1", getParam("e1")));
		List<AbstractRelation> manyRelations = new ArrayList<>();
		manyRelations.addAll(entity.getOneToManyRelations());
		manyRelations.addAll(entity.getManyToManyRelations());
		
		for(AbstractRelation r:manyRelations) {	
			Attr a = ((PhpCls)parent).getAttrByName(r.getAlias()+"Set"); 
			_assign(a, new ArrayInitExpression());
		}
	}

}
