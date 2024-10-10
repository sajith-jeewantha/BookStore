
async function signUp() {

    const data = {
        first_name: document.getElementById("first_fame").value,
        last_name: document.getElementById("last_name").value,
        mobile: document.getElementById("mobile").value,
        email: document.getElementById("email").value,
        password: document.getElementById("password").value
    };

    const response = await fetch("SignUp",
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
        console.log(json.message);

        if (json.status) {
            window.location = "verification.html";
        } else {
            document.getElementById("message").innerHTML = json.message;
        }

    } else {
        console.log("Error");
    }
}