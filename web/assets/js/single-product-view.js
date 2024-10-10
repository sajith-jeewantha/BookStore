/* 
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/JavaScript.js to edit this template
 */

async function loadProduct() {

    const parameters = new URLSearchParams(window.location.search);
    if (parameters.has("id")) {
        const productId = parameters.get("id");
        const response = await fetch("LoadSingleProduct?id=" + productId);
        if (response.ok) {
            const json = await response.json();
            console.log(json);
            if (json.resp.status) {
                const id = json.book.id;
                document.getElementById("book-image").src = "book-images/" + id + ".jpg";
                document.getElementById("book-title").innerHTML = json.book.title;
                document.getElementById("author").innerHTML = "Author : " + json.book.author.name;
                document.getElementById("type").innerHTML = "Type : " + json.book.type.name;
                document.getElementById("condition").innerHTML = "Condition : " + json.book.condition.name;
                document.getElementById("price").innerHTML = "Rs . " + new Intl.NumberFormat('en-US', {minimumFractionDigits: 2}).format(json.book.price);
                document.getElementById("description").innerHTML = json.book.description;
                document.getElementById("page-title").innerHTML = json.book.title;
                document.getElementById("book-add-to-cart").addEventListener(
                        "click",
                        (e) => {
                    addToCart(id, document.getElementById("book-add-to-car-qty").value);
                    e.preventDefault();
                });

                let similerContainer = document.getElementById("similer-container");
                let similerbook = document.getElementById("similer-book");
                similerContainer.innerHTML = "";

                json.bookList.forEach(item => {
                    let similerbookClone = similerbook.cloneNode(true);

                    similerbookClone.querySelector("#similar-image").src = "book-images/" + item.id + ".jpg";
                    similerbookClone.querySelector("#similar-title").innerHTML = item.title;
                    similerbookClone.querySelector("#similar-author").innerHTML = item.author.name;
                    similerbookClone.querySelector("#similar-price").innerHTML = "Rs . " + new Intl.NumberFormat('en-US', {minimumFractionDigits: 2}).format(item.price);
                    similerbookClone.querySelector("#similar-link-1").href = "single-product-view.html?id=" + item.id;
                    similerbookClone.querySelector("#similar-link-2").href = "single-product-view.html?id=" + item.id;

                    similerContainer.appendChild(similerbookClone);
                });

            } else {
                window.location = "index.html";
            }
        } else {
            window.location = "index.html";
        }

    } else {
        window.location = "index.html";
    }

}


async function addToCart(id, qty) {

//    console.log(id);
    console.log(id, qty);

    const response = await fetch("AddToCart?id=" + id + "&qty=" + qty);
    const popup = Notification();
    if (response.ok) {
        const json = await response.json();
        if (json.status) {
//            console.log(json.content);
//            console.log("success");

            popup.success({
                title: 'Success',
                message: json.message
            });
        } else {
//            console.log(json.content);

            popup.error({
                title: 'Error',
                message: json.message
            });
        }

    } else {
        console.log("not ok");
    }


}
