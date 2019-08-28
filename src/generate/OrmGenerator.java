package generate;

import java.io.IOException;
import java.sql.Connection;

import config.OrmConfig;

public abstract class OrmGenerator {
	protected Connection conn;
	protected OrmConfig cfg;
	
	public OrmGenerator(OrmConfig cfg) {
		this.cfg = cfg;
	}
	
	public abstract void generate() throws IOException;
}
