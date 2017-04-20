

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
//import java.util.ArrayList;
//import java.util.Arrays;
//import java.util.ArrayList;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import javax.imageio.ImageIO;

import com.github.javaparser.JavaParser;
import com.github.javaparser.ast.AccessSpecifier;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.Modifier;
import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.body.BodyDeclaration;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.ConstructorDeclaration;
//import com.github.javaparser.ast.body.ConstructorDeclaration;
import com.github.javaparser.ast.body.FieldDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.body.Parameter;
import com.github.javaparser.ast.body.VariableDeclarator;
import com.github.javaparser.ast.stmt.BlockStmt;
import com.github.javaparser.ast.stmt.Statement;
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
    List<String> DependencyList=new ArrayList<String>();
    
   // String outputDir="/Users/shraddhayeole/Desktop/testoutput" + ".png";
	//String classpath="/Users/shraddhayeole/PARSER/parsejava/src/test/java/javatouml/parsejava/uml-parser-test-5";
	String classpath;
	String outputDir;
	

	public Classesparse(String classpath,String outputDir) {
		if (classpath == null || outputDir == null || classpath.equals("") || outputDir.equals("")) {
			usage();
		}
		this.classpath = classpath;
		this.outputDir = outputDir;
	
	}
    
	public void usage() {
		System.out.println("Usage:\njava -jar <classpath> <outputfilename>");
	}

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
		File config = new File(classpath);
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
			checkDependency(classModel);
		}

		for(String s3:varrelation)
		{
			//System.out.println("s3 "+s3);
			grammer = grammer + s3+"\n";
		}

		for(String s4:DependencyList)
		{
			//System.out.println("s4"+s4);
			grammer=grammer+ s4+"\n";
		}
		
		grammer = grammer+ "\n@enduml";
		return grammer;
	}


	public void checkDependency(ClassTemplate classModel){

		String a="";
		for(MethodClass m: classModel.getMethods())
		{
			for (VariableInfo pVar : m.getParameters().values()) {
				
				if((pVar.getData_type() instanceof ReferenceType))
				{
					if(classes.containsKey(pVar.getData_type().toString()) && classes.get(pVar.getData_type().toString()).isInterface())
					{	
						if (classModel.isInterface())
						{
							
						}
						else
							{
							a=classModel.getClass_Name()+"..>"+pVar.getData_type()+":uses";
							}
						if(!DependencyList.contains(a))
							
						    DependencyList.add(a);
					}
						  
					}
					}
			
			
			
				}
			}
		

	


	public void checkRelationship(ClassTemplate classModel){

		String e="";
		String f="";
		String g="";

		Set<String> s= classModel.refvarmap.get(classModel.getClass_Name());
		System.out.println("set"+s);

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
			else if(s2.contains("String"))
			{

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




	public static boolean isGetSetter(ClassTemplate classModel, MethodClass method) {
		for (VariableInfo cVar: classModel.varmap.values()) {
			if(method.getName().equalsIgnoreCase("get" + cVar.getName()) || method.getName().equalsIgnoreCase("set" + cVar.getName())) {
				cVar.setAccess_modifier("+");
				return true;
			}
			//else if(method.getName().contains("set")  || method.getName().contains("get"))
			//{
				//return true;
			//}
		}
		return false;

	}


	public String getClassGrammer(ClassTemplate classModel) {
		String grammer = "";
		String modifier = "", mGrammer = "";
		if (classModel.isInterface()) {
			//interfaceList.add(classModel.getClass_Name());
			grammer = grammer + "interface " + classModel.getClass_Name()+"<<interface>>"+"{";
			
			
			for (MethodClass method : classModel.getMethods()) {

				if (method.getAccess_modifier().toString().contains("PUBLIC"))
					modifier = "+";
				else if (method.getAccess_modifier().toString().contains("PRIVATE"))
					modifier = "-";
				else
					modifier = "#";



				grammer= grammer+"\n"+modifier+method.getName()+"(";

				for (VariableInfo pVar : method.getParameters().values()) {
					String pVariable = pVar.getName() + ":" + pVar.getData_type();
					int i=0;
					i++;
					if (method.getParameters().values().size() != i) {
						pVariable = pVariable + ",";
					}
					grammer = grammer + pVariable;

				}

			
					grammer = grammer +")"+":"+method.getReturn_type();
				


			

			}
		} else {
			grammer = grammer + "class " + classModel.getClass_Name();

			if (classModel.getExtendz() != null)
			{
				grammer=grammer+" extends "+ classModel.getExtendz();
			}

			if (classModel.getInterfaces() != null && classModel.getInterfaces().size() > 0) {
				int i = 0;
				grammer = grammer + " implements ";
				for (String interf : classModel.getInterfaces()) {
					grammer = grammer + interf;
					i++;
					if (i != classModel.getInterfaces().size()) {
						grammer = grammer + ", ";
					}
				}
			}


			grammer=grammer+"{";


			for(VariableInfo vn :classModel.varmap.values())
			{

				grammer = grammer+"\n"+vn.getAccess_modifier()+vn.getName()+":"+vn.getData_type();
			}

			for (MethodClass method : classModel.getMethods()) {

				if (method.getAccess_modifier().toString().contains("PUBLIC"))
					modifier = "+";
				else if (method.getAccess_modifier().toString().contains("PRIVATE"))
					modifier = "-";
				else
					modifier = "#";



				grammer= grammer+"\n"+modifier+method.getName()+"(";

				for (VariableInfo pVar : method.getParameters().values()) {
					String pVariable = pVar.getName() + ":" + pVar.getData_type();
					int i=0;
					i++;
					if (method.getParameters().values().size() != i) {
						pVariable = pVariable + ",";
					}
					grammer = grammer + pVariable;

				}

			
				if(method.getReturn_type() != null)
				{
					grammer = grammer +")"+":"+method.getReturn_type();
				}
				
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
		ImageIO.write(img, "png", new File(outputDir));
		System.out.println(gdesc);
	}


	/*Method  for parsing class type*/
	public static class ClassVisitor extends VoidVisitorAdapter {

		private static final MethodClass MethodClass = null;

		public void visit(ClassOrInterfaceDeclaration n, Object arg) {
			if (arg instanceof ClassTemplate) {

				ClassTemplate jClass = (ClassTemplate) arg;
				ClassTemplate.refervariable =  new HashSet();


				List<ClassOrInterfaceType> extendsList= n.getExtendedTypes();

				if(extendsList != null){

					for (int i = 0; i < extendsList.size(); i++) {

						jClass.setExtendz(extendsList.get(i).getName().toString());

					}
				}



				String vType;
				String modifier ="";
				String vName, initValue = null;
				jClass.setClass_Name(n.getName().toString());
				jClass.setInterface(n.isInterface());


				List<ClassOrInterfaceType> implementz = n.getImplementedTypes();
				for (int i = 0; i < implementz.size(); i++) {
					jClass.addInterface(implementz.get(i).getName().toString());
					//System.out.println("implement--"+implementz.get(i).getName().toString());
					//System.out.println(implement);
				}
				//System.out.println("----------------------------");
				System.out.println("\n"+"ClassName=>" + n.getName().toString()); 
				//System.out.println("interfacelist"+interfaceList);
				List<BodyDeclaration<?>> bDeclrs = n.getMembers();
				Type l1 = null;
				String v4 = null;
				for (BodyDeclaration bDeclr : bDeclrs)
				{
					if (bDeclr instanceof FieldDeclaration) {

						FieldDeclaration var = (FieldDeclaration) bDeclr;
						VariableInfo vi= new VariableInfo("", null, "");
						List<VariableDeclarator> vDeclars = var.getVariables();
						vType = var.getModifiers().toString();

						if (var.getModifiers().toString().contains("PUBLIC"))
							modifier = "+";

						else if (var.getModifiers().toString().contains("PRIVATE"))
							modifier = "-";
						else
							modifier = "#";


						for (VariableDeclarator vDeclar : vDeclars) {
							l1 = vDeclar.getType();

							v4 = vDeclar.getName().toString();
							if (vDeclar.getType() instanceof ReferenceType)
							{

								if (vDeclar.getType().toString().contains("[]") || vDeclar.getType().toString().contains("String") && (modifier.equals("-") || modifier.equals("+")))
								{
									System.out.println("Variable Name=>" + v4);
									vi.setName(v4);
									System.out.println("Variable data_Type=>" + l1);
									vi.setData_type(l1);
									vi.setAccess_modifier(modifier);
									jClass.varmap.put(vi.getName(), vi);
								}
								else 
								{
									jClass.refervariable.add(vDeclar.getType().toString());
								}	

							}else{

								System.out.println("Variable Name--" + v4);
								vi.setName(v4);
								System.out.println("Variable data_Type--" + l1);
								vi.setData_type(l1);
								vi.setAccess_modifier(modifier);

								jClass.varmap.put(vi.getName(), vi);
								
							}

						}
						if (var.getVariables().get(0).getInitializer() != null)
							initValue = var.getVariables().get(0).getInitializer().toString();

					}

					if (bDeclr instanceof MethodDeclaration) {
						MethodDeclaration jmethod = (MethodDeclaration) bDeclr;
						MethodClass mi= new MethodClass("", "", "");
						System.out.println("\n"+"MethodName=>" + jmethod.getName());
						System.out.println("MethodReturn_Type=>"+jmethod.getType());
						EnumSet<Modifier> methmod = jmethod.getModifiers();
						for (Modifier mod1 : methmod) {
							AccessSpecifier m5 = Modifier.getAccessSpecifier(methmod);
							System.out.println("Method_modifier=>" + m5);

							if (m5.toString().compareToIgnoreCase("public")==0)
								modifier = "+";
							else if (m5.toString().compareToIgnoreCase("[private]")==0)
								modifier = "-";
							else
								modifier = "#";

							mi.setAccess_modifier(modifier);
							System.out.println("modifiers=>"+mi.getAccess_modifier());

							
							
							MethodClass method = new MethodClass(Modifier.getAccessSpecifier(methmod).toString(),jmethod.getType().toString(),
									jmethod.getName().toString());
							 

							List<Node> methodbody = jmethod.getChildNodes();
							for (Node node: methodbody)
							{
								//System.out.println(node.toString());
								Statement stm;
								for(VariableInfo s: jClass.varmap.values())
								{
									if(node.toString().contains(s.getName()))
									{
										int flag = 1;
										System.out.println("hello"+s.getName());
									}
									
								}
							
							}
							
							if (!isGetSetter(jClass, method) && modifier == "+") {
								//System.out.println("method_Class_Details=>" + method);
								jClass.addMethod(jmethod.getName().toString(), method);
								//System.out.println("meth+"+jmethod.getName().toString());
							}
							
							
							
							
							
							jmethod.getModifiers();
							//jClass.addMethod(MethodClass.getName(), MethodClass);
							//System.out.println("method class"+MethodClass.getName());
							List<Parameter> param = ((MethodDeclaration) bDeclr).getParameters();
							for (Parameter pm : param) {
								System.out.println("Method_parameter=>:" + pm.getName());
								System.out.println("Method_parameter_Return=>"+pm.getType());
								method.add_Parameter(new VariableInfo(pm.getName().toString(), pm.getType(),null));


							}
							
						
							
							Optional<BlockStmt> blockStmnt = jmethod.getBody();
							if (blockStmnt != null) {
								String body=blockStmnt.toString();
	                          System.out.println("method sttement"+ body);
	                          
	                          String trylist[]= body.split("=");
	                          System.out.println("trylist"+trylist);
	                          //String ab= trylist[0].split(" ")[2];
	                         // System.out.println(ab);
	                          
	                         
	                        }

						}

					} //end of method declaration



					if (bDeclr instanceof ConstructorDeclaration) {
						ConstructorDeclaration constr = (ConstructorDeclaration) bDeclr;

						EnumSet<Modifier> m5 = constr.getModifiers();



						MethodClass method = new MethodClass(m5.toString(),"",constr.getName().toString());
						jClass.addMethod(method.getName(), method);

						if (constr.getParameters() != null) {
							for (Parameter param : constr.getParameters()) {
								method.add_Parameter(new VariableInfo(param.getName().toString(), param.getType(), null));
							}
						}


					}



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




