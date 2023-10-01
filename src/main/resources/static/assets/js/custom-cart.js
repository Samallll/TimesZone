
	
//	Function to display the errormessage
	function displayFlashMessageForStock(message,productId) {
	  const flashMessageElement = document.getElementById('flash-message-' + productId);
	  flashMessageElement.textContent = message;
	  flashMessageElement.style.display = 'block'; // Show the flash message
	
	  setTimeout(() => {
	    flashMessageElement.style.display = 'none'; // Hide the flash message after 3 seconds (adjust as needed)
	  },2000);
	}
	
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
		
		
//  for applying the coupon
	function applyCoupon(id){
		
/*		if(couponId != 0){
			const flashNotification = $("#flash-notification");
			const alertElement = $(`<div class="alert alert-success" role="success">
				 Coupon has been applied once
				 </div>`);
			return
		}
		couponId = id;*/
		
		$.ajax({
         type: "GET",
         url: "/user/coupon/applyCoupon",
         data: {
			 couponId : id
			 },
         success: function (response) { 
			
			document.getElementById('couponAmount').innerText = response.couponAmount + ' Rs';
			document.getElementById('grandTotal').innerText = response.grandTotal + ' Rs';
			
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

	
	const inputField = document.querySelectorAll(".quantity");
	const incrementButton = document.querySelectorAll(".upButton");
	const decrementButton = document.querySelectorAll(".downButton");
	
	// For making changes in the product price based on the quantity
	const productPrice = document.querySelectorAll(".productAmount");
	
	console.log("Inside script");
	
	incrementButton.forEach((button, index) => {
	    button.addEventListener("click", () => {
	        count(inputField[index], "increment", productPrice[index]);
	        console.log("Inside addEventListener increment");
	    });
	    console.log("Inside foreach increment");
	});
	
	decrementButton.forEach((button, index) => {
	    button.addEventListener("click", () => {
	        count(inputField[index], "decrement", productPrice[index]);
	        console.log("Inside addEventListener decrement");
	    });
	    console.log("Inside foreach decrement");
	});

	// For increment/decrement the quantity - function definition
	function count(inputElement, action, productField) {
    // Current value stored in the selected input field from the input fields
    let currentValue = parseInt(inputElement.value);
	const productAmount = productField.textContent;
	console.log(productAmount);
    // Convert productAmount to a numeric value (remove "$" and convert to float)
    const currentAmount = parseFloat(productAmount.replace("$", ""));
    console.log(currentAmount);

    // Check if currentValue is a valid number
    if (!isNaN(currentValue)) {
        if (action === "increment") {
            currentValue = currentValue + 1;
        } else if (action === "decrement" && currentValue > 1) {
            currentValue = currentValue - 1;
        }

        inputElement.value = currentValue;
        const parentElement = inputElement.parentElement;
        const parentElementParentElement = parentElement.parentElement;
        const siblingElement = parentElementParentElement.nextElementSibling;
        const childElement = siblingElement.querySelector('strong');
        childElement.textContent = "$" + finalAmount; // Format to two decimal places
	    }
	}

	   