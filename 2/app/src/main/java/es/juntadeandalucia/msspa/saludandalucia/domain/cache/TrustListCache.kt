package es.juntadeandalucia.msspa.saludandalucia.domain.cache

import ehn.techiop.hcert.kotlin.trust.TrustedCertificateV2

class TrustListCache(val trustList: Array<TrustedCertificateV2>, val creation: Long = 0)
