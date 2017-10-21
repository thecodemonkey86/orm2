package database.column;

public class FirebirdColumn extends Column {
	@Override
	public String getEscapedName() {
		return super.getEscapedName().toUpperCase();
	}
}
