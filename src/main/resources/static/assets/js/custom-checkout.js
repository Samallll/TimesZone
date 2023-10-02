	
	$(document).ready(function () {
	        // Capture form submission
	        $("#proceedCheckout").click(function (event) {
	            // Prevent the default button click behavior
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
	                },
	                error: function (error) {
	                    // Handle errors
	                    console.log("Error:", error);
	                }
	            });
	        });
	    });
	
	function razorpayCall(response){
		
		var options = {
		    "key": "rzp_test_hSHWQL2mOj2hsn", // Enter the Key ID generated from the Dashboard
		    "amount": "50000", // Amount is in currency subunits. Default currency is INR. Hence, 50000 refers to 50000 paise
		    "currency": "INR",
		    "name": "Acme Corp", //your business name
		    "description": "Test Transaction",
		    "image": "https://example.com/your_logo",
		    "order_id": response.order_id, //This is a sample Order ID. Pass the `id` obtained in the response of Step 1
		    "handler": function (response){
		        alert(response.razorpay_payment_id);
		        alert(response.razorpay_order_id);
		        alert(response.razorpay_signature)
		    },
		    "prefill": { //We recommend using the prefill parameter to auto-fill customer's contact information, especially their phone number
		        "name": "Gaurav Kumar", //your customer's name
		        "email": "gaurav.kumar@example.com", 
		        "contact": "9000090000"  //Provide the customer's phone number for better conversion rates 
		    },
		    "notes": {
		        "address": "Razorpay Corporate Office"
		    },
		    "theme": {
		        "color": "#3399cc"
		    }
		};
		var rzp1 = new Razorpay(options);
		rzp1.on('payment.failed', function (response){
		        alert(response.error.code);
		        alert(response.error.description);
		        alert(response.error.source);
		        alert(response.error.step);
		        alert(response.error.reason);
		        alert(response.error.metadata.order_id);
		        alert(response.error.metadata.payment_id);
		});
		rzp1.open();
	}
	
	

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
	    
    
    function myFunction(addressId){
		
		console.log(addressId)
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
	
	