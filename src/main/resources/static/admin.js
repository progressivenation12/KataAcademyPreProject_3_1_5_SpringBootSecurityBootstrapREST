$(async function () {
    await getCurrentUser();
    await loadAllUsers();
});

async function getCurrentUser() {
    const res = await fetch("/api/user/info");
    const data = await res.json();
    let currentUser = data;
    console.log(data);
    console.log(JSON.stringify(currentUser, null, 2));

    document.getElementById("headerUsername").innerText = currentUser["userName"];
    document.getElementById("headerUserRoles").innerText = currentUser["roleSet"].map(role => role.replace('ROLE_', '')).join(", ");
}

async function loadAllUsers() {
    try {
        const response = await fetch("/api/admin/all-users");
        const data = await response.json();
        const users = data["people"];
        const roles = data["roles"];
        const tableBody = document.getElementById("usersTableBody");

        tableBody.innerHTML = ''; // Clear existing rows

        const modalContainer = document.createElement('div'); // Container for modals

        users.forEach(user => {
            let rowContent = `
                <tr id="userRow_${user.id}">
                    <td>${user.id}</td>
                    <td>${user["userName"]}</td>
                    <td>${user["age"]}</td>
                    <td>${user["email"]}</td>
                    <td>${user["roleSet"].map(role => role.replace('ROLE_', '')).join(", ")}</td>
                    <td>
                        <button type="button" class="btn custom-btn-edit btn-sm" data-bs-toggle="modal" data-bs-target="#modalEdit_${user.id}">Edit</button>
                    </td>
                    <td>
                        <button type="button" class="btn custom-btn-delete btn-sm" data-bs-toggle="modal" data-bs-target="#modalDelete_${user.id}">Delete</button>
                    </td>
                </tr>
            `;
            tableBody.innerHTML += rowContent;

            let editModalContent = `
                <div class="modal fade" id="modalEdit_${user.id}" tabindex="-1" aria-labelledby="editModalLabel_${user.id}" aria-hidden="true">
                    <div class="modal-dialog modal-dialog-centered">
                        <div class="modal-content">
                            <div class="modal-header">
                                <h4 class="modal-title fs-4" id="editModalLabel_${user.id}">Edit user</h4>
                                <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                            </div>
                            <form id="editForm_${user.id}" novalidate>
                                <div class="modal-body">
                                    <input type="hidden" name="id" value="${user.id}">
                                    <div class="mb-3">
                                        <label for="id_edit_${user.id}" class="custom-form-label mb-md-2">ID:</label>
                                        <input type="text" id="id_edit_${user.id}" name="id" value="${user.id}" class="form-control" disabled>
                                    </div>
                                    <div class="mb-3">
                                        <label for="userName_edit_${user.id}" class="custom-form-label mb-md-2">Username:</label>
                                        <input type="text" id="userName_edit_${user.id}" name="userName" value="${user["userName"]}" class="form-control" placeholder="Username" required>
                                    </div>
                                    <div class="mb-3">
                                        <label for="age_edit_${user.id}" class="custom-form-label mb-md-2">Age: </label>
                                        <input type="number" id="age_edit_${user.id}" name="age" value="${user["age"]}" class="form-control" placeholder="Age" required>
                                    </div>
                                    <div class="mb-3">
                                        <label for="email_edit_${user.id}" class="custom-form-label mb-md-2">Email: </label>
                                        <input type="email" id="email_edit_${user.id}" name="email" value="${user["email"]}" class="form-control" placeholder="Email" required>
                                    </div>
                                    <div class="mb-3">
                                        <label for="password_edit_${user.id}" class="custom-form-label mb-md-2">Password: </label>
                                        <input type="password" id="password_edit_${user.id}" name="password" value="${user["password"]}" class="form-control" placeholder="Password" required>
                                    </div>
                                    <div class="mb-3">
                                        <label for="roles_edit_${user.id}" class="custom-form-label mb-md-2">Roles: </label>
                                        <select id="roles_edit_${user.id}" name="roleSet" class="form-select" multiple="multiple" size="2" aria-label="size 2 select example">
                                            ${roles.map(role => `
                                                <option value="${role["roleName"]}" ${user["roleSet"].includes(role["roleName"]) ? 'selected' : ''}>
                                                    ${role["roleName"].substring(5)}
                                                </option>
                                            `).join('')}
                                        </select>
                                    </div>
                                </div>
                                <div class="modal-footer">
                                    <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Close</button>
                                    <button type="submit" class="btn btn-primary">Save changes</button>
                                </div>
                            </form>
                        </div>
                    </div>
                </div>
            `;
            modalContainer.innerHTML += editModalContent;
        });

        document.body.appendChild(modalContainer); // Добавьте модальное окно в конец тела

        users.forEach(user => {
            document.getElementById(`editForm_${user.id}`).addEventListener('submit', function(event) {
                event.preventDefault();
                editUser(user.id);
            });
        });
    } catch (error) {
        console.error('Error fetching users:', error);
    }
}

async function editUser(userId) {
    const form = document.getElementById(`editForm_${userId}`);
    const formData = new FormData(form);
    const user = {};

    formData.forEach((value, key) => {
        if (key === 'roleSet') {
            if (!user[key]) user[key] = [];
            user[key].push(value); // Сохранение имени роли
        } else {
            user[key] = value;
        }
    });

    console.log(user);

    try {
        const response = await fetch("/api/admin", {
            method: 'PUT',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify(user),
        });

        if (!response.ok) {
            throw new Error(`HTTP error! status: ${response.status}`);
        }

        const updatedUser = await response.json();

        // Обновление строки пользователя в таблице
        const roles = updatedUser.roleSet ? updatedUser.roleSet.map(role => role.replace('ROLE_', '')).join(", ") : '';
        document.getElementById(`userRow_${updatedUser.id}`).innerHTML = `
            <td>${updatedUser.id}</td>
            <td>${updatedUser.userName}</td>
            <td>${updatedUser.age}</td>
            <td>${updatedUser.email}</td>
            <td>${roles}</td>
            <td>
                <button type="button" class="btn custom-btn-edit btn-sm" data-bs-toggle="modal" data-bs-target="#modalEdit_${updatedUser.id}">Edit</button>
            </td>
            <td>
                <button type="button" class="btn custom-btn-delete btn-sm" data-bs-toggle="modal" data-bs-target="#modalDelete_${updatedUser.id}">Delete</button>
            </td>
        `;

        // Закрыть модальное окно
        const modal = bootstrap.Modal.getInstance(document.getElementById(`modalEdit_${userId}`));
        modal.hide();
    } catch (error) {
        console.error('Error updating user:', error);
    }
}