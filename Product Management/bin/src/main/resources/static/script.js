console.log("This is script file");

// sidebar functionality
function toggleSidebar() {

    const sidebar =  document.getElementsByClassName("sidebar")[0];
    const content =  document.getElementsByClassName("content")[0];

    if(window.getComputedStyle(sidebar).display === "none"){
        sidebar.style.display = "block";
        content.style.marginLeft = "20%";
    }
    else{
        sidebar.style.display = "none";
        content.style.marginLeft = "0%";
    }
}

// search function in view contacts page
const search=()=>{
    // console.log("searching...");

    let query = $("#search-input").val();
    if(query === ""){
        $(".search-result").hide();
    } else{
        // console.log(query);

        // sending request to server
        let url = `http://localhost:8282/search/${query}`;
        fetch(url)
            .then(response => {
            return response.json();
        })
            .then((data) => {

            // data
            // console.log(data);
            let text = `<div class="list-group">`;
            data.forEach((contact) => {
                text += `<a href='/user/${contact.cId}/contact/' class="list-group-item list-group-item-action">${contact.name}</a>`
            })
            text += `</div>`;
            $(".search-result").html(text);
            $(".search-result").show();
        });
    }
};


// first request to create request to create order
const paymentStart = () => {
    console.log("payment started");

    let amount = $("#payment_field").val();
    console.log(amount);

    if(amount == '' || amount == null){
        // alert("Amount is required !!");
        swal("Failed !!", "Amount is required !!", "error");
        return;
    }

    // code...
    // we will use ajax to send request to server to create order

    $.ajax({
        url: "/user/create_order",
        data: JSON.stringify({amount:amount, info: 'order_request'}),
        contentType: 'application/json',
        type: 'POST',
        dataType: 'json',
        success: function (response){
            // invoked when success
            console.log(response);
            if(response.status === "created"){
                // open payment form

                let options = {
                    key: 'rzp_test_nsfOSeUL5FalYU',
                    amount: response.amount,
                    currency: 'INR',
                    name: 'Smart Contact Manager',
                    description: 'Donation',
                    image: 'https://imgnew.outlookindia.com/uploadimage/library/16_9/16_9_5/Razorpay_1642012341.jpg',
                    order_id: response.id,
                    handler: function (response){
                        console.log(response.razorpay_payment_id);
                        console.log(response.razorpay_order_id);
                        console.log(response.razorpay_signature);
                        console.log("Payment successful !!");
                        // alert("Congrats !! Payment done!!");

                        updatePaymentOnServer(
                            response.razorpay_payment_id,
                            response.razorpay_order_id,
                            'paid'
                        );

                    },
                    prefill: {
                        "name": "",
                        "email": "",
                        "contact": ""
                    },
                    notes: {
                        "address": "Razorpay Corporate Office"

                    },
                    theme: {
                        "color": "#3399cc"
                    },
                };

                // payment initiate
                let rzp = new Razorpay(options);

                rzp.on('payment.failed', function (response){
                    console.log(response.error.code);
                    console.log(response.error.description);
                    console.log(response.error.source);
                    console.log(response.error.step);
                    console.log(response.error.reason);
                    console.log(response.error.metadata.order_id);
                    console.log(response.error.metadata.payment_id);
                    swal(
                        "Failed !!",
                        "Your payment is failed !!",
                        "error"
                    );
                    // alert("OOPS !! Payment failed")
                });

                rzp.open();
            }
        },
        error: function (error){
            // invoked when error
            console.log(error);
            alert("something went wrong !!");
        }
    })
    
};


//

function updatePaymentOnServer(payment_id, order_id, status)
{
    $.ajax({
        url: "/user/update_order",
        data: JSON.stringify({
            payment_id: payment_id,
            order_id: order_id,
            status: status
        }),
        contentType: 'application/json',
        type: 'POST',
        dataType: 'json',
        success: function(response){
            swal("Good job!", "Congrats !! Payment done!!", "success");
        },
        error: function (error){
            swal(
                "Failed !!",
                "Your payment is successful , but we did not get on server, we will contact you as soon as possible",
                "error"
            );
        },
    });
}