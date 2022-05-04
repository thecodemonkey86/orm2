package php.entity.method;

import java.util.List;

import database.column.Column;
import database.relation.OneRelation;
import php.core.Attr;
import php.core.Constructor;
import php.core.Param;
import php.core.PhpCls;
import php.core.Types;
import php.core.expression.BoolExpression;
import php.core.expression.Expression;
import php.core.instruction.SuperConstructorCall;
import php.entity.EntityCls;

public class BeanConstructor extends Constructor{
	protected boolean autoIncrement;
	protected List<Column> cols;
	
	public BeanConstructor(boolean autoIncrement, List<Column> cols) {
		this.autoIncrement = autoIncrement;
		this.cols = cols;
		addParam(new Param(Types.Bool, "insertNew", BoolExpression.TRUE));
	}
	
	@Override
	public void addImplementation() {
		addInstr(new SuperConstructorCall(getParam("insertNew")));
		
		PhpCls parent = (PhpCls) this.parent;
		
		_assign(parent.getAttrByName("loaded"), BoolExpression.FALSE);		
		_assign(parent.getAttrByName("autoIncrement"), autoIncrement ? BoolExpression.TRUE : BoolExpression.FALSE);
		
		for(Column col:cols) {
			if (!col.isPartOfPk() && !col.hasOneRelation()) {
				_assign(parent.getAttrByName(col.getCamelCaseName()+ "Modified"), BoolExpression.FALSE);
				
				Expression defValExpr =  EntityCls.getTypeMapper().getColumnDefaultValueExpression(col);
				_assign(parent.getAttrByName(col.getCamelCaseName()),  defValExpr);
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
