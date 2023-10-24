
	
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
                document.getElementById('productAmountId-' + productId ).innerText = response.productAmount+ ' Rs';
				document.getElementById('grandTotal').innerText = response.finalAmount + ' Rs';                
                document.getElementById('subTotal').innerText = response.finalAmount+ ' Rs';
                document.getElementById('couponAmount').innerText = '0.0 Rs';
                if("additionalCoupons" in response){
					
					var couponContainer = document.getElementById('couponContainer');
					while (couponContainer.firstChild) {
					    couponContainer.removeChild(couponContainer.firstChild);
					}
					
					var additionalCoupons = response.additionalCoupons;
					
					additionalCoupons.forEach(function(coupon) {
						console.log(coupon);
			            var couponCard = document.createElement('div');
			            couponCard.className = 'card ms-3';
			
			            var couponCardBody = document.createElement('div');
			            couponCardBody.className = 'card-body';
			
			            var couponTitle = document.createElement('h5');
			            couponTitle.className = 'card-title';
			            couponTitle.textContent = coupon.couponCode;
			
			            var couponDescription = document.createElement('p');
			            couponDescription.className = 'card-text';
			            couponDescription.textContent = coupon.description;
			
			            var applyButton = document.createElement('a');
			            applyButton.className = 'btn btn-primary';
			            applyButton.textContent = 'Apply coupon';
			            applyButton.setAttribute('data-coupon-id', coupon.couponId);
			            applyButton.id = 'coupon-card-' + coupon.couponId;
			            applyButton.onclick = function() {
			                applyCoupon(this.getAttribute('data-coupon-id'));
			            };
			
			            couponCardBody.appendChild(couponTitle);
			            couponCardBody.appendChild(couponDescription);
			            couponCardBody.appendChild(applyButton);
			
			            couponCard.appendChild(couponCardBody);
			
			            // Append the new coupon card to the coupon container
			            document.getElementById('couponContainer').appendChild(couponCard);
			        });

				}				
									
        
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
                document.getElementById('productAmountId-' + productId).innerText = response.productAmount+ ' Rs';
                document.getElementById('grandTotal').innerText = response.finalAmount + ' Rs';                
                document.getElementById('subTotal').innerText = response.finalAmount+ ' Rs';
                document.getElementById('couponAmount').innerText = '0.0 Rs';
                if("additionalCoupons" in response){
					
					var couponContainer = document.getElementById('couponContainer');
					while (couponContainer.firstChild) {
					    couponContainer.removeChild(couponContainer.firstChild);
					}
					
					var additionalCoupons = response.additionalCoupons;					
					additionalCoupons.forEach(function(coupon) {
						console.log(coupon);
			            var couponCard = document.createElement('div');
			            couponCard.className = 'card ms-3';
			
			            var couponCardBody = document.createElement('div');
			            couponCardBody.className = 'card-body';
			
			            var couponTitle = document.createElement('h5');
			            couponTitle.className = 'card-title';
			            couponTitle.textContent = coupon.couponCode;
			
			            var couponDescription = document.createElement('p');
			            couponDescription.className = 'card-text';
			            couponDescription.textContent = coupon.description;
			
			            var applyButton = document.createElement('a');
			            applyButton.className = 'btn btn-primary';
			            applyButton.textContent = 'Apply coupon';
			            applyButton.setAttribute('data-coupon-id', coupon.couponId);
			            applyButton.id = 'coupon-card-' + coupon.couponId;
			            applyButton.onclick = function() {
			                applyCoupon(this.getAttribute('data-coupon-id'));
			            };
			
			            couponCardBody.appendChild(couponTitle);
			            couponCardBody.appendChild(couponDescription);
			            couponCardBody.appendChild(applyButton);
			
			            couponCard.appendChild(couponCardBody);
			
			            // Append the new coupon card to the coupon container
			            document.getElementById('couponContainer').appendChild(couponCard);
			        });

				}
                
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
			 
			if("error" in response){				
				const flashNotification = $("#flash-notification");
				// Remove any existing alert elements
				flashNotification.empty();
				
				 const alertElement = $(`<div class="alert alert-danger" role="success">
				 ${response.error}
				 </div>`);
				 
				 // Append the alert element to the flash notification element
				flashNotification.append(alertElement);
			  
				// Show the flash notification
				flashNotification.fadeIn("slow");
			
				// Hide the flash notification after 3 seconds
				setTimeout(function() {
			  	flashNotification.fadeOut("slow");
				}, 3000);
			}
			else{
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
			
				// Hide the flash notification after 3 seconds
				setTimeout(function() {
			  	flashNotification.fadeOut("slow");
				}, 3000);
			}
						 
			 
         },
         contentType: "text/plain"
     });
	}
	
	


	   