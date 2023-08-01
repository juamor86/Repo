package es.juntadeandalucia.msspa.saludandalucia.data.persistence

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import es.juntadeandalucia.msspa.saludandalucia.data.entities.NotificationData
import io.reactivex.Completable
import io.reactivex.Flowable

@Dao
interface NotificationDao {

    /**
     * Get all notification
     * @return the notification list from the table.
     */
    @Query("SELECT * FROM Notifications ORDER BY date DESC")
    fun getAllNotifications(): Flowable<List<NotificationData>>

    /**
     * Insert a notification in the database. If the notification already exists, replace it.
     * @param notification the notification to be inserted.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertOrUpdate(notification: NotificationData): Completable

    /**
     * Update a notification in the database.
     * @param notification the notification to be inserted.
     */
    @Update
    fun updateNotification(notification: NotificationData): Completable

    /**
     * Delete a notification in the database.
     * @param notification the notification to be deleted.
     */
    @Delete
    fun deleteNotification(notification: NotificationData): Completable

    /**
     * Delete all notifications.
     */
    @Query("DELETE FROM Notifications")
    fun deleteAllNotifications(): Completable

    /**
     * Get all notification
     * @return the notification list from the table.
     */
    @Query("SELECT COUNT(*) FROM Notifications WHERE readed = 0 ")
    fun getNotReadedCount(): Flowable<Int>
}
