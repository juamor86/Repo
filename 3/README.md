# mSSPA Componente de Autenticación [![Platforma](https://img.shields.io/badge/Platforma-Android-green)](https://www.android.com)

# Introducción


Ante la necesidad de autenticar a un usuario en cada aplicación móvil
del ecosistema mSSPA, se implementa una web en el ámbito Backend que
será publicada al exterior mediante CA. Esta web ofrecerá al usuario la
posibilidad de logarse mediante diferentes métodos, siendo configurables
en concreto para cada aplicación móvil.

La web implementa los mecanismos pertinentes de cada método, en
conjunción con cierta lógica dentro de la aplicación móvil, que precisa
un desarrollo de integración. Como resultado final se obtiene un **token
de acceso** que la app usará en todas sus comunicaciones. Para no
repetir la implementación de esta integración para cada aplicación
móvil, se decide implementar este componente mobile en ambas plataformas
(Android e iOS) que podrá ser reutilizable en todas las apps del
ecosistema mSSPA.

## 1. Uso del componente

### Gradle

Añade la siguiente dependencia en tu fichero `build.gradle`.

```gradle
implementation 'es.juntadeandalucia.msspa:authentication:1.0'
```


## 2. Uso

Junto al componente `msspa-authentication-library`, se ha implementado
una app de prueba `mSSPA Authentication-App` donde se detalla cómo
configurar y usar el componente. Básicamente es registrar una Activity
durante el método `onCreate` mediante el `MsspaAuthenticationManager`,
el cual recibe la Activity invocadora y la configuración del componente.

```kotlin
override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    ...
    val config = MsspaAuthenticationConfig(
        MsspaAuthenticationConfig.Environment.PREPRODUCTION,
        "msspa.app.102",
        "l7460a9e8f1e594654ac37ac53fd2ee8e2",
        "2.1.0"
  )
    val manager = MsspaAuthenticationManager(
        activity = this, config = config
    ).apply { register() }
```

Una vez hecho el registro, se lanzará el componente con los callbacks
como parámetros de entrada:

```kotlin
manager.launch(onAuthenticationSuccess = { result: MsspaAuthenticationResult ->
...
}, onAuthenticationError = {resultError: MsspaAuthenticationError->
 ...})
```

Mediante los callbacks se reciben los objetos de salida, según el
proceso de autenticación haya sido un éxito o hubiera algún error.

## Objetos de salida

#### Éxito: `onAuthenticationSuccess(result: MsspaAuthenticationResult)`
    val tokenType: String // Tipo del token
    val accessToken: String // Token de acceso
    val expiresIn: Int? // Expiración del token en segundos

#### Error: `onAuthenticationError(resultError: MsspaAuthenticationError)`
    val error: ERRORS // LOADING_WEB: Error al cargar la web de autenticación, WRONG_CONFIG: Error con la configuración del componente, NETWORK: Error de comunicación
    val description: String // Descripción del error.




# Librerías requeridas

Para usar este componente es necesario usar la nueva API de Activity
Result:

```gradle
implementation 'androidx.activity:activity:1.2.0-beta01'
implementation 'androidx.activity:activity-ktx:1.2.0-beta01'
implementation 'androidx.fragment:fragment:1.3.0-beta01'
```

---


