	
	
	const searchInput = document.getElementById("searchProduct");
	const submitButton = document.getElementById("searchButton");
	
	let productId = 1; // Initialize the product ID
	let selectedProductId = 1;
	var selectedProducts = new Set();
	
//  for submitting the form for product offer ======================================================

	$(document).ready(function () {
	        // Capture form submission
	        $("#productOfferSubmit").click(function (event) {
	            // Prevent the default button click behavior
	            event.preventDefault();
	
	            // Get form data
	            var formData = {
	                offerCode: $("#inputProductOfferCode").val(),
	                offerStartDate: $("#inputProductOfferStartDate").val(),
	                offerExpiryDate: $("#inputProductOfferExpiryDate").val(),
	                percentage: $("#inputProductOfferPercentage").val(),
	                productNameList:selectedProducts
	            };
				
				console.log(formData.offerStartDate);
				console.log(formData.offerExpiryDate);
	            
	            $.ajax({
	                type: "POST", 
	                url: "/admin/offerManagement/submitProductOffer", 
	                data: JSON.stringify(formData),
	                contentType: "application/json", 
	                success: function (response) {
	                    $("#newFormData")[0].reset();
	                    var message = response.message;
						var modal = $('#myModal');
						modal.find('.modal-title').text('Alert');
						modal.find('.modal-body').html(message);
						modal.modal('show');
	                },
	                error: function (error) {
	                    // Handle errors
	                    console.log("Error:", error);
	                }
	            });
	        });
	    });
	
	

//  for searching the product -----------------------------------------------------------------
	submitButton.addEventListener("click", function () {
	    const searchValue = searchInput.value;
	    if (searchProductList) {
	        while (searchProductList.firstChild) {
	            searchProductList.removeChild(searchProductList.firstChild);
	        }
	    }
	    
	    $.ajax({
	        url: '/admin/productOffer/searchProduct', 
	        type: 'GET', 
	        dataType: 'json',
	        data: {
	            search: searchValue 
	        }, 
	        success: function (data) {
	            
	            if (data.productList && Array.isArray(data.productList)) {
	                data.productList.forEach(function(product) {
						
	                    const card = document.createElement('div');
	                    card.className = 'card mb-3'; 
	                    
	                    // Set the ID attribute as the product ID
	                    card.id = 'productId'+productId;
	                    
	                    const cardBody = document.createElement('div');
	                    cardBody.className = 'card-body text-secondary';
	                    cardBody.style.cursor='pointer';
	                    
	                    // Set the data-name attribute as the product name
	                    cardBody.setAttribute('data-name', product);
	                    cardBody.textContent = product;
	                    
	                    card.appendChild(cardBody);
	                    searchProductList.appendChild(card);
	                    
	                    productId++; // Increment the product ID
	       
	                });
	            } else {
	                console.log('Product List is missing.');
	            }
	        },
	        error: function (xhr, status, error) {
	            console.error('Error:', error);
	        }
	    });
	});
	
	// Define a function to handle the click event on selecting a product  --------------------------
	function selectproductsForOffer(event) {
	    // Get the product name from the data-name attribute
	    const productName = event.target.getAttribute('data-name');
	    
	    if (productName) {
	        
	        $.ajax({
				url:'/admin/productOffer/selectProducts',
				type: 'GET', 
		        dataType: 'json',
		        data: {
		            productName: productName 
		        }, 
		        success: function (data) {
					
					if (selectedProductList) {
				        while (selectedProductList.firstChild) {
				            selectedProductList.removeChild(selectedProductList.firstChild);
				        }
				    }
		            
		            if (data.selectedProducts && Array.isArray(data.selectedProducts)) {
		                data.selectedProducts.forEach(function(product) {
		                    const card = document.createElement('div');
		                    card.className = 'card mb-3'; 
		                    
		                    // Set the ID attribute as the product ID
		                    card.id = 'selectedProductId'+selectedProductId;
		                    
		                    const cardBody = document.createElement('div');
		                    cardBody.className = 'card-body text-black';
		                    cardBody.style.cursor='pointer';
		                    
		                    // Set the data-name attribute as the product name
		                    cardBody.setAttribute('data-name', product);
		                    cardBody.textContent = product;
		                    
		                    card.appendChild(cardBody);
		                    selectedProductList.appendChild(card);
		                    
		                    productId++; // Increment the product ID
		                    selectedProducts.add(product);
		                });
		            } else {
		                console.log('Product List is missing.');
		            }
		        },
		        error: function (xhr, status, error) {
		            console.error('Error:', error);
		        }
			});
	    }
	}
	
	// Add a click event listener to the container (searchProductList)
	searchProductList.addEventListener('click', function (event) {
	    // Check if the click occurred on a card within the container
	    if (event.target && event.target.className === 'card-body text-secondary') {
	        selectproductsForOffer(event);
	    }
	});
	
