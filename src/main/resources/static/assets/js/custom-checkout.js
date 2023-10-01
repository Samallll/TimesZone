

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