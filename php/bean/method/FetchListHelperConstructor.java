package php.bean.method;

import java.util.ArrayList;
import java.util.List;

import database.relation.AbstractRelation;
import php.bean.BeanCls;
import php.core.Attr;
import php.core.Constructor;
import php.core.PhpCls;
import php.core.expression.ArrayInitExpression;

public class FetchListHelperConstructor extends Constructor{

	BeanCls bean;
	
	public FetchListHelperConstructor(BeanCls bean) {
		this.bean = bean;
	}
	
	@Override
	public void addImplementation() {
		List<AbstractRelation> manyRelations = new ArrayList<>();
		manyRelations.addAll(bean.getOneToManyRelations());
		manyRelations.addAll(bean.getManyToManyRelations());
		
		for(AbstractRelation r:manyRelations) {	
			Attr a = ((PhpCls)parent).getAttrByName(r.getAlias()+"Set"); 
			_assign(a, new ArrayInitExpression());
		}
	}

}
