/* 
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/JavaScript.js to edit this template
 */


async function checkSession() {

    const respones = await fetch("checkSession");
    if (respones.ok) {
        const json = await respones.json();
        const response_message = json.response_message;

        let sign = document.getElementById("sign-a");

        if (response_message.status) {

            document.getElementById("account").href = "account.html";
            sign.href = "SignOut";
            sign.innerHTML = "Sign Out";

        } else {
//            console.log("not Signed IN");
            document.getElementById("account").remove();
            sign.href = "sign-in.html";
            sign.innerHTML = "Sign In";
        }

    } else {
        console.log("Error");
    }
}