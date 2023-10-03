
	
//	Function to display the errormessage ---------------------------------------------------------------
	function displayFlashMessageForStock(message,productId) {
	  const flashMessageElement = document.getElementById('flash-message-' + productId);
	  flashMessageElement.textContent = message;
	  flashMessageElement.style.display = 'block'; // Show the flash message
	
	  setTimeout(() => {
	    flashMessageElement.style.display = 'none'; // Hide the flash message after 3 seconds (adjust as needed)
	  },2000);
	}
	
	
//Quantity Increment and Decrement ================================================================================	
//  productPage cartItem quantity increment	
	function countIncrement(productId){

		let productQuantity = document.getElementById('inputId-' + productId).innerText;
		$.ajax({
         type: "GET",
         url: "/user/cart/incrementItem",
         data: {
			 cartItemId : productId,
			 productQuantity : productQuantity
			 },
         success: function (response) { 
			
             if ("newQuantity" in response) {
                document.getElementById('inputId-' + productId).innerText = response.newQuantity ;
                document.getElementById('productAmountId-' + productId ).innerText = response.productAmount+ '.0';
				document.getElementById('grandTotal').innerText = response.finalAmount + '.0 Rs';                
                document.getElementById('subTotal').innerText = response.finalAmount+ '.0 Rs';
                document.getElementById('couponAmount').innerText = '0.0 Rs';
             } else {
                 displayFlashMessageForStock(response.error,productId)
             }
         },
         contentType: "text/plain"
     });
	}
	
	
//  productPage cartItem quantity decrement*/		
	function countDecrement(productId){

		let productQuantity = document.getElementById('inputId-' + productId).innerText;		
		$.ajax({
         type: "GET",
         url: "/user/cart/decrementItem",
         data: {
			 cartItemId : productId,
			 productQuantity : productQuantity
			 },
         success: function (response) { 
			
             if ("newQuantity" in response) {
                document.getElementById('inputId-' + productId).innerText = response.newQuantity ;
                document.getElementById('productAmountId-' + productId).innerText = response.productAmount+ '.0';
                document.getElementById('grandTotal').innerText = response.finalAmount + '.0 Rs';                
                document.getElementById('subTotal').innerText = response.finalAmount+ '.0 Rs';
                document.getElementById('couponAmount').innerText = '0.0 Rs';
             } 
         },
         contentType: "text/plain"
     });	
	}
//Quantity Increment and Decrement completes ================================================================================			
		
//  for applying the coupon -----------------------------------------------------------------
	function applyCoupon(id){
		
		$.ajax({
         type: "GET",
         url: "/user/coupon/applyCoupon",
         data: {
			 couponId : id
			 },
         success: function (response) { 
			
			document.getElementById('couponAmount').innerText = response.couponAmount + '.0 Rs';
			document.getElementById('grandTotal').innerText = response.grandTotal + '.0 Rs';
			
			const flashNotification = $("#flash-notification");
			// Remove any existing alert elements
			flashNotification.empty();
			
			 const alertElement = $(`<div class="alert alert-success" role="success">
			 Discount Code applied successfully
			 </div>`); 
			 
			 // Append the alert element to the flash notification element
			flashNotification.append(alertElement);
		  
			// Show the flash notification
			flashNotification.fadeIn("slow");
		  
			$("#discount-code").val("");
		
			// Hide the flash notification after 3 seconds
			setTimeout(function() {
		  	flashNotification.fadeOut("slow");
			}, 3000);
         },
         contentType: "text/plain"
     });
	}
	
	


	   