package php.cls.bean.repo.method;
//
//import java.util.List;
//
//import model.OneToManyRelation;
//import model.PrimaryKey;
//import model.OneRelation;
//import php.Types;
//import php.cls.Method;
//import php.cls.bean.BeanCls;
//import php.cls.bean.repo.ClsBeanRepository;
//import php.cls.bean.repo.expression.ThisBeanRepositoryExpression;
//
//
//public class MethodFetchList extends Method {
//
//	protected List<OneRelation> oneRelations;
//	protected List<OneToManyRelation> manyRelations;
//	protected PrimaryKey pk;
//	protected BeanCls bean;
//	
//	public MethodFetchList(List<OneRelation> oneRelations,List<OneToManyRelation> manyRelations, BeanCls bean,PrimaryKey pk) {
//		super(Public, Types.array(bean), "fetchList"+bean.getName() );
//		this.oneRelations = oneRelations;
//		this.manyRelations = manyRelations;
//		this.pk = pk;
//		this.bean = bean;
//	}
//	
//	@Override
//	public ThisBeanRepositoryExpression _this() {
//		return new ThisBeanRepositoryExpression((ClsBeanRepository) parent);
//	}
//
//	@Override
//	public void addImplementation() {
//
//	}
//	
//	@Override
//	public String toString() {
//		// TODO Auto-generated method stub
//		return super.toString();
//	}
//
//}
