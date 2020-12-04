package cpp;

import cpp.lib.ClsQDebug;
import cpp.lib.ClsQEventLoop;
import cpp.lib.ClsQObject;
import cpp.lib.ClsQString;

public class QtCoreTypes {
	public static final ClsQObject QObject = new ClsQObject();
	public static final ClsQString QString = Types.QString;
	public static final ClsQEventLoop QEventLoop = new ClsQEventLoop();
	public static final ClsQDebug QDebug = new ClsQDebug();
}
