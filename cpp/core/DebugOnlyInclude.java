package cpp.core;

public class DebugOnlyInclude extends Include {
	
	Include i;
	
	public DebugOnlyInclude(Include i) {
		this.i = i;
	}
	
	@Override
	public String toString() {
		return "#ifdef QT_DEBUG\r\n" + i + "\r\n#endif";
	}
}
