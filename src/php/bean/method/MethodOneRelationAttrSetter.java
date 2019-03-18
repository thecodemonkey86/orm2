package php.bean.method;

import database.column.Column;
import database.relation.OneRelation;
import php.bean.OneAttr;
import php.core.Attr;
import php.core.expression.BoolExpression;
import php.core.method.MethodAttributeSetter;


public class MethodOneRelationAttrSetter extends MethodAttributeSetter {

	protected boolean internal; 
	
	public MethodOneRelationAttrSetter(Attr attr, boolean internal) {
		super(attr);
		this.internal = internal;
		if (internal ) {
			this.name = this.name + "Internal";
		}
	}

	
	@Override
	public void addImplementation() {
		super.addImplementation();
		OneRelation r = ((OneAttr) attr).getRelation();
		
		for(int i=0;i<r.getColumnCount();i++) {
			Column destCol = r.getColumns(i).getValue2();
			Column srcCol = r.getColumns(i).getValue1();
			addInstr( _this().assignAttr(srcCol.getCamelCaseName(), getParam(attr.getName()).callMethod("get"+destCol.getUc1stCamelCaseName())));
			
			if(srcCol.isPartOfPk()) {
				addInstr( _this().assignAttr(srcCol.getCamelCaseName()+"Previous",  _this().accessAttr(srcCol.getCamelCaseName())));
				if(!this.internal) {
					addInstr(_this().assignAttr("primaryKeyModified",BoolExpression.TRUE));
				}
			} else {
				if (!this.internal) {
					addInstr(_this().assignAttr(srcCol.getCamelCaseName()+"Modified",BoolExpression.TRUE));
				}
			}
			
			
		}
		
		if (this.internal) {
			this.returnType = getParent();
			_return(_this());
		}
	}
}
