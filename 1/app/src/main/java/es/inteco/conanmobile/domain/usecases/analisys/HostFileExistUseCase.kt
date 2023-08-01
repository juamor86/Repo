package es.inteco.conanmobile.domain.usecases.analisys

import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import es.inteco.conanmobile.domain.entities.AnalysisResultEntity
import es.inteco.conanmobile.domain.entities.ModuleEntity
import es.inteco.conanmobile.domain.usecases.ExistLastAnalysisUseCase
import es.inteco.conanmobile.domain.usecases.GetFirstAnalysisLaunchedUseCase
import es.inteco.conanmobile.domain.usecases.GetLastAnalysisUseCase
import es.inteco.conanmobile.domain.usecases.SetFirstAnalysisLaunchedUseCase
import es.inteco.conanmobile.presentation.App
import timber.log.Timber
import java.io.BufferedWriter
import java.io.File
import java.io.FileWriter
import java.nio.file.Files
import java.nio.file.Paths
import java.util.*
import javax.inject.Inject

/**
 * Host file exist use case
 *
 * @constructor
 *
 * @param context
 * @param analysisItem
 * @param result
 */
class HostFileExistUseCase(
    context: Context, analysisItem: ModuleEntity, result: AnalysisResultEntity
) : BaseAnalysisUseCase(context, analysisItem, result) {
    init {
        App.baseComponent.inject(this)
    }

    @Inject
    lateinit var getLastAnalysisUseCase: GetLastAnalysisUseCase

    @Inject
    lateinit var getFirstAnalysisLaunchedUseCase: GetFirstAnalysisLaunchedUseCase

    @Inject
    lateinit var setFirstAnalysisLaunchedUseCase: SetFirstAnalysisLaunchedUseCase

    @Inject
    lateinit var existLastAnalysisUseCase: ExistLastAnalysisUseCase


    private val ETC = "etc/hosts"

    override fun analyse() {
        try {
            if (getFirstAnalysisLaunchedUseCase.execute()) {
                setFirstAnalysisLaunchedUseCase.execute(onComplete = {
                    checkHostFile()
                }, onError = {
                    Timber.e(it)
                })
            } else {
                if (existLastAnalysisUseCase.buildUseCase().blockingGet()) {
                    getLastAnalysisUseCase.buildUseCase().blockingGet().let {
                        result.device.hostFile = it.device.hostFile
                    }
                }

                checkHostFile()
            }
        } catch (e: Exception) {
            Timber.e(e)
        }
    }

    private fun checkHostFile() {
        if (File(ETC).exists()) {
            if (result.device.hostFile.isEmpty()) {
                result.device.isHostFileEdited = 0
                itemResult.notOk = false
                result.device.hostFile = File(ETC).readText()
            } else {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    if (hostIsEqual()) {
                        result.device.isHostFileEdited = 0
                        itemResult.notOk = false
                    } else {
                        result.device.isHostFileEdited = 1
                        itemResult.notOk = true
                    }
                } else {
                    if (hostIsEqual23()) {
                        result.device.isHostFileEdited = 0
                        itemResult.notOk = false
                    } else {
                        result.device.isHostFileEdited = 1
                        itemResult.notOk = true
                    }
                }
                result.device.hostFile = File(ETC).readText()
            }
        } else {
            if (result.device.hostFile.isNotEmpty()) {
                result.device.isHostFileEdited = 1
                itemResult.notOk = true
                result.device.hostFile = ""
            } else {
                result.device.isHostFileEdited = 1
                itemResult.notOk = true
                result.device.hostFile = File(ETC).readText()
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun hostIsEqual(): Boolean {
        try {
            val fileName = context.filesDir.path + "/host.csv"
            val secondFile = File(fileName)
            if (!secondFile.exists()) {
                secondFile.createNewFile()
            }
            val fileWriter = FileWriter(secondFile)
            val bufferedWriter = BufferedWriter(fileWriter)
            bufferedWriter.write(result.device.hostFile)
            bufferedWriter.close()

            if (Files.size(Paths.get(ETC)) != Files.size(secondFile.toPath())) {
                return false
            }
            val first = Files.readAllBytes(Paths.get(ETC))
            val second = Files.readAllBytes(secondFile.toPath())

            return Arrays.equals(first, second)
        } catch (e: Throwable) {
            Timber.e(e)
        }
        return false
    }

    private fun hostIsEqual23(): Boolean {
        try {
            val firstFile = File(ETC)
            val fileName = context.filesDir.path + "/host.csv"
            val secondFile = File(fileName)
            if (!secondFile.exists()) {
                secondFile.createNewFile()
            }
            val fileWriter = FileWriter(secondFile)
            val bufferedWriter = BufferedWriter(fileWriter)
            bufferedWriter.write(result.device.hostFile)
            bufferedWriter.close()

            val first = firstFile.readBytes()
            val second = secondFile.readBytes()

            return first.contentEquals(second)
        } catch (e: Throwable) {
            Timber.e(e)
        }
        return false
    }
}