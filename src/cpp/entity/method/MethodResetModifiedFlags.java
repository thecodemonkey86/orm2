package cpp.entity.method;

import java.util.List;

import cpp.Types;
import cpp.core.Method;
import cpp.core.expression.BoolExpression;
import cpp.entity.EntityCls;
import database.column.Column;
import database.relation.OneRelation;

public class MethodResetModifiedFlags extends Method{
	
	public MethodResetModifiedFlags() {
		super(Public, Types.Void, "resetModifiedFlags");
	}

	@Override
	public void addImplementation() {
		EntityCls parent = (EntityCls) this.parent;
		addInstr( parent.getAttrByName("primaryKeyModified").assign(BoolExpression.FALSE));
		List<Column> cols = parent.getTbl().getColumnsWithoutPrimaryKey();
		for(Column col: cols) {
			addInstr( parent.getAttrByName(col.getCamelCaseName()+"Modified").assign(BoolExpression.FALSE));
		}
		
		for(OneRelation r:parent.getOneRelations()) {
			if (!r.isPartOfPk()) {
				addInstr( parent.getAttrByName(parent.getOneRelationAttr(r).getName()+ "Modified").assign(BoolExpression.FALSE));
				 
			}
			
		}
		
	}

}
