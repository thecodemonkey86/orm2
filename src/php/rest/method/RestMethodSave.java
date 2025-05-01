package php.rest.method;

import java.util.Collection;

import database.column.Column;
import php.core.PhpConstants;
import php.core.PhpFunctions;
import php.core.PhpGlobals;
import php.core.Types;
import php.core.expression.ArrayInitExpression;
import php.core.expression.BoolExpression;
import php.core.expression.Expression;
import php.core.expression.InlineIfExpression;
import php.core.expression.PhpStringLiteral;
import php.core.expression.Var;
import php.core.instruction.AssignInstruction;
import php.core.instruction.CaseBlock;
import php.core.instruction.IfBlock;
import php.core.instruction.SwitchBlock;
import php.core.instruction.ThrowInstruction;
import php.core.instruction.TryCatchBlock;
import php.core.method.Method;
import php.entity.EntityCls;
import php.entity.method.MethodColumnAttrSetter;
import php.entity.method.MethodCreateNew;
import php.entity.method.MethodSetPrimaryKey;
import php.entityrepository.method.MethodEntitySave;
import php.entityrepository.method.MethodGetById;
import php.lib.ClsException;

public class RestMethodSave extends Method {

	Collection<EntityCls> entities;
	
	public RestMethodSave(Collection<EntityCls> entities) {
		super(Public, Types.Void, "save");
		setStatic(true);
		this.entities = entities;
	}

	@Override
	public void addImplementation() {
		Var vResult = _declare(Types.array(Types.Mixed),"result",new ArrayInitExpression());
		 TryCatchBlock tryCatch = _tryCatch();
		SwitchBlock switchEntityType = tryCatch.getTryBlock()._switch(PhpGlobals.$_GET.arrayIndex(new PhpStringLiteral("entityType")));
		for(EntityCls entity : entities) {
			CaseBlock caseEntityType = switchEntityType._case(new PhpStringLiteral(entity.getName()));
			Var postData = caseEntityType._declare(Types.array(Types.Mixed), "_postdata",PhpFunctions.json_decode.call( PhpGlobals.$_POST.arrayIndex("data"),BoolExpression.TRUE));
			Var vIsPkMod = caseEntityType._declare(Types.Bool,"_isPkMod",postData.arrayKeyExists(new PhpStringLiteral(entity.getTbl().getPrimaryKey().getFirstColumn().getName()+"Previous")));
			
			IfBlock ifInsert= caseEntityType._if(postData.arrayIndex(new PhpStringLiteral("insert")).cast(Types.Bool));
			
			Expression[] getByIdArgs = new Expression[entity.getTbl().getPrimaryKey().getColumnCount()];
			Expression[] getByPrevIdArgs = new Expression[entity.getTbl().getPrimaryKey().getColumnCount()];
			
			int i=0;
			for(Column colPk : entity.getTbl().getPrimaryKey()) {
				getByIdArgs[i] = postData.arrayIndex(new PhpStringLiteral(colPk.getName()));
				getByPrevIdArgs[i] = postData.arrayIndex(new PhpStringLiteral(colPk.getName()+"Previous"));
				++i;
			}
			
			
			Var vEntity=ifInsert.thenBlock()._declare(entity,"_entity",entity.callStaticMethod(MethodCreateNew.getMethodName()));
			Var vEntityUpdate=ifInsert.elseBlock()._declare(entity,"_entity", new InlineIfExpression(vIsPkMod, Types.EntityRepository.callStaticMethod(MethodGetById.getMethodName(entity),getByPrevIdArgs) , Types.EntityRepository.callStaticMethod(MethodGetById.getMethodName(entity),getByIdArgs))	);
			ifInsert.elseBlock()._if(vEntity.isNull()).thenBlock().addInstr(new ThrowInstruction(Types.Exception.newInstance(new PhpStringLiteral("no such entry"))));
			for(Column col:entity.getTbl().getColumnsWithoutPrimaryKey()) {
				ifInsert.thenBlock()._callMethodInstr(vEntity, MethodColumnAttrSetter.getMethodName(col), EntityCls.getTypeMapper().getConvertJsonValueToTypedExpression(postData.arrayIndex(new PhpStringLiteral(col.getName())), col));
				ifInsert.elseBlock()._if(postData.arrayKeyExists(new PhpStringLiteral(col.getName()))).thenBlock()._callMethodInstr(vEntity, MethodColumnAttrSetter.getMethodName(col), EntityCls.getTypeMapper().getConvertJsonValueToTypedExpression(postData.arrayIndex(new PhpStringLiteral(col.getName())), col) );
			}
			
			if(!entity.getTbl().isAutoIncrement()) {
				Expression[] pkArgs=new Expression[entity.getTbl().getPrimaryKey().getColumnCount()];
				i=0;
				for(Column colPk : entity.getTbl().getPrimaryKey()) {
					pkArgs[i++] = postData.arrayIndex(new PhpStringLiteral(colPk.getName()));
				}

				ifInsert.thenBlock()._callMethodInstr(vEntity, MethodSetPrimaryKey.getMethodName(entity), pkArgs);
				ifInsert.elseBlock()._if(postData.arrayKeyExists(new PhpStringLiteral(entity.getTbl().getPrimaryKey().getColumn(0).getName()))).thenBlock()._callMethodInstr(vEntityUpdate, MethodSetPrimaryKey.getMethodName(entity), pkArgs);
			}
			
			
			if(entity.getTbl().isAutoIncrement()) {
				Column col=entity.getTbl().getPrimaryKey().getFirstColumn();
				ifInsert.thenBlock().addInstr(Types.EntityRepository.callStaticMethod(MethodEntitySave.getMethodName(entity), vEntity).asInstruction());
				ifInsert.thenBlock()._arraySet(vResult, new PhpStringLiteral(col.getName()), vEntity.callAttrGetter(col.getCamelCaseName()));
				ifInsert.elseBlock().addInstr(Types.EntityRepository.callStaticMethod(MethodEntitySave.getMethodName(entity), vEntity).asInstruction());
			} else {
				caseEntityType.addInstr(Types.EntityRepository.callStaticMethod(MethodEntitySave.getMethodName(entity), vEntity).asInstruction());
			}
			
			caseEntityType._break();
			
		}
		Var vExc = new Var(Types.Exception, "_ex");
		tryCatch.addCatch(vExc, new AssignInstruction(vResult.arrayIndex("error"),BoolExpression.TRUE),new AssignInstruction(vResult.arrayIndex("message"),vExc.callMethod(ClsException.getMessage)));
		tryCatch.getTryBlock()._arraySet(vResult, new PhpStringLiteral("error"), BoolExpression.FALSE);
		addInstr(PhpFunctions.echo(PhpFunctions.json_encode.call(vResult, PhpConstants.JSON_UNESCAPED_UNICODE)));
	}

}
