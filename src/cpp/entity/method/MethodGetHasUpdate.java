package cpp.entity.method;

import java.util.ArrayList;
import java.util.List;

import cpp.Types;
import cpp.core.Method;
import cpp.core.expression.Expression;
import cpp.core.expression.Expressions;
import cpp.entity.EntityCls;
import database.column.Column;
import database.relation.OneRelation;
import database.table.Table;

public class MethodGetHasUpdate extends Method{
	protected Table tbl;
	
	public MethodGetHasUpdate(Table tbl) {
		super(Public, Types.Bool, "hasUpdate");
		this.tbl = tbl;
		setConstQualifier();
	}

	@Override
	public void addImplementation() {
		EntityCls parent = (EntityCls) this.parent;
//		Var fields = _declare(Types.QStringList, "fields");
//		IfBlock ifIdModified= _if(parent.getAttrByName("primaryKeyModified"));
		
		 List<Expression> conditions=new ArrayList<Expression>();
		conditions.add(parent.getAttrByName("primaryKeyModified"));
		 
		for(Column col: tbl.getColumnsWithoutPrimaryKey()) {
			if(!col.isFileImportEnabled()) {
				conditions.add(parent.getAttrByName(col.getCamelCaseName()+"Modified"));	
			}
		}
		
		for(OneRelation r:parent.getOneRelations()) {
			if (!r.isPartOfPk()) {
				conditions.add(parent.getAttrByName(parent.getOneRelationAttr(r).getName()+ "Modified"));
			}
			
		}
		_return(Expressions.or(conditions));
		
	}

}
