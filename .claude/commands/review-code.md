# Review Code Command

Analiza todo el código Java del proyecto y genera un reporte de mejoras.

## Pasos

1. Escanear todos los archivos .java en src/main/java/com/bbva/gui
2. Aplicar el skill `java-code-review` 
3. Detectar:
   - Anti-patterns
   - Performance issues
   - Security vulnerabilities
   - Code smells
   - Violaciones de best practices
4. Generar reporte en `docs/reports/code-review-report.md`
5. Mostrar resumen ejecutivo en consola

## Priorización

1. Security (Critical)
2. Performance (High)
3. Anti-patterns (High)
4. Code smells (Medium)
5. Style issues (Low)