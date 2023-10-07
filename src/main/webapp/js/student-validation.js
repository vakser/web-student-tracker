function validateForm() {
    let theErrorFields = [];
    let studentForm = document.forms["studentForm"];
    let firstName = studentForm["firstName"].value.trim();
    if (firstName === "") {
        theErrorFields.push("First name");
    }
    let lastName = studentForm["lastName"].value.trim();
    if (lastName === "") {
        theErrorFields.push("Last name");
    }
    let email = studentForm["email"].value.trim();
    if (email === "") {
        theErrorFields.push("Email");
    }
    if (theErrorFields.length > 0) {
        alert("Form validation failed. Please add data for the following fields: " + theErrorFields);
        return false;
    }
}
