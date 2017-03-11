package javatouml.parsejava;

public class VariableInfo {

	private String name;
	private String data_type;
	private String init_value;
	private String access_modifier;
	
	public VariableInfo(String name, String data_type, String init_value, String access_modifier) {
		
		this.name = name;
		this.data_type = data_type;
		this.init_value = init_value;
		this.access_modifier = access_modifier;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getData_type() {
		return data_type;
	}

	public void setData_type(String data_type) {
		this.data_type = data_type;
	}

	public String getInit_value() {
		return init_value;
	}

	public void setInit_value(String init_value) {
		this.init_value = init_value;
	}

	public String getAccess_modifier() {
		return access_modifier;
	}

	public void setAccess_modifier(String access_modifier) {
		this.access_modifier = access_modifier;
	}

	@Override
	public String toString() {
		
		String str =  data_type + " " + name + " :" 
				+ init_value + " " + access_modifier;
		return str;
		
	}
	
	
	
	
}
