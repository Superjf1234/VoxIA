# VoxAI - Diseño de Experiencia e Interfaz de Usuario

## 1. Paleta de Colores Corporativa

### Colores Principales
- **Azul Principal**: #2196F3 (Material Blue)
- **Amarillo Acento**: #FFC107 (Material Amber)
- **Gris Neutral**: #757575 (Material Grey)

### Colores Secundarios
- **Azul Oscuro**: #1976D2
- **Amarillo Claro**: #FFECB3
- **Gris Claro**: #EEEEEE
- **Blanco**: #FFFFFF
- **Negro**: #212121

## 2. Flujos de Usuario

### 2.1 Registro e Inicio de Sesión
1. Pantalla de bienvenida
2. Opción de registro con email/Google
3. Formulario de registro
4. Verificación de email
5. Pantalla de inicio de sesión
6. Recuperación de contraseña

### 2.2 Grabación de Audio
1. Pantalla principal con botón de grabación
2. Indicador de nivel de audio
3. Controles de grabación (Pausar/Reanudar)
4. Vista previa de la grabación
5. Opciones de guardar/descartar

### 2.3 Proceso de IA
1. Indicador de progreso
2. Estado de transcripción
3. Estado de análisis
4. Notificación de completado

### 2.4 Visualización de Resultados
1. Vista de resumen ejecutivo
2. Lista de puntos clave
3. Transcripción completa
4. Opciones de exportación

## 3. Wireframes

### 3.1 Pantallas Principales

#### Pantalla de Inicio
```
+------------------------+
|     VoxAI Logo        |
|                       |
|  [Botón Grabación]    |
|                       |
|  Historial Reciente   |
|  - Grabación 1        |
|  - Grabación 2        |
|                       |
|  [Menú Inferior]      |
+------------------------+
```

#### Pantalla de Grabación
```
+------------------------+
|  [Volver]  Grabando   |
|                       |
|  [Visualizador Audio] |
|                       |
|  [Pausar/Reanudar]    |
|                       |
|  [Detener]            |
+------------------------+
```

#### Pantalla de Resultados
```
+------------------------+
|  [Volver]  Resumen    |
|                       |
|  Puntos Clave:        |
|  • Punto 1            |
|  • Punto 2            |
|                       |
|  [Ver Transcripción]  |
|  [Compartir]          |
+------------------------+
```

## 4. Prototipos Interactivos

### 4.1 Navegación Principal
- Bottom Navigation Bar con 4 secciones:
  1. Inicio
  2. Grabaciones
  3. Favoritos
  4. Perfil

### 4.2 Interacciones Clave
- Gestos de deslizamiento para navegación
- Toque largo para opciones adicionales
- Pull-to-refresh para actualizar contenido
- Swipe para eliminar grabaciones

## 5. Elementos de UI

### 5.1 Tipografía
- **Títulos**: Roboto Bold
- **Subtítulos**: Roboto Medium
- **Cuerpo**: Roboto Regular
- **Botones**: Roboto Medium

### 5.2 Componentes
- Botones flotantes de acción (FAB)
- Tarjetas con elevación
- Barras de progreso circulares
- Indicadores de estado
- Menús desplegables
- Diálogos de confirmación

### 5.3 Iconografía
- Set de iconos Material Design
- Iconos personalizados para:
  - Grabación
  - Transcripción
  - Resumen
  - Compartir
  - Favoritos

## 6. Consideraciones de Accesibilidad
- Soporte para TalkBack
- Contraste de colores adecuado
- Tamaños de texto ajustables
- Gestos alternativos
- Etiquetas descriptivas 