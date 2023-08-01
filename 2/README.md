![Icono](./Icon.png)

# SaludAndalucía Android
---

[![Platforma](https://img.shields.io/badge/Platforma-Android-green)](https://www.android.com)

## Generación de versiones
---
La aplicación hace uso de **Gradle** para la compilación y generación de versiones.

Para generar versiones de la aplicación de SaludAndalucía podremos hacerlo a través de AndroidStudio o manualmente. Para hacerlo manualmente hay que tener en cuenta los distintos tipos de compilaciones existentes:
* **debug**: Será la compilación a generar para versiones de pruebas.
* **release**: Compilación para las versiones generadas para la subida al Play Store. Con esta compilación nos aseguramos que la versión generada irá firmada con el certificado válido correspondiente.

Además, existen los siguientes `flavors`:

* **pre**
* **pro**

Así, para generar versiones manualmente a través de **Gradle** podremos ejecutar los siguientes comandos:
* `./gradlew assemblePreDebug`: Generará una versión de pruebas
* `./gradlew assembleProDebug`: Generará una versión de pruebas
* `./gradlew assemblePreRelease`: Generará una versión para la subida al Play Store
* `./gradlew assembleProRelease`: Generará una versión para la subida al Play Store

El directorio de salida en donde se alojará la versión generada, ya sea a través dede Android Studio como manualmente, será `./app/build/outputs/apk/{flavor}/{buildType}`, siendo `{flavor}` el modo elegido entre `pre` o `pro` y `{buildType}` el tipo de compilación elegida entre `debug` o `release`.

## *Release 3.4.4*
Descarga de documentos dentro de un webView

Revisión de eventos de firebase

Invalidar token

Securización de la pantalla para capturas y grabación de video (solo PRE) -> Fue publicada en la release 3.4.0

Actualización forzosa

Desactivar sms receiver

Alerts con botón aceptar a la derecha y el otro botón a la izquierda

Actualización forzosa para Huawei

Fix:Diálogo bienvenida se está mostrando dos veces

Fix:No se carga las iniciales sobre del icono del usuario mientras te encuentras logado

Fix:Pantalla de novedades se superpone sobre el dialog de actualización forzosa

Componente de autenticación v3.0.12
- Implementación del invalidate token
- Eventos firebase
- Añdir hint text color sobre los combo box


## *Release 3.4.5*

Cambio de la url de la web de autenticación para producción -> https://sspa.lajunta.es/qrmovil

Fix: La pantalla de novedades se crashea cuando se vuelve a mostrar el dialog de novedades justo despues de salir de la app

Fix: El evento de descarga en el webview, justo cuando se abre el fichero descargado se envia el evento y crashea, cuando vuelves a la app se inicia de 0

Fix: Título de la pantalla de novedades

Fix: La pantalla de novedades tras marcar el check de no volver a mostrar si se produce un cambio sobre el campo un last update no se vuelve a mostrar la pantalla de novedades

Componente de autenticación v3.0.13
-No realizar la llamada authorize con legacy clientId sobre el componente de autenticación

## *Release 3.4.5.3 - 3.4.5.4*

- Fix: Añadido lambda para la finalización de los eventos y un single para obtener la finalización de la descarga de archivos sobre el webview de click salud
- Nuevo flavor para HMS

## *Release 3.5.0*

- Encuestas Genérica
- Cuestionarios pacientes crónicos
- Datos personales y almacen de certificados recordar que el usuario tenga activo algun mecanismo de seguridad
- Me Gusta
- Fix: Mostrar gráfico de mediciones
- Fix: Registro de notificaciones Huawei
- Fix: Control del error del Webview de CLick Salud
- Fix: Loading no se oculta cuando volvemos rápidamente a la home