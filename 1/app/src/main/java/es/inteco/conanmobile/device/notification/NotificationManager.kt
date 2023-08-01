package es.inteco.conanmobile.device.notification

/**
 * Notification manager
 *
 * @constructor Create empty Notification manager
 */
interface NotificationManager {
    /**
     * Send notification
     *
     * @param message
     */
    fun sendNotification(message: String)
}