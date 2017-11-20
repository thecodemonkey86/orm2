package database.column;

public class FirebirdColumn extends Column {
	@Override
	public String getEscapedName() {
		return String.format("\"%s\"", getName().toUpperCase());
	}
}
