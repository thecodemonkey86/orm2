package cpp.jsonentityquery.method;

import cpp.JsonTypes;
import cpp.NetworkTypes;
import cpp.Types;
import cpp.core.Method;
import cpp.core.Param;
import cpp.core.QString;
import cpp.core.expression.Var;
import cpp.jsonentity.JsonEntity;
import cpp.jsonentityrepository.method.MethodLoadFromUrl;
import cpp.lib.ClsBaseJsonEntitySelectQuery;
import cpp.lib.ClsQUrl;
import cpp.lib.ClsQUrlQuery;
import cpp.lib.ClsStdFunction;

public class MethodJsonQuery extends Method{

	JsonEntity entity;
	Param pCallback;
	public MethodJsonQuery(JsonEntity entity) {
		super(Public, Types.Void, "query");
		this.entity = entity;
		pCallback = addParam(new ClsStdFunction(Types.Void, Types.qlist(entity.toSharedPtr()).toConstRef()),"callback");
	}

	@Override
	public void addImplementation() {
		Var vUrl = _declare(NetworkTypes.QUrl, "_url",JsonTypes.JsonEntityRepository.callStaticMethod("getBaseUrl"));
		Var vUrlQuery = _declare(NetworkTypes.QUrlQuery, "_urlQuery");
		addInstr(vUrlQuery.callMethodInstruction(ClsQUrlQuery.addQueryItem, QString.fromStringConstant("entityType"), QString.fromStringConstant(entity.getTbl().getUc1stCamelCaseName())));
		addInstr(vUrlQuery.callMethodInstruction(ClsQUrlQuery.addQueryItem, QString.fromStringConstant("queryType"), QString.fromStringConstant("list")));
		addInstr(vUrlQuery.callMethodInstruction(ClsQUrlQuery.addQueryItem,QString.fromStringConstant("condition"), _this().callMethod(ClsBaseJsonEntitySelectQuery.toJsonString)));
		addInstr(vUrl.callMethodInstruction(ClsQUrl.setQuery, vUrlQuery));
		addInstr(JsonTypes.JsonEntityRepository.callStaticMethod(MethodLoadFromUrl.getMethodName(entity), vUrl, pCallback).asInstruction());
		/*Var vUrlQuery = ifNotLoaded.thenBlock()._declare(NetworkTypes.QUrlQuery, "urlQuery");
			ifNotLoaded.thenBlock().addInstr(vUrlQuery.callMethodInstruction(ClsQUrlQuery.addQueryItem, QString.fromStringConstant("entityType"), QString.fromStringConstant(r.getDestTable().getUc1stCamelCaseName())));
			ifNotLoaded.thenBlock().addInstr(vUrlQuery.callMethodInstruction(ClsQUrlQuery.addQueryItem, QString.fromStringConstant("queryType"), QString.fromStringConstant("one")));
			for(int i=0;i<r.getColumnCount();i++) {
				ifNotLoaded.thenBlock().addInstr(vUrlQuery.callMethodInstruction(ClsQUrlQuery.addQueryItem,QString.fromStringConstant("condition"), QString.fromStringConstant("e1."+ r.getColumns(i).getValue2()+"=").concat(JsonOrmUtil.convertToQString(_this().accessAttr(r.getColumns(i).getValue1().getCamelCaseName())))));
			}	
			Var vUrl = ifNotLoaded.thenBlock()._declare(NetworkTypes.QUrl, "url",JsonTypes.JsonEntityRepository.callStaticMethod("getBaseUrl"));
			ifNotLoaded.thenBlock().addInstr(vUrl.callMethodInstruction(ClsQUrl.setQuery, vUrlQuery));
			*/
	}

}
