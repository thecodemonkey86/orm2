package cpp.lib;

import cpp.Types;
import cpp.beanrepository.ClsBeanRepository;
import cpp.core.Attr;
import cpp.core.Cls;
import cpp.core.method.MethodAttributeGetter;

public class ClsBaseRepository extends Cls{

	public static final String saveBean = "saveBean"; 
	public static final String bulkSave = "bulkSave"; 
	public static final String bulkInsert = "bulkInsert"; 
	
	public ClsBaseRepository() {
		super("BaseRepository");
		Attr sqlCon = new Attr(Types.Sql.toRawPointer(), ClsBeanRepository.sqlCon);
		addAttr(sqlCon);
		addMethod(new MethodAttributeGetter(sqlCon));
		addMethod( new LibMethod(Types.Void, saveBean));
		addMethod( new LibMethod(Types.Void, bulkSave));
		addMethod( new LibMethod(Types.Void, bulkInsert));
		headerInclude = name.toLowerCase();
	}

}
