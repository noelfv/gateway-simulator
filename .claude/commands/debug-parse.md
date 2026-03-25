# Debug de parseo ISO8583

Ayuda a diagnosticar problemas al parsear un mensaje ISO8583.

Solicita al usuario:
1. La trama hex del mensaje
2. La red (Visa/Mastercard)
3. El error o comportamiento inesperado

Proceso de diagnostico:
1. Identificar el parser correcto: `VisaDelegateParser` o `MastercardDelegateParser`
2. Revisar el bitmap (primeros 8 o 16 bytes) para identificar campos presentes
3. Verificar la definicion del campo en `VisaISOField` / `MastercardISOField`
4. Revisar `ISOUtil` y `ParserUtil` para la logica de extraccion
5. Revisar `ProcessField` para la logica de transformacion

Campos especiales a revisar primero si hay problemas:
- Campo 48 (TLV): parseo especial
- Campos 49/50/51 (monedas): Visa agrega '0' al inicio
- Campo 52 (PIN): 8 bytes hex
- Campo 64 (MAC): 8 bytes hex
