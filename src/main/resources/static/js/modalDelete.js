function loadRolesForDelete() {
    let selectDelete = document.getElementById("delete-roleSet");
    selectDelete.innerHTML = "";

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
                selectDelete.appendChild(option);
            });
        })
        .catch(error => console.error(error));
}

let formDelete = document.forms["formDelete"]
deleteUser();

const URLDelete = "/api/admin/";

async function deleteModal(id) {
    const modalDelete = new bootstrap.Modal(document.querySelector('#deleteModal'));
    await fillModalFields(formDelete, modalDelete, id, "delete");
}

function deleteUser() {
    formDelete.addEventListener("submit", ev => {
        ev.preventDefault();

        fetch(URLDelete + formDelete.id.value, {
            method: 'DELETE',
            headers: {
                'Content-Type': 'application/json'
            }
        }).then(() => {
            const modalEdit = bootstrap.Modal.getInstance(document.querySelector('#deleteModal'));
            modalEdit.hide();
            getAllUsers();
        });
    });
}

window.addEventListener("load", loadRolesForDelete);