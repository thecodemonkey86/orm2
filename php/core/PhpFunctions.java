package php.core;

public class PhpFunctions {
	
	public static final PhpFunction sprintf = new PhpFunction("sprintf", Types.String);
	public static final PhpFunction unset = new PhpFunction("unset", Types.Void);
	public static final PhpFunction spl_object_hash  = new PhpFunction("spl_object_hash", Types.String);
	public static final PhpFunction count  = new PhpFunction("count", Types.Int);
	public static final PhpFunction isset  = new PhpFunction("isset", Types.Bool);
	
	
}
