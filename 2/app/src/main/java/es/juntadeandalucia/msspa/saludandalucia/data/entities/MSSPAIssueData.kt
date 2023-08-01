package es.juntadeandalucia.msspa.saludandalucia.data.entities

data class MSSPAIssueData(val severity: String, val code: String, val details: MSSPATextData, val diagnostics: String)
