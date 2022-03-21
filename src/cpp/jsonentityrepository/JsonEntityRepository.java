package cpp.jsonentityrepository;

import java.util.Collection;

import cpp.JsonTypes;
import cpp.NetworkTypes;
import cpp.QtCoreTypes;
import cpp.Types;
import cpp.core.Attr;
import cpp.core.Cls;
import cpp.core.Param;
import cpp.core.QtSignal;
import cpp.core.expression.Expressions;
import cpp.core.method.MethodStaticAttributeSetter;
import cpp.jsonentity.JsonEntity;
import cpp.jsonentityrepository.method.ConstructorJsonEntityRepository;
import cpp.jsonentityrepository.method.MethodEntityLoad;
import cpp.jsonentityrepository.method.MethodLoadByIdFromUrlAsynchronous;
import cpp.jsonentityrepository.method.MethodLoadByIdFromUrlSynchronous;
import cpp.jsonentityrepository.method.MethodGetOneFromJson;
import cpp.jsonentityrepository.method.MethodGetVectorFromJson;
import cpp.jsonentityrepository.method.MethodLoadFromUrl;
import cpp.lib.QObjectMacro;

public class JsonEntityRepository extends Cls {
	public static final String CLSNAME = "JsonEntityRepository";
	public static final String network = "network";

	public static String getSignalNameOnLoadedOne(JsonEntity e) {
		return "onLoadedOne" + e.getName();
	}

	public static String getSignalNameOnLoaded(JsonEntity e) {
		return "onLoaded" + e.getName();
	}

	public JsonEntityRepository() {
		super(CLSNAME);

	}

	public void  addDeclarations(Collection<JsonEntity> entityClasses) {
		addConstructor(new QObjectMacro());
		addConstructor(new ConstructorJsonEntityRepository());
		addSuperclass(QtCoreTypes.QObject);
		Attr aNetwork = new Attr(Attr.Private, NetworkTypes.QNetworkAccessManager.toRawPointer(), network, Expressions.Nullptr, true);
		addAttr(new Attr(NetworkTypes.QUrl, "url"));
		addAttr(aNetwork);
		addMethod(new MethodStaticAttributeSetter(aNetwork));
		
		addIncludeLib(NetworkTypes.QNetworkAccessManager);
		addIncludeLib(NetworkTypes.QNetworkReply);
		addIncludeLib(NetworkTypes.QUrlQuery);
		addIncludeLib(JsonTypes.QJsonDocument);
		addIncludeLib(JsonTypes.QJsonObject);
		addIncludeLib(JsonTypes.QJsonArray);
		addIncludeLib(QtCoreTypes.QEventLoop);
		addIncludeLib("memory");
		
		for(JsonEntity e : entityClasses) {
			addForwardDeclaredClass(e);
			addIncludeHeader(JsonEntity.getModelPath() + "entities/"+e.getIncludeHeader());
			addMethod(new MethodGetOneFromJson(e,true));
			addMethod(new MethodGetOneFromJson(e,false));
			addMethod(new MethodGetVectorFromJson(e));
			addMethod(new MethodLoadFromUrl(e));
			addMethod(new MethodLoadByIdFromUrlAsynchronous(e));
			addMethod(new MethodLoadByIdFromUrlSynchronous(e));
			addMethod(new MethodEntityLoad(e));
			addMethod(new QtSignal(getSignalNameOnLoadedOne(e),new Param( e.toSharedPtr().toConstRef(),"entity")));
			addMethod(new QtSignal(getSignalNameOnLoaded(e),new Param(Types.qlist(e.toSharedPtr()).toConstRef(),"entities")));
		}
		
	}

}
