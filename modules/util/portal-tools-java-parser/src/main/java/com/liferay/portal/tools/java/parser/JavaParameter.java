/**
 * Copyright (c) 2000-present Liferay, Inc. All rights reserved.
 *
 * This library is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 2.1 of the License, or (at your option)
 * any later version.
 *
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 */

package com.liferay.portal.tools.java.parser;

import com.liferay.portal.kernel.util.ListUtil;
import com.liferay.portal.kernel.util.StringBundler;
import com.liferay.portal.kernel.util.StringUtil;

import java.util.List;

/**
 * @author Hugo Huijser
 */
public class JavaParameter extends BaseJavaTerm {

	public JavaParameter(String name) {
		_name = new JavaSimpleValue(name);
	}

	public void setJavaAnnotations(List<JavaAnnotation> javaAnnotations) {
		_javaAnnotations = javaAnnotations;
	}

	public void setJavaType(JavaType javaType) {
		_javaType = javaType;
	}

	public void setModifiers(List<JavaSimpleValue> modifiers) {
		_modifiers = modifiers;
	}

	@Override
	public String toString(
		String indent, String prefix, String suffix, int maxLineLength) {

		StringBundler sb = new StringBundler();

		sb.append(indent);
		sb.append(StringUtil.trimLeading(prefix));

		if (ListUtil.isNotEmpty(_javaAnnotations)) {
			appendSingleLine(sb, _javaAnnotations, " ", maxLineLength);
		}

		if (ListUtil.isNotEmpty(_modifiers)) {
			appendSingleLine(sb, _modifiers, " ", " ", "", maxLineLength);
		}

		if (append(sb, _javaType, indent, " ", "", maxLineLength, false) ==
				APPENDED_NEW_LINE) {

			indent += "\t";
		}

		append(sb, _name, indent, " ", suffix, maxLineLength);

		return sb.toString();
	}

	private List<JavaAnnotation> _javaAnnotations;
	private JavaType _javaType;
	private List<JavaSimpleValue> _modifiers;
	private final JavaSimpleValue _name;

}