const URLNavbarUser = "/api/user/accountInfo";
const navbarBrandUser = document.getElementById('navbarBrandUser');
const tableUserUser = document.getElementById('tableUser');

function getCurrentUser() {
    fetch(URLNavbarUser)
        .then((res) => res.json())
        .then((user) => {

            let rolesStringUser = rolesToStringForUser(user.roleSet);
            let dataOfUser = '';

            dataOfUser += `<tr>
            <td>${user.id}</td>
            <td>${user.userName}</td>
            <td>${user.age}</td>
            <td>${user.email}</td>
            <td>${rolesStringUser}</td>
            </tr>`;
            tableUserUser.innerHTML = dataOfUser;

            navbarBrandUser.innerHTML = `<b><span class="email">${user.email}</span></b>
                             <span class="roles">with roles:</span>
                             <span>${rolesStringUser}</span>`;
        });
}

getCurrentUser()

function rolesToStringForUser(roles) {
    let rolesString = '';

    for (const element of roles) {
        if (roles.length > 1) {
            rolesString += (element.roleName.toString().replace('ROLE_', '') + ', ');
        } else {
            rolesString += (element.roleName.toString().replace('ROLE_', ''));
        }
    }

    if (rolesString.charAt(rolesString.length - 2) === ',') {
        rolesString = rolesString.substring(0, rolesString.length - 2);
    }

    return rolesString;
}