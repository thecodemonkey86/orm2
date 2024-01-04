package cpp.jsonentity.method;

import util.StringUtil;
import util.pg.PgCppUtil;

import java.util.ArrayList;

import cpp.CoreTypes;
import cpp.JsonTypes;
import cpp.Types;
import cpp.core.LambdaExpression;
import cpp.core.Method;
import cpp.core.Param;
import cpp.core.expression.BoolExpression;
import cpp.core.expression.Expression;
import cpp.core.expression.Expressions;
import cpp.core.expression.StdFunctionInvocation;
import cpp.core.instruction.IfBlock;
import cpp.entity.Nullable;
import cpp.jsonentity.OneAttr;
import cpp.jsonentity.JsonEntities;
import cpp.jsonentity.JsonEntity;
import cpp.jsonentityrepository.method.MethodLoadById;
import cpp.lib.ClsStdFunction;
import database.relation.OneRelation;

public class MethodLoadOneRelation extends Method{

	Param pCallback;
	JsonEntity e;
	OneRelation r;
	
	public MethodLoadOneRelation(OneRelation r) {
		super(Public, Types.Void		, getMethodName(r));
		this.e=JsonEntities.get(r.getDestTable());
		this.r=r;
		pCallback = addParam(new Param(new ClsStdFunction(CoreTypes.Void, e.toSharedPtr().toConstRef()), "callback"));
		
	}

	@Override
	public void addImplementation() {
			IfBlock ifNotLoaded = _if(Expressions.and(
					Expressions.not(parent.getAttrByName("loaded"))
//					Expressions.not(paramByName("noLoading"))
				)
					
					
			);
			Expression[] pkArgs = new Expression[r.getColumnCount()+1];
			ArrayList<Expression> nullCheckArgs = new ArrayList<>();
			
			for(int i=0;i<r.getColumnCount();++i) {
				if(r.getColumns(i).getValue1().isNullable()) {
					nullCheckArgs.add(_this().accessAttr(r.getColumns(i).getValue1().getCamelCaseName()).callMethod(Nullable.isNull));
					pkArgs[i] = _this().accessAttr(r.getColumns(i).getValue1().getCamelCaseName()).callMethod(Nullable.val);
				} else {
					pkArgs[i] = _this().accessAttr(r.getColumns(i).getValue1().getCamelCaseName());
				}
				
				
			}
			
			if(!nullCheckArgs.isEmpty()) {
				IfBlock ifAnyAttrNull = ifNotLoaded.thenBlock()._if(Expressions.or(nullCheckArgs));
				
				ifAnyAttrNull.thenBlock().addInstr(new StdFunctionInvocation(pCallback, Expressions.Nullptr));
				ifAnyAttrNull.thenBlock()._return(null);
			}
			
			pkArgs[pkArgs.length-1]=pCallback;
			
			ifNotLoaded.thenBlock().addInstr(JsonTypes.JsonEntityRepository.callStaticMethod(MethodLoadById.getMethodName(e), pkArgs).asInstruction());
			 
			/*Var vUrlQuery = ifNotLoaded.thenBlock()._declare(NetworkTypes.QUrlQuery, "urlQuery");
			ifNotLoaded.thenBlock().addInstr(vUrlQuery.callMethodInstruction(ClsQUrlQuery.addQueryItem, QString.fromStringConstant("entityType"), QString.fromStringConstant(r.getDestTable().getUc1stCamelCaseName())));
			ifNotLoaded.thenBlock().addInstr(vUrlQuery.callMethodInstruction(ClsQUrlQuery.addQueryItem, QString.fromStringConstant("queryType"), QString.fromStringConstant("one")));
			for(int i=0;i<r.getColumnCount();i++) {
				ifNotLoaded.thenBlock().addInstr(vUrlQuery.callMethodInstruction(ClsQUrlQuery.addQueryItem,QString.fromStringConstant("condition"), QString.fromStringConstant("e1."+ r.getColumns(i).getValue2()+"=").concat(JsonOrmUtil.convertToQString(_this().accessAttr(r.getColumns(i).getValue1().getCamelCaseName())))));
			}	
			Var vUrl = ifNotLoaded.thenBlock()._declare(NetworkTypes.QUrl, "url",JsonTypes.JsonEntityRepository.callStaticMethod("getBaseUrl"));
			ifNotLoaded.thenBlock().addInstr(vUrl.callMethodInstruction(ClsQUrl.setQuery, vUrlQuery));*/
			
			LambdaExpression callback= new LambdaExpression().setCapture(_this(),pCallback).setArguments(new Param( (JsonEntities.get(r.getDestTable()).toSharedPtr().toConstRef()),"entity"));
			
			callback._assign(parent.getAttrByName("loaded"), BoolExpression.TRUE);
			callback.addInstr(_this().accessAttr(new OneAttr(r)).assign(callback.getArgument(0)));
			callback.addInstr(new StdFunctionInvocation(pCallback, _this().accessAttr(new OneAttr(r))));
//			ifNotLoaded.thenBlock().addInstr(JsonTypes.JsonEntityRepository.callStaticMethod( MethodLoadOneFromUrl.getMethodName(e), vUrl,callback
//					).asInstruction());
			
			ifNotLoaded.elseBlock().addInstr(new StdFunctionInvocation(pCallback, _this().accessAttr(new OneAttr(r))));
		
		
	}
	
	public static String getMethodName(OneRelation r) {
		return "load"+StringUtil.ucfirst(PgCppUtil.getOneRelationDestAttrName(r));
	}
	
/*	public static String getMethodName(OneToManyRelation r) {
		return "get"+StringUtil.ucfirst(PgCppUtil.getOneToManyRelationDestAttrName(r));
	}
	
	public static String getMethodName(ManyRelation r) {
		return "get"+StringUtil.ucfirst(PgCppUtil.getManyRelationDestAttrName(r));
	}*/
}
