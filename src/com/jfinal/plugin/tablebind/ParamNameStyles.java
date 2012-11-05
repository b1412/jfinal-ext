package com.jfinal.plugin.tablebind;

public class ParamNameStyles {

	public static INameStyle module(String moduleName) {
		return new ModuleNameStyle(moduleName);
	}
	public static INameStyle lowerModule(String moduleName) {
		return new LowerModuleNameStyle(moduleName);
	}
	public static INameStyle upModule(String moduleName) {
		return new UpModuleNameStyle(moduleName);
	}
}

class ModuleNameStyle implements INameStyle {
	private String moduleName;

	public ModuleNameStyle(String moduleName) {
		this.moduleName = moduleName;
	}

	public ModuleNameStyle() {}

	@Override
	public String name(String className) {
		return moduleName + className;
	}
}
class LowerModuleNameStyle implements INameStyle {
	private String moduleName;
	
	public LowerModuleNameStyle(String moduleName) {
		this.moduleName = moduleName;
	}
	
	public LowerModuleNameStyle() {}
	
	@Override
	public String name(String className) {
		return moduleName + className.toLowerCase();
	}
}
class UpModuleNameStyle implements INameStyle {
	private String moduleName;
	
	public UpModuleNameStyle(String moduleName) {
		this.moduleName = moduleName;
	}
	
	public UpModuleNameStyle() {}
	
	@Override
	public String name(String className) {
		return moduleName + className.toUpperCase();
	}
}
