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

package com.liferay.user.associated.data.web.internal.portlet.action;

import com.liferay.portal.kernel.portlet.bridges.mvc.BaseMVCResourceCommand;
import com.liferay.portal.kernel.portlet.bridges.mvc.MVCResourceCommand;
import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.user.associated.data.aggregator.UADAggregator;
import com.liferay.user.associated.data.constants.UserAssociatedDataPortletKeys;
import com.liferay.user.associated.data.display.UADEntityDisplay;
import com.liferay.user.associated.data.web.internal.registry.UADRegistry;

import java.util.ArrayList;
import java.util.List;

import javax.portlet.ResourceRequest;
import javax.portlet.ResourceResponse;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Drew Brokke
 */
@Component(
	immediate = true,
	property = {
		"javax.portlet.name=" + UserAssociatedDataPortletKeys.USER_ASSOCIATED_DATA,
		"mvc.command.name=/info_panel"
	},
	service = MVCResourceCommand.class
)
public class InfoPanelMVCResourceCommand extends BaseMVCResourceCommand {

	@Override
	protected void doServeResource(
			ResourceRequest resourceRequest, ResourceResponse resourceResponse)
		throws Exception {

		String[] rowIds = ParamUtil.getStringValues(resourceRequest, "rowIds");

		List<Object> rowObjects = new ArrayList<>();

		String uadRegistryKey = ParamUtil.getString(
			resourceRequest, "uadRegistryKey");

		UADAggregator uadAggregator = _uadRegistry.getUADAggregator(
			uadRegistryKey);

		for (String rowId : rowIds) {
			rowObjects.add(uadAggregator.get(rowId));
		}

		resourceRequest.setAttribute("ROW_OBJECTS", rowObjects);

		UADEntityDisplay uadEntityDisplay = _uadRegistry.getUADEntityDisplay(
			uadRegistryKey);

		resourceRequest.setAttribute("UAD_ENTITY_DISPLAY", uadEntityDisplay);

		include(resourceRequest, resourceResponse, "/info_panel.jsp");
	}

	@Reference
	private UADRegistry _uadRegistry;

}