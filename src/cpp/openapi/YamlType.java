package cpp.openapi;

import java.util.LinkedHashMap;
import java.util.Map;

public class YamlType {
	public static final String TYPE_INTEGER="integer";
	public static final String TYPE_NUMBER="number";
	public static final String TYPE_STRING="string";
	public static final String TYPE_BOOLEAN="boolean";
	public static final String FORMAT_INT64="int64";
	public static final String FORMAT_INT16="int16";
	public static final String FORMAT_DATE="date";
	public static final String FORMAT_DATETIME="date-time";
	public static final String FORMAT_DOUBLE="double";
	
	public static final YamlType INT16=new YamlType(TYPE_INTEGER,FORMAT_INT16);
	public static final YamlType INT32=new YamlType(TYPE_INTEGER,null);
	public static final YamlType INT64=new YamlType(TYPE_INTEGER, FORMAT_INT64);
	public static final YamlType DOUBLE=new YamlType(TYPE_NUMBER, FORMAT_DOUBLE);
	public static final YamlType STRING=new YamlType(TYPE_STRING, null);
	public static final YamlType DATE=new YamlType(TYPE_STRING, FORMAT_DATE);
	public static final YamlType DATETIME=new YamlType(TYPE_STRING, FORMAT_DATETIME);
	public static final YamlType BOOLEAN=new YamlType(TYPE_BOOLEAN, null);
	
	public YamlType(String type, String format) {
		super();
		this.type = type;
		this.format = format;
	}

	private String type,format;
	
	public String getFormat() {
		return format;
	}
	
	public String getType() {
		return type;
	}

	public Map<String,Object> toMap() {
		Map<String,Object> m= new LinkedHashMap<>();
		m.put("type", type);
		if(format!=null) {
			m.put("format", format);
		}
		return m;
	}
}
