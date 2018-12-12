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

package com.liferay.document.library.web.internal.portlet.action;

import com.liferay.asset.kernel.service.AssetTagLocalService;
import com.liferay.bulk.selection.Selection;
import com.liferay.bulk.selection.SelectionFactory;
import com.liferay.document.library.constants.DLPortletKeys;
import com.liferay.document.library.kernel.exception.NoSuchFileEntryException;
import com.liferay.document.library.kernel.model.DLFileEntryConstants;
import com.liferay.document.library.web.internal.constants.DLWebKeys;
import com.liferay.petra.string.StringPool;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.portlet.bridges.mvc.MVCRenderCommand;
import com.liferay.portal.kernel.repository.model.FileEntry;
import com.liferay.portal.kernel.security.auth.PrincipalException;
import com.liferay.portal.kernel.security.permission.ActionKeys;
import com.liferay.portal.kernel.security.permission.PermissionChecker;
import com.liferay.portal.kernel.security.permission.resource.ModelResourcePermission;
import com.liferay.portal.kernel.servlet.SessionErrors;
import com.liferay.portal.kernel.theme.ThemeDisplay;
import com.liferay.portal.kernel.util.SetUtil;
import com.liferay.portal.kernel.util.WebKeys;

import java.util.Collections;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.portlet.PortletException;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Alejandro Tardín
 */
@Component(
	property = {
		"javax.portlet.name=" + DLPortletKeys.DOCUMENT_LIBRARY,
		"javax.portlet.name=" + DLPortletKeys.DOCUMENT_LIBRARY_ADMIN,
		"javax.portlet.name=" + DLPortletKeys.MEDIA_GALLERY_DISPLAY,
		"mvc.command.name=/document_library/edit_tags"
	},
	service = MVCRenderCommand.class
)
public class EditTagsMVCRenderCommand implements MVCRenderCommand {

	@Override
	public String render(
			RenderRequest renderRequest, RenderResponse renderResponse)
		throws PortletException {

		try {
			Selection<FileEntry> selection = _selectionFactory.create(
				renderRequest.getParameterMap());

			ThemeDisplay themeDisplay =
				(ThemeDisplay)renderRequest.getAttribute(WebKeys.THEME_DISPLAY);

			PermissionChecker permissionChecker =
				themeDisplay.getPermissionChecker();

			renderRequest.setAttribute(
				DLWebKeys.DOCUMENT_LIBRARY_SELECTION, selection);

			Stream<FileEntry> fileEntryStream = selection.stream();

			Set<String> commonTags = fileEntryStream.map(
				_getFileEntryTagsSet(permissionChecker)
			).reduce(
				SetUtil::intersect
			).orElse(
				Collections.emptySet()
			);

			Stream<String> commonTagsStream = commonTags.stream();

			String commonTagNames = commonTagsStream.collect(
				Collectors.joining(StringPool.COMMA));

			renderRequest.setAttribute(
				DLWebKeys.DOCUMENT_LIBRARY_COMMON_TAG_NAMES, commonTagNames);

			return "/document_library/edit_tags.jsp";
		}
		catch (NoSuchFileEntryException | PrincipalException e) {
			SessionErrors.add(renderRequest, e.getClass());

			return "/document_library/error.jsp";
		}
		catch (Exception e) {
			throw new PortletException(e);
		}
	}

	private Function<FileEntry, Set<String>> _getFileEntryTagsSet(
		PermissionChecker permissionChecker) {

		return fileEntry -> {
			try {
				if (_fileEntryModelResourcePermission.contains(
						permissionChecker, fileEntry, ActionKeys.UPDATE)) {

					return SetUtil.fromArray(
						_assetTagLocalService.getTagNames(
							DLFileEntryConstants.getClassName(),
							fileEntry.getFileEntryId()));
				}

				return Collections.emptySet();
			}
			catch (PortalException pe) {
				if (_log.isWarnEnabled()) {
					_log.warn(pe, pe);
				}

				return Collections.emptySet();
			}
		};
	}

	private static final Log _log = LogFactoryUtil.getLog(
		EditTagsMVCRenderCommand.class);

	@Reference
	private AssetTagLocalService _assetTagLocalService;

	@Reference(
		target = "(model.class.name=com.liferay.portal.kernel.repository.model.FileEntry)"
	)
	private ModelResourcePermission<FileEntry>
		_fileEntryModelResourcePermission;

	@Reference(
		target = "(model.class.name=com.liferay.portal.kernel.repository.model.FileEntry)"
	)
	private SelectionFactory<FileEntry> _selectionFactory;

}