// Sistema de Navegação SPA
function navigateTo(pageId) {
    document.querySelectorAll('.page').forEach(page => {
        page.classList.remove('active-page');
    });
    
    const page = document.getElementById(`${pageId}-page`);
    if (page) {
        page.classList.add('active-page');
        window.scrollTo(0, 0);
    }
}

// Simulação de banco de dados
let users = JSON.parse(localStorage.getItem('users')) || [
    { name: 'Admin', email: 'admin@example.com', password: '21232f297a57a5a743894a0e4a801fc3' } // admin123 em MD5
];

let currentUser = JSON.parse(sessionStorage.getItem('currentUser')) || null;

// Função de hash simples (MD5 simulada para exemplo)
function simpleHash(str) {
    if (str === 'admin123') return '21232f297a57a5a743894a0e4a801fc3';
    let hash = 0;
    for (let i = 0; i < str.length; i++) {
        const char = str.charCodeAt(i);
        hash = (hash << 5) - hash + char;
        hash |= 0;
    }
    return hash.toString();
}

// Validação de e-mail
function validateEmail(email) {
    const re = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
    return re.test(email);
}

// Funções auxiliares
function clearMessages() {
    document.querySelectorAll('.message').forEach(msg => {
        msg.style.display = 'none';
    });
}

function showMessage(element, message, isSuccess) {
    element.textContent = message;
    element.style.display = 'block';
    element.className = isSuccess ? 'message success' : 'message error';
}

function setLoading(button, isLoading) {
    button.disabled = isLoading;
    if (isLoading) {
        button.classList.add('loading');
    } else {
        button.classList.remove('loading');
    }
}

function togglePassword(id) {
    const input = document.getElementById(id);
    if (input) {
        input.type = input.type === 'password' ? 'text' : 'password';
    }
}

// Configura navegação para todas as páginas
function setupNavigation() {
    document.querySelectorAll('.nav-link').forEach(link => {
        link.addEventListener('click', (e) => {
            e.preventDefault();
            const page = link.getAttribute('data-page');
            navigateTo(page);
        });
    });
}

// Verifica se há usuário logado ao carregar
document.addEventListener('DOMContentLoaded', () => {
    setupNavigation();
    
    if (currentUser) {
        document.getElementById('welcome-message').textContent = `Olá, ${currentUser.name}!`;
        navigateTo('home');
    } else {
        navigateTo('login');
    }
});

// Login
document.getElementById('login-btn').addEventListener('click', async function() {
    const email = document.getElementById('login-email').value;
    const password = document.getElementById('login-password').value;
    const messageElement = document.getElementById('login-message');
    const button = this;

    clearMessages();
    
    if (!validateEmail(email)) {
        showMessage(messageElement, 'Por favor, insira um e-mail válido.', false);
        return;
    }

    if (password.length < 6) {
        showMessage(messageElement, 'A senha deve ter pelo menos 6 caracteres.', false);
        return;
    }

    setLoading(button, true);
    
    await new Promise(resolve => setTimeout(resolve, 1000));
    
    const user = users.find(u => u.email === email && u.password === simpleHash(password));
    
    if (user) {
        showMessage(messageElement, 'Login bem-sucedido!', true);
        currentUser = user;
        sessionStorage.setItem('currentUser', JSON.stringify(user));
        document.getElementById('welcome-message').textContent = `Olá, ${user.name}!`;
        setTimeout(() => navigateTo('home'), 1500);
    } else {
        showMessage(messageElement, 'E-mail ou senha incorretos.', false);
    }
    
    setLoading(button, false);
});

// Registro
document.getElementById('signup-btn').addEventListener('click', async function() {
    const name = document.getElementById('signup-name').value;
    const email = document.getElementById('signup-email').value;
    const password = document.getElementById('signup-password').value;
    const confirmPassword = document.getElementById('signup-confirm-password').value;
    const messageElement = document.getElementById('signup-message');
    const button = this;

    clearMessages();
    
    if (!name || !email || !password || !confirmPassword) {
        showMessage(messageElement, 'Por favor, preencha todos os campos.', false);
        return;
    }

    if (!validateEmail(email)) {
        showMessage(messageElement, 'Por favor, insira um e-mail válido.', false);
        return;
    }

    if (password.length < 6) {
        showMessage(messageElement, 'A senha deve ter pelo menos 6 caracteres.', false);
        return;
    }

    if (password !== confirmPassword) {
        showMessage(messageElement, 'As senhas não coincidem.', false);
        return;
    }

    if (users.some(u => u.email === email)) {
        showMessage(messageElement, 'Este e-mail já está registrado.', false);
        return;
    }

    setLoading(button, true);
    
    await new Promise(resolve => setTimeout(resolve, 1000));
    
    const newUser = { 
        name, 
        email, 
        password: simpleHash(password)
    };
    
    users.push(newUser);
    localStorage.setItem('users', JSON.stringify(users));

    showMessage(messageElement, 'Registro bem-sucedido! Você pode fazer login agora.', true);
    
    setTimeout(() => {
        document.getElementById('signup-name').value = '';
        document.getElementById('signup-email').value = '';
        document.getElementById('signup-password').value = '';
        document.getElementById('signup-confirm-password').value = '';
        navigateTo('login');
    }, 2000);
    
    setLoading(button, false);
});

// Recuperação de senha
document.getElementById('forgot-btn').addEventListener('click', async function() {
    const email = document.getElementById('forgot-email').value;
    const messageElement = document.getElementById('forgot-message');
    const button = this;

    if (!email) {
        showMessage(messageElement, 'Por favor, insira seu e-mail.', false);
        return;
    }

    if (!validateEmail(email)) {
        showMessage(messageElement, 'Por favor, insira um e-mail válido.', false);
        return;
    }

    setLoading(button, true);
    
    await new Promise(resolve => setTimeout(resolve, 1000));
    
    if (!users.some(u => u.email === email)) {
        showMessage(messageElement, 'E-mail não encontrado.', false);
        setLoading(button, false);
        return;
    }

    showMessage(messageElement, 'Um link de redefinição foi enviado para o seu e-mail.', true);
    
    setTimeout(() => {
        document.getElementById('forgot-email').value = '';
        navigateTo('login');
    }, 2000);
    
    setLoading(button, false);
});

// Logout
document.getElementById('logout-btn').addEventListener('click', (e) => {
    e.preventDefault();
    currentUser = null;
    sessionStorage.removeItem('currentUser');
    navigateTo('login');
    
    document.getElementById('login-email').value = '';
    document.getElementById('login-password').value = '';
    clearMessages();
});
