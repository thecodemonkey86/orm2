package database.column;

public class MySqlColumn extends Column{

	@Override
	public String getEscapedName() {
		return String.format("`%s`", getName());
	}

}
