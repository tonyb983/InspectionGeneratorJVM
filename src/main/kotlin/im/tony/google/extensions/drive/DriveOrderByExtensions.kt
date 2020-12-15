package im.tony.google.extensions.drive

import com.google.api.services.drive.Drive

public enum class DriveOrderBy {
  CreatedTime,
  Folder,
  ModifiedByMeTime,
  ModifiedTime,
  Name,
  NameNatural,
  QuotaBytesUsed,
  Recency,
  SharedWithMeTime,
  Starred,
  ViewedByMeTime;

  public override fun toString(): String = when (this) {
    CreatedTime -> "createdTime${if (descending) " desc" else ""}"
    Folder -> "folder${if (descending) " desc" else ""}"
    ModifiedByMeTime -> "modifiedByMeTime${if (descending) " desc" else ""}"
    ModifiedTime -> "modifiedTime${if (descending) " desc" else ""}"
    Name -> "name${if (descending) " desc" else ""}"
    NameNatural -> "name_natural${if (descending) " desc" else ""}"
    QuotaBytesUsed -> "quotaBytesUsed${if (descending) " desc" else ""}"
    Recency -> "recency${if (descending) " desc" else ""}"
    SharedWithMeTime -> "sharedWithMeTime${if (descending) " desc" else ""}"
    Starred -> "starred${if (descending) " desc" else ""}"
    ViewedByMeTime -> "viewedByMeTime${if (descending) " desc" else ""}"
  }

  public fun asDesc(): DriveOrderBy = this.apply { descending = true }

  private var descending: Boolean = false
}

/**
 * Sets the order by which the result of this file list request will be sorted.
 */
public fun Drive.Files.List.setOrderBy(vararg keys: DriveOrderBy): Drive.Files.List = this.setOrderBy(keys.joinToString(","))
