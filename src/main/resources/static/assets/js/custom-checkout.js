	
//Payment Integration with razorpay ============================================================================	
//The button to call the endpoint to fire the api call to razorpay to get the order id --------------------------
	$(document).ready(function () {
	        // Capture form submission
	        $("#proceedCheckout").click(function (event) {

	            event.preventDefault();
	            
	            grandTotal = $("#grandTotal1").val(),
	            selectedPaymentMethod = $("input[name='paymentMethodId']:checked").val();
	            
	            console.log(grandTotal);
	            console.log(selectedPaymentMethod);
	
	            // Get form data
	            var formData = {
	                grandTotal: grandTotal,
	                selectedPaymentMethod : selectedPaymentMethod,
	            };
	
	            $.ajax({
	                type: "POST", 
	                url: "/user/payment", 
	                data: JSON.stringify(formData),
	                contentType: "application/json", 
	                success: function (response) {
						
						if("order_id" in response){
		                   //function call to execute the razorpay popup;
		                   razorpayCall(response);
						}
						else{
							var message = response.message;
							var modal = $('#myModal');
							modal.find('.modal-title').text('Alert');
							modal.find('.modal-body').html(message);
							modal.modal('show');
						}
	                },
	                error: function (error) {
	                    // Handle errors
	                    console.log("Error:", error);
	                }
	            });
	        });
	    });
	    

//The function that will trigger the razorpay checkoutform ---------------------------------------------------
	function razorpayCall(response){
		
		var options = {
		    "key": response.secret_id, // Enter the Key ID generated from the Dashboard
		    "amount": response.amount, // Amount is in currency subunits. Default currency is INR. Hence, 50000 refers to 50000 paise
		    "currency": response.currency,
		    "name": "TimeZone", 
		    "description": "Please continue with payment",
		    "order_id": response.order_id, //Pass the `order_id` obtained in the response of from razorpay
		    "handler": function (responseMap){
		        alert(responseMap.razorpay_payment_id);
		        alert(responseMap.razorpay_order_id);
		        alert(responseMap.razorpay_signature);
		        //payment completed successfully
		        verifyTranscation(responseMap.razorpay_payment_id,responseMap.razorpay_signature);
		    },
		    "prefill": { 
		        "name": response.customer_name, 
		        "email": response.email_id, 
		        "contact": response.phone_number  
		    },
		    "notes": {
		        "address": "Razorpay Corporate Office"
		    },
		    "theme": {
		        "color": "#3399cc"
		    }
		};
		var rzp1 = new Razorpay(options);
		rzp1.on('payment.failed', function (responseMap){

		        alert(responseMap.error.description);
		        alert(responseMap.error.reason);
		});
		rzp1.open();
	}
	
	
//The function that will verify the transcation using the paymentid after the transaction has been committed. -------
	function verifyTranscation(paymentId,signature){
		$.ajax({
                type: "GET", 
                url: "/user/paymentSuccess", 
                data: {
					razorPaymentId : paymentId,
					razorSignature : signature
				},
                contentType: "application/json", 
                success: function (response) {
					
					if("success" in response){
						window.location.href = "/user/viewOrders";
					}
					else{
						var message = response.message;
						var modal = $('#myModal');
						modal.find('.modal-title').text('Alert');
						modal.find('.modal-body').html(message);
						modal.modal('show');
					}
					
                },
                error: function (error) {
                    // Handle errors
                    console.log("Error:", error);
                }
            });
	}

//Payment Integraion completed =======================================================================================



//For submitting the shipping address -----------------------------------------
	$(document).ready(function () {
	        // Capture form submission
	        $("#addressBtn").click(function (event) {
	            // Prevent the default button click behavior
	            event.preventDefault();
	
	            // Get form data
	            var formData = {
	                fullName: $("#inputName").val(),
	                contactNumber: $("#inputContactNumber").val(),
	                address1: $("#inputAddress").val(),
	                address2: $("#inputAddress2").val(),
	                city: $("#inputCity").val(),
	                state: $("#inputState").val(),
	                zip: $("#inputZip").val()
	            };
	
	            
	            $.ajax({
	                type: "POST", 
	                url: "/user/shippingAddress", 
	                data: JSON.stringify(formData),
	                contentType: "application/json", 
	                success: function (response) {
	                    
	                    alert(response.message);
	                    $("#newAddressForm")[0].reset();
	                    window.location.reload();
	                },
	                error: function (error) {
	                    // Handle errors
	                    console.log("Error:", error);
	                }
	            });
	        });
	    });
	    
    
//for selecting one address from the address list as shipping address --------------------------------------    
    function myFunction(addressId){
		$.ajax({
			type:"GET",
			url:"/user/selectAddress",
			data:{
				addressId : addressId
			},
			success: function(response){
			const flashNotification = $("#flash-notification");
			// Remove any existing alert elements
			flashNotification.empty();
			
			 const alertElement = $(`<div class="alert alert-success" role="success">
			 Address Selected Successfully. Please select a convienent payment method.
			 </div>`); 
			 
			 // Append the alert element to the flash notification element
			flashNotification.append(alertElement);
		  
			// Show the flash notification
			flashNotification.fadeIn("slow");
		
			// Hide the flash notification after 3 seconds
			setTimeout(function() {
		  	flashNotification.fadeOut("slow");
			}, 5000);
         },
         contentType: "text/plain"
		});
	}
	
	