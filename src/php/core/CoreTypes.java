package php.core;

import php.lib.ClsDateTime;
import php.lib.ClsDateTimeZone;
import php.lib.PhpStringType;

public class CoreTypes {
	public static final PrimitiveType Int=new PrimitiveType("int") {
		@Override
		public String getSprintfType() {
			return "%d";
		}
	};
	public static final PrimitiveType Short=new PrimitiveType("short");
	//public static final PrimitiveType Char= new PrimitiveType("char");
	public static final PrimitiveType Bool = new PrimitiveType("bool");
	public static final PrimitiveType Float = new PrimitiveType("float") {
		@Override
		public String getSprintfType() {
			return "%f";
		}
	};
	public static final PrimitiveType Void = new PrimitiveType("void") {
		public boolean typeHinting() {
			return false;
		}
	};
	public static final PrimitiveType Uint = new PrimitiveType("uint");
	public static final PhpStringType String = new PhpStringType();
	public static final Type Mixed = new Type("mixed") {

		public boolean typeHinting() {
			return false;
		}

		@Override
		public NullableType toNullable() {
			return null;
		}
		
	};
	public static final ClsDateTime DateTime = new ClsDateTime();
	public static final ClsDateTimeZone DateTimeZone = new ClsDateTimeZone();
}
