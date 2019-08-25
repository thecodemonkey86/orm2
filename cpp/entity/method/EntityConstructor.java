package cpp.entity.method;

import java.util.List;

import cpp.Types;
import cpp.core.Attr;
import cpp.core.Constructor;
import cpp.core.Param;
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
		addParam(new Param(Types.EntityRepository.toSharedPtr(), "repository"));
		try{
//		addParam(new Param(Types.BeanRepository.toRawPointer(), "repository"));
//		addPassToSuperConstructor(params.get(0));
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public void addImplementation() {
		_assign(_this().accessAttr("repository"), getParam("repository"));
		_assign(parent.getAttrByName("loaded"), BoolExpression.FALSE);		
		_assign(parent.getAttrByName("autoIncrement"), autoIncrement ? BoolExpression.TRUE : BoolExpression.FALSE);
		
		for(Column col:cols) {
			if(col.getName().equals("is_male")) {
				System.out.println();
			}
			if (!col.isPartOfPk() && !col.hasOneRelation()) {
				_assign(parent.getAttrByName(col.getCamelCaseName()+ "Modified"), BoolExpression.FALSE);
				
				Expression defValExpr =  EntityCls.getDatabaseMapper().getColumnDefaultValueExpression(col);
				if (defValExpr != null) {
					_assign(parent.getAttrByName(col.getCamelCaseName()),  defValExpr);
				} else {
					_assign(parent.getAttrByName(col.getCamelCaseName()), EntityCls.getDatabaseMapper().getGenericDefaultValueExpression(col)); 
				}
			}
			if(col.isRawValueEnabled()) {
				Attr a = parent.getAttrByName("insertExpression"+col.getUc1stCamelCaseName());
				if(a.getInitValue() != null)
					_assign(a, a.getInitValue());
			}
		    
		}
		EntityCls bean = (EntityCls) parent;
		for(OneRelation r:bean.getOneRelations()) {
			if (!r.isPartOfPk()) {
				_assign(parent.getAttrByName(bean.getOneRelationAttr(r).getName()+ "Modified"), BoolExpression.FALSE);
			}
		}
	}

}
