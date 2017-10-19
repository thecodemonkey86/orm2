package generate;

import java.sql.Connection;

import config.OrmConfig;

public class OrmCommon {
	protected Connection conn;
	protected OrmConfig cfg;
	
	public OrmCommon(OrmConfig cfg) {
		this.cfg = cfg;
	}
}
