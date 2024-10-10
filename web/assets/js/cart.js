/* 
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/JavaScript.js to edit this template
 */


async function loadCartItems() {


    const response = await fetch("LoadCartItems");
    const popup = Notification();

    if (response.ok) {
        const json = await response.json();

        console.log(json);

        if (json.length == 0) {

            let cartItemContainer = document.getElementById("cart-Item-Container");
            cartItemContainer.innerHTML = "";

            let orderSummary = document.getElementById("order-summary");
            orderSummary.innerHTML = "";

            let h1 = document.createElement("h1").cloneNode(true);
            h1.innerHTML = "Cart Empty";
            h1.className = "mb-3 text-2xl";

            let a = document.createElement("a").cloneNode(true);
            a.href = "search.html";
            a.innerHTML = "Continue Shopping";

            cartItemContainer.appendChild(h1);
            cartItemContainer.appendChild(a);


        } else {
            let totalQty = 0;
            let total = 0;

            let cartItemContainer = document.getElementById("cart-Item-Container");
            let cartItemRow = document.getElementById("cart-Item-Row");
            cartItemContainer.innerHTML = "";

            json.forEach(item => {
                let itemSubtotal = item.book.price * item.qty;

                totalQty += item.qty;
                total += itemSubtotal;

                let cartItemRowClone = cartItemRow.cloneNode(true);
                cartItemRowClone.querySelector("#cart-item-a").href = "single-product-view.html?id=" + item.book.id;
                cartItemRowClone.querySelector("#cart-item-img").src = "book-images/" + item.book.id + ".jpg";
                cartItemRowClone.querySelector("#cart-item-title").innerHTML = item.book.title;
                cartItemRowClone.querySelector("#cart-item-title-a").href = "single-product-view.html?id=" + item.book.id;
                cartItemRowClone.querySelector("#cart-item-price").innerHTML = "Rs. " + new Intl.NumberFormat('en-US', {minimumFractionDigits: 2}).format(item.book.price);
                cartItemRowClone.querySelector("#cart-item-qty").value = item.qty;
                cartItemRowClone.querySelector("#cart-item-subtotal").innerHTML = "Rs. " + new Intl.NumberFormat('en-US', {minimumFractionDigits: 2}).format(itemSubtotal);
                cartItemContainer.appendChild(cartItemRowClone);
            });

            document.getElementById("cart-total-qty").innerHTML = totalQty;
            document.getElementById("original-total").innerHTML = new Intl.NumberFormat('en-US', {minimumFractionDigits: 2}).format(total);
            document.getElementById("cart-total").innerHTML = new Intl.NumberFormat('en-US', {minimumFractionDigits: 2}).format(total);
        }

    } else {
        console.log("not ok");
        popup.error({
            title: 'Error',
            message: "Unable to procss your request"
        });
    }


}