function getCsrfToken() {
    let csrfToken = null;
    const cookies = document.cookie.split(';');
    cookies.forEach(cookie => {
        const [name, value] = cookie.split('=');
        if (name.trim() === 'XSRF-TOKEN') {
            csrfToken = value;
        }
    });
    return csrfToken;
}