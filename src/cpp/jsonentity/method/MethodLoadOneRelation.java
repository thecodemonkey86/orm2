package cpp.jsonentity.method;

import util.StringUtil;
import util.pg.PgCppUtil;
import cpp.CoreTypes;
import cpp.JsonTypes;
import cpp.NetworkTypes;
import cpp.Types;
import cpp.core.LambdaExpression;
import cpp.core.Method;
import cpp.core.Param;
import cpp.core.QString;
import cpp.core.expression.BoolExpression;
import cpp.core.expression.Expressions;
import cpp.core.expression.StdFunctionInvocation;
import cpp.core.expression.Var;
import cpp.core.instruction.IfBlock;
import cpp.jsonentity.OneAttr;
import cpp.jsonentity.JsonEntities;
import cpp.jsonentity.JsonEntity;
import cpp.jsonentityrepository.method.MethodLoadOneFromUrl;
import cpp.lib.ClsQUrl;
import cpp.lib.ClsQUrlQuery;
import cpp.lib.ClsStdFunction;
import cpp.util.JsonOrmUtil;
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
			
			 
			Var vUrlQuery = ifNotLoaded.thenBlock()._declare(NetworkTypes.QUrlQuery, "urlQuery");
			ifNotLoaded.thenBlock().addInstr(vUrlQuery.callMethodInstruction(ClsQUrlQuery.addQueryItem, QString.fromStringConstant("entityType"), QString.fromStringConstant(r.getDestTable().getUc1stCamelCaseName())));
			ifNotLoaded.thenBlock().addInstr(vUrlQuery.callMethodInstruction(ClsQUrlQuery.addQueryItem, QString.fromStringConstant("queryType"), QString.fromStringConstant("one")));
			for(int i=0;i<r.getColumnCount();i++) {
				ifNotLoaded.thenBlock().addInstr(vUrlQuery.callMethodInstruction(ClsQUrlQuery.addQueryItem,QString.fromStringConstant("condition"), QString.fromStringConstant("e1."+ r.getColumns(i).getValue2()+"=").concat(JsonOrmUtil.convertToQString(_this().accessAttr(r.getColumns(i).getValue1().getCamelCaseName())))));
			}	
			Var vUrl = ifNotLoaded.thenBlock()._declare(NetworkTypes.QUrl, "url",JsonTypes.JsonEntityRepository.callStaticMethod("getBaseUrl"));
			ifNotLoaded.thenBlock().addInstr(vUrl.callMethodInstruction(ClsQUrl.setQuery, vUrlQuery));
			
			LambdaExpression callback= new LambdaExpression().setCapture(_this(),pCallback).setArguments(new Param( (JsonEntities.get(r.getDestTable()).toSharedPtr().toConstRef()),"entity"));
			
			callback._assign(parent.getAttrByName("loaded"), BoolExpression.TRUE);
			callback.addInstr(_this().accessAttr(new OneAttr(r)).assign(callback.getArgument(0)));
			callback.addInstr(new StdFunctionInvocation(pCallback, _this().accessAttr(new OneAttr(r))));
			ifNotLoaded.thenBlock().addInstr(JsonTypes.JsonEntityRepository.callStaticMethod( MethodLoadOneFromUrl.getMethodName(e), vUrl,callback
					).asInstruction());
			
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
