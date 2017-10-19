package php.cls.bean.method;

import java.util.ArrayList;
import java.util.List;

import model.AbstractRelation;
import php.cls.Attr;
import php.cls.Constructor;
import php.cls.PhpCls;
import php.cls.bean.BeanCls;
import php.cls.expression.ArrayInitExpression;

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
