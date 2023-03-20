package cpp.jsonentityrepository;

import java.util.Collection;

import cpp.JsonTypes;
import cpp.NetworkTypes;
import cpp.core.Attr;
import cpp.core.Cls;
import cpp.core.Constructor;
import cpp.core.expression.Expressions;
import cpp.core.method.MethodStaticAttributeGetter;
import cpp.core.method.MethodStaticAttributeSetter;
import cpp.jsonentity.JsonEntity;
import cpp.jsonentityrepository.method.MethodLoadById;
import cpp.jsonentityrepository.method.MethodCreateNew;
import cpp.jsonentityrepository.method.MethodCreateNewNonNullableOnly;
import cpp.jsonentityrepository.method.MethodGetOneFromJson;
import cpp.jsonentityrepository.method.MethodGetVectorFromJson;
import cpp.jsonentityrepository.method.MethodLoadFromUrl;
import cpp.jsonentityrepository.method.MethodLoadOneFromUrl;
import cpp.jsonentityrepository.method.MethodSave;
import database.column.Column;

public class ClsJsonEntityRepository extends Cls {
	public static final String CLSNAME = "JsonEntityRepository";
	public static final String network = "network";
	public static final String baseUrl = "baseUrl";

//	public static String getSignalNameOnLoadedOne(JsonEntity e) {
//		return "onLoadedOne" + e.getName();
//	}
//
//	public static String getSignalNameOnLoaded(JsonEntity e) {
//		return "onLoaded" + e.getName();
//	}

	public ClsJsonEntityRepository() {
		super(CLSNAME,false);
		headerInclude=JsonEntity.getRepositoryPath()+type.toLowerCase();
	}

	public void  addDeclarations(Collection<JsonEntity> entityClasses) {
		addConstructor(new Constructor() {
			
			@Override
			public void addImplementation() {
			}
		});
		Attr aNetwork = new Attr(Attr.Protected, NetworkTypes.QNetworkAccessManager.toRawPointer(), network, Expressions.Nullptr, true);
		
		Attr aBaseUrl = new Attr(Attr.Protected, NetworkTypes.QUrl, baseUrl,null,true);
		addAttr(aBaseUrl);
		addMethod(new MethodStaticAttributeGetter(aBaseUrl) );
		addMethod(new MethodStaticAttributeSetter(aBaseUrl) );
		
		addAttr(aNetwork);
		addMethod(new MethodStaticAttributeSetter(aNetwork));
		
		addIncludeLib(NetworkTypes.QNetworkAccessManager);
		addIncludeLib(NetworkTypes.QNetworkReply);
		addIncludeLib(NetworkTypes.QUrlQuery);
		addIncludeLib(JsonTypes.QJsonDocument);
		addIncludeLib(JsonTypes.QJsonObject);
		addIncludeLib(JsonTypes.QJsonArray);
		addIncludeLib("memory");
		
		for(JsonEntity e : entityClasses) {
			addForwardDeclaredClass(e);
			addIncludeHeader(e.getHeaderInclude());
			addMethod(new MethodCreateNew(e));
			int countNullable = 0;
			
			for(Column c : e.getTbl().getFieldColumns()) {
				if(c.isNullable()) {
					countNullable++;
				}
			}
			
			int countInitializeFields = e.getTbl().getFieldColumns().size();
			if(!e.getTbl().getPrimaryKey().isAutoIncrement()) {
				countInitializeFields += e.getTbl().getPrimaryKey().getColumnCount();
				
				for(Column c : e.getTbl().getPrimaryKey()) {
					if(c.isNullable()) {
						countNullable++;
					}
				}
			}
			if(countInitializeFields > 0) {
				addMethod(new MethodCreateNew(e,true,false));
				
				if(countNullable > 0) {
					addMethod(new MethodCreateNew(e,true,true));
					MethodCreateNewNonNullableOnly methodRepoCreateNewNonNullableOnly = new MethodCreateNewNonNullableOnly(e);
					if(!methodRepoCreateNewNonNullableOnly.getParams().isEmpty())
						addMethod(methodRepoCreateNewNonNullableOnly);
				}
			}
			addMethod(new MethodGetOneFromJson(e,true));
			addMethod(new MethodGetOneFromJson(e,false));
			addMethod(new MethodLoadById(e));
			addMethod(new MethodGetVectorFromJson(e));
			addMethod(new MethodLoadFromUrl(e));
			addMethod(new MethodLoadOneFromUrl(e));
			addMethod(new MethodSave(e));
//			addMethod(new MethodLoadByIdFromUrlAsynchronous(e));
//			addMethod(new MethodEntityLoad(e));
		}
		
	}

}
