/* =============================================
   AutoDeals - Admin Panel JavaScript
   ============================================= */

document.addEventListener('DOMContentLoaded', function () {

    // ---- Sidebar Toggle ----
    const sidebarToggle = document.getElementById('sidebarToggle');
    const sidebar       = document.getElementById('adminSidebar');

    // Create overlay element
    const overlay = document.createElement('div');
    overlay.className = 'admin-overlay';
    overlay.id = 'adminOverlay';
    document.body.appendChild(overlay);

    if (sidebarToggle && sidebar) {
        sidebarToggle.addEventListener('click', function () {
            sidebar.classList.toggle('show');
            overlay.classList.toggle('show');
        });
        overlay.addEventListener('click', function () {
            sidebar.classList.remove('show');
            overlay.classList.remove('show');
        });
    }

    // ---- Auto-dismiss alerts ----
    document.querySelectorAll('.alert-dismissible').forEach(function (alert) {
        setTimeout(function () {
            const bsAlert = bootstrap.Alert.getOrCreateInstance(alert);
            if (bsAlert) bsAlert.close();
        }, 5000);
    });

    // ---- Image preview helper (used in add/edit forms) ----
    window.previewImage = function (input) {
        if (input.files && input.files[0]) {
            const file = input.files[0];
            // Validate size (max 10MB)
            if (file.size > 10 * 1024 * 1024) {
                showToast('File size exceeds 10MB limit.', 'danger');
                input.value = '';
                return;
            }
            // Validate type
            if (!file.type.startsWith('image/')) {
                showToast('Please select a valid image file.', 'danger');
                input.value = '';
                return;
            }
            const reader = new FileReader();
            reader.onload = function (e) {
                const preview        = document.getElementById('imagePreview');
                const previewWrapper = document.getElementById('imagePreviewWrapper');
                if (preview && previewWrapper) {
                    preview.src             = e.target.result;
                    previewWrapper.style.display = 'block';
                }
            };
            reader.readAsDataURL(file);
        }
    };

    // ---- Toast notification helper ----
    window.showToast = function (message, type) {
        type = type || 'info';
        const toastContainer = getOrCreateToastContainer();
        const toastEl = document.createElement('div');
        toastEl.className = 'toast align-items-center text-bg-' + type + ' border-0';
        toastEl.setAttribute('role', 'alert');
        toastEl.setAttribute('aria-live', 'assertive');
        toastEl.innerHTML = `
            <div class="d-flex">
                <div class="toast-body">${message}</div>
                <button type="button" class="btn-close btn-close-white me-2 m-auto" data-bs-dismiss="toast"></button>
            </div>`;
        toastContainer.appendChild(toastEl);
        const bsToast = new bootstrap.Toast(toastEl, { delay: 4000 });
        bsToast.show();
        toastEl.addEventListener('hidden.bs.toast', function () {
            toastEl.remove();
        });
    };

    function getOrCreateToastContainer() {
        let container = document.getElementById('toastContainer');
        if (!container) {
            container = document.createElement('div');
            container.id        = 'toastContainer';
            container.className = 'toast-container position-fixed bottom-0 end-0 p-3';
            container.style.zIndex = '1100';
            document.body.appendChild(container);
        }
        return container;
    }

    // ---- Confirm delete (used by manage cars page) ----
    window.confirmDelete = function (btn) {
        const carId   = btn.getAttribute('data-car-id');
        const carName = btn.getAttribute('data-car-name');
        const modal   = document.getElementById('deleteModal');
        if (!modal) return;
        document.getElementById('deleteCarName').textContent = carName || 'this car';
        document.getElementById('deleteForm').action = '/admin/cars/delete/' + carId;
        new bootstrap.Modal(modal).show();
    };

    // ---- Table search highlight ----
    const searchInput = document.getElementById('tableSearch');
    if (searchInput) {
        searchInput.addEventListener('input', function () {
            const query = this.value.toLowerCase().trim();
            const rows  = document.querySelectorAll('#carsTableBody .car-row');
            let visible = 0;
            rows.forEach(function (row) {
                const text = row.textContent.toLowerCase();
                const show = query === '' || text.includes(query);
                row.style.display = show ? '' : 'none';
                if (show) visible++;
            });
            const countEl = document.getElementById('rowCount');
            if (countEl) countEl.textContent = visible + ' listing(s)';
        });
    }

    // ---- Active sidebar link based on current URL ----
    const currentPath = window.location.pathname;
    document.querySelectorAll('.sidebar-link').forEach(function (link) {
        const href = link.getAttribute('href');
        if (href && href !== '/' && currentPath.startsWith(href)) {
            link.classList.add('active');
        } else if (href === currentPath) {
            link.classList.add('active');
        }
    });

    // ---- Form dirty check (warn on unsaved changes) ----
    const adminForm = document.getElementById('addCarForm') || document.getElementById('editCarForm');
    if (adminForm) {
        let isDirty = false;
        adminForm.addEventListener('change', function () { isDirty = true; });
        adminForm.addEventListener('submit', function () { isDirty = false; });
        window.addEventListener('beforeunload', function (e) {
            if (isDirty) {
                e.preventDefault();
                e.returnValue = 'You have unsaved changes. Are you sure you want to leave?';
            }
        });
    }
});
