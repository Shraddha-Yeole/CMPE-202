package javatouml.parsejava;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;




public class JavaparserInput {

	String outputDir;
	String classpath;

	Map<String, ClassTemplate> classes = new HashMap<String, ClassTemplate>();

	/*
	public JavaparserInput(String classpath, String outputDir)
	{
		this.classpath = classpath;
		this.outputDir = outputDir;
	}
	 */


	public static void main(String[] args) throws Exception 
	{	

		JavaparserInput input = new JavaparserInput(); 
		input.parseClasses();


	}	


	public void parseClasses() throws Exception {
		File config = new File("/Users/shraddhayeole/PARSER/parsejava/src/test/java/javatouml/parsejava/testcase");
		File[] fileset = config.listFiles();
		if (fileset != null) {
			for (File javaFile : fileset) {
				if (javaFile.isDirectory()) {
					continue;
				} else if (!(javaFile.getAbsolutePath().endsWith(".java"))) {
					continue;
				}
				ClassTemplate javaClass = Classesparse.getCUnit(new FileInputStream(javaFile));
				classes.put(javaClass.getClass_Name(), javaClass);
			}


		}
	}


}
