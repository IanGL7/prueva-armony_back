# ARmony Boot Backend (Render Ready)

## Despliegue rápido en Render (Docker)
1. **Elimina** cualquier clave en `src/main/resources/application.properties`. Ya viene preparado para usar `OPENAI_API_KEY` desde el entorno.
2. Crea el repo en GitHub con estos archivos (incluye `Dockerfile` y `render.yaml`).
3. En Render:
   - New → **Blueprint** → selecciona tu repo.
   - Nombre del servicio: `armony-backend` (o el que quieras).
   - Plan: **Free**.
   - Health Check Path: `/health`.
   - Variables de entorno:
     - `OPENAI_API_KEY`: tu clave de OpenAI (no la publiques).
4. Deploy.

## Probar
```bash
curl https://TU-SERVICIO.onrender.com/health
curl -X POST https://TU-SERVICIO.onrender.com/chat   -H "Content-Type: application/json"   -d '{"prompt":"me siento estresado"}'
```

## Notas
- Puerto: Render inyecta `$PORT`, ya configurado en `application.properties` y `Dockerfile`.
- Si usas el plan Free, el servicio puede “dormir”. Usa `/health` para “despertar”.
