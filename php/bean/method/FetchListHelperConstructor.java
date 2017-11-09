package php.bean.method;

import java.util.ArrayList;
import java.util.List;

import database.relation.AbstractRelation;
import php.bean.BeanCls;
import php.core.Attr;
import php.core.Constructor;
import php.core.Param;
import php.core.PhpCls;
import php.core.expression.ArrayInitExpression;

public class FetchListHelperConstructor extends Constructor{

	BeanCls bean;
	
	public FetchListHelperConstructor(BeanCls bean) {
		this.bean = bean;
		addParam(new Param(bean, "b1"));
	}
	
	@Override
	public void addImplementation() {
		addInstr(_this().assignAttr("b1", getParam("b1")));
		List<AbstractRelation> manyRelations = new ArrayList<>();
		manyRelations.addAll(bean.getOneToManyRelations());
		manyRelations.addAll(bean.getManyToManyRelations());
		
		for(AbstractRelation r:manyRelations) {	
			Attr a = ((PhpCls)parent).getAttrByName(r.getAlias()+"Set"); 
			_assign(a, new ArrayInitExpression());
		}
	}

}
