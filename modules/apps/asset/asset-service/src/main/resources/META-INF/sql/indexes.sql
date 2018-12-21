create index IX_82AB9FB8 on AssetEntryUsage (assetEntryId, classNameId);
create unique index IX_186CA30B on AssetEntryUsage (classNameId, classPK, portletId[$COLUMN_LENGTH:75$]);
create index IX_2138D9F9 on AssetEntryUsage (uuid_[$COLUMN_LENGTH:75$], companyId);
create unique index IX_D0F18ABB on AssetEntryUsage (uuid_[$COLUMN_LENGTH:75$], groupId);