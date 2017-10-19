package cpp.lib;

import cpp.Types;
import cpp.cls.Attr;
import cpp.cls.Cls;
import cpp.cls.bean.repo.ClsBeanRepository;
import cpp.cls.method.MethodAttributeGetter;

public class ClsBaseRepository extends Cls{

	public static final String saveBean = "saveBean"; 
	
	public ClsBaseRepository() {
		super("BaseRepository");
		Attr sqlCon = new Attr(Types.Sql.toRawPointer(), ClsBeanRepository.sqlCon);
		addAttr(sqlCon);
		addMethod(new MethodAttributeGetter(sqlCon));
		addMethod( new LibMethod(Types.Void, saveBean));
		headerInclude = name.toLowerCase();
	}

}
