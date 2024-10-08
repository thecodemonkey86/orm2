package cpp.entity.method;

import java.util.ArrayList;

import codegen.CodeUtil;
import cpp.Types;
import cpp.core.Method;
import cpp.core.Param;
import cpp.core.QString;
import cpp.core.expression.Expression;
import cpp.entity.Entities;
import cpp.entity.EntityCls;
import database.relation.ManyRelation;
import database.relation.OneRelation;
import database.relation.OneToManyRelation;
import util.CodeUtil2;


public class MethodAddRelatedTableJoins extends Method {
	
	public MethodAddRelatedTableJoins(EntityCls cls) {
		super(Public, Types.Void, getMethodName());
		addParam(new Param(Types.beanQuerySelect(cls).toRef(), "query"));
		setStatic(true);
	}

	@Override
	public void addImplementation() {
		EntityCls entity=(EntityCls)parent;
		Expression query = getParam("query");
		
			
		for(OneRelation r:entity.getOneRelations()) {
			ArrayList<String> joinConditions=new ArrayList<>();
			for(int i=0;i<r.getColumnCount();i++) {
				joinConditions.add(CodeUtil.sp("e1."+r.getColumns(i).getValue1().getEscapedName(),'=',r.getAlias()+"."+ r.getColumns(i).getValue2().getEscapedName()));
			}
			
			query = query.callMethod("leftJoin", Entities.get(r.getDestTable()).callStaticMethod("getTableName"),QString.fromStringConstant(r.getAlias()), QString.fromStringConstant(CodeUtil2.concat(joinConditions," AND ")));
		}
		for(OneToManyRelation r:entity.getOneToManyRelations()) {
			ArrayList<String> joinConditions=new ArrayList<>();
			for(int i=0;i<r.getColumnCount();i++) {
				joinConditions.add(CodeUtil.sp("e1."+r.getColumns(i).getValue1().getEscapedName(),'=',r.getAlias()+"."+ r.getColumns(i).getValue2().getEscapedName()));
			}
			
			query = query.callMethod(r.isComposition() ? "join": "leftJoin", Entities.get(r.getDestTable()).callStaticMethod("getTableName"),QString.fromStringConstant(r.getAlias()), QString.fromStringConstant(CodeUtil2.concat(joinConditions," AND ")));
		}
		for(ManyRelation r:entity.getManyRelations()) {
			ArrayList<String> joinConditions=new ArrayList<>();
			for(int i=0;i<r.getSourceColumnCount();i++) {
				joinConditions.add(CodeUtil.sp("e1."+r.getSourceEntityColumn(i).getEscapedName(),'=',r.getAlias("mapping")+"."+ r.getSourceMappingColumn(i).getEscapedName()));
			}
			
			query = query.callMethod("leftJoin", QString.fromStringConstant(r.getMappingTable().getEscapedName()),QString.fromStringConstant(r.getAlias("mapping")), QString.fromStringConstant(CodeUtil2.concat(joinConditions," AND ")));
			
			joinConditions.clear();
			for(int i=0;i<r.getDestColumnCount();i++) {
				joinConditions.add(CodeUtil.sp(r.getAlias("mapping")+"."+r.getDestMappingColumn(i).getEscapedName(),'=',r.getAlias()+"."+r.getDestEntityColumn(i).getEscapedName() ));
			}
			
			query = query.callMethod("leftJoin", Entities.get(r.getDestTable()).callStaticMethod("getTableName"),QString.fromStringConstant(r.getAlias()), QString.fromStringConstant(CodeUtil2.concat(joinConditions," AND ")));
			
		}
		
		if (!entity.getOneRelations().isEmpty() || !entity.getOneToManyRelations().isEmpty() || !entity.getManyRelations().isEmpty()) {
			addInstr(query.asInstruction());
		}
		
	}
	
	@Override
	public boolean includeIfEmpty() {
		return true;
	}

	public static String getMethodName() {
		return "addRelatedTableJoins";
	}

}
