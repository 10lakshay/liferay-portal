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

package com.liferay.asset.auto.tagger.internal;

import com.liferay.asset.auto.tagger.AssetAutoTagProvider;
import com.liferay.asset.auto.tagger.AssetAutoTagger;
import com.liferay.asset.auto.tagger.internal.configuration.AssetAutoTaggerConfiguration;
import com.liferay.asset.auto.tagger.model.AssetAutoTaggerEntry;
import com.liferay.asset.auto.tagger.service.AssetAutoTaggerEntryLocalService;
import com.liferay.asset.kernel.model.AssetEntry;
import com.liferay.asset.kernel.model.AssetRenderer;
import com.liferay.asset.kernel.model.AssetTag;
import com.liferay.asset.kernel.service.AssetEntryLocalService;
import com.liferay.asset.kernel.service.AssetTagLocalService;
import com.liferay.osgi.service.tracker.collections.map.ServiceTrackerMap;
import com.liferay.osgi.service.tracker.collections.map.ServiceTrackerMapFactory;
import com.liferay.portal.configuration.metatype.bnd.util.ConfigurableUtil;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.search.Indexer;
import com.liferay.portal.kernel.search.IndexerRegistry;
import com.liferay.portal.kernel.service.UserLocalService;
import com.liferay.portal.kernel.transaction.Propagation;
import com.liferay.portal.kernel.transaction.TransactionConfig;
import com.liferay.portal.kernel.transaction.TransactionInvokerUtil;
import com.liferay.portal.kernel.util.ListUtil;
import com.liferay.portal.kernel.util.Validator;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.osgi.framework.BundleContext;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Deactivate;
import org.osgi.service.component.annotations.Modified;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Alejandro Tardín
 */
@Component(
	configurationPid = "com.liferay.asset.auto.tagger.internal.configuration.AssetAutoTaggerConfiguration",
	service = AssetAutoTagger.class
)
public class AssetAutoTaggerImpl implements AssetAutoTagger {

	@Override
	public boolean isAutoTaggable(AssetEntry assetEntry) {
		if (_assetAutoTaggerConfiguration.enabled() && assetEntry.isVisible() &&
			ListUtil.isNotEmpty(
				_getAssetAutoTagProviders(assetEntry.getClassName()))) {

			return true;
		}

		return false;
	}

	@Override
	public void tag(AssetEntry assetEntry) throws PortalException {
		if (!isAutoTaggable(assetEntry)) {
			return;
		}

		try {
			TransactionInvokerUtil.invoke(
				_transactionConfig,
				() -> {
					Set<String> tagNames = _getAutoTagNames(assetEntry);

					tagNames.removeAll(Arrays.asList(assetEntry.getTagNames()));

					List<AssetTag> assetTags = _assetTagLocalService.checkTags(
						assetEntry.getUserId(), assetEntry.getGroupId(),
						tagNames.toArray(new String[0]));

					if (assetTags.isEmpty()) {
						return null;
					}

					for (AssetTag assetTag : assetTags) {
						_assetTagLocalService.addAssetEntryAssetTag(
							assetEntry.getEntryId(), assetTag);

						_assetAutoTaggerEntryLocalService.
							addAssetAutoTaggerEntry(assetEntry, assetTag);

						_assetTagLocalService.incrementAssetCount(
							assetTag.getTagId(), assetEntry.getClassNameId());
					}

					_reindex(assetEntry);

					return null;
				});
		}
		catch (Throwable t) {
			throw new PortalException(
				"Unable to auto tag asset entry " + assetEntry.getEntryId(), t);
		}
	}

	@Override
	public void untag(AssetEntry assetEntry) throws PortalException {
		try {
			TransactionInvokerUtil.invoke(
				_transactionConfig,
				() -> {
					List<AssetAutoTaggerEntry> assetAutoTaggerEntries =
						_assetAutoTaggerEntryLocalService.
							getAssetAutoTaggerEntries(assetEntry);

					if (assetAutoTaggerEntries.isEmpty()) {
						return null;
					}

					for (AssetAutoTaggerEntry assetAutoTaggerEntry :
							assetAutoTaggerEntries) {

						_assetTagLocalService.deleteAssetEntryAssetTag(
							assetAutoTaggerEntry.getAssetEntryId(),
							assetAutoTaggerEntry.getAssetTagId());

						_assetTagLocalService.decrementAssetCount(
							assetAutoTaggerEntry.getAssetTagId(),
							assetEntry.getClassNameId());
					}

					_reindex(assetEntry);

					return null;
				});
		}
		catch (Throwable t) {
			throw new PortalException(
				"Unable to remove auto tag from asset entry " +
					assetEntry.getEntryId(),
				t);
		}
	}

	@Activate
	protected void activate(
		BundleContext bundleContext, Map<String, Object> properties) {

		modified(properties);

		_serviceTrackerMap = ServiceTrackerMapFactory.openMultiValueMap(
			bundleContext, AssetAutoTagProvider.class, "model.class.name");
	}

	@Deactivate
	protected void deactivate() {
		_serviceTrackerMap.close();
	}

	@Modified
	protected void modified(Map<String, Object> properties) {
		_assetAutoTaggerConfiguration = ConfigurableUtil.createConfigurable(
			AssetAutoTaggerConfiguration.class, properties);
	}

	private List<AssetAutoTagProvider> _getAssetAutoTagProviders(
		String className) {

		List<AssetAutoTagProvider> assetAutoTagProviders = new ArrayList<>();

		List<AssetAutoTagProvider> generalAssetAutoTagProviders =
			_serviceTrackerMap.getService("*");

		if (!ListUtil.isEmpty(generalAssetAutoTagProviders)) {
			assetAutoTagProviders.addAll(generalAssetAutoTagProviders);
		}

		if (Validator.isNotNull(className)) {
			List<AssetAutoTagProvider> classNameAssetAutoTagProviders =
				_serviceTrackerMap.getService(className);

			if (!ListUtil.isEmpty(classNameAssetAutoTagProviders)) {
				assetAutoTagProviders.addAll(classNameAssetAutoTagProviders);
			}
		}

		return assetAutoTagProviders;
	}

	private Set<String> _getAutoTagNames(AssetEntry assetEntry) {
		AssetRenderer<?> assetRenderer = assetEntry.getAssetRenderer();

		Set<String> tagNames = new HashSet<>();

		if (assetRenderer != null) {
			List<AssetAutoTagProvider> assetAutoTagProviders =
				_getAssetAutoTagProviders(assetEntry.getClassName());

			for (AssetAutoTagProvider assetAutoTagProvider :
					assetAutoTagProviders) {

				tagNames.addAll(
					assetAutoTagProvider.getTagNames(
						assetRenderer.getAssetObject()));
			}
		}

		return tagNames;
	}

	private void _reindex(AssetEntry assetEntry) throws PortalException {
		Indexer<Object> indexer = _indexerRegistry.getIndexer(
			assetEntry.getClassName());

		if (indexer != null) {
			indexer.reindex(assetEntry.getClassName(), assetEntry.getClassPK());
		}
	}

	private volatile AssetAutoTaggerConfiguration _assetAutoTaggerConfiguration;

	@Reference
	private AssetAutoTaggerEntryLocalService _assetAutoTaggerEntryLocalService;

	@Reference
	private AssetEntryLocalService _assetEntryLocalService;

	@Reference
	private AssetTagLocalService _assetTagLocalService;

	@Reference
	private IndexerRegistry _indexerRegistry;

	private ServiceTrackerMap<String, List<AssetAutoTagProvider>>
		_serviceTrackerMap;
	private final TransactionConfig _transactionConfig =
		TransactionConfig.Factory.create(
			Propagation.REQUIRED, new Class<?>[] {Exception.class});

	@Reference
	private UserLocalService _userLocalService;

}