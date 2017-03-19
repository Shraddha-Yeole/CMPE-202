package javatouml.parsejava;

public class JavaparserInput {

	public static void main(String[] args) throws Exception 
	{	
		Classesparse c= new Classesparse();
		c.parseClasses();
		System.out.println("IntermediateGrammer");
		String g1=c.getGrammer();
		System.out.println("PlantUMLGrammer=>\n\n"+g1);

	}	

}
