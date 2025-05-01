package php.entity.method;

import java.util.List;

import database.relation.OneToManyRelation;
import php.core.PhpCls;
import php.core.Types;
import php.core.expression.StaticMethodCall;
import php.core.method.Method;

public class MethodRemove extends Method {
	protected List<OneToManyRelation>  oneToManyRelations;

	public MethodRemove( List<OneToManyRelation> oneToManyRelations) {
		super(Public, Types.Void, "remove");
		this.oneToManyRelations = oneToManyRelations;
	}

	

	@Override
	public void addImplementation() {
		PhpCls parent = (PhpCls) this.parent;
		addInstr(new StaticMethodCall(parent.getSuperclass(), parent.getMethod("save")).asInstruction()) ;
		for(OneToManyRelation r:oneToManyRelations) {
			System.out.println(r);
		}
	}

}
