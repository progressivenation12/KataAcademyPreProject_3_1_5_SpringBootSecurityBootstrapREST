let formNew = document.forms["formNew"];

createNewUser();

function createNewUser() {
    formNew.addEventListener("submit", ev => {
        ev.preventDefault();

        //приводим роли к виду java для отправки в БД
        let rolesForNewUser = [];
        for (let i = 0; i < formNew.roleSet.options.length; i++) {
            if (formNew.roleSet.options[i].selected)
                rolesForNewUser.push({
                    id: formNew.roleSet.options[i].value,
                    role: "ROLE_" + formNew.roleSet.options[i].text
                });
        }

        fetch("http://localhost:8080/api/admin/new-user/", {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify({
                username: formNew.username.value,
                surname: formNew.surname.value,
                age: formNew.age.value,
                email: formNew.email.value,
                password: formNew.password.value,
                roles: rolesForNewUser
            })
        }).then(() => {
            formNew.reset();
            getAllUsers();
            $('#usersTable').click(); //клик по кнопке Users Table

        });
    });
}

function loadRolesForNewUser() {
    let selectAdd = document.getElementById("create-roles");

    selectAdd.innerHTML = "";

    fetch("http://localhost:8080/api/admin/roles")
        .then(res => res.json())
        .then(data => {
            data.forEach(role => {
                let option = document.createElement("option");
                option.value = role.id;
                option.text = role["roleName"].toString().replace('ROLE_', '');
                selectAdd.appendChild(option);
            });
        })
        .catch(error => console.error(error));
}

window.addEventListener("load", loadRolesForNewUser);
