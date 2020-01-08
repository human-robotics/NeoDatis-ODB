
package org.neodatis.tool.wrappers;

import org.neodatis.odb.NeoDatisRuntimeException;
import org.neodatis.odb.core.NeoDatisError;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;



/**To Wrap SimpleDatFormat
 * @sharpen.ignore
 * @author olivier
 *
 */
public class OdbDateFormat {
	private SimpleDateFormat sdf;
	private String pattern;
	
	public OdbDateFormat(String pattern){
		this.pattern = pattern;
		this.sdf = new SimpleDateFormat(pattern);
	}
	
	public String format(Date date){
		return sdf.format(date);
	}
	public Date parse(String text) {
		try {
			return sdf.parse(text);
		} catch (ParseException e) {
			throw new NeoDatisRuntimeException(NeoDatisError.FORMT_INVALID_DATE_FORMAT.addParameter(text).addParameter(pattern));
		}
	}
}
