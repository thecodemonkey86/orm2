package sunjava.cls.bean.method;

import java.util.List;

import model.Column;
import model.OneRelation;
import model.PrimaryKey;
import sunjava.Types;
import sunjava.cls.JavaString;
import sunjava.cls.Method;
import sunjava.cls.Param;
import sunjava.cls.bean.BeanCls;
import sunjava.cls.expression.Expression;
import sunjava.cls.expression.Var;
import sunjava.cls.instruction.IfBlock;
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
		BeanCls parent = (BeanCls) this.parent;
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
