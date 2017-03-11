package javatouml.parsejava;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class ClassTemplate {

	private String class_Name;
	private String extendz;
	private boolean isInterface = false;
	
	public ClassTemplate(String class_Name) {
		
		this.class_Name = class_Name;
	}

	public String getClass_Name() {
		return class_Name;
	}

	public void setClass_Name(String class_Name) {
		this.class_Name = class_Name;
	}
	
	private Collection<String> interfaces = new ArrayList<String>();
	private Map<String, VariableInfo> varMap = new HashMap<String, VariableInfo>();
	private Map<String, MethodClass> methodMap = new HashMap<String, MethodClass>();

	public String getExtendz() {
		return extendz;
	}

	public void setExtendz(String extendz) {
		this.extendz = extendz;
	}

	public boolean isInterface() {
		return isInterface;
	}

	public void setInterface(boolean isInterface) {
		this.isInterface = isInterface;
	}

	
	
	public Collection<String> getInterfaces() {
		return interfaces;
	}

	public void setInterfaces(Collection<String> interfaces) {
		this.interfaces = interfaces;
	}

	
	public void addInterface(String name) {
		interfaces.add(name);
	}
	
	public Map<String, VariableInfo> getVarMap() {
		return varMap;
	}

	public void setVarMap(Map<String, VariableInfo> varMap) {
		this.varMap = varMap;
	}

	public void addVariable(String vName, VariableInfo variable) {
		varMap.put(vName, variable);
	}
	
	
	public Map<String, MethodClass> getMethodMap() {
		return methodMap;
	}

	public void setMethodMap(Map<String, MethodClass> methodMap) {
		this.methodMap = methodMap;
	}
	
	public void addMethod(String mName, MethodClass method) {
		methodMap.put(mName, method);
	}
	
	
	
	
	
	
	
	
	
	
}
