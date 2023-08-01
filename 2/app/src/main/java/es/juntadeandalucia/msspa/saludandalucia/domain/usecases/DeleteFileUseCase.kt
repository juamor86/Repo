package es.juntadeandalucia.msspa.saludandalucia.domain.usecases

import es.juntadeandalucia.msspa.saludandalucia.domain.base.CompletableUseCase
import es.juntadeandalucia.msspa.saludandalucia.utils.FileUtils
import io.reactivex.Completable
import java.io.File

class DeleteFileUseCase() :
    CompletableUseCase() {

    private lateinit var fileToDelete: File

    fun param(file: File) = this.apply {
        this.fileToDelete = file
    }

    override fun buildUseCase(): Completable =
        Completable.fromAction(
            FileUtils.deleteFile(fileToDelete)
        )
}
