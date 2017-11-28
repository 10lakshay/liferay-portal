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

package com.liferay.reading.time.taglib.servlet.taglib;

import com.liferay.portal.kernel.model.GroupedModel;
import com.liferay.portal.kernel.service.ClassNameLocalServiceUtil;
import com.liferay.portal.kernel.util.JavaConstants;
import com.liferay.portal.kernel.util.PortalUtil;
import com.liferay.portal.kernel.util.StringBundler;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.reading.time.message.ReadingTimeMessageProvider;
import com.liferay.reading.time.model.ReadingTimeEntry;
import com.liferay.reading.time.service.ReadingTimeEntryLocalServiceUtil;
import com.liferay.taglib.util.AttributesTagSupport;

import java.io.IOException;

import javax.portlet.RenderResponse;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;

import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.osgi.framework.FrameworkUtil;
import org.osgi.framework.ServiceReference;

/**
 * @author Alejandro Tardín
 */
public class ReadingTimeTag extends AttributesTagSupport {

	@Override
	public int doEndTag() throws JspException {
		try {
			JspWriter out = pageContext.getOut();

			if (_model != null) {
				long classNameId = ClassNameLocalServiceUtil.getClassNameId(
					_model.getModelClass());

				ReadingTimeEntry readingTimeEntry =
					ReadingTimeEntryLocalServiceUtil.fetchOrAddReadingTimeEntry(
						_model);

				if (readingTimeEntry != null) {
					String readingTimeMessage = _getReadingTimeMessage(
						readingTimeEntry);

					if (Validator.isNotNull(readingTimeMessage)) {
						StringBundler sb = new StringBundler(10);

						sb.append("<time class=\"reading-time\" datetime=\"");
						sb.append(
							String.valueOf(
								readingTimeEntry.getReadingTime() / 1000));
						sb.append("\"");

						if (Validator.isNotNull(_id)) {
							sb.append(" id=\"");
							sb.append(_getNamespace());
							sb.append(_id);
							sb.append("\"");
						}

						sb.append(">");
						sb.append(readingTimeMessage);
						sb.append("</time>");

						out.print(sb);
					}
				}
			}

			return EVAL_PAGE;
		}
		catch (IOException ioe) {
			throw new JspException(ioe);
		}
	}

	public void setId(String id) {
		_id = id;
	}

	public void setModel(GroupedModel model) {
		_model = model;
	}

	private String _getNamespace() {
		RenderResponse renderResponse = (RenderResponse)request.getAttribute(
			JavaConstants.JAVAX_PORTLET_RESPONSE);

		return renderResponse.getNamespace();
	}

	private String _getReadingTimeMessage(ReadingTimeEntry readingTimeEntry)
		throws IOException {

		Bundle bundle = FrameworkUtil.getBundle(getClass());

		BundleContext bundleContext = bundle.getBundleContext();

		ServiceReference<ReadingTimeMessageProvider> serviceReference =
			bundleContext.getServiceReference(ReadingTimeMessageProvider.class);

		if (serviceReference != null) {
			ReadingTimeMessageProvider readingTimeMessageProvider =
				bundleContext.getService(serviceReference);

			return readingTimeMessageProvider.provide(
				readingTimeEntry, PortalUtil.getLocale(request));
		}

		return null;
	}

	private String _id;
	private GroupedModel _model;

}