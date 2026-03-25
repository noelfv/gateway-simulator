# Agregar nueva red de pago

Guia paso a paso para agregar soporte a una nueva red (ej: Amex, UnionPay).

Solicita al usuario:
1. Nombre de la red
2. PeerId (ej: PEER03)
3. Formato de campos (si difiere de Visa/Mastercard)

Luego crea siguiendo el patron existente:
1. Enum `{Red}ISOField` en `orchestrator/core/fields/`
2. `{Red}ProcessField` en `orchestrator/core/network/{red}/`
3. `{Red}DelegateParser` en `orchestrator/core/parser/factory/`
4. `{Red}DelegateMapper` en `orchestrator/core/mapper/factory/`
5. `{Red}DelegateFieldLogic` en `orchestrator/core/logic/factory/`
6. Registrar en `ParserFactory`, `MapperFactory`, `FieldLogicFactory`

Revisar primero VisaDelegateParser como referencia antes de crear.
