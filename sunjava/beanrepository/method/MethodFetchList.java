package sunjava.beanrepository.method;

import java.util.List;

import database.relation.OneRelation;
import database.relation.OneToManyRelation;
import database.relation.PrimaryKey;
import sunjava.bean.BeanCls;
import sunjava.beanrepository.ClsBeanRepository;
import sunjava.beanrepository.expression.ThisBeanRepositoryExpression;
import sunjava.core.Method;
import sunjava.core.Types;


public class MethodFetchList extends Method {

	protected List<OneRelation> oneRelations;
	protected List<OneToManyRelation> manyRelations;
	protected PrimaryKey pk;
	protected BeanCls bean;
	
	public MethodFetchList(List<OneRelation> oneRelations,List<OneToManyRelation> manyRelations, BeanCls bean,PrimaryKey pk) {
		super(Public, Types.arraylist(bean), "fetchList"+bean.getName() );
		this.oneRelations = oneRelations;
		this.manyRelations = manyRelations;
		this.pk = pk;
		this.bean = bean;
	}
	
	@Override
	public ThisBeanRepositoryExpression _this() {
		return new ThisBeanRepositoryExpression((ClsBeanRepository) parent);
	}

	@Override
	public void addImplementation() {

	}
	
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return super.toString();
	}

}
