package javatouml.parsejava;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
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
import com.github.javaparser.ast.body.ConstructorDeclaration;
import com.github.javaparser.ast.body.FieldDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.body.Parameter;
import com.github.javaparser.ast.body.VariableDeclarator;
import com.github.javaparser.ast.expr.SimpleName;
import com.github.javaparser.ast.type.Type;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;

public class Classesparse {

	
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
	
	
	/*Method  for parsing class type*/
 public static class ClassVisitor extends VoidVisitorAdapter {

		public void visit(ClassOrInterfaceDeclaration n, Object arg) {
			if (arg instanceof ClassTemplate) {

				ClassTemplate jClass = (ClassTemplate) arg;

				EnumSet<Modifier> vType;
				String vName, initValue = null;
				jClass.setClass_Name(n.getName().toString());
				jClass.setInterface(n.isInterface());

				System.out.println("----------------------------");
				System.out.println("ClassName=>" + n.getName().toString());
				//System.out.println("MapValue" + jClass.getClass_Name());


				jClass.setInterface(n.isInterface());

				List<BodyDeclaration<?>> bDeclrs = n.getMembers();
				//System.out.println("bodylist"+n.getMembers());

				for (BodyDeclaration bDeclr : bDeclrs)
	{
					if (bDeclr instanceof FieldDeclaration) {
						FieldDeclaration var = (FieldDeclaration) bDeclr;
						List<VariableDeclarator> vDeclars = var.getVariables();
						for (VariableDeclarator vDeclar : vDeclars) {
							Type l1 = vDeclar.getType();
							SimpleName v4 = vDeclar.getName();


							System.out.println("Variable Name=>" + v4);
							System.out.println("Variable data_Type=>" + l1);


						}
						vType = var.getModifiers();
						System.out.println("Variable Modifier=>" + vType);
						// jClass.addVariable(v4, new
						// VariableInfo(v4,l1,,vType));

					}

					if (bDeclr instanceof MethodDeclaration) {
						MethodDeclaration jmethod = (MethodDeclaration) bDeclr;

						System.out.println("MethodName=>" + jmethod.getName());
						// SimpleName meth_name = ((MethodDeclaration)
						// bDeclr).getName();
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

						//Add constructors to MethodClass
						/**
						if (bDeclr instanceof ConstructorDeclaration) {
							ConstructorDeclaration constr = (ConstructorDeclaration) bDeclr;

							MethodClass method = new MethodClass(constr.getName().toString(),constr.getTypeParameters(),Modifier.getAccessSpecifier(method));
							jClass.addMethod(method.getName(), method);

							if (constr.getParameters() != null) {
								for (Parameter param : constr.getParameters()) {
									method.addParameter(new Variable(param.getId().getName(), param.getType().toString(), null, null));
								}
							}


						 **/
						List<Parameter> param = ((MethodDeclaration) bDeclr).getParameters();
						for (Parameter pm : param) {
							System.out.println("Method_parameter=>:" + pm.getName());

						}

					} //end of method declaration

				} //end of body declaration
			}
		}//void visit
	}//class visitor

}//classes parse
