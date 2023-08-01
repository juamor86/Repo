package es.juntadeandalucia.msspa.authentication.presentation.login.fragments.dni.login

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.graphics.Color
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import es.gob.jmulticard.ui.passwordcallback.CancelledOperationException
import es.gob.jmulticard.ui.passwordcallback.DialogUIHandler
import es.juntadeandalucia.msspa.authentication.R
import okhttp3.internal.notifyAll
import okhttp3.internal.wait

internal class DNIEPinDialog(context: Context, cachePIN: Boolean) :
    DialogUIHandler {
    private val activity: Activity

    /**
     * Flag que indica si se cachea el PIN.
     */
    private val cachePIN: Boolean

    /**
     * El password introducido. Si está activado el cacheo se reutilizará.
     */
    private var password: CharArray? = null
    override fun showConfirmDialog(message: String): Int {
        return doShowConfirmDialog(message)
    }

    fun doShowConfirmDialog(message: String?): Int {
        val dialog = AlertDialog.Builder(activity, android.R.style.ThemeOverlay_Material_Dialog)
        val instance = this
        val resultBuilder = StringBuilder()
        resultBuilder.append(message)
        synchronized(instance) {
            activity.runOnUiThread {
                try {
                    dialog.setTitle("Proceso de firma con el DNI electrónico")
                    dialog.setMessage(resultBuilder)
                    dialog.setPositiveButton(
                        R.string.lib_dialog_ok
                    ) { dialog, which ->
                        synchronized(instance) {
                            resultBuilder.delete(0, resultBuilder.length)
                            resultBuilder.append("0")
                            instance.notifyAll()
                        }
                    }
                    dialog.setNegativeButton(
                        R.string.lib_dialog_cancel
                    ) { dialog, which ->
                        synchronized(instance) {
                            resultBuilder.delete(0, resultBuilder.length)
                            resultBuilder.append("1")
                            instance.notifyAll()
                        }
                    }
                    dialog.setCancelable(false)
                    dialog.create().show()
                } catch (ex: CancelledOperationException) {
                    Log.e(
                        "DNIE",
                        "Excepción en diálogo de confirmación" + ex.message
                    )
                } catch (err: Error) {
                    Log.e(
                        "DNIE",
                        err.message?.let { "Error en diálogo de confirmación" + err.message } ?: "Error en diálogo de confirmación"
                    )
                }
            }
            return try {
                instance.wait()
                resultBuilder.toString().toInt()
            } catch (e: InterruptedException) {
                throw RuntimeException(e)
            } catch (ex: Exception) {
                throw CancelledOperationException()
            }
        }
    }

    private fun doShowPasswordDialog(retries: Int): CharArray {
        val dialog = AlertDialog.Builder(activity)
        val inflater = activity.layoutInflater
        val passwordBuilder = StringBuilder()
        val instance = this
        dialog.setMessage(getTriesMessage(retries))
        synchronized(instance) {
            activity.runOnUiThread {
                try {
                    val passwordView: View =
                        inflater.inflate(R.layout.lib_password_entry, null)
                    val passwordEdit =
                        passwordView.findViewById<View>(R.id.password_edit) as EditText
                    val passwordShow =
                        passwordView.findViewById<View>(R.id.checkBoxShow) as CheckBox
                    dialog.setPositiveButton(
                        R.string.lib_dialog_ok,
                        DialogInterface.OnClickListener { dialog, which ->

                            /**
                             * @param dialog El diálogo que genera el evento.
                             * @see DialogInterface.OnClickListener.onClick
                             */
                            /**
                             * @param dialog El diálogo que genera el evento.
                             * @see DialogInterface.OnClickListener.onClick
                             */
                            synchronized(instance) {
                                passwordBuilder.delete(0, passwordBuilder.length)
                                passwordBuilder.append(passwordEdit.text.toString())
                                instance.notifyAll()
                            }
                        })
                    dialog.setNegativeButton(
                        R.string.lib_dialog_cancel,
                        DialogInterface.OnClickListener { dialog, which ->

                            /**
                             * @param dialog El diálogo que genera el evento.
                             * @see DialogInterface.OnClickListener.onClick
                             */
                            /**
                             * @param dialog El diálogo que genera el evento.
                             * @see DialogInterface.OnClickListener.onClick
                             */
                            synchronized(instance) {
                                passwordBuilder.delete(0, passwordBuilder.length)
                                instance.notifyAll()
                            }
                        })
                    passwordShow.setOnCheckedChangeListener { buttonView, isChecked ->
                        if (isChecked) {
                            passwordEdit.transformationMethod =
                                HideReturnsTransformationMethod.getInstance()
                            passwordShow.text =
                                activity.getString(R.string.lib_psswd_dialog_show)
                        } else {
                            passwordEdit.transformationMethod =
                                PasswordTransformationMethod.getInstance()
                            passwordShow.text =
                                activity.getString(R.string.lib_psswd_dialog_hide)
                        }
                    }
                    dialog.setCancelable(false)
                    dialog.setView(passwordView)
                    val alert = dialog.create()
                    alert.show()
                    val nbutton: Button = alert.getButton(DialogInterface.BUTTON_NEGATIVE)
                    nbutton.setTextColor(Color.BLACK)
                    nbutton.setBackgroundColor(Color.TRANSPARENT)
                    val pbutton: Button = alert.getButton(DialogInterface.BUTTON_POSITIVE)
                    pbutton.setTextColor(activity.getColor(R.color.green_selected))
                    pbutton.setBackgroundColor(Color.TRANSPARENT)

                } catch (ex: Exception) {
                    Log.e(
                        "MyPasswordFragment",
                        "Excepción en diálogo de contraseña" + ex.message
                    )
                } catch (err: Error) {
                    Log.e(
                        "MyPasswordFragment",
                        "Error en diálogo de contraseña" + err.message
                    )
                }
            }
            return try {
                instance.wait()
                passwordBuilder.toString().toCharArray()
            } catch (e: InterruptedException) {
                throw RuntimeException(e)
            }
        }
    }

    override fun showPasswordDialog(retries: Int): CharArray {
        val returning: CharArray
        returning =
            if (retries < 0 && cachePIN && password != null && password!!.size > 0) password!!.clone() else doShowPasswordDialog(
                retries
            )
        if (cachePIN && returning != null && returning.size > 0) password = returning.clone()
        return returning
    }

    /**
     * Genera el mensaje de reintentos del diálogo de contraseña.
     *
     * @param retries El número de reintentos pendientes. Si es negativo, se considera que no se conocen los intentos.
     * @return El mensaje a mostrar.
     */
    private fun getTriesMessage(retries: Int): String {
        val text: String
        text = if (retries < 0) {
            activity.getString(R.string.lib_dni_password_msg)
        } else if (retries == 1) {
            activity.getString(R.string.lib_dni_password_msg_one_left)
        } else {
            "Introduzca PIN. Quedan $retries reintentos."
        }
        return text
    }

    init {

        // Guardamos el contexto para poder mostrar el diálogo
        activity = context as Activity
        this.cachePIN = cachePIN

        // Cuadro de diálogo para confirmación de firmas
        val alertDialogBuilder = AlertDialog.Builder(activity)
        alertDialogBuilder.setIcon(R.drawable.alert_dialog_icon)
    }
}
