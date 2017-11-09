package cpp.bean.method;

import generate.CodeUtil2;

import java.util.ArrayList;

import codegen.CodeUtil;
import cpp.Types;
import cpp.bean.BeanCls;
import cpp.bean.Beans;
import cpp.core.Method;
import cpp.core.Param;
import cpp.core.QString;
import cpp.core.expression.Expression;
import database.relation.ManyRelation;
import database.relation.OneRelation;
import database.relation.OneToManyRelation;


public class MethodAddRelatedTableJoins extends Method {
	
	public MethodAddRelatedTableJoins(BeanCls cls) {
		super(Public, Types.Void, "addRelatedTableJoins");
		addParam(new Param(Types.baseBeanQuery(cls).toRef(), "query"));
		setStatic(true);
	}

	@Override
	public void addImplementation() {
		BeanCls bean=(BeanCls)parent;
		Expression query = getParam("query");
		
			
		for(OneRelation r:bean.getOneRelations()) {
			ArrayList<String> joinConditions=new ArrayList<>();
			for(int i=0;i<r.getColumnCount();i++) {
				joinConditions.add(CodeUtil.sp("b1."+r.getColumns(i).getValue1().getEscapedName(),'=',r.getAlias()+"."+ r.getColumns(i).getValue2().getEscapedName()));
			}
			
			query = query.callMethod("leftJoin", Beans.get(r.getDestTable()).callStaticMethod("getTableName"),QString.fromStringConstant(r.getAlias()), QString.fromStringConstant(CodeUtil2.concat(joinConditions," AND ")));
		}
		for(OneToManyRelation r:bean.getOneToManyRelations()) {
			ArrayList<String> joinConditions=new ArrayList<>();
			for(int i=0;i<r.getColumnCount();i++) {
				joinConditions.add(CodeUtil.sp("b1."+r.getColumns(i).getValue1().getEscapedName(),'=',r.getAlias()+"."+ r.getColumns(i).getValue2().getEscapedName()));
			}
			
			query = query.callMethod("leftJoin", Beans.get(r.getDestTable()).callStaticMethod("getTableName"),QString.fromStringConstant(r.getAlias()), QString.fromStringConstant(CodeUtil2.concat(joinConditions," AND ")));
		}
		for(ManyRelation r:bean.getManyRelations()) {
			ArrayList<String> joinConditions=new ArrayList<>();
			for(int i=0;i<r.getSourceColumnCount();i++) {
				joinConditions.add(CodeUtil.sp("b1."+r.getSourceEntityColumn(i).getEscapedName(),'=',r.getAlias("mapping")+"."+ r.getSourceMappingColumn(i).getEscapedName()));
			}
			
			query = query.callMethod("leftJoin", QString.fromStringConstant(r.getMappingTable().getName()),QString.fromStringConstant(r.getAlias("mapping")), QString.fromStringConstant(CodeUtil2.concat(joinConditions," AND ")));
			
			joinConditions.clear();
			for(int i=0;i<r.getDestColumnCount();i++) {
				joinConditions.add(CodeUtil.sp(r.getAlias("mapping")+"."+r.getDestMappingColumn(i).getEscapedName(),'=',r.getAlias()+"."+r.getDestEntityColumn(i).getEscapedName() ));
			}
			
			query = query.callMethod("leftJoin", Beans.get(r.getDestTable()).callStaticMethod("getTableName"),QString.fromStringConstant(r.getAlias()), QString.fromStringConstant(CodeUtil2.concat(joinConditions," AND ")));
			
		}
		
		if (!bean.getOneRelations().isEmpty() || !bean.getOneToManyRelations().isEmpty() || !bean.getManyRelations().isEmpty()) {
			addInstr(query.asInstruction());
		}
		
	}
	
	@Override
	public boolean includeIfEmpty() {
		return true;
	}

}
