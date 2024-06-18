function loadRolesForNewUser() {
    let selectAdd = document.getElementById("create-roleSet");
    selectAdd.innerHTML = "";

    fetch("/api/admin/roles")
        .then(res => res.json())
        .then(data => {
            data.sort((a, b) => {
                if (a.roleName.includes('ADMIN') && b.roleName.includes('USER')) {
                    return -1;
                }
                if (a.roleName.includes('USER') && b.roleName.includes('ADMIN')) {
                    return 1;
                }
                return 0;
            });
            data.forEach(role => {
                let option = document.createElement("option");
                option.value = role.id;
                option.text = role.roleName.toString().replace('ROLE_', '');

                selectAdd.appendChild(option);
            });
        })
        .catch(error => console.error(error));
}

let formNew = document.forms["formNew"];

createNewUser();

const URLNewUser = "api/admin/new-user/";

function createNewUser() {
    const csrfToken = getCsrfToken();

    formNew.addEventListener("submit", ev => {
        ev.preventDefault();

        let rolesForNewUser = [];
        for (let i = 0; i < formNew.roleSet.options.length; i++) {
            if (formNew.roleSet.options[i].selected) rolesForNewUser.push({
                id: formNew.roleSet.options[i].value,
                roleName: "ROLE_" + formNew.roleSet.options[i].text
            });
        }

        let userData = {
            userName: formNew.querySelector("#create-userName").value,
            age: formNew.querySelector("#create-age").value,
            email: formNew.querySelector("#create-email").value,
            password: formNew.querySelector("#create-password").value,
            roleSet: rolesForNewUser
        };

        fetch(URLNewUser, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
                'X-XSRF-TOKEN': csrfToken
            },
            body: JSON.stringify(userData)
        }).then(response => {
            if (!response.ok) {
                return response.json().then(errors => {
                    showValidationErrors(errors, "create");
                    throw new Error("Validation failed");
                });
            }
            formNew.reset();

            clearValidationErrors("create");

            getAllUsers();
            $('#nav-users-tab').click();
        }).catch(error => {
            console.error("Error:", error);
        });
    });
}

// Находим модальное окно
let AddNewUserButton = document.getElementById('submit-addNewUser-button');

// Добавляем обработчик события click
AddNewUserButton.addEventListener('click', function () {
    // Удаление класса "is-invalid" у всех элементов
    let invalidElements = document.querySelectorAll(".is-invalid");
    invalidElements.forEach(function (element) {
        element.classList.remove("is-invalid");
    });
    // Очистка текста ошибки
    let errorElements = document.querySelectorAll(".error-message");
    errorElements.forEach(function (element) {
        element.innerText = "";
    });
});

window.addEventListener("load", loadRolesForNewUser);
