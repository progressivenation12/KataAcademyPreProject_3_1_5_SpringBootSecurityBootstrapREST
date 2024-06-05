function loadRolesForEdit(selectedRoles = []) {
    let selectEdit = document.getElementById("edit-roleSet");
    selectEdit.innerHTML = "";

    fetch("/api/admin/roles")
        .then(res => res.json())
        .then(data => {
            data.forEach(role => {
                let option = document.createElement("option");
                option.value = role.id;
                option.text = role["roleName"].replace('ROLE_', '');

                // Проверка, является ли selectedRoles массивом и содержит ли он выбранные роли
                if (Array.isArray(selectedRoles) && selectedRoles.includes(role.roleName)) {
                    option.selected = true;
                }
                selectEdit.appendChild(option);
            });
        })
        .catch(error => console.error(error));
}

let formEdit = document.forms["formEdit"];
editUser();

const URLEdit = "/api/admin/update-user";

async function editModal(id) {
    const modalEdit = new bootstrap.Modal(document.querySelector('#editModal'));
    await fillModalFields(formEdit, modalEdit, id);

    // Получаем пользователя для заполнения формы и выбранных ролей
    // let user = await getUserById(id);
    // await fillModalFields(formEdit, modalEdit, id)
    // loadRolesForEdit(user.roleSet); // Передаем выбранные роли
}

function editUser() {
    formEdit.addEventListener("submit", ev => {
        ev.preventDefault();

        //Приведение ролей из вида js к виду java
        let roleSetForEdit = [];
        for (let i = 0; i < formEdit.roleSet.options.length; i++) {
            if (formEdit.roleSet.options[i].selected) roleSetForEdit.push({
                id: formEdit.roleSet.options[i].value,
                roleName: "ROLE_" + formEdit.roleSet.options[i].text
            });
        }

        let userData = {
            id: formEdit.querySelector("#edit-id").value,
            userName: formEdit.querySelector("#edit-username").value,
            age: formEdit.querySelector("#edit-age").value,
            email: formEdit.querySelector("#edit-email").value,
            password: formEdit.querySelector("#edit-password").value,
            roleSet: roleSetForEdit
        };

        fetch(URLEdit, {
            method: 'PUT',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(userData)
        }).then(response => {
            if (!response.ok) {
                return response.json().then(errorData => {
                    throw new Error(`Error ${response.status}: ${errorData.message}`);
                });
            }
            document.getElementById('editClose').click();
            getAllUsers();
        }).catch(error => {
            console.error('Error:', error);
        });
    });
}

window.addEventListener("load", loadRolesForEdit);