package sunjava.cls.bean.method;

import java.util.ArrayList;
import java.util.List;

import model.AbstractRelation;
import sunjava.cls.Attr;
import sunjava.cls.Constructor;
import sunjava.cls.JavaCls;
import sunjava.cls.bean.BeanCls;
import sunjava.cls.expression.NewOperator;

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
			Attr a = ((JavaCls)parent).getAttrByName(r.getAlias()+"Set"); 
			_assign(a, new NewOperator(a.getType()));
		}
	}

}
