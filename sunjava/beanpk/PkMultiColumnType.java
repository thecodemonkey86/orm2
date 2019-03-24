package sunjava.beanpk;

import database.column.Column;
import database.table.Table;
import sunjava.bean.BeanCls;
import sunjava.beanpk.method.MethodPkEquals;
import sunjava.beanpk.method.MethodPkHashCode;
import sunjava.core.Attr;
import sunjava.core.Constructor;
import sunjava.core.JavaCls;
import sunjava.core.Param;
import sunjava.core.method.MethodAttributeGetter;
import sunjava.core.method.MethodAttributeSetter;

public class PkMultiColumnType extends JavaCls{

	public PkMultiColumnType(String name,String pkg,Table tbl) {
		super(name,pkg);
		Constructor constr = new Constructor() {
			
			@Override
			public void addImplementation() {
				for(Column col: tbl.getPrimaryKey().getColumns()) {
				 addInstr(_this().assignAttr(col.getCamelCaseName(),getParam(col.getCamelCaseName()
							)));
				}
				
			}
		};
		
		addConstructor(constr);
		for(Column col: tbl.getPrimaryKey().getColumns()) {
			Param p = constr.addParam(new Param(BeanCls.getTypeMapper().columnToType( col), col.getCamelCaseName()));
			Attr attr = addAttr(new Attr(p.getType(), col.getCamelCaseName()));
			addMethod(new MethodAttributeSetter(attr));
			addMethod(new MethodAttributeGetter(attr));
			
		}
		
		addMethod(new MethodPkEquals(tbl));
		addMethod(new MethodPkHashCode(tbl));
	}

}
