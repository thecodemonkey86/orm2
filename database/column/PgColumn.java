package database.column;

public class PgColumn extends Column{

	@Override
	public String getEscapedName() {
		return String.format("\"%s\"", getName());
	}

}
