/*******************************************************************************
 * Copyright (c) 2000, 2003 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.graphics;


import org.eclipse.swt.*;

/**
 * Instances of this class describe operating system fonts.
 * Only the public API of this type is platform independent.
 * <p>
 * For platform-independent behaviour, use the get and set methods
 * corresponding to the following properties:
 * <dl>
 * <dt>height</dt><dd>the height of the font in points</dd>
 * <dt>name</dt><dd>the face name of the font, which may include the foundry</dd>
 * <dt>style</dt><dd>A bitwise combination of NORMAL, ITALIC and BOLD</dd>
 * </dl>
 * If extra, platform-dependent functionality is required:
 * <ul>
 * <li>On <em>Windows</em>, the data member of the <code>FontData</code>
 * corresponds to a Windows <code>LOGFONT</code> structure whose fields
 * may be retrieved and modified.</li>
 * <li>On <em>X</em>, the fields of the <code>FontData</code> correspond
 * to the entries in the font's XLFD name and may be retrieved and modified.
 * </ul>
 * Application code does <em>not</em> need to explicitly release the
 * resources managed by each instance when those instances are no longer
 * required, and thus no <code>dispose()</code> method is provided.
 *
 * @see Font
 */
public final class FontData {
	/**
	 * the font name
	 * (Warning: This field is platform dependent)
	 */
	public String name;

	/**
	 * The height of the font data in points
	 * (Warning: This field is platform dependent)
	 */
	public int height;

	/**
	 * the font style
	 * (Warning: This field is platform dependent)
	 */
	public int style;

	/**
	 * The locales of the font
	 * (Warning: These fields are platform dependent)
	 */
	String lang, country, variant;
	
/**	 
 * Constructs a new un-initialized font data.
 */
public FontData () {
	this("", 12, SWT.NORMAL);
}

/**
 * Constructs a new FontData given a string representation
 * in the form generated by the <code>FontData.toString</code>
 * method.
 * <p>
 * Note that the representation varies between platforms,
 * and a FontData can only be created from a string that was 
 * generated on the same platform.
 * </p>
 *
 * @param string the string representation of a <code>FontData</code> (must not be null)
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT - if the argument is null</li>
 *    <li>ERROR_INVALID_ARGUMENT - if the argument does not represent a valid description</li>
 * </ul>
 *
 * @see #toString
 */
public FontData(String string) {
	if (string == null) SWT.error(SWT.ERROR_NULL_ARGUMENT);
	int start = 0;
	int end = string.indexOf('|');
	if (end == -1) SWT.error(SWT.ERROR_INVALID_ARGUMENT);
	String version1 = string.substring(start, end);
	
	start = end + 1;
	end = string.indexOf('|', start);
	if (end == -1) SWT.error(SWT.ERROR_INVALID_ARGUMENT);
	String name = string.substring(start, end);
	
	start = end + 1;
	end = string.indexOf('|', start);
	if (end == -1) SWT.error(SWT.ERROR_INVALID_ARGUMENT);
	int height = 0;
	try {
		height = Integer.parseInt(string.substring(start, end));
	} catch (NumberFormatException e) {
		SWT.error(SWT.ERROR_INVALID_ARGUMENT);
	}
	
	start = end + 1;
	end = string.indexOf('|', start);
	if (end == -1) SWT.error(SWT.ERROR_INVALID_ARGUMENT);
	int style = 0;
	try {
		style = Integer.parseInt(string.substring(start, end));
	} catch (NumberFormatException e) {
		SWT.error(SWT.ERROR_INVALID_ARGUMENT);
	}

	start = end + 1;
	end = string.indexOf('|', start);
	setName(name);
	setHeight(height);
	setStyle(style);
	if (end == -1) return;
	String platform = string.substring(start, end);

	start = end + 1;
	end = string.indexOf('|', start);
	if (end == -1) return;
	String version2 = string.substring(start, end);

	if (platform.equals("CARBON") && version2.equals("1")) {
		return;
	}
}

/**	 
 * Constructs a new font data given a font name,
 * the height of the desired font in points, 
 * and a font style.
 *
 * @param name the name of the font (must not be null)
 * @param height the font height in points
 * @param style a bit or combination of NORMAL, BOLD, ITALIC
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT - when the font name is null</li>
 *    <li>ERROR_INVALID_ARGUMENT - if the height is negative</li>
 * </ul>
 */
public FontData(String name, int height, int style) {
	setName(name);
	setHeight(height);
	setStyle(style);
}

/**
 * Compares the argument to the receiver, and returns true
 * if they represent the <em>same</em> object using a class
 * specific comparison.
 *
 * @param object the object to compare with this object
 * @return <code>true</code> if the object is the same as this object and <code>false</code> otherwise
 *
 * @see #hashCode
 */
public boolean equals (Object object) {
	if (object == this) return true;
	if (!(object instanceof FontData)) return false;
	FontData data = (FontData)object;
	return name.equals(data.name) && height == data.height && style == data.style;
}

/**
 * Returns the height of the receiver in points.
 *
 * @return the height of this FontData
 *
 * @see #setHeight
 */
public int getHeight() {
	return height;
}

/**
 * Returns the locale of the receiver.
 * <p>
 * The locale determines which platform character set this
 * font is going to use. Widgets and graphics operations that
 * use this font will convert UNICODE strings to the platform
 * character set of the specified locale.
 * </p>
 * <p>
 * On platforms where there are multiple character sets for a
 * given language/country locale, the variant portion of the
 * locale will determine the character set.
 * </p>
 * 
 * @return the <code>String</code> representing a Locale object
 * @since 3.0
 */
public String getLocale () {
	StringBuffer buffer = new StringBuffer ();
	char sep = '_';
	if (lang != null) {
		buffer.append (lang);
		buffer.append (sep);
	}
	if (country != null) {
		buffer.append (country);
		buffer.append (sep);
	}
	if (variant != null) {
		buffer.append (variant);
	}
	
	String result = buffer.toString ();
	int length = result.length ();
	if (length > 0) {
		if (result.charAt (length - 1) == sep) {
			result = result.substring (0, length - 1);
		}
	} 
	return result;
}

/**
 * Returns the name of the receiver.
 * On platforms that support font foundries, the return value will
 * be the foundry followed by a dash ("-") followed by the face name.
 *
 * @return the name of this <code>FontData</code>
 *
 * @see #setName
 */
public String getName() {
	return name;
}

/**
 * Returns the style of the receiver which is a bitwise OR of 
 * one or more of the <code>SWT</code> constants NORMAL, BOLD
 * and ITALIC.
 *
 * @return the style of this <code>FontData</code>
 * 
 * @see #setStyle
 */
public int getStyle() {
	return style;
}

/**
 * Returns an integer hash code for the receiver. Any two 
 * objects which return <code>true</code> when passed to 
 * <code>equals</code> must return the same value for this
 * method.
 *
 * @return the receiver's hash
 *
 * @see #equals
 */
public int hashCode () {
	return name.hashCode() ^ height ^ style;
}

/**
 * Sets the height of the receiver. The parameter is
 * specified in terms of points, where a point is one
 * seventy-second of an inch.
 *
 * @param height the height of the <code>FontData</code>
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_INVALID_ARGUMENT - if the height is negative</li>
 * </ul>
 * 
 * @see #getHeight
 */
public void setHeight(int height) {
	if (height < 0) SWT.error(SWT.ERROR_INVALID_ARGUMENT);
	this.height = height;
}

/**
 * Sets the locale of the receiver.
 * <p>
 * The locale determines which platform character set this
 * font is going to use. Widgets and graphics operations that
 * use this font will convert UNICODE strings to the platform
 * character set of the specified locale.
 * </p>
 * <p>
 * On platforms where there are multiple character sets for a
 * given language/country locale, the variant portion of the
 * locale will determine the character set.
 * </p>
 * 
 * @param locale the <code>String</code> representing a Locale object
 * @see java.util.Locale#toString
 */
public void setLocale(String locale) {
	lang = country = variant = null;
	if (locale != null) {
		char sep = '_';
		int length = locale.length();
		int firstSep, secondSep;
		
		firstSep = locale.indexOf(sep);
		if (firstSep == -1) {
			firstSep = secondSep = length;
		} else {
			secondSep = locale.indexOf(sep, firstSep + 1);
			if (secondSep == -1) secondSep = length;
		}
		if (firstSep > 0) lang = locale.substring(0, firstSep);
		if (secondSep > firstSep + 1) country = locale.substring(firstSep + 1, secondSep);
		if (length > secondSep + 1) variant = locale.substring(secondSep + 1);
	}	
}

/**
 * Sets the name of the receiver.
 * <p>
 * Some platforms support font foundries. On these platforms, the name
 * of the font specified in setName() may have one of the following forms:
 * <ol>
 * <li>a face name (for example, "courier")</li>
 * <li>a foundry followed by a dash ("-") followed by a face name (for example, "adobe-courier")</li>
 * </ol>
 * In either case, the name returned from getName() will include the
 * foundry.
 * </p>
 * <p>
 * On platforms that do not support font foundries, only the face name
 * (for example, "courier") is used in <code>setName()</code> and 
 * <code>getName()</code>.
 * </p>
 *
 * @param name the name of the font data (must not be null)
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT - when the font name is null</li>
 * </ul>
 *
 * @see #getName
 */
public void setName(String name) {
	if (name == null) SWT.error(SWT.ERROR_NULL_ARGUMENT);
	this.name = name;
}

/**
 * Sets the style of the receiver to the argument which must
 * be a bitwise OR of one or more of the <code>SWT</code> 
 * constants NORMAL, BOLD and ITALIC.
 *
 * @param style the new style for this <code>FontData</code>
 *
 * @see #getStyle
 */
public void setStyle(int style) {
	this.style = style;
}

/**
 * Returns a string representation of the receiver which is suitable
 * for constructing an equivalent instance using the 
 * <code>FontData(String)</code> constructor.
 *
 * @return a string representation of the FontData
 *
 * @see FontData
 */
public String toString() {
	StringBuffer buffer = new StringBuffer();
	buffer.append("1|");
	buffer.append(getName());
	buffer.append("|");
	buffer.append(getHeight());
	buffer.append("|");
	buffer.append(getStyle());
	buffer.append("|");
	buffer.append("CARBON|1|");	
	return buffer.toString();
}

}
