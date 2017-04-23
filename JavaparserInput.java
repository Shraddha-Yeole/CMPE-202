



public class JavaparserInput {

	

	public static void main(String[] args) throws Exception 
	{	Classesparse c= new Classesparse(args[0],args[1]);
		c.parseClasses();
		String g1=c.getGrammer();
		System.out.println("PlantUMLGrammer=>\n"+g1);
		System.out.println("Generate Digrame");
		c.viewClassDiagram(g1);
	}	
}

