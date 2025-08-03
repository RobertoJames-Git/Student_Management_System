
document.addEventListener("DOMContentLoaded", function () {
    const form = document.querySelector("form");
    const emailErrorSpan = document.getElementById("email_error");
    const passwordErrorSpan = document.getElementById("password_error");

    form.addEventListener("submit", function (event) {
        event.preventDefault(); // Prevent default form submission

        // Clear previous error messages
        emailErrorSpan.textContent = '';
        passwordErrorSpan.textContent = '';

        const admin = {
            email: document.getElementById("emailID").value,
            password: document.getElementById("pwdID").value
        };

        fetch('/verifyAdminCredentials', {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(admin)
        })
        .then(response => {
            if (response.ok) {
                return response.text().then(path => {
                    window.location.href = path;
                });
            } else {
                const contentType = response.headers.get('Content-Type');
                
                if (contentType && contentType.includes('application/json')) {
                    return response.json().then(errorData => {
                        if (errorData.errors && Array.isArray(errorData.errors)) {
                            errorData.errors.forEach(err => {
                                const field = err.field;
                                const message = err.defaultMessage;

                                if (field === "email") {
                                    emailErrorSpan.textContent = message;
                                }
                                if (field === "password") {
                                    passwordErrorSpan.textContent = message;
                                }
                            });
                        }
                    });
                } else {
                    return response.text().then(message => {
                        passwordErrorSpan.textContent = message ;
                        emailErrorSpan.textContent = message;
                    });
                }
            }
        })
        .catch(error => {
            emailErrorSpan.textContent = "An Error occurred";
            passwordErrorSpan.textContent = "An Error occurred";

            console.error("Login error:", error);
        });

    });
});

