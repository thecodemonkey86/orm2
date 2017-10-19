package php.cls.bean.method;

import java.util.List;

import model.Column;
import model.OneRelation;
import php.Types;
import php.cls.Constructor;
import php.cls.PhpCls;
import php.cls.Param;
import php.cls.bean.BeanCls;
import php.cls.expression.BoolExpression;
import php.cls.expression.Expression;
import php.cls.expression.Expressions;
import php.cls.instruction.IfBlock;
import php.cls.instruction.SuperConstructorCall;

public class BeanConstructor extends Constructor{
	protected boolean autoIncrement;
	protected List<Column> cols;
	
	public BeanConstructor(boolean autoIncrement, List<Column> cols) {
		this.autoIncrement = autoIncrement;
		this.cols = cols;
//		addParam(new Param(Types.Sql, "sqlCon"));
		try{
//		addParam(new Param(Types.BeanRepository, "repository"));
//		addPassToSuperConstructor(params.get(0));
		}catch(Exception e) {
			e.printStackTrace();
		}
		
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
				
				Expression defValExpr =  BeanCls.getTypeMapper().getColumnDefaultValueExpression(col);
				/*if (defValExpr != null) {
					_assign(parent.getAttrByName(col.getCamelCaseName()),  defValExpr);
				} else {
					_assign(parent.getAttrByName(col.getCamelCaseName()), BeanCls.getTypeMapper().getGenericDefaultValueExpression(col));
				}*/
				_assign(parent.getAttrByName(col.getCamelCaseName()),  defValExpr);
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
