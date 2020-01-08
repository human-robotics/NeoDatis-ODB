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
package org.neodatis.odb.gui.objectbrowser.update;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;

import org.neodatis.odb.OID;
import org.neodatis.odb.Objects;
import org.neodatis.odb.core.layers.layer2.meta.AbstractObjectInfo;
import org.neodatis.odb.core.layers.layer2.meta.ArrayObjectInfo;
import org.neodatis.odb.core.layers.layer2.meta.ClassAttributeInfo;
import org.neodatis.odb.core.layers.layer2.meta.ClassInfo;
import org.neodatis.odb.core.layers.layer2.meta.CollectionObjectInfo;
import org.neodatis.odb.core.layers.layer2.meta.NonNativeNullObjectInfo;
import org.neodatis.odb.core.layers.layer2.meta.NonNativeObjectInfo;
import org.neodatis.odb.core.layers.layer2.meta.ObjectReference;
import org.neodatis.odb.core.layers.layer4.OidGenerator;
import org.neodatis.odb.core.session.SessionEngine;
import org.neodatis.odb.gui.IBrowserContainer;
import org.neodatis.odb.gui.Messages;
import org.neodatis.odb.gui.component.GUITool;
import org.neodatis.odb.gui.objectbrowser.hierarchy.HierarchicObjectBrowserPanel;
import org.neodatis.odb.gui.objectbrowser.hierarchy.ModalObjectBrowserDialog;
import org.neodatis.odb.tool.ObjectTool;
import org.neodatis.tool.DLogger;
import org.neodatis.tool.ILogger;
import org.neodatis.tool.wrappers.OdbString;
import org.neodatis.tool.wrappers.map.OdbHashMap;

public class NewObjectPanel extends JPanel implements ActionListener {

	private SessionEngine storageEngine;

	private ClassInfo classInfo;

	private JButton btCreate;

	private JButton btCancel;

	private Map<ClassAttributeInfo, JTextField> textFields;

	private Map<JButton, JTextField> idsTextFieldsForButton;

	private Map<JButton, JComboBox> classNames;

	private ILogger logger;

	private IBrowserContainer browser;

	public NewObjectPanel(SessionEngine aStorageEngine, ClassInfo ci, IBrowserContainer aBrowser, ILogger logger) {
		this.storageEngine = aStorageEngine;
		this.classInfo = ci;
		textFields = new OdbHashMap<ClassAttributeInfo, JTextField>();
		idsTextFieldsForButton = new OdbHashMap<JButton, JTextField>();
		classNames = new OdbHashMap<JButton, JComboBox>();
		this.browser = aBrowser;
		this.logger = logger;
		init();
	}

	private void init() {
		JTextField textField = null;
		ClassAttributeInfo cai = null;
		int nbAttributes = classInfo.getAttributes().size();

		JPanel panelLabels = new JPanel(new GridLayout(nbAttributes + 1, 1, 5, 5));
		JPanel panelFields = new JPanel(new GridLayout(nbAttributes + 1, 1, 5, 5));
		JPanel panel1 = null;
		JPanel panel2 = null;
		// Creates a panel for fields
		// JPanel fieldsPanel = new JPanel(new
		// GridLayout(nbAttributes+2,2,4,4));

		setLayout(new BorderLayout(4, 4));
		Color headerFontColor = Color.LIGHT_GRAY;
		Color headerCellColor = new Color(0, 100, 15);

		JLabel label1 = new JLabel(Messages.getString("Attribute Name"));
		label1.setBackground(headerCellColor);
		panel1 = new JPanel();
		panel1.add(label1);
		panelLabels.add(panel1);

		JLabel label3 = new JLabel(Messages.getString("Value"));
		label3.setBackground(headerCellColor);

		panel2 = new JPanel();
		panel2.add(label3);
		panelFields.add(panel2);

		JButton btChoose = null;
		Dimension labelDimension = new Dimension(80, 20);
		JLabel label = null;
		for (int i = 0; i < nbAttributes; i++) {
			cai = classInfo.getAttributeInfo(i);

			if (cai.isNative()) {
				if (cai.getAttributeType().isAtomicNative()) {
					textField = new JTextField(20);
					panel1 = new JPanel(new FlowLayout(FlowLayout.LEFT));
					label = new JLabel(cai.getName(), JLabel.LEFT);
					label.setPreferredSize(labelDimension);
					panel1.add(label);
					panel1.add(textField);

					if (cai.getAttributeType().isDate()) {
						panel1.add(new JLabel("(dd/MM/yyyy HH:mm:ss:SSS)"));
					}

					panelLabels.add(panel1);
				} else if (cai.getAttributeType().isArrayOrCollection()) {
					textField = new JTextField(8);
					panel1 = new JPanel(new FlowLayout(FlowLayout.LEFT));

					label = new JLabel(cai.getName(), JLabel.LEFT);
					label.setPreferredSize(labelDimension);

					panel1.add(label);
					panel1.add(textField);
					JComboBox combo = buildClassesCombo();
					panel1.add(combo);
					btChoose = new JButton(Messages.getString("Add an object"));
					panel1.add(btChoose);
					btChoose.setActionCommand("browse-add." + cai.getClassName());
					btChoose.addActionListener(this);
					panelLabels.add(panel1);
					idsTextFieldsForButton.put(btChoose, textField);
					classNames.put(btChoose, combo);
				}

			} else {
				textField = new JTextField(4);
				panel1 = new JPanel(new FlowLayout(FlowLayout.LEFT));

				label = new JLabel(cai.getName(), JLabel.LEFT);
				label.setPreferredSize(labelDimension);

				panel1.add(label);
				panel1.add(textField);
				btChoose = new JButton(Messages.getString("Choose the object"));
				panel1.add(btChoose);
				btChoose.setActionCommand("browse-set." + cai.getClassName());
				btChoose.addActionListener(this);

				panelLabels.add(panel1);
				idsTextFieldsForButton.put(btChoose, textField);
			}

			textFields.put(cai, textField);
		}

		JPanel optionPanel = new JPanel();
		btCreate = new JButton(Messages.getString("Create&commit"));
		btCancel = new JButton(Messages.getString("Cancel"));
		optionPanel.add(btCancel);
		optionPanel.add(btCreate);
		btCreate.setActionCommand("create");
		btCancel.setActionCommand("cancel");

		btCreate.addActionListener(this);
		btCancel.addActionListener(this);

		JPanel panelContent = new JPanel(new BorderLayout(5, 5));

		JPanel panel4 = new JPanel();
		panel4.add(panelLabels);
		// panel5.add(panelLabels);

		// panelContent.add(panel5,BorderLayout.WEST);
		panelContent.add(panel4, BorderLayout.CENTER);
		JPanel panel3 = new JPanel(new BorderLayout(5, 5));

		panel3.add(new JScrollPane(panelContent), BorderLayout.CENTER);
		panel3.add(optionPanel, BorderLayout.SOUTH);

		add(panel3, BorderLayout.CENTER);
		add(GUITool.buildHeaderPanel(Messages.getString("Create a new object of type ") + classInfo.getFullClassName()), BorderLayout.NORTH);

	}

	private JComboBox buildClassesCombo() {
		Vector vector = new Vector();
		Iterator<ClassInfo> iterator = storageEngine.getSession().getMetaModel().getAllClasses().iterator();
		ClassInfo ci = null;
		while (iterator.hasNext()) {
			ci = (ClassInfo) iterator.next();
			vector.add(ci.getFullClassName());
		}
		return new JComboBox(vector);
	}

	public void actionPerformed(ActionEvent e) {
		String action = e.getActionCommand();
		String tokenSet = "browse-set.";
		String tokenAdd = "browse-add.";
		if (action.startsWith(tokenSet)) {
			String className = OdbString.substring(action, tokenSet.length(), action.length());
			try {
				OID oid = chooseObject(className);
				if (oid != null) {
					JTextField idTextField = (JTextField) idsTextFieldsForButton.get(e.getSource());
					idTextField.setText(String.valueOf(oid));
				}
			} catch (Exception e1) {
				logger.error("Error setting field", e1);
			}
		}
		if (action.startsWith(tokenAdd)) {
			String className = ((JComboBox) classNames.get(e.getSource())).getSelectedItem().toString();
			try {
				OID oid = chooseObject(className);
				if (oid != null) {
					JTextField idTextField = (JTextField) idsTextFieldsForButton.get(e.getSource());
					String ids = idTextField.getText();
					idTextField.setText(ids + (ids.length() > 0 ? "," : "") + String.valueOf(oid));
				}
			} catch (Exception e1) {
				logger.error("Error setting field", e1);
			}
		}
		if ("create".equals(action)) {
			try {
				createObject();
			} catch (Exception e1) {
				logger.error("Error creating object", e1);
				JOptionPane.showMessageDialog(this, "Error while creating object : " + e1.getMessage());
			}
		}

	}

	private void createObject() throws Exception {

		Iterator iterator = textFields.keySet().iterator();
		ClassAttributeInfo cai = null;
		NonNativeObjectInfo nnoi = new NonNativeObjectInfo(classInfo);
		OidGenerator oidGenerator = storageEngine.getSession().getOidGenerator();
		ObjectTool objectTool = new ObjectTool(storageEngine.getSession());

		while (iterator.hasNext()) {
			cai = (ClassAttributeInfo) iterator.next();
			JTextField tfValue = (JTextField) textFields.get(cai);
			String value = tfValue.getText();
			if (cai.isNative()) {
				if (cai.getAttributeType().isAtomicNative()) {
					nnoi.setAttributeValue(cai.getId(), objectTool.stringToObjectInfo(cai.getAttributeType().getId(), value,
							ObjectTool.ID_CALLER_IS_ODB_EXPLORER,null));
				} else {
					if (cai.getAttributeType().isCollection()) {
						// Must be array of collection
						Collection<AbstractObjectInfo> c = new ArrayList<AbstractObjectInfo>();
						Collection<NonNativeObjectInfo> c2 = new ArrayList<NonNativeObjectInfo>();
						StringTokenizer tokenizer = new StringTokenizer(value, ",");
						while (tokenizer.hasMoreElements()) {
							c.add(new ObjectReference(oidGenerator.objectOidFromString(tokenizer.nextElement().toString())));
						}
						CollectionObjectInfo coi = new CollectionObjectInfo(c,c2);
						nnoi.setAttributeValue(cai.getId(), coi);
					}
					if (cai.getAttributeType().isArray()) {
						// Must be array of collection

						StringTokenizer tokenizer = new StringTokenizer(value, ",");
						AbstractObjectInfo[] objects = new AbstractObjectInfo[tokenizer.countTokens()];
						int i = 0;
						while (tokenizer.hasMoreElements()) {
							objects[i++] = new ObjectReference( oidGenerator.objectOidFromString(tokenizer.nextElement().toString()));
						}
						ArrayObjectInfo aoi = new ArrayObjectInfo(objects);
						nnoi.setAttributeValue(cai.getId(), aoi);
					}

				}
			} else {
				if (value != null && value.length() > 0) {
					nnoi.setAttributeValue(cai.getId(), new ObjectReference( oidGenerator.objectOidFromString(value)));
				} else {
					nnoi.setAttributeValue(cai.getId(), new NonNativeNullObjectInfo());
				}
			}
		}
		DLogger.info("Creating object:" + nnoi);

		// OID id =
		// storageEngine.getObjectWriter().writeNonNativeObjectInfo(StorageEngineConstant.NULL_OBJECT_ID,
		// nnoi, -1, true,true);
		OID id = storageEngine.storeMeta(null, nnoi);
		btCancel.setEnabled(false);
		btCreate.setEnabled(false);
		btCreate.setText(Messages.getString("Object Created : id = " + id));

	}

	private OID chooseObject(String className) throws Exception {
		Objects<AbstractObjectInfo> l = storageEngine.getMetaObjects(storageEngine.criteriaQuery(className));
		// TODO: Try to avoid copying the list here. TreeModel needs a list
		// because it uses get(index) and indexOf(Object)
		List<AbstractObjectInfo> list = new ArrayList<AbstractObjectInfo>(l.size());
		list.addAll(l);
		ClassInfo classInfoToBrowse = storageEngine.getSession().getMetaModel().getClassInfo(className, true);
		HierarchicObjectBrowserPanel panel = new HierarchicObjectBrowserPanel(browser, storageEngine, classInfoToBrowse, list, false,
				logger);
		ModalObjectBrowserDialog modalBrowser = new ModalObjectBrowserDialog(panel);
		modalBrowser.pack();
		modalBrowser.setVisible(true);
		if (modalBrowser.objectHasBeenChoosen()) {
			return panel.getSelectedOid();
		}
		return null;
	}

}
