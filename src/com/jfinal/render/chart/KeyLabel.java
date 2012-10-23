package com.jfinal.render.chart;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;


@SuppressWarnings("serial")
public class KeyLabel implements Comparable, Serializable {

	/**
	 * Comparator that can be used for a case insensitive sort of
	 * <code>KeyLabel</code> objects.
	 */
	public static final Comparator CASE_INSENSITIVE_ORDER = new Comparator() {
		public int compare(Object o1, Object o2) {
			String label1 = ((KeyLabel) o1).getLabel();
			String label2 = ((KeyLabel) o2).getLabel();
			return label1.compareToIgnoreCase(label2);
			
		}
	};
	
	public static Map converListToMap(List<KeyLabel> list) {
		Map<String, String> map = new LinkedHashMap<String, String>();

		for (KeyLabel k : list) {
			map.put(k.getKey(), k.getLabel());
		}

		return map;
	}
	public static List<KeyLabel> converMapToList(Map map) {
		List<KeyLabel> list = new ArrayList<KeyLabel>();
		Set keys = map.keySet();
		for (Object key : keys) {
			KeyLabel keyLabel = new KeyLabel();
			keyLabel.setKey(key.toString());
			keyLabel.setLabel(map.get(key)+"");
			list.add(keyLabel);
		}
		return list;
	}
	public KeyLabel() {
		super();
	}

	/**
	 * Construct an instance with the supplied property values.
	 * 
	 * @param label The label to be displayed to the user.
	 * @param key The key to be returned to the server.
	 */
	public KeyLabel(String key, String label) {
		this.label = label;
		this.key = key;
	}

	// ------------------------------------------------------------- Properties
	/**
	 * The property which supplies the option label visible to the end user.
	 */
	private String label = null;

	public String getLabel() {
		return this.label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	/**
	 * The property which supplies the key returned to the server.
	 */
	private String key = null;

	public String getKey() {
		return this.key;
	}

	public void setKey(String value) {
		this.key = value;
	}

	// --------------------------------------------------------- Public Methods
	/**
	 * Compare KeyLabelBeans based on the label, because that's the human
	 * viewable part of the object.
	 * 
	 * @see Comparable
	 */
	public int compareTo(Object o) {
		// Implicitly tests for the correct type, throwing
		// ClassCastException as required by interface
		String otherLabel = ((KeyLabel) o).getLabel();

		return this.getLabel().compareTo(otherLabel);
	}

	/**
	 * Return a string representation of this object.
	 */
	public String toString() {
		StringBuffer sb = new StringBuffer("KeyLabel[");
		sb.append(this.label);
		sb.append(", ");
		sb.append(this.key);
		sb.append("]");
		return (sb.toString());
	}

	/**
	 * KeyLabelBeans are equal if their values are both null or equal.
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	public boolean equals(Object obj) {
		if (obj == this) {
			return true;
		}

		if (!(obj instanceof KeyLabel)) {
			return false;
		}

		KeyLabel bean = (KeyLabel) obj;
		int nil = (this.getKey() == null) ? 1 : 0;
		nil += (bean.getKey() == null) ? 1 : 0;

		if (nil == 2) {
			return true;
		} else if (nil == 1) {
			return false;
		} else {
			return this.getKey().equals(bean.getKey());
		}
	}

	/**
	 * The hash code is based on the object's key.
	 * 
	 * @see java.lang.Object#hashCode()
	 */
	public int hashCode() {
		return (this.getKey() == null) ? 17 : this.getKey().hashCode();
	}
}