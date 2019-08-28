package cpp.entity;

import java.util.ArrayList;
import java.util.List;

import cpp.Types;
import cpp.core.Attr;
import cpp.core.Struct;
import cpp.core.Type;
import database.relation.AbstractRelation;

public class FetchListHelperClass extends Struct{

	public FetchListHelperClass(EntityCls parent) {
		super(parent.getName()+"FetchListHelper");
		Attr e1 = new Attr(parent.toSharedPtr(), "e1");
		addAttr(e1);
//		addMethod(new Method(Method.Public, e1.getType().toRef(), "get"+StringUtil.ucfirst(e1.getName())) {
//
//			@Override
//			public void addImplementation() {
//				_return(e1);
//			}
//			
//		});
		
		List<AbstractRelation> manyRelations = new ArrayList<>();
		manyRelations.addAll(parent.getOneToManyRelations());
		manyRelations.addAll(parent.getManyToManyRelations());
		
		//int bCount=2;
		for(AbstractRelation r:manyRelations) {
			Type beanPk=Types.getRelationForeignPrimaryKeyType(r);
			if(r.getDestTable().getPrimaryKey().isMultiColumn()) {
				parent.addForwardDeclaredClass((Struct) beanPk );
			}
			Attr attrSet = new Attr(Types.qset(beanPk), r.getAlias()+"Set");
//			addMethod(new Method(Method.Public, attrSet.getType().toRef(), "get"+StringUtil.ucfirst(attrSet.getName())) {
//
//				@Override
//				public void addImplementation() {
//					_return(attrSet);
//				}
//				
//			});
			addAttr(attrSet);
		}
	}
	
	/*@Override
	public String toSourceString() {
		StringBuilder sb=new StringBuilder();
		
//		addSourceCode(sb);
//		for(Constructor c:constructors) {
//			CodeUtil.writeLine(sb, c);
//		}
//		if (destructor !=null)
//			CodeUtil.writeLine(sb, destructor);
//		
//		for(Method m:methods) {
//			CodeUtil.writeLine(sb, m);
//		}
		
		for(Attr a:attrs) {
			if (a.isStatic())
				CodeUtil.writeLine(sb, CodeUtil.sp(a.getType(),getName()+"::"+a.getName(),"=",a.getInitValue(),";"));
		}
//		if (nonMemberMethods!=null) {
//			for(Method m:nonMemberMethods) {
//				CodeUtil.writeLine(sb, m.toString());
//			}
//		} 
//		if (nonMemberOperators!=null) {
//			for(Operator op:nonMemberOperators) {
//				CodeUtil.writeLine(sb, op.toString());
//			}
//		}
		return sb.toString();
	}*/

	
	
}
