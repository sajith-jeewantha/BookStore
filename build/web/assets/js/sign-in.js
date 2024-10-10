async function signIn() {

    const data = {
        email: document.getElementById("email").value,
        password: document.getElementById("password").value
    };

    const response = await fetch("SignIn",
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
            window.location = "index.html";
        } else {
            document.getElementById("message").innerHTML = json.message;
        }

    } else {
        console.log("Error");
    }

}