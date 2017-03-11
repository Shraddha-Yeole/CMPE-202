package javatouml.parsejava;

import java.io.FileInputStream;
//import java.util.ArrayList;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.github.javaparser.JavaParser;
import com.github.javaparser.ast.AccessSpecifier;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.Modifier;
import com.github.javaparser.ast.body.BodyDeclaration;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.FieldDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.body.Parameter;
import com.github.javaparser.ast.body.VariableDeclarator;
import com.github.javaparser.ast.expr.SimpleName;
import com.github.javaparser.ast.type.Type;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;

public class Classesparse {

	public static void main(String[] args) throws Exception {
		// creates an input stream for the file to be parsed
		// FileInputStream in = new
		// FileInputStream("/Users/shraddhayeole/PARSER/parsejava/src/test/java/javatouml/parsejava/A.java");
		ClassTemplate javaClass = getCUnit(
				new FileInputStream("/Users/shraddhayeole/PARSER/parsejava/src/test/java/javatouml/parsejava/A.java"));
		ClassTemplate javaClass2 = getCUnit(
				new FileInputStream("/Users/shraddhayeole/PARSER/parsejava/src/test/java/javatouml/parsejava/B.java"));

		Map<String, ClassTemplate> classes = new HashMap<String, ClassTemplate>();
		classes.put(javaClass.getClass_Name(), javaClass);
		System.out.println("ca---"+javaClass.toString());
		
		int x = classes.size();
		System.out.println("no of classes:" + x);
		/*
		 * List<ClassTemplate> classes1 = new ArrayList();
		 * classes1.add(javaClass); classes1.add(javaClass2);
		 * 
		 * for (ClassTemplate class1 : classes) {
		 * System.out.println(class1.getClass_Name()); }
		 */

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
	/*Method to for parsing class type*/
	public static class ClassVisitor extends VoidVisitorAdapter {

		public void visit(ClassOrInterfaceDeclaration n, Object arg) {
			if (arg instanceof ClassTemplate) {

				ClassTemplate jClass = (ClassTemplate) arg;

				EnumSet<Modifier> vType;
				String vName, initValue = null;
				jClass.setClass_Name(n.getName().toString());
				jClass.setInterface(n.isInterface());

				/*
				 * System.out.println("ClassName" + n.getName().toString());
				 * System.out.println("MapValue" + jClass.getClass_Name());
				 */

				jClass.setInterface(n.isInterface());

				List<BodyDeclaration<?>> bDeclrs = n.getMembers();

				for (BodyDeclaration bDeclr : bDeclrs) {
					if (bDeclr instanceof FieldDeclaration) {
						FieldDeclaration var = (FieldDeclaration) bDeclr;
						List<VariableDeclarator> vDeclars = var.getVariables();
						for (VariableDeclarator vDeclar : vDeclars) {
							Type l1 = vDeclar.getType();
							SimpleName v4 = vDeclar.getName();

							/*
							 * System.out.println("Variable Name=>" + v4);
							 * System.out.println("Variable Type=>" + l1);
							 */

						}
						vType = var.getModifiers();
						System.out.println("Variable Modifier=>" + vType);
						// jClass.addVariable(v4, new
						// VariableInfo(v4,l1,,vType));

					}

					if (bDeclr instanceof MethodDeclaration) {
						MethodDeclaration jmethod = (MethodDeclaration) bDeclr;

						System.out.println("classname" + jmethod.getName());
						// SimpleName meth_name = ((MethodDeclaration)
						// bDeclr).getName();
						// System.out.println("Type=>"+jmethod.getType());
						EnumSet<Modifier> methmod = jmethod.getModifiers();
						for (Modifier mod1 : methmod) {
							AccessSpecifier m5 = Modifier.getAccessSpecifier(methmod);
							System.out.println("modifier" + m5);

							MethodClass method = new MethodClass(jmethod.getName().toString(),
									jmethod.getType().toString(), Modifier.getAccessSpecifier(methmod).toString());
							System.out.println("method-----" + method);

						}

						jmethod.getModifiers();
						

						List<Parameter> param = ((MethodDeclaration) bDeclr).getParameters();
						for (Parameter pm : param) {
							System.out.println("parameter=>:" + pm.getName());

							
							// MethodClass(jmethod.getName(),
							// jmethod.getType().toString(),
							// Modifier.toString(jmethod.getModifiers()));

						}

					}

				}
			}
		}
	}

}
