package es.juntadeandalucia.msspa.saludandalucia.data.persistence

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import es.juntadeandalucia.msspa.saludandalucia.data.entities.WalletData
import io.reactivex.Completable
import io.reactivex.Single

@Dao
interface CovidCertificateDao {

    /**
     * Insert a certificate in the database. If the certificate already exists, abort.
     * @param covidWallet the certificate to be inserted
     */
    @Insert(onConflict = OnConflictStrategy.ABORT)
    fun insert(covidWallet: WalletData): Completable

    /**
     * Insert a certificate in the database that exist.
     * @param covidWallet the certificate to be inserted
     */
    @Update(onConflict = OnConflictStrategy.REPLACE)
    fun update(covidWallet: WalletData): Completable

    /**
     *Get all certificates
     * @return the certificate list from the table.
     */
    @Query("SELECT * FROM Certificates")
    fun getAllCovidCertificates(): Single<List<WalletData>>

    /**
     * Find a certificate in database
     * @return true or false if the certificate exist or not
     */
    @Query("SELECT EXISTS (SELECT 1 FROM certificates WHERE id = :id)")
    fun findCert(id: String): Single<Boolean>

}