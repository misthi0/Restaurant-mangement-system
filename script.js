const API_BASE = '/api';
let authToken = localStorage.getItem('authToken');
let currentUser = JSON.parse(localStorage.getItem('currentUser'));
let cart = [];

// Initialize app
document.addEventListener('DOMContentLoaded', () => {
    if (authToken && currentUser) {
        updateAuthUI();
    }
    loadMenu();
});

// Section Navigation
function showSection(sectionName) {
    document.querySelectorAll('.section').forEach(section => {
        section.classList.remove('active');
    });
    document.getElementById(sectionName + 'Section').classList.add('active');

    if (sectionName === 'menu') loadMenu();
    if (sectionName === 'order') loadOrderMenu();
    if (sectionName === 'inventory') loadInventory();
}

// Authentication
document.getElementById('loginForm').addEventListener('submit', async (e) => {
    e.preventDefault();
    const username = document.getElementById('loginUsername').value;
    const password = document.getElementById('loginPassword').value;

    try {
        const response = await axios.post(`${API_BASE}/auth/login`, { username, password });
        authToken = response.data.token;
        currentUser = { username: response.data.username, role: response.data.role };
        localStorage.setItem('authToken', authToken);
        localStorage.setItem('currentUser', JSON.stringify(currentUser));
        updateAuthUI();
        showSection('home');
        alert('Login successful!');
    } catch (error) {
        alert('Login failed: ' + (error.response?.data?.message || error.message));
    }
});

document.getElementById('registerForm').addEventListener('submit', async (e) => {
    e.preventDefault();
    const data = {
        username: document.getElementById('regUsername').value,
        email: document.getElementById('regEmail').value,
        password: document.getElementById('regPassword').value,
        fullName: document.getElementById('regFullName').value,
        phone: document.getElementById('regPhone').value,
        role: 'CUSTOMER'
    };

    try {
        const response = await axios.post(`${API_BASE}/auth/register`, data);
        alert('Registration successful! Please login.');
        showSection('login');
    } catch (error) {
        alert('Registration failed: ' + (error.response?.data?.message || error.message));
    }
});

function updateAuthUI() {
    document.getElementById('authButtons').style.display = 'none';
    document.getElementById('userInfo').style.display = 'block';
    document.getElementById('username').textContent = currentUser.username;

    if (currentUser.role === 'ADMIN' || currentUser.role === 'MANAGER') {
        document.getElementById('inventoryLink').style.display = 'inline';
    }
}

function logout() {
    localStorage.removeItem('authToken');
    localStorage.removeItem('currentUser');
    authToken = null;
    currentUser = null;
    document.getElementById('authButtons').style.display = 'block';
    document.getElementById('userInfo').style.display = 'none';
    document.getElementById('inventoryLink').style.display = 'none';
    showSection('home');
}

// Menu
async function loadMenu(category = 'all') {
    try {
        const url = category === 'all' 
            ? `${API_BASE}/menu` 
            : `${API_BASE}/menu/category/${category}`;
        const response = await axios.get(url);
        displayMenu(response.data);
    } catch (error) {
        console.error('Error loading menu:', error);
    }
}

function displayMenu(items) {
    const container = document.getElementById('menuItems');
    container.innerHTML = items.map(item => `
        <div class="menu-item">
            <img src="${item.imageUrl || 'https://via.placeholder.com/300x200'}" alt="${item.name}">
            <div class="menu-item-content">
                <h3>${item.name}</h3>
                <p>${item.description || ''}</p>
                <div class="price">₹${item.price.toFixed(2)}</div>
                <span class="badge">${item.category}</span>
            </div>
        </div>
    `).join('');
}

function filterMenu(category) {
    document.querySelectorAll('.category-filter button').forEach(btn => {
        btn.classList.remove('active');
    });
    event.target.classList.add('active');
    loadMenu(category);
}

// Reservations
document.getElementById('reservationForm').addEventListener('submit', async (e) => {
    e.preventDefault();
    const data = {
        customerName: document.getElementById('resName').value,
        customerEmail: document.getElementById('resEmail').value,
        customerPhone: document.getElementById('resPhone').value,
        reservationDateTime: document.getElementById('resDateTime').value,
        numberOfGuests: parseInt(document.getElementById('resGuests').value),
        specialRequests: document.getElementById('resRequests').value
    };

    try {
        await axios.post(`${API_BASE}/reservations`, data);
        alert('Reservation created successfully!');
        e.target.reset();
    } catch (error) {
        alert('Failed to create reservation: ' + (error.response?.data?.message || error.message));
    }
});

// Orders
async function loadOrderMenu() {
    try {
        const response = await axios.get(`${API_BASE}/menu/available`);
        displayOrderMenu(response.data);
    } catch (error) {
        console.error('Error loading order menu:', error);
    }
}

function displayOrderMenu(items) {
    const container = document.getElementById('orderMenuItems');
    container.innerHTML = items.map(item => `
        <div class="order-item">
            <div>
                <strong>${item.name}</strong>
                <div>₹${item.price.toFixed(2)}</div>
            </div>
            <button onclick="addToCart(${item.id}, '${item.name}', ${item.price})" class="btn-primary">Add</button>
        </div>
    `).join('');
}

function addToCart(id, name, price) {
    const existing = cart.find(item => item.menuItemId === id);
    if (existing) {
        existing.quantity++;
    } else {
        cart.push({ menuItemId: id, name, price, quantity: 1 });
    }
    updateCart();
}

function removeFromCart(id) {
    cart = cart.filter(item => item.menuItemId !== id);
    updateCart();
}

function updateQuantity(id, delta) {
    const item = cart.find(item => item.menuItemId === id);
    if (item) {
        item.quantity += delta;
        if (item.quantity <= 0) {
            removeFromCart(id);
        } else {
            updateCart();
        }
    }
}

function updateCart() {
    const container = document.getElementById('cartItems');
    const total = cart.reduce((sum, item) => sum + (item.price * item.quantity), 0);

    container.innerHTML = cart.map(item => `
        <div class="cart-item">
            <div>
                <strong>${item.name}</strong>
                <div class="quantity-control">
                    <button onclick="updateQuantity(${item.menuItemId}, -1)" class="btn-secondary">-</button>
                    <span>${item.quantity}</span>
                    <button onclick="updateQuantity(${item.menuItemId}, 1)" class="btn-secondary">+</button>
                </div>
            </div>
            <div>
                <div>₹${(item.price * item.quantity).toFixed(2)}</div>
                <button onclick="removeFromCart(${item.menuItemId})" class="btn-secondary">Remove</button>
            </div>
        </div>
    `).join('');

    document.getElementById('orderTotal').textContent = total.toFixed(2);
}

async function placeOrder() {
    if (cart.length === 0) {
        alert('Cart is empty!');
        return;
    }

    const data = {
        customerId: currentUser ? null : null,
        tableNumber: parseInt(document.getElementById('tableNumber').value) || null,
        orderType: document.getElementById('orderType').value,
        items: cart.map(item => ({
            menuItemId: item.menuItemId,
            quantity: item.quantity
        })),
        specialInstructions: document.getElementById('orderInstructions').value
    };

    try {
        await axios.post(`${API_BASE}/orders`, data);
        alert('Order placed successfully!');
        cart = [];
        updateCart();
        document.getElementById('orderInstructions').value = '';
        document.getElementById('tableNumber').value = '';
    } catch (error) {
        alert('Failed to place order: ' + (error.response?.data?.message || error.message));
    }
}

// Inventory
async function loadInventory() {
    try {
        const response = await axios.get(`${API_BASE}/inventory`);
        displayInventory(response.data);
    } catch (error) {
        console.error('Error loading inventory:', error);
    }
}

function displayInventory(items) {
    const container = document.getElementById('inventoryItems');
    container.innerHTML = `
        <table>
            <thead>
                <tr>
                    <th>Item Name</th>
                    <th>Quantity</th>
                    <th>Unit</th>
                    <th>Min Threshold</th>
                    <th>Supplier</th>
                    <th>Status</th>
                </tr>
            </thead>
            <tbody>
                ${items.map(item => `
                    <tr class="${item.quantity <= item.minThreshold ? 'low-stock' : ''}">
                        <td>${item.itemName}</td>
                        <td>${item.quantity}</td>
                        <td>${item.unit}</td>
                        <td>${item.minThreshold}</td>
                        <td>${item.supplier || 'N/A'}</td>
                        <td>${item.quantity <= item.minThreshold ? '⚠️ Low Stock' : '✓ Good'}</td>
                    </tr>
                `).join('')}
            </tbody>
        </table>
    `;
}

function showAddInventory() {
    document.getElementById('addInventoryModal').style.display = 'block';
}

function closeAddInventory() {
    document.getElementById('addInventoryModal').style.display = 'none';
}

document.getElementById('inventoryForm').addEventListener('submit', async (e) => {
    e.preventDefault();
    const data = {
        itemName: document.getElementById('invItemName').value,
        quantity: parseInt(document.getElementById('invQuantity').value),
        unit: document.getElementById('invUnit').value,
        minThreshold: parseInt(document.getElementById('invMinThreshold').value),
        supplier: document.getElementById('invSupplier').value
    };

    try {
        await axios.post(`${API_BASE}/inventory`, data);
        alert('Inventory item added successfully!');
        closeAddInventory();
        e.target.reset();
        loadInventory();
    } catch (error) {
        alert('Failed to add inventory: ' + (error.response?.data?.message || error.message));
    }
});