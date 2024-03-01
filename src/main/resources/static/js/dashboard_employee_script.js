// Functie om de inhoud van de sidebar in te laden
function loadSidebar() {
    fetch('/templates/sidebar_employee.html')
        .then(response => response.text())
        .then(data => {
            document.getElementById('sidebarEmployee').innerHTML = data;
        })
        .catch(error => {
            console.error('Error loading sidebar:', error);
        });
}

// Roep de functie aan om de sidebar in te laden wanneer de pagina wordt geladen
window.onload = function() {
    loadSidebar();
};

