package sunjava.cls.bean.method;

import java.util.List;

import model.Column;
import model.OneRelation;
import sunjava.Types;
import sunjava.cls.Constructor;
import sunjava.cls.JavaCls;
import sunjava.cls.Param;
import sunjava.cls.bean.BeanCls;
import sunjava.cls.expression.BoolExpression;
import sunjava.cls.expression.Expression;
import sunjava.cls.instruction.SuperConstructorCall;

public class BeanConstructor extends Constructor{
	protected boolean autoIncrement;
	protected List<Column> cols;
	protected boolean paramInsertNew;
	
	public BeanConstructor(boolean autoIncrement, boolean paramInsertNew, List<Column> cols) {
		this.autoIncrement = autoIncrement;
		this.cols = cols;
//		addParam(new Param(Types.Sql, "sqlCon"));
		try{
//		addParam(new Param(Types.BeanRepository, "repository"));
//		addPassToSuperConstructor(params.get(0));
		}catch(Exception e) {
			e.printStackTrace();
		}
		
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
				
				Expression defValExpr =  BeanCls.getTypeMapper().getDefaultValueExpression(col,col.getDefaultValue());
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
