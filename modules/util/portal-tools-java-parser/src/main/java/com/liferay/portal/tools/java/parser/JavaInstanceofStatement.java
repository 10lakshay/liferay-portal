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

import com.liferay.portal.kernel.util.StringBundler;

/**
 * @author Hugo Huijser
 */
public class JavaInstanceofStatement extends JavaExpression {

	public JavaInstanceofStatement(JavaType classJavaType) {
		_classJavaType = classJavaType;
	}

	@Override
	public String getString(
		String indent, String prefix, String suffix, int maxLineLength,
		boolean forceLineBreak) {

		StringBundler sb = new StringBundler();

		sb.append(indent);
		sb.append(prefix);

		int addedLinesCount = append(
			sb, _valueJavaExpression, indent, "", " instanceof", maxLineLength);

		for (int i = 0; i < addedLinesCount; i++) {
			indent += "\t";
		}

		append(sb, _classJavaType, indent, " ", suffix, maxLineLength);

		return sb.toString();
	}

	public void setValue(JavaExpression valueJavaExpression) {
		_valueJavaExpression = valueJavaExpression;
	}

	private final JavaType _classJavaType;
	private JavaExpression _valueJavaExpression;

}