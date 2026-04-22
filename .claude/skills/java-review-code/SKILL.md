---

name: java-code-review
description: Analyze Java code for anti-patterns, performance issues, security vulnerabilities, and code smells. Generate detailed improvement reports in markdown format with actionable recommendations.
when_to_use: Use when reviewing code quality, refactoring, optimizing performance, or conducting code audits. Triggers include mentions of "review code", "optimize", "anti-patterns", "code analysis", "improve code", or "refactor".

---

# Java Code Review Skill

## Workflow

1. **Scan** - Analizar archivos Java del proyecto
2. **Detect** - Identificar problemas por categoría
3. **Report** - Generar reporte en `docs/reports/code-review-report.md`
4. **Output** - Asegurar que el directorio `docs/reports` existe antes de escribir.

## Categorías de análisis

### 1. Anti-patterns
- God Class (>500 líneas o >10 responsabilidades)
- Shotgun Surgery (cambios dispersos)
- Feature Envy (método usa más otra clase que la propia)
- Primitive Obsession (usar primitivos en vez de objetos)
- Long Method (>20 líneas)
- Long Parameter List (>3 parámetros)
- Duplicated Code
- Dead Code (métodos/clases no usados)

### 2. Performance Issues
- N+1 queries en repositorios JPA
- Falta de índices en @Query
- String concatenation en loops
- No usar StringBuilder
- Streams mal optimizados
- Objetos inmutables recreados en loops
- Falta de caching
- Lazy loading innecesario

### 3. Spring Boot Specific
- Inyección de campo (@Autowired en campos)
- Falta de @Transactional
- @RequestMapping sin method específico
- No validar DTOs con @Valid
- Excepciones no manejadas
- Falta de @Slf4j o logging
- Configuration beans mal diseñados

### 4. Security
- SQL Injection risks
- Path Traversal vulnerabilities  
- Falta de validación de entrada
- Información sensible en logs
- Contraseñas hardcodeadas
- Falta de sanitización

### 5. Code Smells
- Violaciones SOLID
- Acoplamiento alto
- Cohesión baja
- Nombres no descriptivos
- Magic numbers/strings
- Comentarios obsoletos
- Falta de manejo de excepciones
- Try-catch vacíos

### 6. Best Practices
- No usar Lombok donde corresponde
- Falta de validaciones
- No usar Optional correctamente
- Stream operations ineficientes
- No cerrar recursos (try-with-resources)
- Falta de immutability

## Estructura del reporte

````markdown
# Code Review Report - [Fecha]

## Executive Summary
- Total files analyzed: X
- Critical issues: X
- Warnings: X  
- Suggestions: X
- Overall code quality score: X/10

## Critical Issues

### [Categoría] - [Archivo:Línea]
**Problem**: [Descripción del problema]
**Impact**: [Alto/Medio/Bajo]
**Current Code**:
```java
// código problemático
```

**Recommended Fix**:
```java
// código mejorado
```

**Rationale**: [Por qué es mejor]

---

## Performance Optimizations

[Misma estructura]

## Security Vulnerabilities

[Misma estructura]

## Code Smells

[Misma estructura]

## Refactoring Opportunities

[Misma estructura]

## Summary of Changes
- [ ] Fix N+1 query in UserService.java:45
- [ ] Replace field injection in OrderController.java
- [ ] Add @Transactional to PaymentService.java:78
[...]
````

## Ejemplo de análisis

### Anti-pattern detectado:
````java
// BAD: God Class
@Service
public class UserService {
    public void createUser() { }
    public void sendEmail() { }
    public void generateReport() { }
    public void processPayment() { }
    // 500+ líneas más...
}
````

**Fix**:
````java
// GOOD: Single Responsibility
@Service
public class UserService {
    private final EmailService emailService;
    private final ReportService reportService;
    private final PaymentService paymentService;
    
    public void createUser() { 
        // solo lógica de creación
    }
}
````

### Performance issue:
````java
// BAD: N+1 Query
public List<OrderDTO> getOrders() {
    return orderRepository.findAll().stream()
        .map(order -> {
            order.getItems().size(); // Lazy loading trigger
            return toDTO(order);
        }).toList();
}
````

**Fix**:
````java
// GOOD: Fetch join
@Query("SELECT o FROM Order o LEFT JOIN FETCH o.items")
List<Order> findAllWithItems();

public List<OrderDTO> getOrders() {
    return orderRepository.findAllWithItems().stream()
        .map(this::toDTO)
        .toList();
}
````

### Spring Boot issue:
````java
// BAD: Field injection
@RestController
public class UserController {
    @Autowired
    private UserService userService;
}
````

**Fix**:
````java
// GOOD: Constructor injection con Lombok
@RestController
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
}
````

## Scoring System

- **Critical** (0-4): Security issues, data loss risks
- **High** (5-7): Performance problems, major anti-patterns
- **Medium** (8-9): Code smells, minor improvements
- **Low** (10): Suggestions, style improvements

Siempre generar el reporte en:
1. `docs/reports/code-review-report.md`

## Comandos útiles para análisis

````bash
# Buscar clases grandes
find . -name "*.java" -exec wc -l {} \; | sort -rn | head -10

# Buscar métodos largos
grep -r "public\|private\|protected" --include="*.java" -A 50 . | grep -c "^}"

# Buscar TODO/FIXME
grep -r "TODO\|FIXME" --include="*.java" .

# Detectar field injection
grep -r "@Autowired" --include="*.java" . | grep -v "private final"
````

## Ejemplo de invocación

````bash
claude code run review-code
# o
claude code analyze src/main/java/com/example/
````