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

public class MethodAddManyToManyRelatedEntity extends Method {

	protected ManyRelation rel;
	
	public MethodAddManyToManyRelatedEntity(ManyRelation r, Param p) {
		super(Public, Types.Void, OrmUtil.getAddRelatedEntityMethodName(r));
		addParam(p);
		rel=r;
	}

	@Override
	public void addImplementation() {
		JavaCls parent = (JavaCls) this.parent;
		
		Attr a=parent.getAttrByName(OrmUtil.getManyRelationDestAttrName(rel));
		_if(a.isNull()).addIfInstr(a.assign(new NewOperator(a.getType())));
		Param pEntity = getParam("entity");
		addInstr(a.callMethod(ClsArrayList.add,pEntity).asInstruction());
		EntityCls relationEntity = Entities.get( rel.getDestTable());
		Attr aAdded = parent.getAttrByName(
				a.getName()+"Added");
		_if(aAdded.isNull()).addIfInstr(aAdded.assign(new NewOperator(aAdded.getType())));
		if (relationEntity.getTbl().getPrimaryKey().isMultiColumn()) {
			Type pkType=relationEntity.getPkType();
			
			Expression[] idAddedArgs = new Expression[relationEntity.getTbl().getPrimaryKey().getColumnCount()];
			
			for(int i = 0; i < idAddedArgs.length; i++) {
				idAddedArgs[i] = pEntity.callAttrGetter(relationEntity.getTbl().getPrimaryKey().getColumn(i).getCamelCaseName());
			}
			
			Var idAdded = _declareNew(pkType, "idAdded", idAddedArgs);
			for(Column col:relationEntity.getTbl().getPrimaryKey().getColumns()) {
				addInstr(idAdded.callAttrSetterMethodInstr(col
						.getCamelCaseName(), pEntity
						.callAttrGetter(
								col
								.getCamelCaseName()
						)));
			}
			
			addInstr(
					aAdded.callMethod(ClsArrayList.add,
							idAdded).asInstruction());	
				
		} else {
			
						
			addInstr(
					
					aAdded.callMethod(ClsArrayList.add,
									pEntity
									.callAttrGetter(
											relationEntity.getTbl().getPrimaryKey().getFirstColumn()
											.getCamelCaseName()
									)
								).asInstruction());	
		}
		
		
		
		

	}

}
