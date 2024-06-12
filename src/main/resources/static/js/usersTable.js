const URLTableUsers = "/api/admin/all-users";

getAllUsers();

function getAllUsers() {
    fetch(URLTableUsers)
        .then(function (response) { return response.json();
        })
        .then(function (users) {
            let dataOfUsers = '';
            let rolesString = '';

            const tableUsers = document.getElementById('tableUsers');

            for (let user of users) {

                rolesString = rolesToStringForAdmin(user.roleSet);

                dataOfUsers += `<tr>
                        <td>${user.id}</td>
                        <td>${user.userName}</td>
                        <td>${user.age}</td>
                        <td>${user.email}</td>
                        <td>${rolesString}</td>

                        <td>
                          <button type="button"
                          class="btn custom-btn-edit btn-sm"
                          data-bs-toogle="modal"
                          data-bs-target="#editModal"
                          onclick="editModal(${user.id})">
                                Edit
                            </button>
                        </td>

                        <td>
                            <button type="button" 
                            class="btn custom-btn-delete btn-sm" 
                            data-toggle="modal" 
                            data-target="#deleteModal" 
                            onclick="deleteModal(${user.id})">
                                Delete
                            </button>
                        </td>
                    </tr>`;
            }
            tableUsers.innerHTML = dataOfUsers;
        })
}