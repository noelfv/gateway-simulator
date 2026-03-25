# Build del proyecto

Ejecuta la compilacion completa del Gateway Simulator.

```bash
mvn clean package -DskipTests
```

Luego verifica que `target/dist/gateway-simulator-2.0.0-SNAPSHOT.jar` existe.
Si falla, muestra el error completo y sugiere la causa probable.
