## Plannex

Plannex es una solución móvil avanzada para la gestión y control de eventos, diseñada bajo estándares de ingeniería de software de alto nivel. La aplicación maximiza el potencial del hardware del dispositivo para ofrecer una experiencia fluida, segura y resiliente en cualquier entorno de conectividad.

## Características Principales

- Offline-First (Caché de Alto Rendimiento): Gracias a la implementación de Room, la app garantiza el acceso a los 3 eventos más próximos cronológicamente incluso sin conexión a internet.
- Automatización vía QR: Registro instantáneo de eventos mediante el hardware de la Cámara, utilizando ML Kit para procesar estructuras JSON.
- Asistencia Geolocalizada: Integración con GPS y Google Maps SDK para el cálculo de distancias en tiempo real y confirmación automática de llegada.
- Seguridad Biométrica: Acceso resguardado mediante el hardware de huella dactilar para proteger las funciones administrativas.
- Sincronización Inteligente: Uso de WorkManager para actualizaciones automáticas nocturnas (12 AM - 5 AM) con gestión eficiente de batería.
- MaaS (Push Notifications): Integración con Firebase Cloud Messaging para alertas y actualizaciones en tiempo real.

## Stack Tecnológico

- Lenguaje: Kotlin 2.0.21
- Interfaz de Usuario: Jetpack Compose con Material Theme 3.0.
- Arquitectura: Clean Architecture + MVVM.
- Inyección de Dependencias: Hilt (Dagger) automatizado.
- Persistencia: Room Database.
- Networking: Retrofit 2 + Gson.
- Procesamiento en segundo plano: WorkManager.
- Hardware: Cámara (CameraX), GPS (Play Services Location), Biometría, Motor Háptico.

## Arquitectura Limpia

El proyecto está dividido en capas de responsabilidad para garantizar la escalabilidad:
1. Data: Implementaciones de repositorios, DAOs de Room y servicios de red.
2. Domain: Entidades de negocio, interfaces de repositorios y Casos de Uso (Use Cases).
3. Presentation: ViewModels con gestión de estado mediante StateFlow y Vistas reactivas en Compose.

## Configuración del Proyecto

Para ejecutar este proyecto localmente, asegúrate de:
1. Incluir el archivo google-services.json en la carpeta /app.
2. Configurar una API Key válida de Google Maps en el AndroidManifest.xml.
3. Utilizar Android Studio Ladybug o superior con soporte para Java 11/17.

## Capturas de Pantalla y Evidencia Técnica

Capturas de Pantalla y Evidencia Técnica

Para una visualización óptima, las capturas se presentan escaladas a un ancho de 250px:

| 1. Autenticación Biométrica | 2. Persistencia y Modo Offline |
| :---: | :---: |
| <img src="https://github.com/user-attachments/assets/61c666ba-c0ee-4c3a-ae9f-618b113ecb66" width="250"> | <img src="https://github.com/user-attachments/assets/a2f992c5-b485-4879-a946-0559cefd2ac7" width="250"> |
| *Interfaz de Acceso Seguro* | *Caché de Room (Offline)* |

| 3. Scanner QR y ML Kit | 4. Geolocalización y Mapas |
| :---: | :---: |
| <img src="https://github.com/user-attachments/assets/3ec8cd55-bd81-4895-ab7c-c964573f3120" width="250"> | <img src="https://github.com/user-attachments/assets/fd8f51ee-82a1-4a3e-a496-d61fdc405bf3" width="250"> |
| *Registro Automatizado* | *Monitoreo GPS en tiempo real* |
