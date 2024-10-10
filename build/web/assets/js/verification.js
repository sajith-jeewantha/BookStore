/* 
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/JavaScript.js to edit this template
 */

async function verify() {

    const data = {
        code: document.getElementById("code").value
    };

    const response = await fetch("Verification",
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

        if (json.status) {
            window.location = "index.html";

        } else {
            document.getElementById("message").innerHTML = json.message;
            console.log(json.message);
        }
    } else {
        console.log("Error");
    }

}