function getUserById(id) {
    return fetch('/api/admin/user/' + id)
        .then(function(response) {
            return response.json();
        });
}

function fillModalFields(form, modal, id) {
    getUserById(id).then(user => {
        form.querySelector("#edit-id").value = user.id; // Видимое поле, отключенное
        form.querySelector("#hidden-id").value = user.id; // Скрытое поле
        form.querySelector("#edit-username").value = user.userName;
        form.querySelector("#edit-age").value = user.age;
        form.querySelector("#edit-email").value = user.email;
        form.querySelector("#edit-password").value = user.password;

        loadRolesForEdit(user.roleSet);// Передаем выбранные роли
        modal.show();
    });
}