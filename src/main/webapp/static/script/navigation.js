window.onload = function() {
    if (window.history && window.history.pushState) {
        window.history.pushState('forward', null, '');
        window.onpopstate = function() {
            window.location.href = document.referrer;
        };
    }
};