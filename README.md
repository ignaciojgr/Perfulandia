# Perfulandia
 Conjunto de APIs comunicándose entre sí para realizar las funciones de un sistema ecommerce.

 Los microservicios que componen estas APIs deben ejecutarse en conjunto para probar los endpoints. El endpoint del ms-order-bff tipo POST que crea una orden de venta produce el flujo con la API de catálogo para ver la información y precio de los productos, y se comunica con la API de pagos para generar la conexión con Transbank. Este endpoint devuelve la orden de venta generada con su ID y demás campos (incluyendo la información de la transacción producto de la conexión con Transbank). 

 ## Dto
 Para testear el endpoint de ms-order-bff se debe generar un request tipo POST a la URL [ht](http://localhost:8080/api/v1/orders), con una información parecida a esta: 
    {
    "userId": "user123",
    "currency": "CLP",
    "returnUrl": "http://localhost:3000/order-confirmation",
    "customerEmail": "juan.perez@email.com",
    "shippingAddress": "Av. Providencia 1234, Santiago, Chile",
    "paymentMethod": "CREDIT_CARD",
    "items": [{
    "productId": "1",
    "quantity": 2
    }, {
    "productId": "2",
    "quantity": 3
    }]
    }

** Observación: antes de realizar este request se debe crear algún producto con esos id. 
