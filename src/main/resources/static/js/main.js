/* =============================================
   AutoDeals - Main JavaScript
   ============================================= */

document.addEventListener('DOMContentLoaded', function () {

    // ---- Auto-dismiss alerts after 5 seconds ----
    document.querySelectorAll('.alert-dismissible').forEach(function (alert) {
        setTimeout(function () {
            const bsAlert = bootstrap.Alert.getOrCreateInstance(alert);
            if (bsAlert) bsAlert.close();
        }, 5000);
    });

    // ---- Lazy image fallback ----
    document.querySelectorAll('img[loading="lazy"]').forEach(function (img) {
        img.addEventListener('error', function () {
            this.src = '/images/car-placeholder.svg';
            this.onerror = null;
        });
    });

    // ---- Smooth scroll for anchor links ----
    document.querySelectorAll('a[href^="#"]').forEach(function (anchor) {
        anchor.addEventListener('click', function (e) {
            const target = document.querySelector(this.getAttribute('href'));
            if (target) {
                e.preventDefault();
                target.scrollIntoView({ behavior: 'smooth', block: 'start' });
            }
        });
    });

    // ---- Animate stat numbers on scroll ----
    const statNumbers = document.querySelectorAll('.stat-item h3, .stat-card h3');
    const observer = new IntersectionObserver(function (entries) {
        entries.forEach(function (entry) {
            if (entry.isIntersecting) {
                animateNumber(entry.target);
                observer.unobserve(entry.target);
            }
        });
    }, { threshold: 0.5 });

    statNumbers.forEach(function (el) {
        observer.observe(el);
    });

    // ---- Number animation ----
    function animateNumber(el) {
        const rawText = el.textContent.trim();
        const num = parseInt(rawText.replace(/[^0-9]/g, ''), 10);
        if (isNaN(num) || num === 0) return;
        let start = 0;
        const duration = 800;
        const step = Math.ceil(num / (duration / 16));
        const timer = setInterval(function () {
            start += step;
            if (start >= num) {
                start = num;
                clearInterval(timer);
            }
            el.textContent = rawText.replace(/[0-9,]+/, start.toLocaleString());
        }, 16);
    }

    // ---- Price range slider sync (if present) ----
    const minPriceInput = document.querySelector('input[name="minPrice"]');
    const maxPriceInput = document.querySelector('input[name="maxPrice"]');
    if (minPriceInput && maxPriceInput) {
        minPriceInput.addEventListener('change', function () {
            if (maxPriceInput.value && parseFloat(this.value) > parseFloat(maxPriceInput.value)) {
                maxPriceInput.value = this.value;
            }
        });
    }

    // ---- Car card hover enhancement ----
    document.querySelectorAll('.car-card').forEach(function (card) {
        card.addEventListener('mouseenter', function () {
            this.style.zIndex = '10';
        });
        card.addEventListener('mouseleave', function () {
            this.style.zIndex = '';
        });
    });

    // ---- Form validation feedback ----
    document.querySelectorAll('form[novalidate]').forEach(function (form) {
        form.addEventListener('submit', function (e) {
            if (!form.checkValidity()) {
                e.preventDefault();
                e.stopPropagation();
            }
            form.classList.add('was-validated');
        });
    });
});
