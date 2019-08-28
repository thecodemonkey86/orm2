package php.beanpk;

import database.column.Column;
import database.table.Table;
import php.bean.BeanCls;
import php.beanpk.method.MethodPkEquals;
import php.core.Attr;
import php.core.Constructor;
import php.core.Param;
import php.core.PhpCls;
import php.core.method.MethodAttributeGetter;
import php.core.method.MethodAttributeSetter;

public class PkMultiColumnType extends PhpCls{

	public PkMultiColumnType(String name,String namespace,Table tbl) {
		super(name,namespace);
		Constructor constr = new Constructor() {
			
			@Override
			public void addImplementation() {
				for(Column col: tbl.getPrimaryKey().getColumns()) {
				 addInstr(_this().assignAttr(col.getCamelCaseName(),getParam(col.getCamelCaseName()
							)));
				}
				
			}
		};
		
		setConstructor(constr);
		for(Column col: tbl.getPrimaryKey().getColumns()) {
			Param p = constr.addParam(new Param(BeanCls.getTypeMapper().columnToType( col), col.getCamelCaseName()));
			Attr attr = addAttr(new Attr(p.getType(), col.getCamelCaseName()));
			addMethod(new MethodAttributeSetter(attr));
			addMethod(new MethodAttributeGetter(attr));
			
		}
		
		addMethod(new MethodPkEquals(tbl,this));
	}

}
