package generate;

import java.nio.file.Path;
import java.nio.file.Paths;

import config.SetPassConfigReader;
import config.json.JsonModeConfigReader;
import config.json.JsonOrmConfig;
import io.PasswordManager;
import util.Pair;
import xml.reader.DefaultXMLReader;

public class JsonOrmGenerator {

	public static void main(String[] args) throws Exception {
		if(args.length == 0) {
			throw new Exception("Please provide xml config file");
		}
		PasswordManager.setSuperPassword(new byte[] {
				0x7,
				58,
				1,
				0xf,
				0x7f,
				0x8,
				65,
				0x58
		});
		
		
		Path xmlFile = Paths.get(args[args.length-1]);
		
		boolean setPass= args[0].equals("--setpass");
		if(setPass) {
			SetPassConfigReader cfgReader = new SetPassConfigReader();
			DefaultXMLReader.read(xmlFile, cfgReader);
			PasswordManager.saveToFile(cfgReader.getCredentials(), args[1] );
			return;
		}
		Pair<OrmGenerator, OrmGenerator> ormGenerators = JsonModeConfigReader.read(xmlFile);
		ormGenerators.getValue1().generate();
		ormGenerators.getValue2().generate();
	}

}
