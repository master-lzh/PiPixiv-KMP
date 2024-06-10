package com.mrl.pixiv.datasource.local.database

import androidx.room.Dao
import androidx.room.Database
import androidx.room.Delete
import androidx.room.Entity
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.RoomDatabase

@Database(entities = [DownloadEntity::class], version = 1)
abstract class DownloadDatabase : RoomDatabase() {
    abstract fun downloadDao(): DownloadDao
    companion object {
        const val DATABASE_NAME = "download_database"
    }
}

@Dao
interface DownloadDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDownload(downloadEntity: DownloadEntity)
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDownload(downloadEntity: List<DownloadEntity>)
    @Delete(entity = DownloadEntity::class)
    suspend fun deleteDownload(downloadEntity: DownloadEntity)
    @Query("SELECT * FROM DownloadEntity WHERE illustId = :illustId AND picIndex = :picIndex")
    suspend fun queryDownload(illustId: Long, picIndex: Int): DownloadEntity?
    @Query("SELECT * FROM DownloadEntity")
    suspend fun queryAllDownload(): List<DownloadEntity>
    @Delete(entity = DownloadEntity::class)
    suspend fun deleteDownload(deleteDownloadReq: DeleteDownloadReq)
    @Query("DELETE FROM DownloadEntity WHERE path = :path")
    suspend fun deleteDownload(path: String)
    @Query("DELETE FROM DownloadEntity")
    suspend fun deleteAllDownload()
}

@Entity(primaryKeys = ["illustId", "picIndex", "url"])
data class DownloadEntity(
    val illustId: Long,
    val picIndex: Int,
    val title: String,
    val url: String,
    // Android为文件路径，iOS为identifier
    val path: String,
    val createTime: Long,
)

data class DeleteDownloadReq(
    val illustId: Long,
    val picIndex: Int,
)