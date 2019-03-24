package cpp.jsonentityrepository;

import java.util.Collection;

import cpp.JsonTypes;
import cpp.NetworkTypes;
import cpp.QtCoreTypes;
import cpp.core.Attr;
import cpp.core.Cls;
import cpp.core.Param;
import cpp.core.QtSignal;
import cpp.core.expression.Expressions;
import cpp.core.method.MethodStaticAttributeSetter;
import cpp.jsonentity.JsonEntity;
import cpp.jsonentityrepository.method.ConstructorJsonEntityRepository;
import cpp.jsonentityrepository.method.MethodEntityLoad;
import cpp.jsonentityrepository.method.MethodLoadByIdFromUrl;
import cpp.jsonentityrepository.method.MethodGetFromJson;
import cpp.jsonentityrepository.method.MethodLoadFromUrl;
import cpp.lib.QObjectMacro;

public class JsonEntityRepository extends Cls{
	public static final String CLSNAME = "JsonEntityRepository";
	public static final String network = "network";
	
	
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
		addIncludeLib("memory");
		
		for(JsonEntity e : entityClasses) {
			addForwardDeclaredClass(e);
			addIncludeHeader(JsonEntity.getModelPath() + "beans/"+e.getIncludeHeader());
			addMethod(new MethodGetFromJson(e));
			addMethod(new MethodLoadFromUrl(e));
			addMethod(new MethodLoadByIdFromUrl(e));
			addMethod(new MethodEntityLoad(e));
			addMethod(new QtSignal("onLoaded"+e.getName(),new Param( e.toSharedPtr().toConstRef(),"bean")));
		}
		
	}
	

}
