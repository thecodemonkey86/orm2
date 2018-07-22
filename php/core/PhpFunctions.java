package php.core;

public class PhpFunctions {
	
	public static final PhpFunction sprintf = new PhpFunction("sprintf", Types.String);
	public static final PhpFunction unset = new PhpFunction("unset", Types.Void);
	//public static final PhpFunction spl_object_hash  = new PhpFunction("spl_object_hash", Types.String);
	public static final PhpFunction count  = new PhpFunction("count", Types.Int);
	public static final PhpFunction isset  = new PhpFunction("isset", Types.Bool);
	public static final PhpFunction array_values  = new PhpFunction("array_values", Types.array(Types.Mixed));
	public static final PhpFunction ibase_fetch_assoc = new PhpFunction("ibase_fetch_assoc", Types.array(Types.Mixed));
	public static final PhpFunction ibase_trans = new PhpFunction("ibase_trans", Types.Resource);
	public static final PhpFunction ibase_commit = new PhpFunction("ibase_commit", Types.Bool);
	public static final PhpFunction ibase_rollback = new PhpFunction("ibase_rollback", Types.Bool);
	public static final PhpFunction strtoupper = new PhpFunction("strtoupper", Types.String);
	public static final PhpFunction substr = new PhpFunction("substr", Types.String);
	public static final PhpFunction md5 = new PhpFunction("md5", Types.String);
	public static final PhpFunction serialize = new PhpFunction("serialize", Types.String);
	
	
	
}
