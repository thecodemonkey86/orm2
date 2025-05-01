package cpp.entity.method;

import java.util.List;

import cpp.core.Attr;
import cpp.core.Constructor;
import cpp.core.expression.BoolExpression;
import cpp.core.expression.Expression;
import cpp.entity.EntityCls;
import database.column.Column;
import database.relation.AbstractRelation;
import database.relation.OneRelation;

public class EntityConstructor extends Constructor{
	protected boolean autoIncrement;
	protected List<Column> cols;
	
	public EntityConstructor(boolean autoIncrement, List<Column> cols) {
		this.autoIncrement = autoIncrement;
		this.cols = cols;		
	}
	
	@Override
	public void addImplementation() {
		addPassToSuperConstructor( autoIncrement ? BoolExpression.TRUE : BoolExpression.FALSE);
		EntityCls entity = (EntityCls) parent;
		for(AbstractRelation r:entity.getAllRelations()) {
			_assign(parent.getAttrByName("loaded"+ r.getIdentifier()), BoolExpression.FALSE);
		}
		for(Column col:cols) {
			 
			if (!col.hasOneRelation() && !col.isFileImportEnabled()) {
				
				Expression defValExpr =  EntityCls.getDatabaseMapper().getColumnDefaultValueExpression(col);
				if (defValExpr != null) {
					_assign(parent.getAttrByName(col.getCamelCaseName()),  defValExpr);
				} else {
					_assign(parent.getAttrByName(col.getCamelCaseName()), EntityCls.getDatabaseMapper().getGenericDefaultValueExpression(col)); 
				}
				if (!col.isPartOfPk()) {
					_assign(parent.getAttrByName(col.getCamelCaseName()+ "Modified"), BoolExpression.FALSE);
				} else {
					if (defValExpr != null) {
						_assign(parent.getAttrByName(col.getCamelCaseName()+"Previous"),  defValExpr);
					} else {
						_assign(parent.getAttrByName(col.getCamelCaseName()+"Previous"), EntityCls.getDatabaseMapper().getGenericDefaultValueExpression(col)); 
					}
				}
			}
			if(col.isRawValueEnabled()) {
				Attr a = parent.getAttrByName("insertExpression"+col.getUc1stCamelCaseName());
				if(a.getInitValue() != null)
					_assign(a, a.getInitValue());
			}
		    
		}
		
		for(OneRelation r:entity.getOneRelations()) {
			if (!r.isPartOfPk()) {
				_assign(parent.getAttrByName(entity.getOneRelationAttr(r).getName()+ "Modified"), BoolExpression.FALSE);
			}
		}
		
	}

}
