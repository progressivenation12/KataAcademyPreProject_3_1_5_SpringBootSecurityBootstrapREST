function getUserById(id) {
    return fetch('/api/admin/user/' + id)
        .then(function(response) {
            return response.json();
        });
}

function fillModalFields(form, modal, id, type) {
    getUserById(id).then(user => {
        if (type === "edit") {
            form.querySelector("#edit-id").value = user.id;
            form.querySelector("#hidden-id").value = user.id;
            form.querySelector("#edit-userName").value = user.userName;
            form.querySelector("#edit-age").value = user.age;
            form.querySelector("#edit-email").value = user.email;
            form.querySelector("#edit-password").value = user.password;

            // Загружаем роли только для редактирования
            loadRolesForEdit(user.roleSet);
        } else if (type === "delete") {
            form.querySelector("#delete-id").value = user.id;
            form.querySelector("#delete-userName").value = user.userName;
            form.querySelector("#delete-age").value = user.age;
            form.querySelector("#delete-email").value = user.email;
            form.querySelector("#delete-password").value = user.password;

            // Не нужно загружать роли для удаления
        }

        modal.show();
    });
}