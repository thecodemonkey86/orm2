package sunjava.cls.bean.method;

import sunjava.Types;
import sunjava.cls.Attr;
import sunjava.cls.JavaCls;
import sunjava.cls.Method;
import sunjava.cls.Param;
import sunjava.cls.Type;
import sunjava.cls.bean.BeanCls;
import sunjava.cls.bean.Beans;
import sunjava.cls.expression.Expression;
import sunjava.cls.expression.NewOperator;
import sunjava.cls.expression.Var;
import sunjava.lib.ClsArrayList;
import sunjava.orm.OrmUtil;
import util.StringUtil;
import model.Column;
import model.ManyRelation;

public class MethodRemoveManyToManyRelatedBean extends Method {

	protected ManyRelation rel;
	
	public MethodRemoveManyToManyRelatedBean(ManyRelation r, Param p) {
		super(Public, Types.Void, "remove"+StringUtil.ucfirst(OrmUtil.getManyRelationDestAttrNameSingular(r)));
		addParam(p);
		rel=r;
	}

	@Override
	public void addImplementation() {
		JavaCls parent = (JavaCls) this.parent;
		Attr a=parent.getAttrByName(OrmUtil.getManyRelationDestAttrName(rel));
		addInstr(a.callMethod(ClsArrayList.remove,getParam("bean")).asInstruction());
		BeanCls relationBean = Beans.get( rel.getDestTable());
		Param pBean = getParam("bean");
		Attr aRemoved = parent.getAttrByName(
				a.getName()+"Removed");
		_if(aRemoved.isNull()).addIfInstr(aRemoved.assign(new NewOperator(aRemoved.getType())));
		
		if (relationBean.getTbl().getPrimaryKey().isMultiColumn()) {
			Type pkType=relationBean.getPkType();
Expression[] idAddedArgs = new Expression[relationBean.getTbl().getPrimaryKey().getColumnCount()];
			
			for(int i = 0; i < idAddedArgs.length; i++) {
				idAddedArgs[i] = pBean.callAttrGetter(relationBean.getTbl().getPrimaryKey().getColumn(i).getCamelCaseName());
			}
			Var idRemoved = _declareNew(pkType, "idRemoved", idAddedArgs);
			for(Column col:relationBean.getTbl().getPrimaryKey().getColumns()) {
				addInstr( idRemoved.callAttrSetterMethodInstr(col
						.getCamelCaseName(), getParam("bean")
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
									getParam("bean")
									.callAttrGetter(
											relationBean.getTbl().getPrimaryKey().getFirstColumn()
											.getCamelCaseName()
									)
								).asInstruction());	
		}
	}

}
