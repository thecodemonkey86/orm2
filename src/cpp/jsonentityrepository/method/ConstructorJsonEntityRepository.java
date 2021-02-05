package cpp.jsonentityrepository.method;

import cpp.NetworkTypes;
import cpp.core.Constructor;
import cpp.core.Param;
import cpp.jsonentityrepository.ClsJsonEntityRepository;

public class ConstructorJsonEntityRepository extends Constructor {

	Param pUrl;
	
	public ConstructorJsonEntityRepository() {
		pUrl = addParam(NetworkTypes.QUrl.toConstRef(), "url");
	}
	
	@Override
	public void addImplementation() {
		_assign(_this().accessAttr(ClsJsonEntityRepository.baseUrl),pUrl);
	}

}
