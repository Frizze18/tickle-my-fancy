// Render the button into the container element


paypal.Button.render({

    // Pass the client ids to use to create your transaction on sandbox and production environments

    env: 'sandbox',

    client: {
        sandbox:    'AWC1-PhJOrXvLj706s6WNEH856RNcRzEgkOpO4v1gB33p_YTS0oDMsGfZQor2r_7pl6dcf7H6v5x_9-V', // from https://developer.paypal.com/developer/applications/
       production: 'AWFTu3GK2q35ClZvhzsvhUdJurBAAIj2X-7CKSwSvIf1.RTUlTmImhFn'  // from https://developer.paypal.com/developer/applications/
    },

    // Pass the payment details for your transaction
    // See https://developer.paypal.com/docs/api/payments/#payment_create for the expected json parameters

    payment: function(data, actions) {
        return actions.payment.create({
            transactions: [
                {
                    amount: {
                        total: parseFloat(document.getElementById("paypal").innerText),
                        currency: 'SEK'
                    }
                }
            ]
        });
    },
    // Display a "Pay Now" button rather than a "Continue" button
    commit: true,
    // Pass a function to be called when the customer completes the payment
    onAuthorize: function(data, actions) {
        return actions.payment.execute().then(function(response) {
            console.log('The payment was completed!');
        });
    },
    // Pass a function to be called when the customer cancels the payment
    onCancel: function(data) {
        console.log('The payment was cancelled!');
    }

}, '#myContainerElement');




paypal.Button.render({

    // Set up a getter to create a Payment ID using the payments api, on your server side:

    payment: function () {
        return new paypal.Promise(function (resolve, reject) {

            // Make an ajax call to get the Payment ID. This should call your back-end,
            // which should invoke the PayPal Payment Create api to retrieve the Payment ID.

            // When you have a Payment ID, you need to call the `resolve` method, e.g `resolve(data.paymentID)`
            // Or, if you have an error from your server side, you need to call `reject`, e.g. `reject(err)`

            jQuery.post('/my-api/create-payment')
                .done(function (data) {
                    resolve(data.paymentID);
                })
                .fail(function (err) {
                    reject(err);
                });
        });
    },

    // Pass a function to be called when the customer approves the payment,
    // then call execute payment on your server:

    onAuthorize: function (data) {

        console.log('The payment was authorized!');
        console.log('Payment ID = ', data.paymentID);
        console.log('PayerID = ', data.payerID);

        // At this point, the payment has been authorized, and you will need to call your back-end to complete the
        // payment. Your back-end should invoke the PayPal Payment Execute api to finalize the transaction.

        jQuery.post('/my-api/execute-payment', {paymentID: data.paymentID, payerID: data.payerID})
            .done(function (data) { /* Go to a success page */
            })
            .fail(function (err) { /* Go to an error page  */
            });
    },

    // Pass a function to be called when the customer cancels the payment

    onCancel: function (data) {

        console.log('The payment was cancelled!');
        console.log('Payment ID = ', data.paymentID);
    }
});
