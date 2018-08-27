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

package com.liferay.asset.list.service.persistence;

import aQute.bnd.annotation.ProviderType;

import com.liferay.asset.list.exception.NoSuchEntryException;
import com.liferay.asset.list.model.AssetListEntry;

import com.liferay.portal.kernel.service.persistence.BasePersistence;

/**
 * The persistence interface for the asset list entry service.
 *
 * <p>
 * Caching information and settings can be found in <code>portal.properties</code>
 * </p>
 *
 * @author Brian Wing Shun Chan
 * @see com.liferay.asset.list.service.persistence.impl.AssetListEntryPersistenceImpl
 * @see AssetListEntryUtil
 * @generated
 */
@ProviderType
public interface AssetListEntryPersistence extends BasePersistence<AssetListEntry> {
	/*
	 * NOTE FOR DEVELOPERS:
	 *
	 * Never modify or reference this interface directly. Always use {@link AssetListEntryUtil} to access the asset list entry persistence. Modify <code>service.xml</code> and rerun ServiceBuilder to regenerate this interface.
	 */

	/**
	* Returns all the asset list entries where groupId = &#63;.
	*
	* @param groupId the group ID
	* @return the matching asset list entries
	*/
	public java.util.List<AssetListEntry> findByGroupId(long groupId);

	/**
	* Returns a range of all the asset list entries where groupId = &#63;.
	*
	* <p>
	* Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to {@link QueryUtil#ALL_POS} will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent and pagination is required (<code>start</code> and <code>end</code> are not {@link QueryUtil#ALL_POS}), then the query will include the default ORDER BY logic from {@link AssetListEntryModelImpl}. If both <code>orderByComparator</code> and pagination are absent, for performance reasons, the query will not have an ORDER BY clause and the returned result set will be sorted on by the primary key in an ascending order.
	* </p>
	*
	* @param groupId the group ID
	* @param start the lower bound of the range of asset list entries
	* @param end the upper bound of the range of asset list entries (not inclusive)
	* @return the range of matching asset list entries
	*/
	public java.util.List<AssetListEntry> findByGroupId(long groupId,
		int start, int end);

	/**
	* Returns an ordered range of all the asset list entries where groupId = &#63;.
	*
	* <p>
	* Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to {@link QueryUtil#ALL_POS} will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent and pagination is required (<code>start</code> and <code>end</code> are not {@link QueryUtil#ALL_POS}), then the query will include the default ORDER BY logic from {@link AssetListEntryModelImpl}. If both <code>orderByComparator</code> and pagination are absent, for performance reasons, the query will not have an ORDER BY clause and the returned result set will be sorted on by the primary key in an ascending order.
	* </p>
	*
	* @param groupId the group ID
	* @param start the lower bound of the range of asset list entries
	* @param end the upper bound of the range of asset list entries (not inclusive)
	* @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	* @return the ordered range of matching asset list entries
	*/
	public java.util.List<AssetListEntry> findByGroupId(long groupId,
		int start, int end,
		com.liferay.portal.kernel.util.OrderByComparator<AssetListEntry> orderByComparator);

	/**
	* Returns an ordered range of all the asset list entries where groupId = &#63;.
	*
	* <p>
	* Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to {@link QueryUtil#ALL_POS} will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent and pagination is required (<code>start</code> and <code>end</code> are not {@link QueryUtil#ALL_POS}), then the query will include the default ORDER BY logic from {@link AssetListEntryModelImpl}. If both <code>orderByComparator</code> and pagination are absent, for performance reasons, the query will not have an ORDER BY clause and the returned result set will be sorted on by the primary key in an ascending order.
	* </p>
	*
	* @param groupId the group ID
	* @param start the lower bound of the range of asset list entries
	* @param end the upper bound of the range of asset list entries (not inclusive)
	* @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	* @param retrieveFromCache whether to retrieve from the finder cache
	* @return the ordered range of matching asset list entries
	*/
	public java.util.List<AssetListEntry> findByGroupId(long groupId,
		int start, int end,
		com.liferay.portal.kernel.util.OrderByComparator<AssetListEntry> orderByComparator,
		boolean retrieveFromCache);

	/**
	* Returns the first asset list entry in the ordered set where groupId = &#63;.
	*
	* @param groupId the group ID
	* @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	* @return the first matching asset list entry
	* @throws NoSuchEntryException if a matching asset list entry could not be found
	*/
	public AssetListEntry findByGroupId_First(long groupId,
		com.liferay.portal.kernel.util.OrderByComparator<AssetListEntry> orderByComparator)
		throws NoSuchEntryException;

	/**
	* Returns the first asset list entry in the ordered set where groupId = &#63;.
	*
	* @param groupId the group ID
	* @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	* @return the first matching asset list entry, or <code>null</code> if a matching asset list entry could not be found
	*/
	public AssetListEntry fetchByGroupId_First(long groupId,
		com.liferay.portal.kernel.util.OrderByComparator<AssetListEntry> orderByComparator);

	/**
	* Returns the last asset list entry in the ordered set where groupId = &#63;.
	*
	* @param groupId the group ID
	* @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	* @return the last matching asset list entry
	* @throws NoSuchEntryException if a matching asset list entry could not be found
	*/
	public AssetListEntry findByGroupId_Last(long groupId,
		com.liferay.portal.kernel.util.OrderByComparator<AssetListEntry> orderByComparator)
		throws NoSuchEntryException;

	/**
	* Returns the last asset list entry in the ordered set where groupId = &#63;.
	*
	* @param groupId the group ID
	* @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	* @return the last matching asset list entry, or <code>null</code> if a matching asset list entry could not be found
	*/
	public AssetListEntry fetchByGroupId_Last(long groupId,
		com.liferay.portal.kernel.util.OrderByComparator<AssetListEntry> orderByComparator);

	/**
	* Returns the asset list entries before and after the current asset list entry in the ordered set where groupId = &#63;.
	*
	* @param assetListEntryId the primary key of the current asset list entry
	* @param groupId the group ID
	* @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	* @return the previous, current, and next asset list entry
	* @throws NoSuchEntryException if a asset list entry with the primary key could not be found
	*/
	public AssetListEntry[] findByGroupId_PrevAndNext(long assetListEntryId,
		long groupId,
		com.liferay.portal.kernel.util.OrderByComparator<AssetListEntry> orderByComparator)
		throws NoSuchEntryException;

	/**
	* Removes all the asset list entries where groupId = &#63; from the database.
	*
	* @param groupId the group ID
	*/
	public void removeByGroupId(long groupId);

	/**
	* Returns the number of asset list entries where groupId = &#63;.
	*
	* @param groupId the group ID
	* @return the number of matching asset list entries
	*/
	public int countByGroupId(long groupId);

	/**
	* Returns the asset list entry where groupId = &#63; and title = &#63; or throws a {@link NoSuchEntryException} if it could not be found.
	*
	* @param groupId the group ID
	* @param title the title
	* @return the matching asset list entry
	* @throws NoSuchEntryException if a matching asset list entry could not be found
	*/
	public AssetListEntry findByG_T(long groupId, String title)
		throws NoSuchEntryException;

	/**
	* Returns the asset list entry where groupId = &#63; and title = &#63; or returns <code>null</code> if it could not be found. Uses the finder cache.
	*
	* @param groupId the group ID
	* @param title the title
	* @return the matching asset list entry, or <code>null</code> if a matching asset list entry could not be found
	*/
	public AssetListEntry fetchByG_T(long groupId, String title);

	/**
	* Returns the asset list entry where groupId = &#63; and title = &#63; or returns <code>null</code> if it could not be found, optionally using the finder cache.
	*
	* @param groupId the group ID
	* @param title the title
	* @param retrieveFromCache whether to retrieve from the finder cache
	* @return the matching asset list entry, or <code>null</code> if a matching asset list entry could not be found
	*/
	public AssetListEntry fetchByG_T(long groupId, String title,
		boolean retrieveFromCache);

	/**
	* Removes the asset list entry where groupId = &#63; and title = &#63; from the database.
	*
	* @param groupId the group ID
	* @param title the title
	* @return the asset list entry that was removed
	*/
	public AssetListEntry removeByG_T(long groupId, String title)
		throws NoSuchEntryException;

	/**
	* Returns the number of asset list entries where groupId = &#63; and title = &#63;.
	*
	* @param groupId the group ID
	* @param title the title
	* @return the number of matching asset list entries
	*/
	public int countByG_T(long groupId, String title);

	/**
	* Returns all the asset list entries where groupId = &#63; and type = &#63;.
	*
	* @param groupId the group ID
	* @param type the type
	* @return the matching asset list entries
	*/
	public java.util.List<AssetListEntry> findByG_TY(long groupId, int type);

	/**
	* Returns a range of all the asset list entries where groupId = &#63; and type = &#63;.
	*
	* <p>
	* Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to {@link QueryUtil#ALL_POS} will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent and pagination is required (<code>start</code> and <code>end</code> are not {@link QueryUtil#ALL_POS}), then the query will include the default ORDER BY logic from {@link AssetListEntryModelImpl}. If both <code>orderByComparator</code> and pagination are absent, for performance reasons, the query will not have an ORDER BY clause and the returned result set will be sorted on by the primary key in an ascending order.
	* </p>
	*
	* @param groupId the group ID
	* @param type the type
	* @param start the lower bound of the range of asset list entries
	* @param end the upper bound of the range of asset list entries (not inclusive)
	* @return the range of matching asset list entries
	*/
	public java.util.List<AssetListEntry> findByG_TY(long groupId, int type,
		int start, int end);

	/**
	* Returns an ordered range of all the asset list entries where groupId = &#63; and type = &#63;.
	*
	* <p>
	* Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to {@link QueryUtil#ALL_POS} will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent and pagination is required (<code>start</code> and <code>end</code> are not {@link QueryUtil#ALL_POS}), then the query will include the default ORDER BY logic from {@link AssetListEntryModelImpl}. If both <code>orderByComparator</code> and pagination are absent, for performance reasons, the query will not have an ORDER BY clause and the returned result set will be sorted on by the primary key in an ascending order.
	* </p>
	*
	* @param groupId the group ID
	* @param type the type
	* @param start the lower bound of the range of asset list entries
	* @param end the upper bound of the range of asset list entries (not inclusive)
	* @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	* @return the ordered range of matching asset list entries
	*/
	public java.util.List<AssetListEntry> findByG_TY(long groupId, int type,
		int start, int end,
		com.liferay.portal.kernel.util.OrderByComparator<AssetListEntry> orderByComparator);

	/**
	* Returns an ordered range of all the asset list entries where groupId = &#63; and type = &#63;.
	*
	* <p>
	* Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to {@link QueryUtil#ALL_POS} will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent and pagination is required (<code>start</code> and <code>end</code> are not {@link QueryUtil#ALL_POS}), then the query will include the default ORDER BY logic from {@link AssetListEntryModelImpl}. If both <code>orderByComparator</code> and pagination are absent, for performance reasons, the query will not have an ORDER BY clause and the returned result set will be sorted on by the primary key in an ascending order.
	* </p>
	*
	* @param groupId the group ID
	* @param type the type
	* @param start the lower bound of the range of asset list entries
	* @param end the upper bound of the range of asset list entries (not inclusive)
	* @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	* @param retrieveFromCache whether to retrieve from the finder cache
	* @return the ordered range of matching asset list entries
	*/
	public java.util.List<AssetListEntry> findByG_TY(long groupId, int type,
		int start, int end,
		com.liferay.portal.kernel.util.OrderByComparator<AssetListEntry> orderByComparator,
		boolean retrieveFromCache);

	/**
	* Returns the first asset list entry in the ordered set where groupId = &#63; and type = &#63;.
	*
	* @param groupId the group ID
	* @param type the type
	* @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	* @return the first matching asset list entry
	* @throws NoSuchEntryException if a matching asset list entry could not be found
	*/
	public AssetListEntry findByG_TY_First(long groupId, int type,
		com.liferay.portal.kernel.util.OrderByComparator<AssetListEntry> orderByComparator)
		throws NoSuchEntryException;

	/**
	* Returns the first asset list entry in the ordered set where groupId = &#63; and type = &#63;.
	*
	* @param groupId the group ID
	* @param type the type
	* @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	* @return the first matching asset list entry, or <code>null</code> if a matching asset list entry could not be found
	*/
	public AssetListEntry fetchByG_TY_First(long groupId, int type,
		com.liferay.portal.kernel.util.OrderByComparator<AssetListEntry> orderByComparator);

	/**
	* Returns the last asset list entry in the ordered set where groupId = &#63; and type = &#63;.
	*
	* @param groupId the group ID
	* @param type the type
	* @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	* @return the last matching asset list entry
	* @throws NoSuchEntryException if a matching asset list entry could not be found
	*/
	public AssetListEntry findByG_TY_Last(long groupId, int type,
		com.liferay.portal.kernel.util.OrderByComparator<AssetListEntry> orderByComparator)
		throws NoSuchEntryException;

	/**
	* Returns the last asset list entry in the ordered set where groupId = &#63; and type = &#63;.
	*
	* @param groupId the group ID
	* @param type the type
	* @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	* @return the last matching asset list entry, or <code>null</code> if a matching asset list entry could not be found
	*/
	public AssetListEntry fetchByG_TY_Last(long groupId, int type,
		com.liferay.portal.kernel.util.OrderByComparator<AssetListEntry> orderByComparator);

	/**
	* Returns the asset list entries before and after the current asset list entry in the ordered set where groupId = &#63; and type = &#63;.
	*
	* @param assetListEntryId the primary key of the current asset list entry
	* @param groupId the group ID
	* @param type the type
	* @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	* @return the previous, current, and next asset list entry
	* @throws NoSuchEntryException if a asset list entry with the primary key could not be found
	*/
	public AssetListEntry[] findByG_TY_PrevAndNext(long assetListEntryId,
		long groupId, int type,
		com.liferay.portal.kernel.util.OrderByComparator<AssetListEntry> orderByComparator)
		throws NoSuchEntryException;

	/**
	* Removes all the asset list entries where groupId = &#63; and type = &#63; from the database.
	*
	* @param groupId the group ID
	* @param type the type
	*/
	public void removeByG_TY(long groupId, int type);

	/**
	* Returns the number of asset list entries where groupId = &#63; and type = &#63;.
	*
	* @param groupId the group ID
	* @param type the type
	* @return the number of matching asset list entries
	*/
	public int countByG_TY(long groupId, int type);

	/**
	* Caches the asset list entry in the entity cache if it is enabled.
	*
	* @param assetListEntry the asset list entry
	*/
	public void cacheResult(AssetListEntry assetListEntry);

	/**
	* Caches the asset list entries in the entity cache if it is enabled.
	*
	* @param assetListEntries the asset list entries
	*/
	public void cacheResult(java.util.List<AssetListEntry> assetListEntries);

	/**
	* Creates a new asset list entry with the primary key. Does not add the asset list entry to the database.
	*
	* @param assetListEntryId the primary key for the new asset list entry
	* @return the new asset list entry
	*/
	public AssetListEntry create(long assetListEntryId);

	/**
	* Removes the asset list entry with the primary key from the database. Also notifies the appropriate model listeners.
	*
	* @param assetListEntryId the primary key of the asset list entry
	* @return the asset list entry that was removed
	* @throws NoSuchEntryException if a asset list entry with the primary key could not be found
	*/
	public AssetListEntry remove(long assetListEntryId)
		throws NoSuchEntryException;

	public AssetListEntry updateImpl(AssetListEntry assetListEntry);

	/**
	* Returns the asset list entry with the primary key or throws a {@link NoSuchEntryException} if it could not be found.
	*
	* @param assetListEntryId the primary key of the asset list entry
	* @return the asset list entry
	* @throws NoSuchEntryException if a asset list entry with the primary key could not be found
	*/
	public AssetListEntry findByPrimaryKey(long assetListEntryId)
		throws NoSuchEntryException;

	/**
	* Returns the asset list entry with the primary key or returns <code>null</code> if it could not be found.
	*
	* @param assetListEntryId the primary key of the asset list entry
	* @return the asset list entry, or <code>null</code> if a asset list entry with the primary key could not be found
	*/
	public AssetListEntry fetchByPrimaryKey(long assetListEntryId);

	@Override
	public java.util.Map<java.io.Serializable, AssetListEntry> fetchByPrimaryKeys(
		java.util.Set<java.io.Serializable> primaryKeys);

	/**
	* Returns all the asset list entries.
	*
	* @return the asset list entries
	*/
	public java.util.List<AssetListEntry> findAll();

	/**
	* Returns a range of all the asset list entries.
	*
	* <p>
	* Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to {@link QueryUtil#ALL_POS} will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent and pagination is required (<code>start</code> and <code>end</code> are not {@link QueryUtil#ALL_POS}), then the query will include the default ORDER BY logic from {@link AssetListEntryModelImpl}. If both <code>orderByComparator</code> and pagination are absent, for performance reasons, the query will not have an ORDER BY clause and the returned result set will be sorted on by the primary key in an ascending order.
	* </p>
	*
	* @param start the lower bound of the range of asset list entries
	* @param end the upper bound of the range of asset list entries (not inclusive)
	* @return the range of asset list entries
	*/
	public java.util.List<AssetListEntry> findAll(int start, int end);

	/**
	* Returns an ordered range of all the asset list entries.
	*
	* <p>
	* Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to {@link QueryUtil#ALL_POS} will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent and pagination is required (<code>start</code> and <code>end</code> are not {@link QueryUtil#ALL_POS}), then the query will include the default ORDER BY logic from {@link AssetListEntryModelImpl}. If both <code>orderByComparator</code> and pagination are absent, for performance reasons, the query will not have an ORDER BY clause and the returned result set will be sorted on by the primary key in an ascending order.
	* </p>
	*
	* @param start the lower bound of the range of asset list entries
	* @param end the upper bound of the range of asset list entries (not inclusive)
	* @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	* @return the ordered range of asset list entries
	*/
	public java.util.List<AssetListEntry> findAll(int start, int end,
		com.liferay.portal.kernel.util.OrderByComparator<AssetListEntry> orderByComparator);

	/**
	* Returns an ordered range of all the asset list entries.
	*
	* <p>
	* Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to {@link QueryUtil#ALL_POS} will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent and pagination is required (<code>start</code> and <code>end</code> are not {@link QueryUtil#ALL_POS}), then the query will include the default ORDER BY logic from {@link AssetListEntryModelImpl}. If both <code>orderByComparator</code> and pagination are absent, for performance reasons, the query will not have an ORDER BY clause and the returned result set will be sorted on by the primary key in an ascending order.
	* </p>
	*
	* @param start the lower bound of the range of asset list entries
	* @param end the upper bound of the range of asset list entries (not inclusive)
	* @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	* @param retrieveFromCache whether to retrieve from the finder cache
	* @return the ordered range of asset list entries
	*/
	public java.util.List<AssetListEntry> findAll(int start, int end,
		com.liferay.portal.kernel.util.OrderByComparator<AssetListEntry> orderByComparator,
		boolean retrieveFromCache);

	/**
	* Removes all the asset list entries from the database.
	*/
	public void removeAll();

	/**
	* Returns the number of asset list entries.
	*
	* @return the number of asset list entries
	*/
	public int countAll();

	@Override
	public java.util.Set<String> getBadColumnNames();
}