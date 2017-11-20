package database.column;

public class SqliteColumn extends Column{

	@Override
	public String getEscapedName() {
		return String.format("\"%s\"", getName());
	}

}
