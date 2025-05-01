package sunjava.entity.method;

import database.column.Column;
import database.relation.ManyRelation;
import sunjava.core.Attr;
import sunjava.core.JavaCls;
import sunjava.core.Method;
import sunjava.core.Param;
import sunjava.core.Type;
import sunjava.core.Types;
import sunjava.core.expression.Expression;
import sunjava.core.expression.NewOperator;
import sunjava.core.expression.Var;
import sunjava.entity.Entities;
import sunjava.entity.EntityCls;
import sunjava.lib.ClsArrayList;
import sunjava.orm.OrmUtil;
import util.StringUtil;

public class MethodRemoveManyToManyRelatedEntity extends Method {

	protected ManyRelation rel;
	
	public MethodRemoveManyToManyRelatedEntity(ManyRelation r, Param p) {
		super(Public, Types.Void, "remove"+StringUtil.ucfirst(OrmUtil.getManyRelationDestAttrNameSingular(r)));
		addParam(p);
		rel=r;
	}

	@Override
	public void addImplementation() {
		JavaCls parent = (JavaCls) this.parent;
		Attr a=parent.getAttrByName(OrmUtil.getManyRelationDestAttrName(rel));
		addInstr(a.callMethod(ClsArrayList.remove,getParam("entity")).asInstruction());
		EntityCls relationEntity = Entities.get( rel.getDestTable());
		Param pEntity = getParam("entity");
		Attr aRemoved = parent.getAttrByName(
				a.getName()+"Removed");
		_if(aRemoved.isNull()).addIfInstr(aRemoved.assign(new NewOperator(aRemoved.getType())));
		
		if (relationEntity.getTbl().getPrimaryKey().isMultiColumn()) {
			Type pkType=relationEntity.getPkType();
Expression[] idAddedArgs = new Expression[relationEntity.getTbl().getPrimaryKey().getColumnCount()];
			
			for(int i = 0; i < idAddedArgs.length; i++) {
				idAddedArgs[i] = pEntity.callAttrGetter(relationEntity.getTbl().getPrimaryKey().getColumn(i).getCamelCaseName());
			}
			Var idRemoved = _declareNew(pkType, "idRemoved", idAddedArgs);
			for(Column col:relationEntity.getTbl().getPrimaryKey().getColumns()) {
				addInstr( idRemoved.callAttrSetterMethodInstr(col
						.getCamelCaseName(), getParam("entity")
						.callAttrGetter(
								col
								.getCamelCaseName()
						)));
			}
			addInstr(
					aRemoved
							.callMethod(ClsArrayList.add,
									idRemoved
								).asInstruction());	
				
		} else {
			addInstr(
					aRemoved
							.callMethod(ClsArrayList.add,
									getParam("entity")
									.callAttrGetter(
											relationEntity.getTbl().getPrimaryKey().getFirstColumn()
											.getCamelCaseName()
									)
								).asInstruction());	
		}
	}

}
