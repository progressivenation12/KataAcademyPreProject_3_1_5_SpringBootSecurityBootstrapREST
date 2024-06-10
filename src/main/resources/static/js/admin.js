const URLNavbarAdmin = "/api/admin/accountInfo";
const navbarBrandAdmin = document.getElementById('navbarBrandAdmin');
const tableUserAdmin = document.getElementById('tableAdmin');

function getCurrentAdmin() {
    fetch(URLNavbarAdmin)
        .then((res) => res.json())
        .then((userAdmin) => {

            let rolesStringAdmin = rolesToStringForAdmin(userAdmin.roleSet);
            let data = '';

            data += `<tr>
            <td>${userAdmin.id}</td>
            <td>${userAdmin.userName}</td>
            <td>${userAdmin.age}</td>
            <td>${userAdmin.email}</td>
            <td>${rolesStringAdmin}</td>
            </tr>`;
            tableUserAdmin.innerHTML = data;

            navbarBrandAdmin.innerHTML = `<b><span class="email">${userAdmin.email}</span></b>
                             <span class="roles">with roles:</span>
                             <span>${rolesStringAdmin}</span>`;
        });
}

getCurrentAdmin()

function rolesToStringForAdmin(roles) {
    let rolesString = '';

    for (const element of roles) {
        rolesString += (element.toString().replace('ROLE_', '') + ', ');
    }
    rolesString = rolesString.substring(0, rolesString.length - 2);
    return rolesString;
}

function showValidationErrors(errors, formPrefix) {
    // Очистка предыдущих ошибок
    document.querySelectorAll(`#${formPrefix} .invalid-feedback`).forEach(el => el.innerText = "");
    document.querySelectorAll(`#${formPrefix} .form-control, #${formPrefix} .form-select`).forEach(el => el.classList.remove("is-invalid"));

    for (const field in errors) {
        const errorElement = document.getElementById(`${formPrefix}-${field}-error`);
        const fieldElement = document.getElementById(`${formPrefix}-${field}`);
        if (errorElement && fieldElement) {
            errorElement.innerText = errors[field];
            fieldElement.classList.add("is-invalid");
        }
    }
}

function clearValidationErrors(formPrefix) {
    document.querySelectorAll(`#${formPrefix} .invalid-feedback`).forEach(el => el.innerText = "");
    document.querySelectorAll(`#${formPrefix} .form-control, #${formPrefix} .form-select`).forEach(el => el.classList.remove("is-invalid"));
}
