package cpp.entity.method;

import java.util.List;

import cpp.CoreTypes;
import cpp.core.Method;
import cpp.core.expression.BoolExpression;
import cpp.core.expression.Expressions;
import cpp.lib.ClsQList;
import cpp.orm.OrmUtil;
import database.relation.ManyRelation;
import database.relation.OneRelation;
import database.relation.OneToManyRelation;

public class MethodUnload extends Method {

	List<OneRelation> oneRelations;
	List<OneToManyRelation> oneToManyRelations;
	List<ManyRelation> manyRelations;
	
	public MethodUnload(List<OneRelation> oneRelations,
			List<OneToManyRelation> oneToManyRelations,
			List<ManyRelation> manyRelations) {
		super(Public, CoreTypes.Void, "unload");
		this.oneRelations = oneRelations;
		this.oneToManyRelations = oneToManyRelations;
		this.manyRelations = manyRelations;
		/*public static String getMethodName(ManyRelation r) {
		return "get"+StringUtil.ucfirst(PgCppUtil.getManyRelationDestAttrName(r));
	}*/
	}

	@Override
	public void addImplementation() {
		for(ManyRelation relation : manyRelations) {
			addInstr(parent.getAttrByName(OrmUtil.getManyRelationDestAttrName(relation)).callMethod(ClsQList.clear).asInstruction());
			addInstr( parent.getAttrByName("loaded"+relation.getIdentifier()).assign(BoolExpression.FALSE));
		}
		for(OneToManyRelation relation : oneToManyRelations) {
			addInstr( parent.getAttrByName(OrmUtil.getOneToManyRelationDestAttrName(relation)).callMethod(ClsQList.clear).asInstruction());
			addInstr( parent.getAttrByName("loaded"+relation.getIdentifier()).assign(BoolExpression.FALSE));
		}
		for(OneRelation relation : oneRelations) {
			addInstr(parent.getAttrByName(OrmUtil.getOneRelationDestAttrName(relation)).assign(Expressions.Nullptr));
			addInstr( parent.getAttrByName("loaded"+relation.getIdentifier()).assign(BoolExpression.FALSE));
		}
		

	}

}
