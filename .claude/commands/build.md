# Build del proyecto (genera instalador)

Compila el proyecto y genera la estructura distribuible en `build/`:

```bash
mvn clean package -DskipTests
```

Verifica que se generaron correctamente:
- `build/gateway-simulator-2.0.0-SNAPSHOT.jar`  <- JAR principal
- `build/libs/`   <- dependencias
- `build/config/` <- application*.yml + log4j2.xml
- `build/log/`    <- carpeta de logs
- `build/run.bat` <- launcher

Si la verificacion es exitosa, informa al usuario que el instalador esta listo
y que puede ejecutarlo con doble clic en `build/run.bat`.

Si falla, muestra el error completo del build y sugiere la causa probable.
