function loadRolesForNewUser() {
    let selectAdd = document.getElementById("create-roleSet");
    selectAdd.innerHTML = "";

    fetch("/api/admin/roles")
        .then(res => res.json())
        .then(data => {
            data.forEach(role => {
                let option = document.createElement("option");
                option.value = role.id;
                option.text = role["roleName"].replace('ROLE_', '');
                selectAdd.appendChild(option);
            });
        })
        .catch(error => console.error(error));
}

let formNew = document.forms["formNew"];
createNewUser();

const URLNewUser = "api/admin/new-user/";

function createNewUser() {
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
                'Content-Type': 'application/json'
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

window.addEventListener("load", loadRolesForNewUser);