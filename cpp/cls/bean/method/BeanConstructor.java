package cpp.cls.bean.method;

import java.util.List;

import model.Column;
import model.OneRelation;
import cpp.Types;
import cpp.cls.Constructor;
import cpp.cls.Param;
import cpp.cls.bean.BeanCls;
import cpp.cls.expression.BoolExpression;
import cpp.cls.expression.Expression;

public class BeanConstructor extends Constructor{
	protected boolean autoIncrement;
	protected List<Column> cols;
	
	public BeanConstructor(boolean autoIncrement, List<Column> cols) {
		this.autoIncrement = autoIncrement;
		this.cols = cols;
		// Shared Pointer due to circular dependency / forward declaration issue 
		addParam(new Param(Types.BeanRepository.toSharedPtr(), "repository"));
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
			if (!col.isPartOfPk() && !col.hasOneRelation()) {
				_assign(parent.getAttrByName(col.getCamelCaseName()+ "Modified"), BoolExpression.FALSE);
				
				Expression defValExpr =  BeanCls.getDatabaseMapper().getColumnDefaultValueExpression(col);
				if (defValExpr != null) {
					_assign(parent.getAttrByName(col.getCamelCaseName()),  defValExpr);
				} else {
					_assign(parent.getAttrByName(col.getCamelCaseName()), BeanCls.getDatabaseMapper().getGenericDefaultValueExpression(col)); 
				}
			}
		    
		}
		BeanCls bean = (BeanCls) parent;
		for(OneRelation r:bean.getOneRelations()) {
			if (!r.isPartOfPk()) {
				_assign(parent.getAttrByName(bean.getOneRelationAttr(r).getName()+ "Modified"), BoolExpression.FALSE);
			}
		}
	}

}
