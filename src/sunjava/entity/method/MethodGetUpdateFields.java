package sunjava.entity.method;

import java.util.List;

import database.column.Column;
import database.relation.OneRelation;
import database.relation.PrimaryKey;
import sunjava.core.JavaString;
import sunjava.core.Method;
import sunjava.core.Param;
import sunjava.core.Types;
import sunjava.core.expression.Expression;
import sunjava.core.expression.Var;
import sunjava.core.instruction.IfBlock;
import sunjava.entity.EntityCls;
import sunjava.lib.ClsArrayList;
import sunjava.lib.ClsSqlParam;

public class MethodGetUpdateFields extends Method{
	protected List<Column> cols;
	protected PrimaryKey pk;
	public MethodGetUpdateFields(List<Column> cols,PrimaryKey pk) {
		super(Public, Types.arraylist(Types.String), "getUpdateFields");
		addParam(new Param(Types.arraylist(Types.SqlParam), "params"));
		this.cols = cols;
		this.pk = pk;
		setOverrideAnnotation(true);
	}

	@Override
	public void addImplementation() {
		EntityCls parent = (EntityCls) this.parent;
		Var fields = _declareInitDefaultConstructor(Types.arraylist(Types.String), "fields");
		IfBlock ifIdModified= _if(parent.getAttrByName("primaryKeyModified"));
		for(Column colPk:pk.getColumns()) {
			Expression colAttr = parent.accessThisAttrByColumn(colPk); //parent.getAttrByName(colPk.getCamelCaseName());
			ifIdModified.setIfInstr(
					fields.callMethodInstruction(ClsArrayList.add, JavaString.stringConstant(colPk.getEscapedName()+"=?"))
					,
					paramByName("params").callMethodInstruction(ClsArrayList.add,  Types.SqlParam.callStaticMethod(ClsSqlParam.get,colAttr))
					
					);
		}
		for(Column col: cols) {
				Expression colAttr = parent.accessThisAttrByColumn(col);
				
				if (!col.hasOneRelation()) {
				_if(parent.getAttrByName(col.getCamelCaseName()+"Modified"))
					.setIfInstr(
							fields.callMethodInstruction(ClsArrayList.add, JavaString.stringConstant(col.getEscapedName()+"=?"))
							,
							paramByName("params").callMethodInstruction(ClsArrayList.add, Types.SqlParam.callStaticMethod(ClsSqlParam.get,colAttr))
							
							);
				}
		}
		
		for(OneRelation r:parent.getOneRelations()) {
			if (!r.isPartOfPk()) {
				IfBlock ifBlock= _if(parent.getAttrByName(parent.getOneRelationAttr(r).getName()+ "Modified"));
				
				for(int i=0;i<r.getColumnCount();i++) {
					Column col = r.getColumns(i).getValue1();
					Expression colAttr = parent.accessThisAttrByColumn(col);
					ifBlock.setIfInstr(fields.callMethodInstruction(ClsArrayList.add, JavaString.stringConstant(col.getEscapedName()+"=?")),
							paramByName("params").callMethodInstruction(ClsArrayList.add, colAttr));
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
