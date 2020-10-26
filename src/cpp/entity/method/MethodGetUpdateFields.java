package cpp.entity.method;

import java.util.List;

import cpp.Types;
import cpp.CoreTypes;
import cpp.core.Attr;
import cpp.core.Method;
import cpp.core.Param;
import cpp.core.QString;
import cpp.core.expression.CreateObjectExpression;
import cpp.core.expression.Expression;
import cpp.core.expression.InlineIfExpression;
import cpp.core.expression.Var;
import cpp.core.instruction.IfBlock;
import cpp.entity.EntityCls;
import cpp.entity.Nullable;
import cpp.lib.ClsQString;
import cpp.lib.ClsQStringList;
import cpp.lib.ClsQVariant;
import database.column.Column;
import database.relation.OneRelation;
import database.relation.PrimaryKey;

public class MethodGetUpdateFields extends Method{
	protected List<Column> cols;
	protected PrimaryKey pk;
	protected Param pParams;
	
	public MethodGetUpdateFields(List<Column> cols,PrimaryKey pk) {
		super(Public, Types.QStringList, "getUpdateFields");
		pParams = addParam(CoreTypes.QVariantList.toRawPointer(), "params");
		this.cols = cols;
		this.pk = pk;
		setConstQualifier();
	}

	@Override
	public void addImplementation() {
		EntityCls parent = (EntityCls) this.parent;
		Var fields = _declare(Types.QStringList, "fields");
		IfBlock ifIdModified= _if(parent.getAttrByName("primaryKeyModified"));
		for(Column colPk:pk.getColumns()) {
			
			Expression colAttr = parent.accessThisAttrGetterByColumn(colPk); //parent.getAttrByName(colPk.getCamelCaseName());
			
			if(colPk.isNullable())
				colAttr = colAttr.callMethod(Nullable.val);
			else if(colAttr.getType().equals(Types.QString))
				colAttr = new InlineIfExpression(colAttr.callMethod(ClsQString.isNull), QString.fromStringConstant(""), colAttr);
			ifIdModified.setIfInstr(
					fields.callMethodInstruction(ClsQStringList.append, QString.fromStringConstant(colPk.getEscapedName()+"=?"))
					,
					pParams.callMethodInstruction(ClsQStringList.append, Types.QVariant.callStaticMethod(ClsQVariant.fromValue, colAttr) )
					
					);
		}
		for(Column col: cols) {
			if(col.isFileImportEnabled()) {
				Attr attrFilePath = parent.getAttrByName(col.getCamelCaseName()+"FilePath");
				 _ifNot(attrFilePath.callMethod(ClsQString.isNull)).setIfInstr(
							fields.callMethodInstruction(ClsQStringList.append, QString.fromStringConstant(String.format("%s=%s(?)", col.getEscapedName(),EntityCls.getDatabase().getFileLoadFunction())))
							,
							pParams.callMethodInstruction(ClsQStringList.append,  Types.QVariant.callStaticMethod(ClsQVariant.fromValue, attrFilePath))
							
							);
			} else	{
				Expression colAttr = parent.accessThisAttrGetterByColumn(col);
				
				if (!col.isRelationSourceColumn()) {
				_if(parent.getAttrByName(col.getCamelCaseName()+"Modified"))
					.setIfInstr(
							fields.callMethodInstruction(ClsQStringList.append, QString.fromStringConstant(col.getEscapedName()+"=?"))
							,
							pParams.callMethodInstruction(ClsQStringList.append, col.isNullable() ? new InlineIfExpression(colAttr.callMethod(ClsQString.isNull), new CreateObjectExpression(CoreTypes.QVariant), colAttr.callMethod("val"))   : Types.QVariant.callStaticMethod(ClsQVariant.fromValue, colAttr.getType().equals(Types.QString) ? new InlineIfExpression(colAttr.callMethod(ClsQString.isNull), QString.fromStringConstant(""), colAttr): colAttr))
							
							);
				}
			}
		}
		
		for(OneRelation r:parent.getOneRelations()) {
			if (!r.isPartOfPk()) {
				IfBlock ifBlock= _if(parent.getAttrByName(parent.getOneRelationAttr(r).getName()+ "Modified"));
				
				for(int i=0;i<r.getColumnCount();i++) {
					Column col = r.getColumns(i).getValue1();
					Expression colAttr = parent.accessThisAttrGetterByColumn(col);
					if(col.isNullable()) {
						colAttr = colAttr.callMethod(Nullable.val);
					}
					ifBlock.setIfInstr(fields.callMethodInstruction(ClsQStringList.append, QString.fromStringConstant(col.getEscapedName()+"=?")),
							pParams.callMethodInstruction(ClsQStringList.append, Types.QVariant.callStaticMethod(ClsQVariant.fromValue, colAttr)));
				}
			}
			
		}
		_return(fields);
		
	}

}
