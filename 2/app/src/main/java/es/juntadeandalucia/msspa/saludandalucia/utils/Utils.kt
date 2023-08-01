package es.juntadeandalucia.msspa.saludandalucia.utils

import android.content.Context
import android.content.Intent
import android.content.res.Resources
import android.database.Cursor
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.net.Uri
import android.os.Build
import android.provider.CalendarContract
import android.provider.ContactsContract
import android.graphics.Matrix
import android.media.ExifInterface
import android.os.Environment
import android.provider.MediaStore
import android.provider.OpenableColumns
import android.util.Base64
import android.view.Window
import android.view.WindowManager
import android.util.Patterns
import com.google.android.gms.common.GoogleApiAvailability
import com.google.zxing.BarcodeFormat
import com.google.zxing.EncodeHintType
import com.google.zxing.common.BitMatrix
import com.google.zxing.qrcode.QRCodeWriter
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel
import com.huawei.hms.api.ConnectionResult
import com.huawei.hms.api.HuaweiApiAvailability
import es.juntadeandalucia.msspa.saludandalucia.BuildConfig
import es.juntadeandalucia.msspa.saludandalucia.R
import es.juntadeandalucia.msspa.saludandalucia.domain.entities.*
import es.juntadeandalucia.msspa.saludandalucia.domain.entities.AppointmentEntity
import es.juntadeandalucia.msspa.saludandalucia.domain.entities.WalletType
import es.juntadeandalucia.msspa.saludandalucia.domain.entities.dynamic.DynamicScreenEntity
import es.juntadeandalucia.msspa.saludandalucia.utils.Consts.Companion.SPLIT_URI
import es.juntadeandalucia.msspa.saludandalucia.utils.Consts.Companion.SPLIT_URI_DYNAMIC
import es.juntadeandalucia.msspa.saludandalucia.utils.Consts.Companion.STRING_FORMAT_SCREEN
import es.juntadeandalucia.msspa.saludandalucia.utils.Consts.Companion.STRING_FORMAT_SELECTED_OPTION
import es.juntadeandalucia.msspa.saludandalucia.utils.Consts.Companion.SPAIN_PREFIX
import timber.log.Timber
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.text.DecimalFormat
import java.text.Normalizer
import java.text.SimpleDateFormat
import java.util.*


class Utils {

    companion object {
        fun secureAgainstScreenshots(window : Window) {
          if(!BuildConfig.DEBUG && (BuildConfig.FLAVOR.contains(Consts.BUILD_TYPE_PRO))) {
                window.setFlags(
                    WindowManager.LayoutParams.FLAG_SECURE,
                    WindowManager.LayoutParams.FLAG_SECURE
                )
            }
        }

        fun checkIsHuaweiDevice() = Consts.HUAWEI == Build.MANUFACTURER.lowercase(Locale.ROOT)

        fun checkIsHMSActivated(context: Context) = HuaweiApiAvailability.getInstance()
            .isHuaweiMobileServicesAvailable(context) == ConnectionResult.SUCCESS

        fun checkIsGMSActivated(context: Context) = GoogleApiAvailability.getInstance()
            .isGooglePlayServicesAvailable(context) == ConnectionResult.SUCCESS

        fun isHuawei(context: Context): Boolean =
            if (!checkIsGMSActivated(context)) {
                checkIsHMSActivated(context)
            } else {
                false
            }

        fun checkAppInstalled(context: Context, packageName: String) =
            context.packageManager.getLaunchIntentForPackage(packageName) != null

        fun validateDni(dni: String): Boolean {
            var isValid = false
            val dniLetters = charArrayOf(
                'T',
                'R',
                'W',
                'A',
                'G',
                'M',
                'Y',
                'F',
                'P',
                'D',
                'X',
                'B',
                'N',
                'J',
                'Z',
                'S',
                'Q',
                'V',
                'H',
                'L',
                'C',
                'K',
                'E'
            )
            if (dni.length == 9 && dni[8].isLetter()) {
                var i = 0
                do {
                    val character = dni.codePointAt(i)
                    isValid = character in 48..57
                    i++
                } while (i < dni.length - 1 && isValid)
            }
            if (isValid) {
                val letter = dni[8].toUpperCase()
                val numbers = dni.substring(0, 8).toInt()
                val rest = numbers % 23
                isValid = letter == dniLetters[rest]
            }
            return isValid
        }

        fun validateNie(nie: String): Boolean {
            var nie = nie
            var isValid = false
            val dniLetters = charArrayOf(
                'T',
                'R',
                'W',
                'A',
                'G',
                'M',
                'Y',
                'F',
                'P',
                'D',
                'X',
                'B',
                'N',
                'J',
                'Z',
                'S',
                'Q',
                'V',
                'H',
                'L',
                'C',
                'K',
                'E'
            )
            if (nie.length == 9 &&
                nie[8].isLetter() &&
                (nie.substring(0, 1).toUpperCase() == "X" ||
                        nie.substring(0, 1).toUpperCase() == "Y" ||
                        nie.substring(0, 1).toUpperCase() == "Z")
            ) {
                var i = 1
                do {
                    val character = nie.codePointAt(i)
                    isValid = character in 48..57
                    i++
                } while (i < nie.length - 1 && isValid)
            }
            if (isValid && nie.substring(0, 1).toUpperCase() == "X") {
                nie = "0" + nie.substring(1, 9)
            } else if (isValid && nie.substring(0, 1).toUpperCase() == "Y") {
                nie = "1" + nie.substring(1, 9)
            } else if (isValid && nie.substring(0, 1).toUpperCase() == "Z") {
                nie = "2" + nie.substring(1, 9)
            }
            if (isValid) {
                val letter = nie[8].toUpperCase()
                val numbers = nie.substring(0, 8).toInt()
                val rest = numbers % 23
                isValid = letter == dniLetters[rest]
            }
            return isValid
        }

        fun getAddToCalendarIntent(appointment: AppointmentEntity): Intent {
            val formatter = SimpleDateFormat("dd/MM/yyyy HH:mm")
            val begin = formatter.parse(appointment.date)
            val end = begin.time + (15 * 60 * 1000)
            val intent = Intent(Intent.ACTION_INSERT)
                .setData(CalendarContract.Events.CONTENT_URI)
                .putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME, begin.time)
                .putExtra(CalendarContract.EXTRA_EVENT_END_TIME, end)
                .putExtra(CalendarContract.Events.TITLE, appointment.task)
                .putExtra(CalendarContract.Events.DESCRIPTION, appointment.topic)
                .putExtra(CalendarContract.Events.EVENT_LOCATION, appointment.center)
                .putExtra(
                    CalendarContract.Events.AVAILABILITY,
                    CalendarContract.Events.AVAILABILITY_BUSY
                )
            return intent
        }

        fun getPath(context: Context, uri: Uri): String {
            var result: String? = null
            val proj = arrayOf(MediaStore.Images.Media.DATA)
            val cursor: Cursor? = context.contentResolver.query(uri, proj, null, null, null)
            if (cursor != null) {
                if (cursor.moveToFirst()) {
                    val column_index: Int = cursor.getColumnIndexOrThrow(proj[0])
                    result = cursor.getString(column_index)
                }
                cursor.close()
            }
            if (result == null) {
                result = "Not found"
            }
            return result
        }

        fun rotateBitmapOrientation(photoFilePath: String): Bitmap {
            // Create and configure BitmapFactory
            val bounds = BitmapFactory.Options()
            val bm =  BitmapFactory.decodeFile(photoFilePath, bounds)
            // Read EXIF Data
            var exif: ExifInterface? = null
            try {
                exif = ExifInterface(photoFilePath)
            } catch (e: IOException) {
                e.printStackTrace()
            }
            val orientString: String? = exif?.getAttribute(ExifInterface.TAG_ORIENTATION)
            val orientation =
                orientString?.toInt() ?: ExifInterface.ORIENTATION_NORMAL
            var rotationAngle = 0.0F
            if (orientation == ExifInterface.ORIENTATION_ROTATE_90) rotationAngle = 90.0F
            if (orientation == ExifInterface.ORIENTATION_ROTATE_180) rotationAngle = 180.0F
            if (orientation == ExifInterface.ORIENTATION_ROTATE_270) rotationAngle = 270.0F
            // Rotate Bitmap
            val matrix = Matrix()
            matrix.setRotate(rotationAngle, bm.width.toFloat() / 2, bm.height.toFloat() / 2)
            // Return result
            return Bitmap.createBitmap(bm, 0, 0, bounds.outWidth, bounds.outHeight, matrix, true)
        }

        fun getBitmapFromBytearray(data: ByteArray): Bitmap =
            BitmapFactory.decodeByteArray(data, 0, data.size)

        fun compressPicture(bitmap: Bitmap): ByteArray {
            val baos = ByteArrayOutputStream()
            bitmap.compress(Bitmap.CompressFormat.JPEG, 60, baos)
            val byteArray = baos.toByteArray()
            baos.close()
            return byteArray
        }

        fun getFileName(context:Context,uri: Uri): String? {
            var result: String? = null
            if (uri.scheme == "content") {
                val cursor: Cursor? = context.contentResolver.query(uri, null, null, null, null)
                cursor?.use { cursor ->
                    if (cursor.moveToFirst()) {
                        result =
                            cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME))
                    }
                }
            }
            if (result == null) {
                result = uri.path
                val cut = result!!.lastIndexOf('/')
                if (cut != -1) {
                    result = result?.substring(cut + 1)
                }
            }
            return result
        }

        fun encodeBase64(byteArray: ByteArray): String =
            Base64.encodeToString(byteArray, Base64.DEFAULT)

        fun decodeBase64(codedString: String): ByteArray =
            Base64.decode(codedString, Base64.DEFAULT)

        fun getPhotoFileUri(fileName: String,context: Context): File {
            val mediaStorageDir =
                File(context.getExternalFilesDir(Environment.DIRECTORY_PICTURES)?.toURI())
            if (!mediaStorageDir.exists() && !mediaStorageDir.mkdirs()) {
                Timber.d( "failed to create directory")
            }
            return File(mediaStorageDir.path + File.separator + fileName)
        }

        fun getConvertBytesInMb(sizeinBytes: Int): String {
            val df = DecimalFormat("0.00")
            return df.format((sizeinBytes / 1000F) / 1000F )
        }


        fun generateQR(context: Context, source: String): Bitmap? {
            val size = context.resources.displayMetrics.widthPixels
            val result: BitMatrix = try {
                val hints = EnumMap<EncodeHintType, Any>(EncodeHintType::class.java)
                hints[EncodeHintType.MARGIN] = 0
                hints[EncodeHintType.ERROR_CORRECTION] = ErrorCorrectionLevel.L
                QRCodeWriter().encode(source, BarcodeFormat.QR_CODE, size, size, hints)
            } catch (e: Exception) {
                return null
            }
            val w = result.width
            val h = result.height
            val pixels = IntArray(w * h)
            for (y in 0 until h) {
                val offset = y * w
                for (x in 0 until w) {
                    pixels[offset + x] = if (result[x, y]) Color.BLACK else Color.WHITE
                }
            }
            val bitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888)
            bitmap.setPixels(pixels, 0, size, 0, 0, w, h)
            return bitmap
        }

        fun getPdf(context: Context, pdfString: String, pdfName: String): File? {
            var fos: FileOutputStream? = null

            val outputFile = File.createTempFile(pdfName, ".pdf", context.filesDir)
            try {
                fos = context.openFileOutput(outputFile.name, Context.MODE_APPEND)
                val decodedString: ByteArray = Base64.decode(pdfString, Base64.DEFAULT)
                fos.write(decodedString)
                fos.flush()
                fos.close()
                return outputFile
            } catch (e: Exception) {
                Timber.e(e)
            } finally {
                if (fos != null) {
                    fos = null
                }
            }
            return null
        }

        fun getSelectedOptionUrl(url: String): String {
            return url.substringAfter(STRING_FORMAT_SELECTED_OPTION)
        }

        fun getDynamicTarget(url: String): String {
            return if (url.contains("&")) {
                url.substring(
                    url.indexOf(SPLIT_URI) + SPLIT_URI.length,
                    url.indexOf("&")
                )
            } else {
                url.substringAfter(SPLIT_URI)
            }
        }

        fun getUrlWhenDynamicScreen(url: String): String {
            return url.substringAfter(STRING_FORMAT_SCREEN)
        }

        fun getDynamicScreenParam(targetString: String): String {
            targetString.let { url ->
                return when {
                    url.contains(STRING_FORMAT_SELECTED_OPTION) -> {
                        getSelectedOptionUrl(url)
                    }
                    url.contains(SPLIT_URI_DYNAMIC) -> {
                        getUrlWhenDynamicScreen(targetString)
                    }
                    else -> {
                        getDynamicTarget(url)
                    }
                }
            }
        }

        private val REGEX_UNACCENT = "\\p{InCombiningDiacriticalMarks}+".toRegex()

        fun unaccent(event: String): String {
            val temp =
                Normalizer.normalize(event, Normalizer.Form.NFD)
            return REGEX_UNACCENT.replace(
                temp,
                ""
            ).replace("ñ", "n", true)
                .replace(" ", "")
        }

        fun getCovidCertificateCode(typeCertificate: String): Int =
            when (typeCertificate) {
                Consts.TYPE_VACCINATION -> 1
                Consts.TYPE_TEST -> 2
                Consts.TYPE_RECOVERY -> 3
                else -> 4
            }

        fun getCurrentCertLogo(certType: WalletType, icons: DynamicScreenEntity): String {
            for (item in icons.children) {
                if (item.navigation.target == certType.type) return item.icon.source
            }
            // TODO Add a placeholder url
            return ""
        }

        @JvmStatic
        val EMAIL_REGEX = "(?:[a-z0-9!#\$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#\$%&'*+/=?^_`{|}~-]+)*|\"(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21\\x23-\\x5b\\x5d-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])*\")@(?:(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?|\\[(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?|[a-z0-9-]*[a-z0-9]:(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21-\\x5a\\x53-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])+)\\])";
        fun isEmailValid(email: String): Boolean {
            return EMAIL_REGEX.toRegex().matches(email);
        }

        fun getUniqueAdviceId() = UUID.randomUUID().toString()

        fun getTopicCode(criteria: String): String {
            val splittedCriteria: List<String> = criteria.split("topic.code=")
            return if (splittedCriteria.isNotEmpty()) {
                splittedCriteria[1]
            } else {
                ""
            }
        }

        fun getAdviceFromTypeCriteria(
            idCategory: String,
            advices: List<AdviceEntity>
        ): AdviceEntity? {
            advices.forEach { advice ->
                advice.entry?.let { entries ->
                    for (entry in entries) {
                        val entryTypeId =
                            entry.extension.find { it.url == "evento" }?.valueReference?.id ?: ""
                        if (entryTypeId == idCategory) {
                            return advice
                        }
                    }
                }
            }
            return null
        }

        val PHONE_CLEANER_REGEX = "[()-]"
        fun phoneFormatted(phone: String, needPrefix: Boolean = true): String{
            var phoneFormatted = phone.trim().replace(" ","").replace(SPAIN_PREFIX,"")
            phoneFormatted = PHONE_CLEANER_REGEX.toRegex().replace(phoneFormatted, "")
            return if(needPrefix) SPAIN_PREFIX + phoneFormatted else phoneFormatted
        }

        fun checkPhoneIsCorrect(phone: String): Boolean {
            return when (phone.length) {
                9 -> try {
                    val numberPhone = phone.toInt()
                    true
                } catch (e: Exception) {
                    false
                }
                12 -> try {
                    val auxPhone = phone.replace("+", "00")
                    val numberPhone = auxPhone.toDouble()
                    return auxPhone.length == 13
                } catch (e: Exception) {
                    false
                }
                else -> false
            }
        }

        fun getContactByPhoneNumber(context: Context, phoneNumber: String?): String? {
            context.contentResolver?.let { contentResolver ->
                val uri = Uri.withAppendedPath(ContactsContract.PhoneLookup.CONTENT_FILTER_URI, Uri.encode(phoneNumber))
                val projection = arrayOf(ContactsContract.PhoneLookup.DISPLAY_NAME, ContactsContract.PhoneLookup.NORMALIZED_NUMBER)

                var cursor: Cursor? = null
                var name: String? = null
                var nPhoneNumber = phoneNumber
                try {
                    cursor = contentResolver.query(uri, projection, null, null, null)
                    if (cursor == null || cursor.getCount() == 0) {
                        return phoneNumber;
                    }
                    cursor.moveToFirst()
                    nPhoneNumber = cursor.getString(cursor.getColumnIndex(ContactsContract.PhoneLookup.NORMALIZED_NUMBER))
                    name = cursor.getString(cursor.getColumnIndex(ContactsContract.PhoneLookup.DISPLAY_NAME))
                } catch (e: Exception) {
                    Timber.e("Error getting contact: ${e.message}")
                } finally {
                    cursor?.close()
                }
                return name ?: nPhoneNumber
            }
            return phoneNumber
        }

        fun fillNameContactAdvices(context: Context, advices:List<AdviceEntity>):List<AdviceEntity> {
            val adviceList: MutableList<AdviceEntity> = mutableListOf()
            advices.forEach {
                getContactByPhoneNumber(context, it.dataView?.sharedBy).let { name ->
                    it.dataView?.sharedBy = name
                }
                adviceList.add(it)
            }
            return adviceList
        }

        fun sortedByAdviceCatalogList(
            adviceCatalogList: List<AdviceCatalogTypeEntity>,
            advices: List<AdviceEntity>,
            isOwner: Boolean
        ): List<AdviceEntity> {
            val advicesSorted = mutableListOf<AdviceEntity>()
            adviceCatalogList.forEach { typeCatalog ->
                advices.forEach { advice ->
                    advice.entry?.forEach {
                        if (it.extension[0].valueReference.id == typeCatalog.id && it.isOwner == isOwner) {
                            advicesSorted.add(advice)
                        }
                    }
                }
            }
            return advicesSorted
        }

        fun getLabelContact(resources: Resources, item: ContactAdviceEntity): String =
            when (item.type) {
                ContactsContract.CommonDataKinds.Phone.TYPE_HOME -> {
                    resources.getString(R.string.label_type_contact_home)
                }
                ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE -> {
                    resources.getString(R.string.label_type_contact_mobile)
                }
                ContactsContract.CommonDataKinds.Phone.TYPE_WORK -> {
                    resources.getString(R.string.label_type_contact_work)
                }
                ContactsContract.CommonDataKinds.Phone.TYPE_FAX_WORK -> {
                    resources.getString(R.string.label_type_contact_fax_work)
                }
                ContactsContract.CommonDataKinds.Phone.TYPE_FAX_HOME -> {
                    resources.getString(R.string.label_type_contact_fax_home)
                }
                ContactsContract.CommonDataKinds.Phone.TYPE_PAGER -> {
                    resources.getString(R.string.label_type_contact_pager)
                }
                ContactsContract.CommonDataKinds.Phone.TYPE_OTHER -> {
                    resources.getString(R.string.label_type_contact_other)
                }
                ContactsContract.CommonDataKinds.Phone.TYPE_MAIN -> {
                    resources.getString(R.string.label_type_contact_main)
                }
                ContactsContract.CommonDataKinds.BaseTypes.TYPE_CUSTOM -> {
                    if(item.label.isEmpty()){
                        resources.getString(R.string.label_type_contact_other)
                    }else{
                        item.label
                    }
                }
                else -> ""
            }

        fun isValidUrl(url:String): Boolean {
            return Patterns.WEB_URL.matcher(url).matches()
        }

    }
}
