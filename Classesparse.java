package javatouml.parsejava;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
//import java.util.ArrayList;
//import java.util.Arrays;
//import java.util.ArrayList;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.imageio.ImageIO;

import com.github.javaparser.JavaParser;
import com.github.javaparser.ast.AccessSpecifier;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.Modifier;
import com.github.javaparser.ast.body.BodyDeclaration;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
//import com.github.javaparser.ast.body.ConstructorDeclaration;
import com.github.javaparser.ast.body.FieldDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.body.Parameter;
import com.github.javaparser.ast.body.VariableDeclarator;
import com.github.javaparser.ast.type.ClassOrInterfaceType;
import com.github.javaparser.ast.type.ReferenceType;
import com.github.javaparser.ast.type.Type;
//import com.github.javaparser.ast.expr.SimpleName;
//import com.github.javaparser.ast.type.Type;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;

import net.sourceforge.plantuml.SourceStringReader;
import net.sourceforge.plantuml.core.DiagramDescription;

public class Classesparse {


	static Map<String, ClassTemplate> classes = new HashMap<String, ClassTemplate>();

	Set <String> varrelation= new HashSet<String>();
	Set <String> varcollection=new HashSet<String>();


	public static ClassTemplate getCUnit(FileInputStream input) throws Exception {
		try {
			CompilationUnit unit = JavaParser.parse(input);
			ClassTemplate classModel = new ClassTemplate("");
			new ClassVisitor().visit(unit, classModel);
			return classModel;
		} finally {
			input.close();
		}
	}


	public void parseClasses() throws Exception {
		File config = new File("/Users/shraddhayeole/PARSER/parsejava/src/test/java/javatouml/parsejava/testcase2");
		File[] fileset = config.listFiles();
		if (fileset != null) {
			for (File javaFile : fileset) {
				if (javaFile.isDirectory()) {
					continue;
				} else if (!(javaFile.getAbsolutePath().endsWith(".java"))) {
					continue;
				}

				ClassTemplate javaClass = Classesparse.getCUnit(new FileInputStream(javaFile));

			}


		}
	}


	public String getGrammer()
	{

		String grammer = "@startuml"+"\n" + "skinparam classAttributeIconSize 0" + "\n" ;
		for (ClassTemplate classModel : classes.values()) {


			grammer = grammer + "\n" + getClassGrammer(classModel)+"\n";
			checkRelationship(classModel);

		}

		//System.out.println("varrelation"+varrelation);


		for(String s3:varrelation)
		{
			System.out.println(s3);
			grammer = grammer + s3+"\n";
		}



		grammer = grammer+ "\n@enduml";
		return grammer;
	}

	public void checkRelationship(ClassTemplate classModel){

		String e="";
		String f="";
		String g="";

		Set<String> s= classModel.refvarmap.get(classModel.getClass_Name());
		//System.out.println("set"+s);

		for(String s2: s)
		{

			if (s2.contains("Collection"))
			{
				String result=s2.substring(s2.indexOf("<")+1,s2.indexOf(">"));
				System.out.println("result"+result);
				e=classModel.getClass_Name()+result;
				System.out.println("EE"+e);
				String erev=new StringBuffer(e).reverse().toString();
				if(varcollection.contains(erev))
				{
					//System.out.println("do nothing");
				}
				else
				{

					f=classModel.getClass_Name()+"\""+"1"+"\""+"---"+"\""+"*"+"\""+result;
					varrelation.add(f);
					varcollection.add(e);
				}
			}
			else
			{
				e=classModel.getClass_Name()+s2;
				//System.out.println("EE"+e);
				String erev=new StringBuffer(e).reverse().toString();
				if(varcollection.contains(erev))
				{
					//System.out.println("do nothing");
				}
				else
				{

					f=classModel.getClass_Name()+"\""+"1"+"\""+"---"+"\""+"1"+"\""+s2;
					varrelation.add(f);
					varcollection.add(e);
				}	
			}	

		}
	}

	public String getClassGrammer(ClassTemplate classModel) {
		String grammer = "";

		if (classModel.isInterface()) {
			grammer = grammer + "interface " + classModel.getClass_Name();
		} else {
			grammer = grammer + "class " + classModel.getClass_Name()+"{";

			for(VariableInfo vn :classModel.varmap.values())
			{

				grammer = grammer +"\n"+vn.getAccess_modifier()+vn.getName()+":"+vn.getData_type();
			}



		}

		grammer = grammer+"\n"+"}";

		return grammer;
	}

	public void viewClassDiagram(String grammer) throws IOException {
		ByteArrayOutputStream boutStram = new ByteArrayOutputStream();
		SourceStringReader reader = new SourceStringReader(grammer);
		DiagramDescription gdesc = reader.generateImage(boutStram);
		byte[] byteArray = boutStram.toByteArray();
		InputStream input = new ByteArrayInputStream(byteArray);
		BufferedImage img = ImageIO.read(input);
		ImageIO.write(img, "png", new File("/Users/shraddhayeole/Desktop/testoutput" + ".png"));
		System.out.println(gdesc);
	}

	/*Method  for parsing class type*/
	public static class ClassVisitor extends VoidVisitorAdapter {

		//ClassTemplate jClass1= new ClassTemplate(null);

		public void visit(ClassOrInterfaceDeclaration n, Object arg) {
			if (arg instanceof ClassTemplate) {

				ClassTemplate jClass = (ClassTemplate) arg;
				ClassTemplate.refervariable =  new HashSet();

				String vType;
				String modifier ="";
				String vName, initValue = null;

				jClass.setClass_Name(n.getName().toString());
				jClass.setInterface(n.isInterface());

				/*
				Method method = new Method(constr.getName(), "", Modifier.toString(constr.getModifiers()));
                jClass.addMethod(method.getName(), method);
				*/
				List<ClassOrInterfaceType> extendz = n.getExtendedTypes();
				if (extendz != null) {
					for (int i = 0; i < extendz.size(); i++) {
						jClass.setExtendz(extendz.get(i).getName().toString());
						System.out.println("extends"+extendz.get(i).getName().toString());
					}
				}

				List<ClassOrInterfaceType> implementz = n.getImplementedTypes();
				if (implementz != null) {
					for (int i = 0; i < implementz.size(); i++) {
						jClass.addInterface(implementz.get(i).getName().toString());
						System.out.println("implement"+implementz.get(i).getName().toString());
					}
				}
				
				



				System.out.println("----------------------------");
				System.out.println("ClassName=>" + n.getName().toString()); 

				List<BodyDeclaration<?>> bDeclrs = n.getMembers();
				Type l1 = null;
				String v4 = null;
				for (BodyDeclaration bDeclr : bDeclrs)
				{
					if (bDeclr instanceof FieldDeclaration) {
						FieldDeclaration var = (FieldDeclaration) bDeclr;
						VariableInfo vi= new VariableInfo("", "", "");
						List<VariableDeclarator> vDeclars = var.getVariables();
						vType = var.getModifiers().toString();

						if (var.getModifiers().toString().compareToIgnoreCase("public")==0)
							modifier = "+";
						else if (var.getModifiers().toString().compareToIgnoreCase("[private]")==0)
							modifier = "-";
						else
							modifier = "#";



						for (VariableDeclarator vDeclar : vDeclars) {
							l1 = vDeclar.getType();
							v4 = vDeclar.getName().toString();


							if (vDeclar.getType() instanceof ReferenceType)
							{

								if (vDeclar.getType().toString().contains("[]"))
								{
									System.out.println("Variable Name=>" + v4);
									vi.setName(v4);
									System.out.println("Variable data_Type=>" + l1);
									vi.setData_type(l1.toString());
									vi.setAccess_modifier(modifier);
									jClass.varmap.put(vi.getName(), vi);
								}

								
								else 
								{
									jClass.refervariable.add(vDeclar.getType().toString());
								}	
							}else{

								System.out.println("Variable Name=>" + v4);
								vi.setName(v4);
								System.out.println("Variable data_Type=>" + l1);
								vi.setData_type(l1.toString());
								vi.setAccess_modifier(modifier);

								jClass.varmap.put(vi.getName(), vi);

							}

						}
						if (var.getVariables().get(0).getInitializer() != null)
							initValue = var.getVariables().get(0).getInitializer().toString();

					}

					if (bDeclr instanceof MethodDeclaration) {
						MethodDeclaration jmethod = (MethodDeclaration) bDeclr;

						System.out.println("MethodName=>" + jmethod.getName());
						System.out.println("MethodReturn_Type=>"+jmethod.getType());
						EnumSet<Modifier> methmod = jmethod.getModifiers();
						for (Modifier mod1 : methmod) {
							AccessSpecifier m5 = Modifier.getAccessSpecifier(methmod);
							System.out.println("Method_modifier=>" + m5);

							MethodClass method = new MethodClass(jmethod.getName().toString(),
									jmethod.getType().toString(), Modifier.getAccessSpecifier(methmod).toString());
							System.out.println("method_Class_Details=>" + method);

						}

						jmethod.getModifiers();

						List<Parameter> param = ((MethodDeclaration) bDeclr).getParameters();
						for (Parameter pm : param) {
							System.out.println("Method_parameter=>:" + pm.getName());

						}

					} //end of method declaration

				} //end of body declaration for loop

				jClass.refvarmap.put(n.getName().toString(), jClass.refervariable);
				classes.put(n.getName().toString(), jClass);
				//System.out.println("mapval--->>"+Arrays.asList(jClass.refvarmap));
				//System.out.println(jClass.refvarmap.values());

			}
			//System.out.println(n.getName().toString());


			//System.out.println("mapval--->>"+Arrays.asList(classes));
			//System.out.println(classes.values());

		}//void visit
	}//class visitor

}//classes parse



