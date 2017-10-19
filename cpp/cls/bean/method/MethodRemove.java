package cpp.cls.bean.method;

import java.util.List;

import model.OneToManyRelation;
import cpp.Types;
import cpp.cls.Method;
import cpp.cls.expression.StaticMethodCall;

public class MethodRemove extends Method {
	protected List<OneToManyRelation>  oneToManyRelations;

	public MethodRemove( List<OneToManyRelation> oneToManyRelations) {
		super(Public, Types.Void, "remove");
		this.oneToManyRelations = oneToManyRelations;
	}

	

	@Override
	public void addImplementation() {
		addInstr(new StaticMethodCall(parent.getSuperclass(), parent.getMethod("save")).asInstruction()) ;
		for(OneToManyRelation r:oneToManyRelations) {
//			IfBlock ifRemoveBeans = _if(Expressions.not(parent.getAttrByName("_removed"+ StringUtil.ucfirst(PgCppUtil.getManyRelationDestAttrName(r)) )
//					.callMethod("empty")
//					));
			//String sql="delete from "+r.get;
			//ifRemoveBeans.getIfInstr().
			System.out.println(r);
		}
	}

}
