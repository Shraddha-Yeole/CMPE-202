package javatouml.parsejava;

import java.util.HashMap;
import java.util.Map;

public class MethodClass {
	
	private String access_modifier;
	private String return_type;
	private String name;
	
	
	private Map<String, VariableInfo> parameters = new HashMap<String, VariableInfo>();		
	private Map<String, VariableInfo> localvar = new HashMap<String, VariableInfo>();
	
	
	public MethodClass(String access_modifier, String return_type, String name) {
		
		this.access_modifier = access_modifier;
		this.return_type = return_type;
		this.name = name;
	}

	public String getAccess_modifier() {
		return access_modifier;
	}

	public void setAccess_modifier(String access_modifier) {
		this.access_modifier = access_modifier;
	}

	public String getReturn_type() {
		return return_type;
	}

	public void setReturn_type(String return_type) {
		this.return_type = return_type;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	
	public void add_Parameter(VariableInfo param) {
		parameters.put(param.getName(), param);
	}

	public Map<String, VariableInfo> getParameters() {
		return parameters;
	}

	public void add_LocalVar(VariableInfo param) {
		localvar.put(param.getName(), param);
	}

	public Map<String, VariableInfo> getLocalvar() {
		return localvar;
	}

	public String toString() {
		String str = "\n" + access_modifier + " " + return_type + " " + name + " :"; 
		for (String pname : parameters.keySet()) {
			VariableInfo param = parameters.get(pname);
			str = str + param.toString();
		}		
		return str;
	}
	
	
}
