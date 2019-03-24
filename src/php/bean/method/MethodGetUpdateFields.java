package php.bean.method;

import java.util.List;

import database.column.Column;
import database.relation.OneRelation;
import database.relation.PrimaryKey;
import php.bean.BeanCls;
import php.core.Param;
import php.core.Types;
import php.core.expression.Expression;
import php.core.expression.PhpStringLiteral;
import php.core.expression.Var;
import php.core.instruction.IfBlock;
import php.core.method.Method;
import php.lib.ClsSqlParam;

public class MethodGetUpdateFields extends Method{
	protected List<Column> cols;
	protected PrimaryKey pk;
	protected Param pParams;
	
	public MethodGetUpdateFields(List<Column> cols,PrimaryKey pk) {
		super(Public, Types.array(Types.String), "getUpdateFields");
		pParams = addParam(Types.array(Types.SqlParam), "params");
		this.cols = cols;
		this.pk = pk;
	}

	@Override
	public void addImplementation() {
		BeanCls parent = (BeanCls) this.parent;
		Var fields = _declareInitDefaultConstructor(Types.array(Types.String), "fields");
		IfBlock ifIdModified= _if(parent.getAttrByName("primaryKeyModified"));
		for(Column colPk:pk.getColumns()) {
			Expression colAttr = parent.accessThisAttrByColumn(colPk); //parent.getAttrByName(colPk.getCamelCaseName());
			ifIdModified.setIfInstr(
					fields.arrayPush( new PhpStringLiteral(colPk.getEscapedName()+"=?"))
					,
					getParam("params").arrayPush( Types.SqlParam.callStaticMethod(ClsSqlParam.getMethodName(BeanCls.getTypeMapper().columnToType(colPk)),colAttr))
					
					);
		}
		for(Column col: cols) {
				Expression colAttr = parent.accessThisAttrByColumn(col);
				
				if (!col.hasOneRelation()) {
				_if(parent.getAttrByName(col.getCamelCaseName()+"Modified"))
					.setIfInstr(
							fields.arrayPush(  new PhpStringLiteral(col.getEscapedName()+"=?"))
							,
							getParam("params").arrayPush(  Types.SqlParam.callStaticMethod(ClsSqlParam.getMethodName(BeanCls.getTypeMapper().columnToType(col)),colAttr))
							
							);
				}
		}
		
		for(OneRelation r:parent.getOneRelations()) {
			if (!r.isPartOfPk()) {
				IfBlock ifBlock= _if(parent.getAttrByName(parent.getOneRelationAttr(r).getName()+ "Modified"));
				
				for(int i=0;i<r.getColumnCount();i++) {
					Column col = r.getColumns(i).getValue1();
					Expression colAttr = parent.accessThisAttrByColumn(col);
					ifBlock.setIfInstr(fields.arrayPush( new PhpStringLiteral(col.getEscapedName()+"=?")),
							getParam("params").arrayPush(  colAttr));
				}
			}
			
		}
		_return(fields);
		
	}

	@Override
	public String toString() {
		if (parent.getName().equals("Track")) {
			System.out.println("");
		}
		return super.toString();
	}
}
