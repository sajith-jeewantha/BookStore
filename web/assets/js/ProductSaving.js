/* 
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/JavaScript.js to edit this template
 */
async function ProductSaving() {

    const title = document.getElementById("title");
    const description = document.getElementById("description");
    const image = document.getElementById("image");
    const type = document.getElementById("type");
    const author = document.getElementById("author");
    const condition = document.getElementById("condition");
    const price = document.getElementById("price");
    const quantity = document.getElementById("quantity");
    const year = document.getElementById("year");

    const data = new FormData();
    data.append("title", title.value);
    data.append("description", description.value);
    data.append("image", image.files[0]);
    data.append("type", type.value);
    data.append("author", author.value);
    data.append("condition", condition.value);
    data.append("price", price.value);
    data.append("quantity", quantity.value);
    data.append("year", year.value);

    const response = await fetch("ProductSaving",
            {
                method: "POST",
                body: data
            });

    if (response.ok) {
        const json = await response.json();
        const popup = Notification();

        if (json.status) {


            popup.success({
                title: 'Success',
                message: "Success"
            });
            
            title.value = "";
            description.value = "";
            image.value = null;
            type.value = 0;
            author.value = 0;
            condition.value = 0;
            price.value = "";
            quantity.value = "";
            year.value = "";

        } else {

            popup.error({
                title: 'Error',
                message: json.message
            });

        }
    } else {
        console.log("not ok");
    }
}