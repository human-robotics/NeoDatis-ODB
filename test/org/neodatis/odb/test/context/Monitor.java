/**
 * 
 */
package org.neodatis.odb.test.context;

import org.neodatis.odb.core.context.NeoDatisObjectAdapter;

/**
 * @author olivier
 *
 */
public class Monitor extends NeoDatisObjectAdapter {
	protected String resolution;
	protected String model;
	public Monitor(String resolution, String model) {
		super();
		this.resolution = resolution;
		this.model = model;
	}
	public String getResolution() {
		return resolution;
	}
	public void setResolution(String resolution) {
		this.resolution = resolution;
	}
	public String getModel() {
		return model;
	}
	public void setModel(String model) {
		this.model = model;
	}
}
