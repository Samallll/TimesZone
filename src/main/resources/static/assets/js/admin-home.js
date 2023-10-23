	
	
	var statusKeys = Object.keys(orderStatusMapData);
	var statusValues = Object.values(orderStatusMapData);
	
	var orderKeys = Object.keys(orderByDateMapdata);
	var orderValues = Object.values(orderByDateMapdata);
	
	var ctx = document.getElementById("chart-bars").getContext("2d");

    new Chart(ctx, {
      type: "bar",
      data: {
        labels: statusKeys,
        datasets: [{
          label: "Orders",
          tension: 0.4,
          borderWidth: 0,
          borderRadius: 4,
          borderSkipped: false,
          backgroundColor: "rgba(255, 255, 255, .8)",
          data: statusValues,
          maxBarThickness: 6
        }, ],
      },
      options: {
        responsive: true,
        maintainAspectRatio: false,
        plugins: {
          legend: {
            display: false,
          }
        },
        interaction: {
          intersect: false,
          mode: 'index',
        },
        scales: {
          y: {
            grid: {
              drawBorder: false,
              display: true,
              drawOnChartArea: true,
              drawTicks: false,
              borderDash: [5, 5],
              color: 'rgba(255, 255, 255, .2)'
            },
            ticks: {
              suggestedMin: 0,
              suggestedMax: 500,
              beginAtZero: true,
              padding: 10,
              font: {
                size: 14,
                weight: 300,
                family: "Roboto",
                style: 'normal',
                lineHeight: 2
              },
              color: "#fff"
            },
          },
          x: {
            grid: {
              drawBorder: false,
              display: true,
              drawOnChartArea: true,
              drawTicks: false,
              borderDash: [5, 5],
              color: 'rgba(255, 255, 255, .2)'
            },
            ticks: {
              display: true,
              color: '#f8f9fa',
              padding: 10,
              font: {
                size: 14,
                weight: 300,
                family: "Roboto",
                style: 'normal',
                lineHeight: 2
              },
            }
          },
        },
      },
    });
    
    var ctx2 = document.getElementById("chart-line").getContext("2d");
    
    var myChart = new Chart(ctx2, {
	      type: "line",
	      data: {
	        labels: orderKeys,
	        datasets: [{
	          label: "Orders",
	          tension: 0,
	          borderWidth: 0,
	          pointRadius: 5,
	          pointBackgroundColor: "rgba(255, 255, 255, .8)",
	          pointBorderColor: "transparent",
	          borderColor: "rgba(255, 255, 255, .8)",
	          borderColor: "rgba(255, 255, 255, .8)",
	          borderWidth: 4,
	          backgroundColor: "transparent",
	          fill: true,
	          data: orderValues,
	          maxBarThickness: 6
	
	        }],
	      },
	      options: {
	        responsive: true,
	        maintainAspectRatio: false,
	        plugins: {
	          legend: {
	            display: false,
	          }
	        },
	        interaction: {
	          intersect: false,
	          mode: 'index',
	        },
	        scales: {
	          y: {
	            grid: {
	              drawBorder: false,
	              display: true,
	              drawOnChartArea: true,
	              drawTicks: false,
	              borderDash: [5, 5],
	              color: 'rgba(255, 255, 255, .2)'
	            },
	            ticks: {
	              display: true,
	              color: '#f8f9fa',
	              padding: 10,
	              font: {
	                size: 14,
	                weight: 300,
	                family: "Roboto",
	                style: 'normal',
	                lineHeight: 2
	              },
	            }
	          },
	          x: {
	            grid: {
	              drawBorder: false,
	              display: false,
	              drawOnChartArea: false,
	              drawTicks: false,
	              borderDash: [5, 5]
	            },
	            ticks: {
	              display: true,
	              color: '#f8f9fa',
	              padding: 10,
	              font: {
	                size: 14,
	                weight: 300,
	                family: "Roboto",
	                style: 'normal',
	                lineHeight: 2
	              },
	            }
	          },
	        },
	      },
	    });
	
//	generating the order chart based on the order duration -----------------------------------------------
	
	function generateOrderChart(key,value){
		myChart.data.labels = key; // Update labels with new data
		myChart.data.datasets[0].data = value; // Update data with new values
		myChart.update();
	}	
	
	function generateChart(orderDuration){
		
		$.ajax({
			url:'/admin/generateChart',
			type:'GET',
			data:{
				orderDuration : orderDuration
			},
			success:function(response){
				orderKeys = [];
				orderValues = [];
				for (var key in response) {
				    orderKeys.push(key);
				    orderValues.push(response[key]);
				}
				generateOrderChart(orderKeys,orderValues);
			},
            error: function (error) {
                // Handle errors
                console.log("Error:", error);
            }
		});
	}
	
//	download the sales report ---------------------------------------------------------------------------

	function downloadReport() {

		    var selectedOption = document.querySelector('[name="selectedOption"]').value;
		    var dateFrom = document.getElementById('dateFrom').value;
		    var dateTo = document.getElementById('dateTo').value;
		    var fileType;
			
		    if (selectedOption === 'Choose' || dateFrom === '' || dateTo === '') {
		        alert('Please select a valid option and enter valid dates.');
		        return; 
		    }
		    dateFrom = new Date(dateFrom);
		    dateTo = new Date(dateTo);
		    var currentDate = new Date(); 
		    
		    if(selectedOption == 'Orders'){
				fileType = document.getElementById('fileType').value;
				if(!fileType){
					alert('Please select a valid File Type.');
		        	return; 
				}
				
			}
		
		    if (dateFrom > dateTo) {
		        alert('Please select a date range where "From" date is before "To" date.');
		        return; 
		    }
		
		    if (dateFrom > currentDate) {
		        alert('Please select a "From" date on or before the current date.');
		        return; 
		    }
		
		    if (dateTo >= currentDate) {
		        alert('Please select a "To" date before the current date.');
		        return; 
		    }
		
		    var formattedDateFrom = formatDate(dateFrom);
		    var formattedDateTo = formatDate(dateTo);
		
		    const url = `/admin/dashboard/generateReport?selectedOption=${selectedOption}&dateFrom=${formattedDateFrom}&dateTo=${formattedDateTo}&type=${fileType}`;
			
			window.location.href = url;
		}
		
		// Function to format a date as 'yyyy-mm-dd'
		function formatDate(date) {
		    var year = date.getFullYear();
		    var month = String(date.getMonth() + 1).padStart(2, '0'); // Ensure two-digit month
		    var day = String(date.getDate()).padStart(2, '0'); // Ensure two-digit day
		    return year + '-' + month + '-' + day;
		}
		
//		Show filetype for orders
		document.addEventListener("DOMContentLoaded", function() {
		    // Get references to the dropdowns
		    var selectedOptionDropdown = document.querySelector('[name="selectedOption"]');
		    var fileTypeDropdown = document.getElementById("fileTypeDropdown");
		
		    // Add an event listener to the "selectedOption" dropdown
		    selectedOptionDropdown.addEventListener("change", function() {
		        var selectedOption = selectedOptionDropdown.value;
		
		        // Show or hide the "File Type" dropdown based on the selected option
		        if (selectedOption === "Orders") {
		            fileTypeDropdown.style.display = "block"; // Show the dropdown
		        } else {
		            fileTypeDropdown.style.display = "none"; // Hide the dropdown
		        }
		    });
		
		    // Initialize the visibility based on the initial selected option
		    var initialSelectedOption = selectedOptionDropdown.value;
		    if (initialSelectedOption === "Orders") {
		        fileTypeDropdown.style.display = "block"; // Show the dropdown
		    } else {
		        fileTypeDropdown.style.display = "none"; // Hide the dropdown
		    }
		});