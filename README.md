# GoogleMapsPrac

Aplicación Android con **Google Maps** para buscar **lugares turísticos de Quevedo** por ubicación, radio y filtros de categoría/subcategoría.

**Estudiante:** John Silva — `jsilvat2@uteq.edu.ec`  
**Asignatura:** Aplicaciones Móviles — 6to semestre  
**Docente:** Cristian Zambrano  
**Universidad Técnica Estatal de Quevedo**

## Descripción

La pantalla principal (`MainActivity2`) muestra un mapa en vista satélite con:

- **Latitud y longitud** actualizadas al mover el mapa
- **Slider de radio** en metros (círculo rojo sobre el mapa)
- **Spinners** de Categoría y Subcategoría
- **Marcadores** de lugares turísticos obtenidos por **Volley**
- Solo se muestran lugares **dentro del círculo**

`MainActivity` es la práctica previa del sílabo (polilínea y marcador UTEQ).

## API utilizada

Base: `http://35.153.103.86/turismo10022025/`

| Endpoint | Uso |
|----------|-----|
| `categoria/getlistadoCB` | Llenar spinner de categorías |
| `subcategoria/getlistadoCB/{idCategoria}` | Llenar spinner de subcategorías |
| `lugar_turistico/json_getlistadoMapa` | Obtener lugares en el mapa |

### Parámetros de búsqueda de lugares

| Parámetro | Descripción |
|-----------|-------------|
| `lat` | Latitud del centro del mapa |
| `lng` | Longitud del centro del mapa |
| `radio` | Valor del slider (1–5) |
| `idc` | ID de categoría (opcional) |
| `idsc` | ID de subcategoría (opcional) |

Ejemplo:

```
.../lugar_turistico/json_getlistadoMapa?lat=-1.0124&lng=-79.4692&radio=1&idc=2&idsc=10
```

Respuesta JSON (`data`): `id`, `nombre`, `lat`, `lng`, `categoria`, `subcategoria`, `categoria_id`, `subcategoria_id`, `url`, `logo`.

## Tecnologías

- Java
- Google Maps SDK (`play-services-maps`)
- Volley (peticiones HTTP)
- Material Slider y Spinner
- Android SDK 29–36

## Estructura del proyecto

```
app/src/main/java/com/uteq/software/googlemaps/
├── MainActivity2.java     # Mapa turístico, Volley, filtros, círculo
└── MainActivity.java      # Práctica UTEQ: polilínea y marcador

app/src/main/res/layout/
├── activity_main2.xml     # Interfaz del mapa turístico
└── activity_main.xml      # Layout práctica UTEQ
```

## Requisitos

- Android Studio
- JDK 11+
- Emulador o dispositivo con **minSdk 29**
- Conexión a internet
- API Key de Google Maps configurada en `AndroidManifest.xml`

## Ejecución

1. Abrir el proyecto en Android Studio.
2. Verificar la **API Key** de Google Maps en `AndroidManifest.xml`.
3. Sincronizar Gradle y dar **Run**.
4. La app abre en `MainActivity2` (lugares turísticos).

## Compilar

```bash
./gradlew assembleDebug
```

APK de debug: `app/build/outputs/apk/debug/app-debug.apk`

## Rúbrica (10 pts)

| Criterio | Pts |
|----------|-----|
| Interfaz del mapa: círculo, slider y mapa | 3 |
| Lista de categorías y subcategorías | 2 |
| Obtener y mostrar lugares turísticos | 3 |
| Aplicar filtros a la búsqueda | 2 |

## Informe

Documento de entrega: `Informe_Mapas_Lugares_Turisticos_Quevedo.docx`  
Regenerar con: `python generar_informe.py`
