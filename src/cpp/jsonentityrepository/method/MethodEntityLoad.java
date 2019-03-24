package cpp.jsonentityrepository.method;

import java.util.List;

import cpp.Types;
import cpp.core.Method;
import cpp.core.Param;
import cpp.jsonentity.JsonEntity;
import database.relation.ManyRelation;
import database.relation.OneRelation;
import database.relation.OneToManyRelation;
import database.relation.PrimaryKey;

public class MethodEntityLoad extends Method {

	protected List<OneRelation> oneRelations;
	protected List<OneToManyRelation> oneToManyRelations;
	protected List<ManyRelation> manyRelations;
	protected PrimaryKey primaryKey;
	protected JsonEntity bean;
	protected Param pBean;
	
	public static String getMethodName() {
		return "load";
	}
	
	public MethodEntityLoad(JsonEntity bean) {
		super(Public, Types.Void, getMethodName());
		
		this.oneRelations = bean.getOneRelations();
		this.oneToManyRelations = bean.getOneToManyRelations();
		this.manyRelations = bean.getManyRelations();
		this.primaryKey = bean.getTbl().getPrimaryKey();
		this.bean = bean;
		pBean = addParam(bean.toRawPointer(), "bean");
		setStatic(true);
	}

	@Override
	public boolean includeIfEmpty() {
		return true;
	}
	
	@Override
	public void addImplementation() {
		
	}

}
