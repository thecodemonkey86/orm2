package cpp;

import cpp.lib.ClsBaseJsonEntity;
import cpp.lib.ClsBaseJsonEntitySelectQuery;
import cpp.lib.ClsBaseJsonEntityDeleteQuery;
import cpp.lib.ClsQJsonArray;
import cpp.lib.ClsQJsonDocument;
import cpp.lib.ClsQJsonObject;
import cpp.lib.ClsQJsonValue;
import cpp.jsonentityrepository.ClsJsonEntityRepository;
public class JsonTypes {
	public static final ClsQJsonValue QJsonValue = new ClsQJsonValue();
	public static final ClsQJsonArray QJsonArray = new ClsQJsonArray();
	public static final ClsQJsonObject QJsonObject = new ClsQJsonObject();
	public static final ClsQJsonDocument QJsonDocument = new ClsQJsonDocument();
	
	public static final ClsBaseJsonEntity BaseJsonEntity = new ClsBaseJsonEntity();
	public static final ClsJsonEntityRepository JsonEntityRepository = new ClsJsonEntityRepository();
	public static final ClsBaseJsonEntitySelectQuery BaseJsonEntitySelectQuery = new ClsBaseJsonEntitySelectQuery();
	public static final ClsBaseJsonEntityDeleteQuery BaseJsonEntityDeleteQuery = new ClsBaseJsonEntityDeleteQuery();
	
}
