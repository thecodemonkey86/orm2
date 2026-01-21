package cpp.openapi;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.Yaml;

import config.cpp.CppOrmConfig;
import database.column.Column;
import database.relation.ManyRelation;
import database.relation.OneRelation;
import database.relation.OneToManyRelation;
import database.table.Table;

public class OrmYamlWriter {
	public static void write(Table tbl, CppOrmConfig cfg) {
		// Create data
		List<OneToManyRelation> manyRelations = cfg.getOneToManyRelations(tbl);
		List<OneRelation> oneRelations = cfg.getOneRelations(tbl);
		List<ManyRelation> manyToManyRelations = cfg.getManyRelations(tbl);
        Map<String,Object> data = new LinkedHashMap<>();
        data.put("title", tbl.getUc1stCamelCaseName());
        data.put("type", "object");
        Map<String,Object> props = new LinkedHashMap<>();
        OpenApiYamlDatabaseTypeMapper typemapper=null;
        
        if(cfg.isEnginePostgres()) {
        	typemapper=new PgOpenApiYamlDatabaseTypeMapper();
        }
        System.out.println(String.format("OpenApi::%s WorkflowController::convert%s(%s %s)",tbl.getUc1stCamelCaseName(),tbl.getUc1stCamelCaseName(),tbl.getUc1stCamelCaseName(),tbl.getCamelCaseName()));
        System.out.println("{");
        for(Column col:tbl.getAllColumns()) {
        	//if (!col.hasOneRelation()) {
        		var colType = typemapper.toYamlType(col).toMap();
        		props.put(col.getCamelCaseName(), colType);
        		System.out.println(tbl.getName().charAt(0)+"."+col.getCamelCaseName()+"="+tbl.getCamelCaseName()+".get"+col.getUc1stCamelCaseName()+"()"+(col.isNullable()?".toStdOptional();":";"));
        		//System.out.println(tbl.getCamelCaseName()+"->set"+col.getUc1stCamelCaseName()+"("+ tbl.getName().charAt(0)+"."+col.getCamelCaseName()+");");
        	//}
        }
        System.out.println("}\r\n\r\n");
        data.put("properties", props);
        List<String> required = new ArrayList<>();
        for(Column col:tbl.getAllColumns()) {
        	if(!col.isNullable())
        	required.add(col.getCamelCaseName());
        }
        data.put("required",required);
        //data.put("languages", Arrays.asList("Java", "Python", "Go"));

        // Configure YAML output
        DumperOptions options = new DumperOptions();
        options.setIndent(2);
        options.setPrettyFlow(true);
        options.setDefaultFlowStyle(DumperOptions.FlowStyle.BLOCK);

        Yaml yaml = new Yaml(options);

        // Write YAML to file
        try (FileWriter writer = new FileWriter(tbl.getCamelCaseName().toLowerCase()+ ".yaml")) {
        	Map<String,Object> m = new LinkedHashMap<>();
        	m.put(tbl.getUc1stCamelCaseName(), data);
            yaml.dump(m, writer);
            System.out.println("YAML file written successfully.");
        } catch (IOException e) {
            e.printStackTrace();
        }
	}
}
