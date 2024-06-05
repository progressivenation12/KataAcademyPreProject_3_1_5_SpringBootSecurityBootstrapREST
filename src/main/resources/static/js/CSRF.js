function addCsrfTokenToForm(formId) {
    // Получаем токен CSRF из мета-тега
    const csrfToken = document.querySelector('meta[name="_csrf"]').content;
    const csrfParameterName = document.querySelector('meta[name="_csrf_parameter"]').content;

    // Находим форму по ее ID
    const form = document.getElementById(formId);

    // Создаем скрытое поле с токеном CSRF и добавляем его в форму
    const csrfInput = document.createElement('input');
    csrfInput.type = 'hidden';
    csrfInput.name = csrfParameterName;
    csrfInput.value = csrfToken;
    form.appendChild(csrfInput);
}
