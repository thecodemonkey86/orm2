package sunjava.cls.bean.repo.method;

import java.util.List;

import model.OneToManyRelation;
import model.PrimaryKey;
import model.OneRelation;
import sunjava.Types;
import sunjava.cls.Method;
import sunjava.cls.bean.BeanCls;
import sunjava.cls.bean.repo.ClsBeanRepository;
import sunjava.cls.bean.repo.expression.ThisBeanRepositoryExpression;


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
