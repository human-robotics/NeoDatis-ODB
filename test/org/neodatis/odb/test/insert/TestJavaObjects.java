/*
 NeoDatis ODB : Native Object Database (odb.info@neodatis.org)
 Copyright (C) 2007 NeoDatis Inc. http://www.neodatis.org

 "This file is part of the NeoDatis ODB open source object database".

 NeoDatis ODB is free software; you can redistribute it and/or
 modify it under the terms of the GNU Lesser General Public
 License as published by the Free Software Foundation; either
 version 2.1 of the License, or (at your option) any later version.

 NeoDatis ODB is distributed in the hope that it will be useful,
 but WITHOUT ANY WARRANTY; without even the implied warranty of
 MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 Lesser General Public License for more details.

 You should have received a copy of the GNU Lesser General Public
 License along with this library; if not, write to the Free Software
 Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301  USA
 */
package org.neodatis.odb.test.insert;

import org.neodatis.odb.NeoDatis;
import org.neodatis.odb.NeoDatisConfig;
import org.neodatis.odb.ODB;
import org.neodatis.odb.Objects;
import org.neodatis.odb.test.ODBTest;
import org.neodatis.odb.test.vo.login.Function;
import org.neodatis.odb.test.vo.login.Profile;

import javax.swing.*;
import java.net.URL;

public class TestJavaObjects extends ODBTest {

	public void testStrignBuffer() throws Exception {

		StringBuffer buffer = new StringBuffer("Olá chico");

		ODB odb = open(getBaseName());
		odb.store(buffer);
		odb.close();

		odb = open(getBaseName());
		Objects l = odb.query(StringBuffer.class).objects();
		StringBuffer b2 = (StringBuffer) l.first();
		odb.close();
		assertEquals(buffer.toString(), b2.toString());

	}

	public void testObjectWithList() throws Exception {


		Profile profile = new Profile("profile", new Function());
		profile.getFunctions().clear();
		
		ODB odb = open(getBaseName());
		odb.store(profile);
		odb.close();

		odb = open(getBaseName());
		Objects<Profile> l = odb.query(Profile.class).objects();
		Profile p = l.first();
		odb.close();
		assertEquals(profile.getFunctions().size(),p.getFunctions().size());

	}

	public void testJFrame() throws Exception {

		JFrame frame = new JFrame("Ol\u00E1 chico");
		NeoDatisConfig config = NeoDatis.getConfig();
		ODB odb = open(getBaseName(),config);
		odb.store(frame);
		odb.close();

		odb = open(getBaseName(),config);
		Objects<JFrame> l = odb.query(JFrame.class).objects();
		JFrame frame2 = l.first();
		odb.close();
		assertEquals(frame.getTitle(), frame2.getTitle());

	}

	/**
	 * This junit does not work because of a problem? in URL: The hashcode of
	 * the 2 urls url1 & url2 are equal! even if theu point to different domains
	 * (having the same IP)
	 * 
	 * @throws Exception
	 */
	public void testURL() throws Exception {

		URL url1 = new URL("http://wiki.neodatis.org");
		URL url2 = new URL("http://www.neodatis.org");

		Object o1 = url1.getContent();
		Object o2 = url2.getContent();

		int h1 = url1.hashCode();
		int h2 = url2.hashCode();
		println(h1 + " - " + h2);

		println(url1.getHost() + " - " + url1.getDefaultPort() + " - " + url1.getFile() + " - " + url1.getRef());
		println(url2.getHost() + " - " + url2.getDefaultPort() + " - " + url2.getFile() + " - " + url2.getRef());

		println(url1.getHost().hashCode() + " - " + url1.getDefaultPort() + " - " + url1.getFile().hashCode() + " - " + url1.getRef());
		println(url2.getHost().hashCode() + " - " + url2.getDefaultPort() + " - " + url2.getFile().hashCode() + " - " + url2.getRef());

		ODB odb = open(getBaseName());
		odb.store(url1);
		odb.store(url2);
		odb.close();

		odb = open(getBaseName());
		Objects l = odb.getObjects(URL.class);
		odb.close();

		if (testNewFeature) {
			assertEquals("Same HashCode Problem", 2, l.size());
		}

	}

}
