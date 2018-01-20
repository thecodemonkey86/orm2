package cpp.bean.method;

import java.util.List;

import cpp.Types;
import cpp.CoreTypes;
import cpp.bean.BeanCls;
import cpp.bean.Nullable;
import cpp.core.Method;
import cpp.core.Param;
import cpp.core.QString;
import cpp.core.expression.CreateObjectExpression;
import cpp.core.expression.Expression;
import cpp.core.expression.InlineIfExpression;
import cpp.core.expression.Var;
import cpp.core.instruction.IfBlock;
import database.column.Column;
import database.relation.OneRelation;
import database.relation.PrimaryKey;

public class MethodGetUpdateFields extends Method{
	protected List<Column> cols;
	protected PrimaryKey pk;
	public MethodGetUpdateFields(List<Column> cols,PrimaryKey pk) {
		super(Public, Types.QStringList, "getUpdateFields");
		addParam(new Param(CoreTypes.QVariantList.toRawPointer(), "params"));
		this.cols = cols;
		this.pk = pk;
	}

	@Override
	public void addImplementation() {
		BeanCls parent = (BeanCls) this.parent;
		Var fields = _declare(Types.QStringList, "fields");
		IfBlock ifIdModified= _if(parent.getAttrByName("primaryKeyModified"));
		for(Column colPk:pk.getColumns()) {
			
			Expression colAttr = parent.accessThisAttrGetterByColumn(colPk); //parent.getAttrByName(colPk.getCamelCaseName());
			
			if(colPk.isNullable())
				colAttr = colAttr.callMethod(Nullable.val);
			ifIdModified.setIfInstr(
					fields.callMethodInstruction("append", QString.fromStringConstant(colPk.getEscapedName()+"=?"))
					,
					paramByName("params").callMethodInstruction("append",  colAttr)
					
					);
		}
		for(Column col: cols) {
				Expression colAttr = parent.accessThisAttrGetterByColumn(col);
				
				if (!col.hasOneRelation()) {
				_if(parent.getAttrByName(col.getCamelCaseName()+"Modified"))
					.setIfInstr(
							fields.callMethodInstruction("append", QString.fromStringConstant(col.getEscapedName()+"=?"))
							,
							paramByName("params").callMethodInstruction("append", col.isNullable() ? new InlineIfExpression(colAttr.callMethod("isNull"), new CreateObjectExpression(CoreTypes.QVariant), colAttr.callMethod("val"))   : colAttr)
							
							);
				}
		}
		
		for(OneRelation r:parent.getOneRelations()) {
			if (!r.isPartOfPk()) {
				IfBlock ifBlock= _if(parent.getAttrByName(parent.getOneRelationAttr(r).getName()+ "Modified"));
				
				for(int i=0;i<r.getColumnCount();i++) {
					Column col = r.getColumns(i).getValue1();
					Expression colAttr = parent.accessThisAttrGetterByColumn(col);
					ifBlock.setIfInstr(fields.callMethodInstruction("append", QString.fromStringConstant(col.getEscapedName()+"=?")),
							paramByName("params").callMethodInstruction("append", colAttr));
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
