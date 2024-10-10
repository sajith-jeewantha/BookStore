/* 
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/JavaScript.js to edit this template
 */


async function loadhome() {

    const response = await fetch("Home");

    if (response.ok) {
        const json = await response.json();
        const featuredBooks = json.featuredBooks

        let Featured_container = document.getElementById("Featured_container");
        let Featured_row = document.getElementById("Featured_row");
        Featured_container.innerHTML = "";
        featuredBooks.forEach(book => {
            let  Featured_row_clone = Featured_row.cloneNode(true);
            Featured_row_clone.querySelector("#Featured_image").src = "book-images/" + book.id + ".jpg";
            Featured_row_clone.querySelector("#Featured_title").innerHTML = book.title;
            Featured_row_clone.querySelector("#Featured_author").innerHTML = book.author.name;
            Featured_row_clone.querySelector("#Featured_price").innerHTML = "Rs . " + new Intl.NumberFormat('en-US', {minimumFractionDigits: 2}).format(book.price);
            Featured_row_clone.querySelector("#Featured_a").href = "single-product-view.html?id=" + book.id;
            Featured_container.appendChild(Featured_row_clone);
        });

        const slide = json.slide;
        document.getElementById("slide_title_1").innerHTML = slide[0].title;
        document.getElementById("slide_description_1").innerHTML = slide[0].description;
        document.getElementById("slide_image_1").src = "book-images/" + slide[0].id + ".jpg";
        document.getElementById("slide-a-1").href = "single-product-view.html?id=" + slide[0].id;

        document.getElementById("slide_title_2").innerHTML = slide[1].title;
        document.getElementById("slide_description_2").innerHTML = slide[1].description;
        document.getElementById("slide_image_2").src = "book-images/" + slide[1].id + ".jpg";
        document.getElementById("slide-a-2").href = "single-product-view.html?id=" + slide[1].id;

    } else {
        console.log("error");
    }

}