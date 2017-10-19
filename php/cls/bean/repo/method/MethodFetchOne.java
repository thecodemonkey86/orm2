package php.cls.bean.repo.method;


//public class MethodFetchOne extends Method {
//
//	protected List<OneRelation> oneRelations;
//	protected List<OneToManyRelation> manyRelations;
//	protected PrimaryKey pk;
//	protected BeanCls bean;
//	
//	public MethodFetchOne(List<OneRelation> oneRelations,List<OneToManyRelation> manyRelations, BeanCls bean,PrimaryKey pk) {
//		super(Public, bean, "fetchOne"+bean.getName() );
////		addParam(new Param(Types.QSqlQuery, "query"));	
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
////		_return(parent.callStaticMethod("fetchOne"+bean.getName()+"Static", _this().accessAttr("sqlCon"), getParam("query")));
//		_return(Expressions.Null);
//	}
//	
//	@Override
//	public String toString() {
//		// TODO Auto-generated method stub
//		return super.toString();
//	}
//
//}
