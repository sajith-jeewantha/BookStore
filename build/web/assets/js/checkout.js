/* 
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/JavaScript.js to edit this template
 */

// Payment completed. It can be a successful failure.
payhere.onCompleted = function onCompleted(orderId) {
    console.log("Payment completed. OrderID:" + orderId);
    const popup = Notification();
    popup.success({
        message: "Order Placed. Thank you"
    });

    window.location = "index.html";

    // Note: validate the payment and show success or failure page to the customer
};

// Payment window closed
payhere.onDismissed = function onDismissed() {
    // Note: Prompt user to pay again or show an error page
    console.log("Payment dismissed");
    const popup = Notification();
    popup.error({
        message: "Payment dismissed."
    });
};

// Error occurred
payhere.onError = function onError(error) {
    // Note: show an error page
    console.log("Error:" + error);
    const popup = Notification();
    popup.error({
        message: "Error:" + error
    });
};

async function loadcheckout() {

    console.log("loadcheckout");
    const response = await fetch("loadCheckOut");
    if (response.ok) {
        const json = await response.json();
        console.log(json);
        if (json.success) {
            console.log("success");
            const isAddressAvailable = json.isAddressAvailable;
            const address = json.address;
            const cityList = json.citylist;
            const cartList = json.cartlist;


            //load city
            let citySelect = document.getElementById("city");
            citySelect.length = 1;
            cityList.forEach(city => {
                let cityOption = document.createElement("option");
                cityOption.value = city.id;
                cityOption.innerHTML = city.name;
                citySelect.appendChild(cityOption);
            });


            //load address
            const checkAddress = document.getElementById("checkbox");

            if (isAddressAvailable) {

                checkAddress.addEventListener("change", e => {

                    let first_name = document.getElementById("first_name");
                    let last_name = document.getElementById("last_name");
                    let mobile = document.getElementById("mobile");
                    let line1 = document.getElementById("line1");
                    let line2 = document.getElementById("line2");
                    let city = document.getElementById("city");
                    let postal_code = document.getElementById("postal-code");


                    if (checkAddress.checked) {

                        first_name.value = address.first_name;
                        last_name.value = address.last_name;
                        mobile.value = address.mobile;
                        line1.value = address.line1;
                        line2.value = address.line2;
                        city.value = address.city.id;
                        postal_code.value = address.postal_code;


                        first_name.disabled = true;
                        last_name.disabled = true;
                        city.disabled = true;
                        line1.disabled = true;
                        line2.disabled = true;
                        postal_code.disabled = true;
                        mobile.disabled = true;

                        city.dispatchEvent(new Event("change"));

                    } else {
                        first_name.value = "";
                        last_name.value = "";
                        mobile.value = "";
                        line1.value = "";
                        line2.value = "";
                        city.value = 0;
                        postal_code.value = "";

                        first_name.disabled = false;
                        last_name.disabled = false;
                        mobile.disabled = false;
                        city.disabled = false;
                        line1.disabled = false;
                        line2.disabled = false;
                        postal_code.disabled = false;

                        city.dispatchEvent(new Event("change"));
                    }
                });
            }else{
                console.log("No Address");
//                checkAddress.className = "sr-only";
                document.getElementById("checkbox_container").className = "sr-only";
            }

            let container = document.getElementById("container");
            let book_item = document.getElementById("book_item");
            let subtotal_container = document.getElementById("subtotal_container");
            let shipping_container = document.getElementById("shipping_container");
            let total_container = document.getElementById("total_container");
            container.innerHTML = "";

            let sub_total = 0;
            let total = 0;
            let shipping_amount = 0;

            cartList.forEach(item => {
                let book_item_clone = book_item.cloneNode(true);
                book_item_clone.querySelector("#book_title").innerHTML = item.book.title;
                book_item_clone.querySelector("#book_qty").innerHTML = item.qty;
                book_item_clone.querySelector("#book_price").innerHTML = " x " + item.book.price;
                book_item_clone.querySelector("#book_author").innerHTML = item.book.author.name;

                let item_sub_total = item.book.price * item.qty;
                sub_total += item_sub_total;
                book_item_clone.querySelector("#book_sub_price").innerHTML = new Intl.NumberFormat('en-US', {minimumFractionDigits: 2}).format(item_sub_total);
                container.appendChild(book_item_clone);
            });


            //order subtotal
            let subtotal_container_clone = subtotal_container.cloneNode(true);
            subtotal_container_clone.querySelector("#sub_total").innerHTML = new Intl.NumberFormat('en-US', {minimumFractionDigits: 2}).format(sub_total);
            container.appendChild(subtotal_container_clone);

            //order shipping clone
            let shipping_container_clone = shipping_container.cloneNode(true);

            //order total clone
            let total_container_clone = total_container.cloneNode(true);


            //update total on city change
            citySelect.addEventListener("change", e => {

                let item_count = cartList.length;

                if (citySelect.value == 1) {
                    shipping_amount = item_count * 500;
                } else {
                    shipping_amount = item_count * 1200;
                }
                //order shipping
                shipping_container_clone.querySelector("#shipping-amount").innerHTML = new Intl.NumberFormat('en-US', {minimumFractionDigits: 2}).format(shipping_amount);
                container.appendChild(shipping_container_clone);

                //order total
                total = shipping_amount + sub_total;
                total_container_clone.querySelector("#total-amount").innerHTML = new Intl.NumberFormat('en-US', {minimumFractionDigits: 2}).format(total);
                container.appendChild(total_container_clone);
            });

        } else {
            window.location = "sign-in.html";
        }
    }
}

async function orderPlace() {

    const popup = Notification();
    let checkAddress = document.getElementById("checkbox");
    let first_name = document.getElementById("first_name");
    let last_name = document.getElementById("last_name");
    let mobile = document.getElementById("mobile");
    let line1 = document.getElementById("line1");
    let line2 = document.getElementById("line2");
    let city = document.getElementById("city");
    let postal_code = document.getElementById("postal-code");


    const data = {
        checkAddress: checkAddress.checked,
        first_name: first_name.value,
        last_name: last_name.value,
        mobile: mobile.value,
        line1: line1.value,
        line2: line2.value,
        city_id: city.value,
        postal_code: postal_code.value
    };

    const response = await fetch("checkout",
            {
                method: "POST",
                body: JSON.stringify(data),
                header: {
                    "Content-Type": "application/json"
                }
            });

    if (response.ok) {
        const json = await response.json();


        if (json.success) {
            console.log("work");
//            console.log(json.payherejson);
//            
            // Show the payhere.js popup, when "PayHere Pay" is clicked
            payhere.startPayment(json.payherejson);

        } else {
            console.log("Error");

            popup.error({
                title: 'Error',
                message: json.message
            });
        }
    } else {
        console.log("no");
        popup.error({
            title: 'Error',
            message: " try again"
        });
    }

}