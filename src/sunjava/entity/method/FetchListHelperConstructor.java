package sunjava.entity.method;

import java.util.ArrayList;
import java.util.List;

import database.relation.AbstractRelation;
import sunjava.core.Attr;
import sunjava.core.Constructor;
import sunjava.core.JavaCls;
import sunjava.core.expression.NewOperator;
import sunjava.entity.EntityCls;

public class FetchListHelperConstructor extends Constructor{

	EntityCls bean;
	
	public FetchListHelperConstructor(EntityCls bean) {
		this.bean = bean;
	}
	
	@Override
	public void addImplementation() {
		List<AbstractRelation> manyRelations = new ArrayList<>();
		manyRelations.addAll(bean.getOneToManyRelations());
		manyRelations.addAll(bean.getManyToManyRelations());
		
		for(AbstractRelation r:manyRelations) {	
			Attr a = ((JavaCls)parent).getAttrByName(r.getAlias()+"Set"); 
			_assign(a, new NewOperator(a.getType()));
		}
	}

}
