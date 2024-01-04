package cpp.jsonentityquery.method;

import cpp.CoreTypes;
import cpp.JsonTypes;
import cpp.Types;
import cpp.core.Method;
import cpp.core.Param;
import cpp.core.QStringLiteral;
import cpp.entity.method.MethodAttrGetter;
import cpp.jsonentity.JsonEntity;
import cpp.jsonentityrepository.ClsJsonEntityRepository;
import cpp.lib.ClsBaseJsonEntityDeleteQuery;
import cpp.lib.ClsStdFunction;

public class MethodJsonQueryExecute extends Method{
	JsonEntity entity;
	Param pCallback;
	public MethodJsonQueryExecute(JsonEntity entity) {
		super(Public,Types.Void, getMethodName());
		this.entity = entity;
		pCallback = addParam(new Param(new ClsStdFunction(CoreTypes.Void, Types.Bool,Types.QString.toConstRef()), "callback"));
	}

	@Override
	public void addImplementation() {
		 
		addInstr(_this().callMethodInstruction(ClsBaseJsonEntityDeleteQuery.exec, JsonTypes.JsonEntityRepository.callStaticMethod(MethodAttrGetter.getMethodName(JsonTypes.JsonEntityRepository.getAttrByName(ClsJsonEntityRepository.baseUrl))),QStringLiteral.fromStringConstant(entity.getTbl().getUc1stCamelCaseName()),pCallback ) );
	}

	public static String getMethodName() {
		return "execute";
	}
}
