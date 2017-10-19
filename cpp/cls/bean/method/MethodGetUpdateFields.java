package cpp.cls.bean.method;

import java.util.List;

import model.Column;
import model.OneRelation;
import model.PrimaryKey;
import cpp.Types;
import cpp.cls.Method;
import cpp.cls.Param;
import cpp.cls.QString;
import cpp.cls.bean.BeanCls;
import cpp.cls.expression.CreateObjectExpression;
import cpp.cls.expression.Expression;
import cpp.cls.expression.InlineIfExpression;
import cpp.cls.expression.Var;
import cpp.cls.instruction.IfBlock;

public class MethodGetUpdateFields extends Method{
	protected List<Column> cols;
	protected PrimaryKey pk;
	public MethodGetUpdateFields(List<Column> cols,PrimaryKey pk) {
		super(Public, Types.QStringList, "getUpdateFields");
		addParam(new Param(Types.QVariantList.toRawPointer(), "params"));
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
							paramByName("params").callMethodInstruction("append", col.isNullable() ? new InlineIfExpression(colAttr.callMethod("isNull"), new CreateObjectExpression(Types.QVariant), colAttr.callMethod("val"))   : colAttr)
							
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
