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

package com.liferay.document.library.web.internal.display.context;

import com.liferay.asset.kernel.service.AssetEntryServiceUtil;
import com.liferay.asset.kernel.service.persistence.AssetEntryQuery;
import com.liferay.document.library.constants.DLPortletKeys;
import com.liferay.document.library.display.context.DLAdminDisplayContext;
import com.liferay.document.library.kernel.exception.NoSuchFolderException;
import com.liferay.document.library.kernel.model.DLFileEntry;
import com.liferay.document.library.kernel.model.DLFileEntryConstants;
import com.liferay.document.library.kernel.model.DLFileEntryType;
import com.liferay.document.library.kernel.model.DLFileEntryTypeConstants;
import com.liferay.document.library.kernel.model.DLFileShortcutConstants;
import com.liferay.document.library.kernel.model.DLFolder;
import com.liferay.document.library.kernel.model.DLFolderConstants;
import com.liferay.document.library.kernel.service.DLAppLocalServiceUtil;
import com.liferay.document.library.kernel.service.DLAppServiceUtil;
import com.liferay.document.library.kernel.service.DLFileEntryTypeLocalServiceUtil;
import com.liferay.document.library.kernel.util.DLUtil;
import com.liferay.document.library.kernel.util.comparator.RepositoryModelModifiedDateComparator;
import com.liferay.document.library.web.internal.constants.DLWebKeys;
import com.liferay.document.library.web.internal.display.context.logic.DLPortletInstanceSettingsHelper;
import com.liferay.document.library.web.internal.display.context.util.DLRequestHelper;
import com.liferay.document.library.web.internal.portlet.toolbar.contributor.DLPortletToolbarContributor;
import com.liferay.document.library.web.internal.settings.DLPortletInstanceSettings;
import com.liferay.document.library.web.internal.util.DLTrashUtil;
import com.liferay.dynamic.data.mapping.model.DDMStructure;
import com.liferay.frontend.taglib.clay.servlet.taglib.util.CreationMenu;
import com.liferay.frontend.taglib.clay.servlet.taglib.util.DropdownItem;
import com.liferay.frontend.taglib.clay.servlet.taglib.util.DropdownItemList;
import com.liferay.frontend.taglib.clay.servlet.taglib.util.NavigationItem;
import com.liferay.frontend.taglib.clay.servlet.taglib.util.NavigationItemList;
import com.liferay.frontend.taglib.clay.servlet.taglib.util.SafeConsumer;
import com.liferay.frontend.taglib.clay.servlet.taglib.util.ViewTypeItemList;
import com.liferay.petra.string.StringBundler;
import com.liferay.petra.string.StringPool;
import com.liferay.portal.kernel.bean.BeanPropertiesUtil;
import com.liferay.portal.kernel.dao.search.SearchContainer;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.kernel.language.LanguageUtil;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.model.Group;
import com.liferay.portal.kernel.model.Portlet;
import com.liferay.portal.kernel.model.User;
import com.liferay.portal.kernel.portlet.LiferayPortletRequest;
import com.liferay.portal.kernel.portlet.LiferayPortletResponse;
import com.liferay.portal.kernel.portlet.PortalPreferences;
import com.liferay.portal.kernel.portlet.PortletPreferencesFactoryUtil;
import com.liferay.portal.kernel.portlet.PortletProvider;
import com.liferay.portal.kernel.portlet.PortletProviderUtil;
import com.liferay.portal.kernel.portlet.PortletURLFactoryUtil;
import com.liferay.portal.kernel.portlet.PortletURLUtil;
import com.liferay.portal.kernel.repository.model.FileEntry;
import com.liferay.portal.kernel.repository.model.Folder;
import com.liferay.portal.kernel.search.Document;
import com.liferay.portal.kernel.search.Field;
import com.liferay.portal.kernel.search.Hits;
import com.liferay.portal.kernel.search.Indexer;
import com.liferay.portal.kernel.search.IndexerRegistryUtil;
import com.liferay.portal.kernel.search.QueryConfig;
import com.liferay.portal.kernel.search.SearchContext;
import com.liferay.portal.kernel.search.SearchContextFactory;
import com.liferay.portal.kernel.search.SearchResult;
import com.liferay.portal.kernel.search.SearchResultUtil;
import com.liferay.portal.kernel.search.Sort;
import com.liferay.portal.kernel.security.auth.PrincipalException;
import com.liferay.portal.kernel.security.permission.PermissionChecker;
import com.liferay.portal.kernel.service.PortletLocalServiceUtil;
import com.liferay.portal.kernel.servlet.taglib.ui.Menu;
import com.liferay.portal.kernel.servlet.taglib.ui.URLMenuItem;
import com.liferay.portal.kernel.theme.PortletDisplay;
import com.liferay.portal.kernel.theme.ThemeDisplay;
import com.liferay.portal.kernel.util.ArrayUtil;
import com.liferay.portal.kernel.util.Constants;
import com.liferay.portal.kernel.util.GetterUtil;
import com.liferay.portal.kernel.util.HtmlUtil;
import com.liferay.portal.kernel.util.ListUtil;
import com.liferay.portal.kernel.util.OrderByComparator;
import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.portal.kernel.util.PortalUtil;
import com.liferay.portal.kernel.util.StringUtil;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.kernel.util.WebKeys;
import com.liferay.portal.kernel.webdav.WebDAVUtil;
import com.liferay.portal.kernel.workflow.WorkflowConstants;
import com.liferay.portal.util.PropsValues;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import javax.portlet.PortletException;
import javax.portlet.PortletRequest;
import javax.portlet.PortletURL;

import javax.servlet.http.HttpServletRequest;

/**
 * @author Alejandro Tardín
 */
public class DefaultDLAdminDisplayContext implements DLAdminDisplayContext {

	public DefaultDLAdminDisplayContext(
		LiferayPortletRequest liferayPortletRequest,
		LiferayPortletResponse liferayPortletResponse) {

		_liferayPortletRequest = liferayPortletRequest;
		_liferayPortletResponse = liferayPortletResponse;

		_currentURLObj = PortletURLUtil.getCurrent(
			liferayPortletRequest, liferayPortletResponse);
		_request = liferayPortletRequest.getHttpServletRequest();

		_dlRequestHelper = new DLRequestHelper(_request);

		_dlPortletInstanceSettings =
			_dlRequestHelper.getDLPortletInstanceSettings();

		_dlPortletInstanceSettingsHelper = new DLPortletInstanceSettingsHelper(
			_dlRequestHelper);

		_themeDisplay = (ThemeDisplay)_request.getAttribute(
			WebKeys.THEME_DISPLAY);

		_permissionChecker = _themeDisplay.getPermissionChecker();

		_portalPreferences = PortletPreferencesFactoryUtil.getPortalPreferences(
			liferayPortletRequest);

		_dlTrashUtil = (DLTrashUtil)_request.getAttribute(
			DLWebKeys.DOCUMENT_LIBRARY_TRASH_UTIL);

		_computeFolder();
		_computeRootFolder();
	}

	@Override
	public List<DropdownItem> getActionDropdownItems() {
		return new DropdownItemList() {
			{
				User user = _themeDisplay.getUser();
				Group scopeGroup = _themeDisplay.getScopeGroup();

				if (!user.isDefaultUser() &&
					(!scopeGroup.isStaged() || scopeGroup.isStagingGroup() ||
					 !scopeGroup.isStagedPortlet(
						 DLPortletKeys.DOCUMENT_LIBRARY))) {

					add(
						SafeConsumer.ignore(
							dropdownItem -> {
								dropdownItem.setHref(
									StringBundler.concat(
										"javascript:Liferay.fire('",
										_liferayPortletResponse.getNamespace(),
										"editEntry', {action: 'download'});",
										"void(0);"));

								dropdownItem.setIcon("download");

								dropdownItem.setLabel(
									LanguageUtil.get(_request, "download"));

								dropdownItem.setQuickAction(true);
							}));

					add(
						SafeConsumer.ignore(
							dropdownItem -> {
								dropdownItem.setHref(
									StringBundler.concat(
										"javascript:Liferay.fire('",
										_liferayPortletResponse.getNamespace(),
										"editEntry', {action: '",
										Constants.CHECKIN, "'}); void(0);"));

								dropdownItem.setIcon("unlock");

								dropdownItem.setLabel(
									LanguageUtil.get(_request, "unlock"));

								dropdownItem.setQuickAction(true);
							}));
					add(
						SafeConsumer.ignore(
							dropdownItem -> {
								dropdownItem.setHref(
									StringBundler.concat(
										"javascript:Liferay.fire('",
										_liferayPortletResponse.getNamespace(),
										"editEntry', {action: '",
										Constants.CHECKOUT, "'}); void(0);"));

								dropdownItem.setIcon("lock");

								dropdownItem.setLabel(
									LanguageUtil.get(_request, "lock"));

								dropdownItem.setQuickAction(true);
							}));
					add(
						SafeConsumer.ignore(
							dropdownItem -> {
								dropdownItem.setHref(
									StringBundler.concat(
										"javascript:Liferay.fire('",
										_liferayPortletResponse.getNamespace(),
										"editEntry', {action: '",
										Constants.MOVE, "'}); void(0);"));

								dropdownItem.setIcon("change");

								dropdownItem.setLabel(
									LanguageUtil.get(_request, "move"));

								dropdownItem.setQuickAction(true);
							}));
				}

				if (!user.isDefaultUser()) {
					add(
						SafeConsumer.ignore(
							dropdownItem -> {
								dropdownItem.setHref(
									StringBundler.concat(
										"javascript:",
										_liferayPortletResponse.getNamespace(),
										"deleteEntries();"));

								if (_dlTrashUtil.isTrashEnabled(
										scopeGroup.getGroupId(),
										getRepositoryId())) {

									dropdownItem.setIcon("trash");
									dropdownItem.setLabel(
										LanguageUtil.get(
											_request, "recycle-bin"));
								}
								else {
									dropdownItem.setIcon("times");
									dropdownItem.setLabel(
										LanguageUtil.get(_request, "delete"));
								}

								dropdownItem.setQuickAction(true);
							}));
				}
			}
		};
	}

	@Override
	public String getClearResultsURL() {
		PortletURL clearResultsURL = _liferayPortletResponse.createRenderURL();

		clearResultsURL.setParameter(
			"mvcRenderCommandName", "/document_library/view");

		return clearResultsURL.toString();
	}

	@Override
	public CreationMenu getCreationMenu() {
		String portletName = _liferayPortletRequest.getPortletName();

		if (!portletName.equals(DLPortletKeys.DOCUMENT_LIBRARY_ADMIN)) {
			return null;
		}

		DLPortletToolbarContributor dlPortletToolbarContributor =
			(DLPortletToolbarContributor)_request.getAttribute(
				DLWebKeys.DOCUMENT_LIBRARY_PORTLET_TOOLBAR_CONTRIBUTOR);

		List<Menu> menus = dlPortletToolbarContributor.getPortletTitleMenus(
			_liferayPortletRequest, _liferayPortletResponse);

		CreationMenu creationMenu = new CreationMenu();

		for (Menu menu : menus) {
			List<URLMenuItem> urlMenuItems =
				(List<URLMenuItem>)(List<?>)menu.getMenuItems();

			for (URLMenuItem urlMenuItem : urlMenuItems) {
				creationMenu.addDropdownItem(
					dropdownItem -> {
						dropdownItem.setHref(urlMenuItem.getURL());
						dropdownItem.setLabel(urlMenuItem.getLabel());
					});
			}
		}

		return creationMenu;
	}

	@Override
	public String getDisplayStyle() {
		String displayStyle = ParamUtil.getString(_request, "displayStyle");

		String[] displayViews = _dlPortletInstanceSettings.getDisplayViews();

		if (Validator.isNull(displayStyle)) {
			displayStyle = _portalPreferences.getValue(
				DLPortletKeys.DOCUMENT_LIBRARY, "display-style",
				PropsValues.DL_DEFAULT_DISPLAY_VIEW);
		}
		else {
			if (ArrayUtil.contains(displayViews, displayStyle)) {
				_portalPreferences.setValue(
					DLPortletKeys.DOCUMENT_LIBRARY, "display-style",
					displayStyle);

				_request.setAttribute(
					WebKeys.SINGLE_PAGE_APPLICATION_CLEAR_CACHE, Boolean.TRUE);
			}
		}

		if (!ArrayUtil.contains(displayViews, displayStyle)) {
			displayStyle = displayViews[0];
		}

		return displayStyle;
	}

	public String[] getDisplayViews() {
		DLPortletInstanceSettings dlPortletInstanceSettings =
			_dlRequestHelper.getDLPortletInstanceSettings();

		return dlPortletInstanceSettings.getDisplayViews();
	}

	@Override
	public List<DropdownItem> getFilterDropdownItems() {
		return new DropdownItemList() {
			{
				addGroup(
					SafeConsumer.ignore(
						dropdownGroupItem -> {
							dropdownGroupItem.setDropdownItems(
								_getFilterNavigationDropdownItems());
							dropdownGroupItem.setLabel(
								LanguageUtil.get(
									_request, "filter-by-navigation"));
						}));

				if (!_isSearch() && !_isNavigationRecent()) {
					addGroup(
						dropdownGroupItem -> {
							dropdownGroupItem.setDropdownItems(
								_getOrderByDropdownItems());
							dropdownGroupItem.setLabel(
								LanguageUtil.get(_request, "order-by"));
						});
				}
			}
		};
	}

	@Override
	public Folder getFolder() {
		return _folder;
	}

	@Override
	public long getFolderId() {
		return _folderId;
	}

	public String getNavigation() {
		return ParamUtil.getString(_request, "navigation", "home");
	}

	@Override
	public List<NavigationItem> getNavigationItems() {
		String mvcRenderCommandName = ParamUtil.getString(
			_liferayPortletRequest, "mvcRenderCommandName",
			"/document_library/view");

		return new NavigationItemList() {
			{
				add(
					navigationItem -> {
						_populateDocumentLibraryNavigationItem(
							navigationItem, mvcRenderCommandName);
					});

				if (DLPortletKeys.DOCUMENT_LIBRARY_ADMIN.equals(
						_dlRequestHelper.getPortletName())) {

					add(
						navigationItem -> {
							_populateFileEntryTypesNavigationItem(
								navigationItem, mvcRenderCommandName);
						});

					add(
						DefaultDLAdminDisplayContext.this
							::_populateMetadataSetsNavigationItem);
				}
			}
		};
	}

	@Override
	public String getOrderByCol() {
		String orderByCol = ParamUtil.getString(_request, "orderByCol");

		if (Validator.isNotNull(orderByCol)) {
			_portalPreferences.setValue(
				DLPortletKeys.DOCUMENT_LIBRARY, "order-by-col", orderByCol);
		}
		else {
			orderByCol = _portalPreferences.getValue(
				DLPortletKeys.DOCUMENT_LIBRARY, "order-by-col", "modifiedDate");
		}

		return orderByCol;
	}

	@Override
	public String getOrderByType() {
		String orderByType = ParamUtil.getString(_request, "orderByType");

		if (Validator.isNotNull(orderByType)) {
			_portalPreferences.setValue(
				DLPortletKeys.DOCUMENT_LIBRARY, "order-by-type", orderByType);
		}
		else {
			orderByType = _portalPreferences.getValue(
				DLPortletKeys.DOCUMENT_LIBRARY, "order-by-type", "asc");
		}

		return orderByType;
	}

	@Override
	public long getRepositoryId() {
		Folder folder = getFolder();

		if (folder != null) {
			return folder.getRepositoryId();
		}

		return _themeDisplay.getScopeGroupId();
	}

	@Override
	public long getRootFolderId() {
		return _rootFolderId;
	}

	@Override
	public String getRootFolderName() {
		return _rootFolderName;
	}

	@Override
	public SearchContainer getSearchContainer() {
		if (_searchContainer == null) {
			try {
				if (_isSearch()) {
					_searchContainer = _getSearchSearchContainer();
				}
				else {
					_searchContainer = _getDLSearchContainer();
				}
			}
			catch (PortalException pe) {
				throw new SystemException(pe);
			}
		}

		return _searchContainer;
	}

	public PortletURL getSearchSearchContainerURL() {
		PortletURL portletURL = _liferayPortletResponse.createRenderURL();

		portletURL.setParameter(
			"mvcRenderCommandName", "/document_library/search");

		String redirect = ParamUtil.getString(_request, "redirect");

		portletURL.setParameter("redirect", redirect);

		long searchFolderId = ParamUtil.getLong(_request, "searchFolderId");

		portletURL.setParameter(
			"searchFolderId", String.valueOf(searchFolderId));

		String keywords = ParamUtil.getString(_request, "keywords");

		portletURL.setParameter("keywords", keywords);

		return portletURL;
	}

	@Override
	public PortletURL getSearchURL() {
		PortletURL searchURL = _liferayPortletResponse.createRenderURL();

		searchURL.setParameter(
			"mvcRenderCommandName", "/document_library/search");

		long repositoryId = getRepositoryId();

		searchURL.setParameter("repositoryId", String.valueOf(repositoryId));
		searchURL.setParameter(
			"searchRepositoryId", String.valueOf(repositoryId));

		long folderId = getFolderId();

		searchURL.setParameter("folderId", String.valueOf(folderId));
		searchURL.setParameter("searchFolderId", String.valueOf(folderId));
		searchURL.setParameter(
			"showRepositoryTabs", Boolean.toString(folderId == 0));

		searchURL.setParameter("showSearchInfo", Boolean.TRUE.toString());

		return searchURL;
	}

	@Override
	public PortletURL getSortingURL() {
		int deltaEntry = ParamUtil.getInteger(_request, "deltaEntry");

		PortletURL sortingURL = _liferayPortletResponse.createRenderURL();

		long folderId = getFolderId();

		if (folderId == DLFolderConstants.DEFAULT_PARENT_FOLDER_ID) {
			sortingURL.setParameter(
				"mvcRenderCommandName", "/document_library/view");
		}
		else {
			sortingURL.setParameter(
				"mvcRenderCommandName", "/document_library/view_folder");
		}

		sortingURL.setParameter("navigation", getNavigation());

		if (deltaEntry > 0) {
			sortingURL.setParameter("deltaEntry", String.valueOf(deltaEntry));
		}

		sortingURL.setParameter("folderId", String.valueOf(folderId));

		long fileEntryTypeId = ParamUtil.getLong(
			_request, "fileEntryTypeId", -1);

		sortingURL.setParameter(
			"fileEntryTypeId", String.valueOf(fileEntryTypeId));

		sortingURL.setParameter(
			"orderByType",
			Objects.equals(getOrderByType(), "asc") ? "desc" : "asc");

		return sortingURL;
	}

	@Override
	public int getTotalItems() {
		SearchContainer searchContainer = getSearchContainer();

		return searchContainer.getTotal();
	}

	@Override
	public ViewTypeItemList getViewTypes() {
		if (_isSearch()) {
			return null;
		}

		String navigation = ParamUtil.getString(_request, "navigation", "home");

		int curEntry = ParamUtil.getInteger(_request, "curEntry");
		int deltaEntry = ParamUtil.getInteger(_request, "deltaEntry");

		long folderId = getFolderId();

		long fileEntryTypeId = ParamUtil.getLong(
			_request, "fileEntryTypeId", -1);

		String keywords = ParamUtil.getString(_request, "keywords");

		PortletURL displayStyleURL = _liferayPortletResponse.createRenderURL();

		String mvcRenderCommandName = "/document_library/search";

		if (Validator.isNull(keywords)) {
			if (folderId == DLFolderConstants.DEFAULT_PARENT_FOLDER_ID) {
				mvcRenderCommandName = "/document_library/view";
			}
			else {
				mvcRenderCommandName = "/document_library/view_folder";
			}
		}

		displayStyleURL.setParameter(
			"mvcRenderCommandName", mvcRenderCommandName);

		displayStyleURL.setParameter(
			"navigation", HtmlUtil.escapeJS(navigation));

		if (curEntry > 0) {
			displayStyleURL.setParameter("curEntry", String.valueOf(curEntry));
		}

		if (deltaEntry > 0) {
			displayStyleURL.setParameter(
				"deltaEntry", String.valueOf(deltaEntry));
		}

		displayStyleURL.setParameter("folderId", String.valueOf(folderId));

		if (fileEntryTypeId != -1) {
			displayStyleURL.setParameter(
				"fileEntryTypeId", String.valueOf(fileEntryTypeId));
		}

		return new ViewTypeItemList(displayStyleURL, getDisplayStyle()) {
			{
				if (ArrayUtil.contains(getDisplayViews(), "icon")) {
					addCardViewTypeItem();
				}

				if (ArrayUtil.contains(getDisplayViews(), "descriptive")) {
					addListViewTypeItem();
				}

				if (ArrayUtil.contains(getDisplayViews(), "list")) {
					addTableViewTypeItem();
				}
			}

		};
	}

	@Override
	public boolean isDefaultFolderView() {
		return _defaultFolderView;
	}

	@Override
	public boolean isDisabled() {
		try {
			if (DLAppServiceUtil.getFoldersAndFileEntriesAndFileShortcutsCount(
					getRepositoryId(), getFolderId(),
					WorkflowConstants.STATUS_ANY, true) <= 0) {

				return true;
			}

			return false;
		}
		catch (PortalException pe) {
			throw new SystemException(pe);
		}
	}

	@Override
	public boolean isSelectable() {
		return _dlPortletInstanceSettingsHelper.isShowActions();
	}

	@Override
	public boolean isShowSearch() {
		return _dlPortletInstanceSettingsHelper.isShowSearch();
	}

	@Override
	public boolean isShowSearchInfo() {
		if (_isSearch() && ParamUtil.getBoolean(_request, "showSearchInfo")) {
			return true;
		}

		return false;
	}

	private PortletURL _clonePortletURL() {
		try {
			return PortletURLUtil.clone(
				_currentURLObj, _liferayPortletResponse);
		}
		catch (PortletException pe) {
			throw new RuntimeException(pe);
		}
	}

	private void _computeFolder() {
		try {
			_folder = (Folder)_request.getAttribute(
				WebKeys.DOCUMENT_LIBRARY_FOLDER);

			_folderId = BeanPropertiesUtil.getLong(
				_folder, "folderId", getRootFolderId());

			_defaultFolderView = false;

			if ((_folder == null) &&
				(_folderId != DLFolderConstants.DEFAULT_PARENT_FOLDER_ID)) {

				_defaultFolderView = true;
			}

			if (_defaultFolderView) {
				try {
					_folder = DLAppLocalServiceUtil.getFolder(_folderId);
				}
				catch (NoSuchFolderException nsfe) {
					_folderId = DLFolderConstants.DEFAULT_PARENT_FOLDER_ID;
				}
			}
		}
		catch (PortalException pe) {
			throw new SystemException(pe);
		}
	}

	private void _computeRootFolder() {
		_rootFolderId = _dlPortletInstanceSettings.getRootFolderId();
		_rootFolderName = StringPool.BLANK;

		if (_rootFolderId != DLFolderConstants.DEFAULT_PARENT_FOLDER_ID) {
			try {
				Folder rootFolder = DLAppLocalServiceUtil.getFolder(
					_rootFolderId);

				_rootFolderName = rootFolder.getName();

				if (rootFolder.getGroupId() !=
						_themeDisplay.getScopeGroupId()) {

					_rootFolderId = DLFolderConstants.DEFAULT_PARENT_FOLDER_ID;
					_rootFolderName = StringPool.BLANK;
				}
			}
			catch (NoSuchFolderException | PrincipalException e) {
				_rootFolderId = DLFolderConstants.DEFAULT_PARENT_FOLDER_ID;
			}
			catch (PortalException pe) {
				throw new SystemException(pe);
			}
		}
	}

	private SearchContainer _getDLSearchContainer() throws PortalException {
		String navigation = ParamUtil.getString(_request, "navigation", "home");

		String currentFolder = ParamUtil.getString(_request, "curFolder");
		String deltaFolder = ParamUtil.getString(_request, "deltaFolder");

		long fileEntryTypeId = ParamUtil.getLong(
			_request, "fileEntryTypeId", -1);

		String dlFileEntryTypeName = LanguageUtil.get(
			_request, "basic-document");

		int status = WorkflowConstants.STATUS_APPROVED;

		User user = _themeDisplay.getUser();

		if (_permissionChecker.isContentReviewer(
				user.getCompanyId(), _themeDisplay.getScopeGroupId())) {

			status = WorkflowConstants.STATUS_ANY;
		}

		long categoryId = ParamUtil.getLong(_request, "categoryId");
		String tagName = ParamUtil.getString(_request, "tag");

		boolean useAssetEntryQuery = false;

		if ((categoryId > 0) || Validator.isNotNull(tagName)) {
			useAssetEntryQuery = true;
		}

		PortletURL portletURL = _liferayPortletResponse.createRenderURL();

		long folderId = getFolderId();

		if (folderId == DLFolderConstants.DEFAULT_PARENT_FOLDER_ID) {
			portletURL.setParameter(
				"mvcRenderCommandName", "/document_library/view");
		}
		else {
			portletURL.setParameter(
				"mvcRenderCommandName", "/document_library/view_folder");
		}

		portletURL.setParameter("navigation", navigation);
		portletURL.setParameter("curFolder", currentFolder);
		portletURL.setParameter("deltaFolder", deltaFolder);
		portletURL.setParameter("folderId", String.valueOf(folderId));

		SearchContainer dlSearchContainer = new SearchContainer(
			_liferayPortletRequest, null, null, "curEntry",
			_dlPortletInstanceSettings.getEntriesPerPage(), portletURL, null,
			null);

		String[] entryColumns =
			_dlPortletInstanceSettingsHelper.getEntryColumns();

		dlSearchContainer.setHeaderNames(ListUtil.fromArray(entryColumns));

		String orderByCol = getOrderByCol();
		String orderByType = getOrderByType();

		boolean orderByModel = false;

		if (navigation.equals("home")) {
			orderByModel = true;
		}

		OrderByComparator<?> orderByComparator =
			DLUtil.getRepositoryModelOrderByComparator(
				orderByCol, orderByType, orderByModel);

		if (navigation.equals("recent")) {
			orderByComparator = new RepositoryModelModifiedDateComparator();
		}

		dlSearchContainer.setOrderByCol(orderByCol);
		dlSearchContainer.setOrderByComparator(orderByComparator);
		dlSearchContainer.setOrderByType(orderByType);

		List results = new ArrayList();
		int total = 0;

		if (fileEntryTypeId >= 0) {
			Indexer indexer = IndexerRegistryUtil.getIndexer(
				DLFileEntryConstants.getClassName());

			if (fileEntryTypeId > 0) {
				DLFileEntryType dlFileEntryType =
					DLFileEntryTypeLocalServiceUtil.getFileEntryType(
						fileEntryTypeId);

				dlFileEntryTypeName = dlFileEntryType.getName(
					_request.getLocale());
			}

			SearchContext searchContext = SearchContextFactory.getInstance(
				_request);

			searchContext.setAttribute("paginationType", "none");
			searchContext.setEnd(dlSearchContainer.getEnd());

			if (orderByCol.equals("creationDate")) {
				orderByCol = "createDate";
			}
			else if (orderByCol.equals("readCount")) {
				orderByCol = "downloads";
			}
			else if (orderByCol.equals("modifiedDate")) {
				orderByCol = "modified";
			}

			Sort sort = new Sort(
				orderByCol, !StringUtil.equalsIgnoreCase(orderByType, "asc"));

			searchContext.setSorts(sort);

			searchContext.setStart(dlSearchContainer.getStart());

			Hits hits = indexer.search(searchContext);

			total = hits.getLength();

			dlSearchContainer.setTotal(total);

			for (Document doc : hits.getDocs()) {
				long fileEntryId = GetterUtil.getLong(
					doc.get(Field.ENTRY_CLASS_PK));

				FileEntry fileEntry = null;

				try {
					fileEntry = DLAppLocalServiceUtil.getFileEntry(fileEntryId);
				}
				catch (Exception e) {
					if (_log.isWarnEnabled()) {
						_log.warn(
							StringBundler.concat(
								"Documents and Media search index is stale ",
								"and contains file entry {",
								String.valueOf(fileEntryId), "}"));
					}

					continue;
				}

				results.add(fileEntry);
			}
		}
		else {
			if (navigation.equals("home")) {
				if (useAssetEntryQuery) {
					long[] classNameIds = {
						PortalUtil.getClassNameId(
							DLFileEntryConstants.getClassName()),
						PortalUtil.getClassNameId(
							DLFileShortcutConstants.getClassName())
					};

					AssetEntryQuery assetEntryQuery = new AssetEntryQuery(
						classNameIds, dlSearchContainer);

					assetEntryQuery.setEnablePermissions(true);
					assetEntryQuery.setExcludeZeroViewCount(false);

					total = AssetEntryServiceUtil.getEntriesCount(
						assetEntryQuery);

					dlSearchContainer.setTotal(total);

					results = AssetEntryServiceUtil.getEntries(assetEntryQuery);
				}
				else {
					long repositoryId = getRepositoryId();

					total =
						DLAppServiceUtil.
							getFoldersAndFileEntriesAndFileShortcutsCount(
								repositoryId, folderId, status, true);

					dlSearchContainer.setTotal(total);

					results =
						DLAppServiceUtil.
							getFoldersAndFileEntriesAndFileShortcuts(
								repositoryId, folderId, status, true,
								dlSearchContainer.getStart(),
								dlSearchContainer.getEnd(),
								dlSearchContainer.getOrderByComparator());
				}
			}
			else if (navigation.equals("mine") || navigation.equals("recent")) {
				long groupFileEntriesUserId = 0;

				if (navigation.equals("mine") && _themeDisplay.isSignedIn()) {
					groupFileEntriesUserId = _themeDisplay.getUserId();

					status = WorkflowConstants.STATUS_ANY;
				}

				long repositoryId = getRepositoryId();

				total = DLAppServiceUtil.getGroupFileEntriesCount(
					repositoryId, groupFileEntriesUserId, folderId, null,
					status);

				dlSearchContainer.setTotal(total);

				results = DLAppServiceUtil.getGroupFileEntries(
					repositoryId, groupFileEntriesUserId, folderId, null,
					status, dlSearchContainer.getStart(),
					dlSearchContainer.getEnd(),
					dlSearchContainer.getOrderByComparator());
			}
		}

		dlSearchContainer.setResults(results);

		if (fileEntryTypeId >= 0) {
			dlSearchContainer.setEmptyResultsMessage(
				LanguageUtil.format(
					_request, "there-are-no-documents-or-media-files-of-type-x",
					HtmlUtil.escape(dlFileEntryTypeName)));
		}
		else {
			dlSearchContainer.setEmptyResultsMessage(
				"there-are-no-documents-or-media-files-in-this-folder");
		}

		return dlSearchContainer;
	}

	private List<DropdownItem> _getFilterNavigationDropdownItems() {
		long fileEntryTypeId = ParamUtil.getLong(
			_request, "fileEntryTypeId", -1);
		long folderId = getFolderId();
		final String navigation = ParamUtil.getString(
			_request, "navigation", "home");
		final long rootFolderId = getRootFolderId();

		return new DropdownItemList() {
			{
				add(
					dropdownItem -> {
						PortletURL viewDocumentsHomeURL =
							_liferayPortletResponse.createRenderURL();

						viewDocumentsHomeURL.setParameter(
							"mvcRenderCommandName", "/document_library/view");
						viewDocumentsHomeURL.setParameter(
							"folderId", String.valueOf(rootFolderId));

						dropdownItem.setHref(viewDocumentsHomeURL);
						dropdownItem.setActive(
							(navigation.equals("home")) &&
							(folderId == rootFolderId) &&
							(fileEntryTypeId == -1));
						dropdownItem.setLabel(
							LanguageUtil.get(_request, "all"));
					});
				add(
					dropdownItem -> {
						PortletURL viewRecentDocumentsURL =
							_liferayPortletResponse.createRenderURL();

						viewRecentDocumentsURL.setParameter(
							"mvcRenderCommandName", "/document_library/view");
						viewRecentDocumentsURL.setParameter(
							"navigation", "recent");
						viewRecentDocumentsURL.setParameter(
							"folderId", String.valueOf(rootFolderId));

						dropdownItem.setHref(viewRecentDocumentsURL);
						dropdownItem.setActive(navigation.equals("recent"));
						dropdownItem.setLabel(
							LanguageUtil.get(_request, "recent"));
					});

				if (_themeDisplay.isSignedIn()) {
					add(
						dropdownItem -> {
							PortletURL viewMyDocumentsURL =
								_liferayPortletResponse.createRenderURL();

							viewMyDocumentsURL.setParameter(
								"mvcRenderCommandName",
								"/document_library/view");
							viewMyDocumentsURL.setParameter(
								"navigation", "mine");
							viewMyDocumentsURL.setParameter(
								"folderId", String.valueOf(rootFolderId));

							dropdownItem.setHref(viewMyDocumentsURL);
							dropdownItem.setActive(navigation.equals("mine"));
							dropdownItem.setLabel(
								LanguageUtil.get(_request, "mine"));
						});
				}

				add(
					SafeConsumer.ignore(
						dropdownItem -> {
							dropdownItem.setActive(fileEntryTypeId != -1);

							String label = LanguageUtil.get(
								_request, "document-types");

							if (fileEntryTypeId != -1) {
								String fileEntryTypeName = LanguageUtil.get(
									_request, "basic-document");

								if (fileEntryTypeId !=
										DLFileEntryTypeConstants.
											FILE_ENTRY_TYPE_ID_BASIC_DOCUMENT) {

									DLFileEntryType fileEntryType =
										DLFileEntryTypeLocalServiceUtil.
											getFileEntryType(fileEntryTypeId);

									fileEntryTypeName = fileEntryType.getName(
										_request.getLocale());
								}

								label = String.format(
									"%s: %s", label, fileEntryTypeName);
							}

							dropdownItem.setLabel(label);
							dropdownItem.setHref(
								"javascript:" +
									_liferayPortletResponse.getNamespace() +
										"openDocumentTypesSelector();");
						}));
			}
		};
	}

	private List<DropdownItem> _getOrderByDropdownItems() {
		final Map<String, String> orderColumns = new HashMap<>();

		orderColumns.put("creationDate", "create-date");
		orderColumns.put("downloads", "downloads");
		orderColumns.put("modifiedDate", "modified-date");
		orderColumns.put("size", "size");
		orderColumns.put("title", "title");

		return new DropdownItemList() {
			{
				for (Map.Entry<String, String> orderByColEntry :
						orderColumns.entrySet()) {

					String orderByCol = orderByColEntry.getKey();

					add(
						SafeConsumer.ignore(
							dropdownItem -> {
								dropdownItem.setActive(
									orderByCol.equals(getOrderByCol()));
								dropdownItem.setHref(
									getSortingURL(), "orderByCol", orderByCol);
								dropdownItem.setLabel(
									LanguageUtil.get(
										_request, orderByColEntry.getValue()));
							}));
				}
			}
		};
	}

	private Portlet _getPortlet(ThemeDisplay themeDisplay) {
		PortletDisplay portletDisplay = themeDisplay.getPortletDisplay();

		return PortletLocalServiceUtil.getPortletById(portletDisplay.getId());
	}

	private List _getSearchResults(SearchContainer searchContainer)
		throws PortalException {

		SearchContext searchContext = SearchContextFactory.getInstance(
			_request);

		searchContext.setAttribute("paginationType", "regular");

		long searchRepositoryId = ParamUtil.getLong(
			_request, "searchRepositoryId");

		if (searchRepositoryId == 0) {
			searchRepositoryId = _themeDisplay.getScopeGroupId();
		}

		searchContext.setAttribute("searchRepositoryId", searchRepositoryId);
		searchContext.setEnd(searchContainer.getEnd());

		long searchFolderId = ParamUtil.getLong(_request, "searchFolderId");

		searchContext.setFolderIds(new long[] {searchFolderId});

		searchContext.setIncludeDiscussions(true);

		String keywords = ParamUtil.getString(_request, "keywords");

		searchContext.setKeywords(keywords);

		QueryConfig queryConfig = searchContext.getQueryConfig();

		queryConfig.setSearchSubfolders(true);

		searchContext.setStart(searchContainer.getStart());

		Hits hits = DLAppServiceUtil.search(searchRepositoryId, searchContext);

		List<SearchResult> searchResults = SearchResultUtil.getSearchResults(
			hits, _request.getLocale());

		List dlSearchResults = new ArrayList<>();

		for (SearchResult searchResult : searchResults) {
			FileEntry fileEntry = null;
			Folder curFolder = null;

			String className = searchResult.getClassName();

			try {
				if (className.equals(DLFileEntry.class.getName()) ||
					FileEntry.class.isAssignableFrom(
						Class.forName(className))) {

					fileEntry = DLAppLocalServiceUtil.getFileEntry(
						searchResult.getClassPK());

					dlSearchResults.add(fileEntry);
				}
				else if (className.equals(DLFolder.class.getName())) {
					curFolder = DLAppLocalServiceUtil.getFolder(
						searchResult.getClassPK());

					dlSearchResults.add(curFolder);
				}
			}
			catch (ClassNotFoundException cnfe) {
				throw new PortalException(cnfe);
			}
		}

		return dlSearchResults;
	}

	private SearchContainer _getSearchSearchContainer() throws PortalException {
		SearchContainer searchContainer = new SearchContainer(
			_liferayPortletRequest, getSearchSearchContainerURL(), null, null);

		searchContainer.setResults(_getSearchResults(searchContainer));

		return searchContainer;
	}

	private boolean _isNavigationRecent() {
		if (Objects.equals(getNavigation(), "recent")) {
			return true;
		}

		return false;
	}

	private boolean _isSearch() {
		String mvcRenderCommandName = ParamUtil.getString(
			_request, "mvcRenderCommandName");

		return mvcRenderCommandName.equals("/document_library/search");
	}

	private void _populateDocumentLibraryNavigationItem(
		NavigationItem navigationItem, String mvcRenderCommandName) {

		navigationItem.setActive(
			!mvcRenderCommandName.equals(
				"/document_library/view_file_entry_types"));

		PortletURL viewDocumentLibraryURL =
			_liferayPortletResponse.createRenderURL();

		viewDocumentLibraryURL.setParameter(
			"mvcRenderCommandName", "/document_library/view");
		viewDocumentLibraryURL.setParameter(
			"redirect", _currentURLObj.toString());

		navigationItem.setHref(viewDocumentLibraryURL.toString());

		navigationItem.setLabel(
			LanguageUtil.get(
				_liferayPortletRequest.getHttpServletRequest(),
				"documents-and-media"));
	}

	private void _populateFileEntryTypesNavigationItem(
		NavigationItem navigationItem, String mvcRenderCommandName) {

		navigationItem.setActive(
			mvcRenderCommandName.equals(
				"/document_library/view_file_entry_types"));

		PortletURL viewFileEntryTypesURL = _clonePortletURL();

		viewFileEntryTypesURL.setParameter(
			"mvcRenderCommandName", "/document_library/view_file_entry_types");
		viewFileEntryTypesURL.setParameter(
			"redirect", _currentURLObj.toString());

		navigationItem.setHref(viewFileEntryTypesURL.toString());

		navigationItem.setLabel(
			LanguageUtil.get(
				_liferayPortletRequest.getHttpServletRequest(),
				"document-types"));
	}

	private void _populateMetadataSetsNavigationItem(
		NavigationItem navigationItem) {

		navigationItem.setActive(false);

		Portlet portlet = _getPortlet(_themeDisplay);

		PortletURL viewMetadataSetsURL = PortletURLFactoryUtil.create(
			_liferayPortletRequest,
			PortletProviderUtil.getPortletId(
				DDMStructure.class.getName(), PortletProvider.Action.VIEW),
			PortletRequest.RENDER_PHASE);

		viewMetadataSetsURL.setParameter("mvcPath", "/view.jsp");
		viewMetadataSetsURL.setParameter(
			"backURL", _themeDisplay.getURLCurrent());
		viewMetadataSetsURL.setParameter(
			"groupId", String.valueOf(_themeDisplay.getScopeGroupId()));
		viewMetadataSetsURL.setParameter(
			"refererPortletName", DLPortletKeys.DOCUMENT_LIBRARY);
		viewMetadataSetsURL.setParameter(
			"refererWebDAVToken", WebDAVUtil.getStorageToken(portlet));
		viewMetadataSetsURL.setParameter(
			"showAncestorScopes", Boolean.TRUE.toString());
		viewMetadataSetsURL.setParameter(
			"showManageTemplates", Boolean.FALSE.toString());

		navigationItem.setHref(viewMetadataSetsURL.toString());

		navigationItem.setLabel(
			LanguageUtil.get(
				_liferayPortletRequest.getHttpServletRequest(),
				"metadata-sets"));
	}

	private static final Log _log = LogFactoryUtil.getLog(
		DefaultDLAdminDisplayContext.class);

	private final PortletURL _currentURLObj;
	private boolean _defaultFolderView;
	private final DLPortletInstanceSettings _dlPortletInstanceSettings;
	private final DLPortletInstanceSettingsHelper
		_dlPortletInstanceSettingsHelper;
	private final DLRequestHelper _dlRequestHelper;
	private final DLTrashUtil _dlTrashUtil;
	private Folder _folder;
	private long _folderId;
	private final LiferayPortletRequest _liferayPortletRequest;
	private final LiferayPortletResponse _liferayPortletResponse;
	private final PermissionChecker _permissionChecker;
	private final PortalPreferences _portalPreferences;
	private final HttpServletRequest _request;
	private long _rootFolderId;
	private String _rootFolderName;
	private SearchContainer _searchContainer;
	private final ThemeDisplay _themeDisplay;

}