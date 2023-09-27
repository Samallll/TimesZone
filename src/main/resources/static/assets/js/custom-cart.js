	
	var couponApplied = false;
	var couponId = 0;
	
//	Function to display the errormessage
	function displayFlashMessage(message) {
	  const flashMessageElement = document.getElementById('flash-message');
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
			 productQuantity : productQuantity,
			 couponApplied : couponApplied,
			 couponId : couponId
			 },
         success: function (response) { 
			
             if ("newQuantity" in response) {
                document.getElementById('inputId-' + productId).innerText = response.newQuantity ;
                document.getElementById('productAmountId-' + productId ).innerText = response.productAmount;
                if(document.getElementById('finalAmount')!= null){
					document.querySelectorAll('.finalAmount').innerText = response.finalAmount;
				}
             } else {

                 displayFlashMessage(response.error)
             }
         },
         contentType: "text/plain"
     });
	}
	
	
//  productPage cartItem quantity decrement*/		
	function countDecrement(productId){

		let productQuantity = document.getElementById('inputId-' + productId).innerText;
		console.log("Inside decerment cart"+productQuantity);
		
		$.ajax({
         type: "GET",
         url: "/user/cart/decrementItem",
         data: {
			 cartItemId : productId,
			 productQuantity : productQuantity,
			 couponApplied : couponApplied,
			 couponId : couponId
			 },
         success: function (response) { 
			
             if ("newQuantity" in response) {
                document.getElementById('inputId-' + productId).innerText = response.newQuantity ;
                document.getElementById('productAmountId-' + productId).innerText = response.productAmount;
                if(document.getElementById('finalAmount')!= null){
					document.querySelectorAll('.finalAmount').innerText = response.finalAmount;
				}
             } 
         },
         contentType: "text/plain"
     });
		
	}
	

	/*function countIncrement(cartItemId){
		
		console.log(cartItemId);*/
		
		/*$.ajax({
         type: "GET",
         url: "/cart/countIncrement",
         data: {
			 cartItemId : cartItemId,
			 cartItemQuantity : cartItemQuantity
			 },
         success: function (response) {

             const quantity = document.getElementById(inputId-${cartItemId});
             const priceTagOriginal = document.getElementById(priceTagOriginal-${variantId})
             const actualPrice = document.getElementById(actualPrice-${variantId})
             const priceTotal = document.getElementById("priceTotal");
             const offerPriceTotal = document.getElementById("offerPriceTotal")
             const bagLimitText = document.getElementById(bagLimitText-${variantId});
             const content = "Your Bag limit is full";
             const content2 = "";
             if ("newQuantity" in response) {
                console.log(response)
                 var newQuantity = response.newQuantity;
                const stockValue = response[variantStock-${variantId}];
		
			  if(newQuantity >= stockValue ) {
                    bagLimitText.textContent = content;

                }

                 const offerPriceforTotal = response.total_Offer_price;
                 const priceforTotal = response.total_price;

                 const price = response.price;
                 const offerPrice = response.offerPrice;



                 var id = response.variantId;
                 quantity.textContent = newQuantity
                 priceTotal.textContent = priceforTotal ;
                 offerPriceTotal.textContent = offerPriceforTotal ;
                        priceTagOriginal.textContent = offerPrice * quantity.textContent;
                        actualPrice.textContent = price * quantity.textContent ;


             } else {

                 console.error("Error: " + response.error);
             }
         },
		 error: function (xhr, status, error) {
             console.log(error);
             // Handle error case if needed
             var errorMessage = "Failed to increase cart item.";
             sessionStorage.setItem('errorMessage', errorMessage); // Store the error message
         },
         contentType: "text/plain"
     });
}*/


		
		
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


		  //for applying coupon code
		  $("#apply-coupon-apply").on('click', function() {
			  const discountCoupon = document.querySelector("#discount-code").value;
			  const flashNotification = $("#flash-notification");
			
			  // Remove any existing alert elements
			  flashNotification.empty();
			
			  // Create the alert element
			  const alertElement = $(`<div class="alert alert-${discountCoupon === "hello" ? "success" : "danger"}" role="${discountCoupon === "hello" ? "success" : "error"}">
			    ${discountCoupon === "hello" ? "Discount Code applied successfully" : "Entered Discount Code is invalid"}
			  </div>`);
			  
			  let applied = discountCoupon === "hello";
			  
			  console.log(applied);
			
			  // Append the alert element to the flash notification element
			  flashNotification.append(alertElement);
			  
			 
			
			  // Show the flash notification
			  flashNotification.fadeIn("slow");
			  
			  $("#discount-code").val("");
			
			  // Hide the flash notification after 3 seconds
			  setTimeout(function() {
			    flashNotification.fadeOut("slow");
			  }, 3000);
		   });