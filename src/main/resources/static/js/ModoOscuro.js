// Función para aplicar el tema
function aplicarTema() {
  const darkMode = localStorage.getItem("darkMode") === "enabled";
  document.documentElement.setAttribute(
    "data-theme",
    darkMode ? "dark" : "light"
  );
  document.documentElement.classList.toggle("dark-mode", darkMode);

  // Actualizar el icono en todos los botones de tema
  const botonesTheme = document.querySelectorAll(
    ".theme-switch i, #theme-switch i"
  );
  botonesTheme.forEach((icono) => {
    icono.classList.remove("fa-moon", "fa-sun");
    icono.classList.add(darkMode ? "fa-sun" : "fa-moon");
  });
}

// Función para cambiar el tema
function toggleTema() {
  const isDark = localStorage.getItem("darkMode") === "enabled";
  localStorage.setItem("darkMode", isDark ? "disabled" : "enabled");
  aplicarTema();
}

// Aplicar tema al cargar la página
document.addEventListener("DOMContentLoaded", () => {
  aplicarTema();
});
