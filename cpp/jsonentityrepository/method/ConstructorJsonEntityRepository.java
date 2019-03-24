package cpp.jsonentityrepository.method;

import cpp.NetworkTypes;
import cpp.core.Constructor;
import cpp.core.Param;

public class ConstructorJsonEntityRepository extends Constructor {

	Param pUrl;
	
	public ConstructorJsonEntityRepository() {
		pUrl = addParam(NetworkTypes.QUrl.toConstRef(), "url");
	}
	
	@Override
	public void addImplementation() {
		_assign(_this().accessAttr(pUrl.getName()),pUrl);
	}

}
