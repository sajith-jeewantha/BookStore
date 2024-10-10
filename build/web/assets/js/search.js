/* 
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/JavaScript.js to edit this template
 */


async function search(firstResult) {

    const popup = Notification();

    const sort = document.getElementById("sort").value;
    const title = document.getElementById("title").value;
    const type = document.getElementById("type").value;
    const author = document.getElementById("author").value;
    const condition = document.getElementById("condition").value;
    const year = document.getElementById("year").value;
    const start_price = document.getElementById("start_price").value;
    const end_price = document.getElementById("end_price").value;


    const data = {
        firstResult: firstResult,
        sort: sort,
        title: title,
        type: type,
        author: author,
        condition: condition,
        year: year,
        startprice: start_price,
        endprice: end_price

    };

    const response = await fetch("AdvanceSearch",
            {
                method: "POST",
                body: JSON.stringify(data),
                header: {
                    "Content-Type": "application/json"
                }
            }
    );

    if (response.ok) {
        const json = await response.json();
        console.log("Succes");
        console.log(json);


        if (json.success) {
            setBookList(json);
        } else {
            console.log(json.message);

            popup.error({
                title: 'Error',
                message: json.message
            });

        }

    } else {
        console.log("not work");
    }
}

var bookdiv = document.getElementById("book");
var currentPage = 0;
var pagination_button = document.getElementById("pagination-button");

function setBookList(json) {

//start load Product
    let container = document.getElementById("container");
    container.innerHTML = "";

    json.bookList.forEach(book => {

        let book_clone = bookdiv.cloneNode(true);
        book_clone.querySelector("#book_a_1").href = "single-product-view.html?id=" + book.id;
        book_clone.querySelector("#book_a_2").href = "single-product-view.html?id=" + book.id;
        book_clone.querySelector("#book_image").src = "book-images/" + book.id + ".jpg";
        book_clone.querySelector("#book_title").innerHTML = book.title;
        book_clone.querySelector("#book_author").innerHTML = book.author.name;
        book_clone.querySelector("#book_condition").innerHTML = book.condition.name;
        book_clone.querySelector("#book_price").innerHTML = "Rs. " + new Intl.NumberFormat('en-US', {minimumFractionDigits: 2}).format(book.price);

        container.appendChild(book_clone);
    });

    //Pagination
    let pagination_container = document.getElementById("pagination-container");
    pagination_container.innerHTML = "";

    const product_count = json.allProductCount;
    const product_per_page = 4;

    let pages = Math.ceil(product_count / product_per_page);

    console.log(product_count);
    console.log(product_per_page);
    console.log(pages);


    //add prev button  
    if (currentPage != 0) {

        let pagination_button_clone_prev = pagination_button.cloneNode(true);
        pagination_button_clone_prev.innerHTML = "Preview";
        pagination_button_clone_prev.addEventListener("click", e => {
            currentPage--;
            search(currentPage * 2);
        });
        pagination_container.appendChild(pagination_button_clone_prev);
    }

    for (let i = 0; i < pages; i++) {
        let pagination_button_clone = pagination_button.cloneNode(true);
        pagination_button_clone.innerHTML = i + 1;
        pagination_button_clone.addEventListener("click", e => {
            currentPage = i;
            search(i * 2);
        });

        if (i == currentPage) {
            pagination_button_clone.className = "w-24 bg-indigo-600 px-3 py-2 text-white text-lg rounded-md shadow-sm hover:bg-indigo-700";
        } else {
            pagination_button_clone.className = "w-24 bg-indigo-300 px-3 py-2 text-white text-lg rounded-md shadow-sm hover:bg-indigo-400";
        }

        pagination_container.appendChild(pagination_button_clone);
    }

//    //add next button
    if (currentPage != (pages - 1)) {
        let pagination_button_clone_next = pagination_button.cloneNode(true);
        pagination_button_clone_next.innerHTML = "Next";
        pagination_button_clone_next.addEventListener("click", e => {
            currentPage++;
            search(currentPage * 2);
        });

        pagination_container.appendChild(pagination_button_clone_next);
    }

}