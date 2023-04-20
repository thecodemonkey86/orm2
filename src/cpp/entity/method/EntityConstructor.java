package cpp.entity.method;

import java.util.List;

import cpp.core.Attr;
import cpp.core.Constructor;
import cpp.core.expression.BoolExpression;
import cpp.core.expression.Expression;
import cpp.entity.EntityCls;
import database.column.Column;
import database.relation.OneRelation;

public class EntityConstructor extends Constructor{
	protected boolean autoIncrement;
	protected List<Column> cols;
	
	public EntityConstructor(boolean autoIncrement, List<Column> cols) {
		this.autoIncrement = autoIncrement;
		this.cols = cols;
		// Shared Pointer due to circular dependency / forward declaration issue 
		try{
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public void addImplementation() {
		addPassToSuperConstructor( autoIncrement ? BoolExpression.TRUE : BoolExpression.FALSE);
		
		for(Column col:cols) {
			 
			if (!col.isPartOfPk() && !col.hasOneRelation() && !col.isFileImportEnabled()) {
				
				Expression defValExpr =  EntityCls.getDatabaseMapper().getColumnDefaultValueExpression(col);
				if (defValExpr != null) {
					_assign(parent.getAttrByName(col.getCamelCaseName()),  defValExpr);
				} else {
					_assign(parent.getAttrByName(col.getCamelCaseName()), EntityCls.getDatabaseMapper().getGenericDefaultValueExpression(col)); 
				}
				
				_assign(parent.getAttrByName(col.getCamelCaseName()+ "Modified"), BoolExpression.FALSE);
			}
			if(col.isRawValueEnabled()) {
				Attr a = parent.getAttrByName("insertExpression"+col.getUc1stCamelCaseName());
				if(a.getInitValue() != null)
					_assign(a, a.getInitValue());
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
