/**
 * 
 */
package org.neodatis.odb.test.context;

import org.neodatis.odb.core.context.NeoDatisObjectAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * @author olivier
 *
 */
public class Computer extends NeoDatisObjectAdapter {
	protected String name;
	protected int type;
	
	protected List<Monitor> monitors;
	protected List<Component> components;
	
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		neoDatisContext.markAsChanged();
		this.name = name;
	}
	public int getType() {
		return type;
	}
	public void setType(int type) {
		neoDatisContext.markAsChanged();
		this.type = type;
	}
	public Computer(String name, int type) {
		super();
		this.name = name;
		this.type = type;
		components = new ArrayList<Component>();
		monitors = new ArrayList<Monitor>();
	}
	public List<Monitor> getMonitors() {
		return monitors;
	}
	public void setMonitors(List<Monitor> monitors) {
		this.monitors = monitors;
	}
	public List<Component> getComponents() {
		return components;
	}
	public void setComponents(List<Component> components) {
		this.components = components;
	}
	public void addComponent(Component c){
		components.add(c);
	}
	public void addMonitor(Monitor m){
		monitors.add(m);
	}
}
