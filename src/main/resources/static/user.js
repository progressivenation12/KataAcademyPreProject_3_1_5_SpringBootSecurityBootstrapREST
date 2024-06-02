$(function () {
    getCurrentUser();
    setupSidebar();
})
function getCurrentUser() {

    fetch("/api/user/info").then(res => res.json())
        .then(data => {
            let currentUser = data;
            const tableBody = document.getElementById("currentUserTableBody");
            tableBody.innerHTML = '';

            console.log(data)
            console.log(JSON.stringify(currentUser, null, 2));

            let rowContent = `
                        <tr>
                            <td>${currentUser.id}</td>
                            <td>${currentUser.userName}</td>
                            <td>${currentUser.age}</td>
                            <td>${currentUser.email}</td>
                            <td>${currentUser.roleSet.map(role => role.replace('ROLE_', '')).join(", ")}</td>
                        </tr>
                `;
            tableBody.innerHTML += rowContent;

            document.getElementById("headerUsername").innerText = currentUser.userName;
            document.getElementById("headerUserRoles").innerText = currentUser.roleSet.map(role => role.replace('ROLE_', '')).join(", ");
        });
}

function setupSidebar() {
    fetch("/api/user/info")
        .then(response => response.json())
        .then(currentUser => {
            const sidebar = document.getElementById("sidebar");
            sidebar.innerHTML = ''; // Очистить существующие ссылки

            const currentPath = window.location.pathname;

            if (currentUser["roleSet"].includes("ROLE_ADMIN")) {
                sidebar.innerHTML += `<a href="/admin" class="${currentPath === '/admin' ? 'active' : ''}">Admin</a>`;
                sidebar.innerHTML += `<a href="/user" class="${currentPath === '/user' ? 'active' : ''}">User</a>`;
            } else if (currentUser["roleSet"].includes("ROLE_USER")) {
                sidebar.innerHTML += `<a href="/user" class="${currentPath === '/user' ? 'active' : ''}">User</a>`;
            }
        })
        .catch(error => console.error('Error fetching user data:', error));
}