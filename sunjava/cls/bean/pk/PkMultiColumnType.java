package sunjava.cls.bean.pk;

import model.Column;
import model.Table;
import sunjava.cls.Attr;
import sunjava.cls.Constructor;
import sunjava.cls.JavaCls;
import sunjava.cls.Param;
import sunjava.cls.bean.BeanCls;
import sunjava.cls.bean.pk.method.MethodPkEquals;
import sunjava.cls.bean.pk.method.MethodPkHashCode;
import sunjava.cls.method.MethodAttributeGetter;
import sunjava.cls.method.MethodAttributeSetter;

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
