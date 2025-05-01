package php.rest.method;

import java.util.Collection;

import php.core.instruction.SemicolonTerminatedInstruction;
import php.core.PhpConstants;
import php.core.PhpFunctions;
import php.core.PhpGlobals;
import php.core.Types;
import php.core.expression.ArrayInitExpression;
import php.core.expression.BoolExpression;
import php.core.expression.PhpStringLiteral;
import php.core.expression.Var;
import php.core.instruction.AssignInstruction;
import php.core.instruction.CaseBlock;
import php.core.instruction.ForeachLoop;
import php.core.instruction.MethodCallInstruction;
import php.core.instruction.SwitchBlock;
import php.core.instruction.ThrowInstruction;
import php.core.instruction.TryCatchBlock;
import php.core.method.Method;
import php.entity.EntityCls;
import php.entityrepository.method.MethodCreateQuery;
import php.lib.ClsBaseEntityQuery;
import php.lib.ClsException;

public class RestMethodDelete extends Method {

	Collection<EntityCls> entities;
	
	public RestMethodDelete(Collection<EntityCls> entities) {
		super(Public, Types.Void, "delete");
		setStatic(true);
		this.entities = entities;
	}

	@Override
	public void addImplementation() {
		Var vResult = _declare(Types.array(Types.Mixed),"result",new ArrayInitExpression());
		 TryCatchBlock tryCatch = _tryCatch();
		 Var vQueryJson =   tryCatch.getTryBlock()._declare(Types.array(Types.Mixed), "_json", PhpFunctions.json_decode.call(PhpGlobals.$_GET.arrayIndex(new PhpStringLiteral("condition")) ,BoolExpression.TRUE)); 
		SwitchBlock switchEntityType = tryCatch.getTryBlock()._switch(PhpGlobals.$_GET.arrayIndex(new PhpStringLiteral("entityType")));
		
		
		for(EntityCls entity : entities) {
			CaseBlock caseEntityType = switchEntityType._case(new PhpStringLiteral(entity.getName()));
			Var vQueryDelete = caseEntityType._declare(Types.entityQuery(entity), "_queryDelete", Types.EntityRepository.callStaticMethod(MethodCreateQuery.getMethodName(entity)));
			caseEntityType.addInstr(vQueryDelete.callMethodInstruction(ClsBaseEntityQuery.delete));
				
			Var vCondJson = caseEntityType._declare(Types.array(Types.Mixed), "_condJson",vQueryJson.arrayIndex("conditions") );
			ForeachLoop forCond= caseEntityType._foreach(new Var(Types.Mixed, "_c"), vCondJson);
			Var vSqlCond =  forCond._declare(Types.String, "_sqlCond", forCond.getVar().arrayIndex("cond"));
			
			forCond._if(PhpFunctions.str_contains.call(vSqlCond,new PhpStringLiteral("\'"))
					._or(PhpFunctions.str_contains.call(vSqlCond,new PhpStringLiteral("\""))
					._or(PhpFunctions.str_contains.call(vSqlCond,new PhpStringLiteral("/*"))
					._or(PhpFunctions.str_contains.call(vSqlCond,new PhpStringLiteral("*/"))
					._or(PhpFunctions.str_contains.call(vSqlCond,new PhpStringLiteral("//"))
					))))).thenBlock().
			addInstr(new ThrowInstruction(Types.Exception, new PhpStringLiteral("invalid SQL")));
			
			forCond.addInstr(new MethodCallInstruction(vQueryDelete.callMethod(ClsBaseEntityQuery.where,vSqlCond,  forCond.getVar().arrayIndex(new PhpStringLiteral("params")))));
			caseEntityType.addInstr(vQueryDelete.callMethodInstruction(ClsBaseEntityQuery.execute));
			caseEntityType._break();
			
		}
		Var vExc = new Var(Types.Exception, "_ex");
		tryCatch.addCatch(vExc,new SemicolonTerminatedInstruction("http_response_code(500)"), new AssignInstruction(vResult.arrayIndex("error"),BoolExpression.TRUE),new AssignInstruction(vResult.arrayIndex("message"),vExc.callMethod(ClsException.getMessage)));
		tryCatch.getTryBlock()._arraySet(vResult, new PhpStringLiteral("error"), BoolExpression.FALSE);
		addInstr(PhpFunctions.echo(PhpFunctions.json_encode.call(vResult, PhpConstants.JSON_UNESCAPED_UNICODE)));
	}

}
