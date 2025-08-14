// Show only one form container at a time
function showForm(elementID) {
    const all = document.querySelectorAll('.form_container');
    //elements in the div are not visible anymore
    all.forEach(el => el.style.display = 'none');

    const target = document.getElementById(elementID);//retrieves the Id of the element that is suppose to be visible
    if (target) {
        target.style.display = 'block';//displays the current div

        // Update URL with elementID as a query parameter
        const newUrl = new URL(window.location);
        newUrl.searchParams.set('elementID', elementID);
        window.history.replaceState({}, '', newUrl);
    }

    if(elementID=='view_students'){
        getAllStudents();
        
    }
}


function getAllStudents(){

    fetch('/getAllStudents')
        .then(response => {
            if (!response.ok) {
                throw new Error('Fetch failed');
            }
            return response.json();
        })
        .then(data => {
            
            const container = document.getElementById('student_table');
            
            if (Array.isArray(data) && data.length === 0) {
                const span = document.createElement('span');
                span.className = 'form_error';
                span.textContent = "No Students Found";
                container.innerHTML = ''; // Clear previous content (optional)
                container.appendChild(span);

                console.log("No students found.");
                return; // Exit early
            }

            // Clear previous content (if any)
            container.innerHTML = '';

            // Create table element
            const table = document.createElement('table');
            table.style.borderCollapse = 'collapse';
            table.style.width = '100%';
            table.style.borderRadius='10px';
            // Create table header
            const headerRow = document.createElement('tr');
            const headers = ['Student ID', 'First Name', 'Last Name', 'Email', 'Date of Birth','Remove Student'];
            headers.forEach(text => {
                //create and style table heading
                const th = document.createElement('th');
                th.textContent = text;
                th.style.border = '1px solid #1964da';
                th.style.padding = '8px';
                //ad table heading to table
                headerRow.appendChild(th);
            });
            table.appendChild(headerRow);

            // Add rows for each student
            data.forEach(student => {
                const row = document.createElement('tr');

                // Add student fields except password
                ['studentID', 'fname', 'lname', 'email', 'dob'].forEach(key => {
                    const td = document.createElement('td');
                    td.textContent = student[key];
                    td.style.border = '1px solid #1964da';
                    td.style.padding = '8px';
                    row.appendChild(td);
                });

                // Create remove button
                const button = document.createElement('button');
                button.textContent = 'Remove';
                button.style.margin = '0 8px';
                button.style.padding = '6px 12px';
                button.style.cursor = 'pointer';
                button.style.width ='120px';

                // Attach delete handler
                button.onclick = function () {
                    deleteStudent(student.studentID, row); // Pass row so it can be remove it after deletion
                };

                // Add button cell to row
                const buttonCell = document.createElement('td');
                buttonCell.appendChild(button);
                buttonCell.style.border = '1px solid #1964da';
                buttonCell.style.padding = '8px';

                row.appendChild(buttonCell);

                // Append row to table
                table.appendChild(row);
            });

            container.appendChild(table);
        })
        .catch(error => {
            const container = document.getElementById('student_table');
            container.innerHTML = '';

            const errorMsg = document.createElement('p');
            errorMsg.className = 'form_error';
            errorMsg.textContent = 'Unable to get students.';
            container.appendChild(errorMsg);
        });

}

// On page load, check if elementID is present in the URL
window.addEventListener('DOMContentLoaded', () => {
    const urlParams = new URLSearchParams(window.location.search);
    const elementID = urlParams.get('elementID');
    //if elementID is present then display the container
    if (elementID) {
        showForm(elementID);
    }
});


//wait until page loads
document.addEventListener('DOMContentLoaded', () => {


    const form = document.getElementById('addStudentForm');
    const passErr   = document.getElementById('password_error');
    const confirmErr= document.getElementById('confirm_password_error');

    form.addEventListener('submit', async (evt) => {
        //clears error message from form
        clearAllSpansInForm('addStudentForm');

        evt.preventDefault();//prevent page from refreshing on form submission
        passErr.textContent = '';
        confirmErr.textContent = '';

        const pwd  = form.password.value;
        const cPwd = form.confirmPassword.value;
        if (pwd !== cPwd) {
        confirmErr.textContent = 'Password and Confirm Password must match';
        return;
        }

        const data = Object.fromEntries(new FormData(form).entries());
        try {
        const res = await fetch(form.action, {
            method:  form.method,
            headers: { 'Content-Type': 'application/json' },
            body:    JSON.stringify(data)
        });

        const contentType = res.headers.get('Content-Type') || '';

        // Handle 4xx/5xx errors
        if (!res.ok) {
            // Try JSON first, then fallback to text
            let errBody;
            if (contentType.includes('application/json')) {
                errBody = await res.json();
                errBody.errors?.forEach(e => {
                console.error(e.defaultMessage);
                console.log(`${e.field}: ${e.defaultMessage}`);
                //add error to corresponding span  that displays errors
                document.getElementById(e.field+"_error").innerHTML = e.defaultMessage;
                });
            } else {
            errBody = await res.text();
            //display error when adding student records in console
            console.error('Server error:', errBody);
            if(errBody.includes('email')){
                document.getElementById("email_error").textContent = errBody;
                
            }
            else if(errBody.includes('18')){
                document.getElementById("dob_error").textContent = errBody;
            }
            else if(errBody.includes('alphanumeric')){
                document.getElementById("password_error").textContent=errBody
            }
            else{
                const serverResponse = {false: "Failed to add student"};
                //display error on webpage for user
                displayServerMessage(serverResponse);
            }
            }
            return;
        }

        // Success path: parse JSON or text
        if (contentType.includes('application/json')) {
            const result = await res.json();
            console.log('Success (JSON):', result);
        } else {
            const text = await res.text();
            console.log('Success (text):', text);

            
        const serverResponse = {
        true: "Student Added Successfully"};
        //display success message on webpage for user
        displayServerMessage(serverResponse);
        document.getElementById('addStudentForm').reset();//clear all fields in the form

        }

        } catch (networkError) {
        console.error('Fetch failed:', networkError);
        }
    });

    
});



//wait until page loads
document.addEventListener('DOMContentLoaded', () => {

    const form = document.getElementById('addModuleForm');

    form.addEventListener('submit', async (evt) => {
        // Clear all existing error messages before a new submission
        clearAllSpansInForm('addModuleForm');
        evt.preventDefault(); // Prevent page from refreshing on form submission
 
        const data = {
            moduleCode: document.getElementById("mCodeID").value,
            moduleName: document.getElementById("mNameID").value,
            credits: parseInt(document.getElementById("creditsID").value, 10)
            
        };

        try {
            const res = await fetch(form.action, {
                method: form.method,
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify(data)
            });

            const contentType = res.headers.get('Content-Type') || '';
            const responseBody = contentType.includes('application/json') ? await res.json() : await res.text();

            if (!res.ok) {
                // Handle 4xx/5xx errors
                if (contentType.includes('application/json') && responseBody.errors) {
                    // This block handles the detailed validation errors from the backend
                    responseBody.errors.forEach(e => {
                        console.error(`${e.field}: ${e.defaultMessage}`);
                        const errorSpan = document.getElementById(e.field + "_error");
                        if (errorSpan) {
                            errorSpan.textContent = e.defaultMessage;
                        }
                    });
                } else {
                    // This block handles other types of errors, like a simple string response
                    console.error('Server error:', responseBody);
                    const serverResponse = {false:responseBody};
                    displayServerMessage(serverResponse);
                }
                return;
            }

            // Success path: The response status is 200 OK
            if (responseBody) {
                // The backend response body contains the success message
                console.log('Success:', responseBody);
                const serverResponse = {true:responseBody};
                displayServerMessage(serverResponse);
                document.getElementById('addModuleForm').reset(); // Clear all fields in the form
            } 

        } catch (networkError) {
            console.error('Fetch failed:', networkError);
            const networkErrorSpan = document.createElement('span');
            networkErrorSpan.className = 'form_error';
            networkErrorSpan.textContent = 'Network error. Please try again later.';
            form.prepend(networkErrorSpan);
        }
    });
});



function clearAllSpansInForm(elementID) {
    const form = document.getElementById(elementID);
    if (!form) return; // no such form on the page

    // select all span elements inside the form
    const spans = form.querySelectorAll('span');

    // clear each one’s text
    spans.forEach(span => {
        span.textContent = '';
    });
}


let serverMessageTimeoutID; // Global or scoped outside the function

function displayServerMessage(serverMessage) {
    const serverDiv = document.getElementById("server_msg");

    // Extract the only key/value pair
    const [[key, message]] = Object.entries(serverMessage);
    const isSuccess = key === "true";

    // If message is already visible, cancel the previous timeout
    if (serverDiv.style.display === "block" && serverMessageTimeoutID) {
        clearTimeout(serverMessageTimeoutID);
    }

    // Set text and styles
    serverDiv.textContent = message;
    const color = isSuccess ? "green" : "red";
    serverDiv.style.borderColor = color;
    serverDiv.style.color = color;

    // Show the message
    serverDiv.style.display = "block";

    // Start new timeout and store ID
    serverMessageTimeoutID = setTimeout(() => {
        serverDiv.style.display = "none";
        serverMessageTimeoutID = null; // Reset the ID
    }, 5000);
}



function deleteStudent(studentID, rowElement) {
    fetch(`/deleteStudent?studentID=${encodeURIComponent(studentID)}`, {
        method: 'DELETE'
    })
    .then(response => response.text().then(message => {
        const container = document.getElementById('student_table');

        if (response.ok) {
            const serverResponse = { true: message };
            displayServerMessage(serverResponse); // Displays: "Successfully Removed student"

            // Remove the row
            rowElement.remove();

            // Check if table has any remaining data rows
            const table = container.querySelector('table');
            if (table) {
                const remainingRows = table.querySelectorAll('tr').length;

                // If only the header remains (or nothing), remove the table
                if (remainingRows <= 1) {
                    container.innerHTML = ''; // Remove the table

                    const span = document.createElement('span');
                    span.className = 'form_error';
                    span.textContent = "All Students were removed";
                    container.appendChild(span);
                }
            }
        } else {
            const serverResponse = { false: message };
            displayServerMessage(serverResponse); // Displays: error in bottom left of pge to user
        }
    }))
    .catch(error => {
        const serverResponse = { true: error };
        displayServerMessage(serverResponse);
    });
}



