package sunjava.entity.method;

import java.util.List;

import database.column.Column;
import database.relation.OneRelation;
import sunjava.core.Constructor;
import sunjava.core.JavaCls;
import sunjava.core.Param;
import sunjava.core.Types;
import sunjava.core.expression.BoolExpression;
import sunjava.core.expression.Expression;
import sunjava.core.instruction.SuperConstructorCall;
import sunjava.entity.EntityCls;

public class EntityConstructor extends Constructor{
	protected boolean autoIncrement;
	protected List<Column> cols;
	protected boolean paramInsertNew;
	
	public EntityConstructor(boolean autoIncrement, boolean paramInsertNew, List<Column> cols) {
		this.autoIncrement = autoIncrement;
		this.cols = cols;
		if(paramInsertNew) {
			addParam(new Param(Types.Bool, "insertNew"));
		}
		this.paramInsertNew = paramInsertNew;
	}
	
	@Override
	public void addImplementation() {
		if(paramInsertNew) {
			addInstr(new SuperConstructorCall(getParam("insertNew")));
		}
		JavaCls parent = (JavaCls) this.parent;
		
		_assign(parent.getAttrByName("loaded"), BoolExpression.FALSE);		
		_assign(parent.getAttrByName("autoIncrement"), autoIncrement ? BoolExpression.TRUE : BoolExpression.FALSE);
		
		for(Column col:cols) {
			if (!col.isPartOfPk() && !col.hasOneRelation()) {
				_assign(parent.getAttrByName(col.getCamelCaseName()+ "Modified"), BoolExpression.FALSE);
				
				Expression defValExpr =  EntityCls.getTypeMapper().getDefaultValueExpression(col,col.getDefaultValue());
				/*if (defValExpr != null) {
					_assign(parent.getAttrByName(col.getCamelCaseName()),  defValExpr);
				} else {
					_assign(parent.getAttrByName(col.getCamelCaseName()), EntityCls.getTypeMapper().getGenericDefaultValueExpression(col));
				}*/
				_assign(parent.getAttrByName(col.getCamelCaseName()),  defValExpr);
			}
		    
		}
		EntityCls entity = (EntityCls) parent;
		for(OneRelation r:entity.getOneRelations()) {
			if (!r.isPartOfPk()) {
				_assign(parent.getAttrByName(entity.getOneRelationAttr(r).getName()+ "Modified"), BoolExpression.FALSE);
			}
		}
	}

}
