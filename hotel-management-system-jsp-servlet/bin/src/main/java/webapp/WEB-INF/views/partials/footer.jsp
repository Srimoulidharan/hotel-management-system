    </main>
</div>

<script>
    const sidebarToggle = document.getElementById("sidebarToggle");
    const appShell = document.getElementById("appShell");

    sidebarToggle.addEventListener("click", function () {
        appShell.classList.toggle("sidebar-closed");

        if (appShell.classList.contains("sidebar-closed")) {
            sidebarToggle.innerHTML = "☰";
        } else {
            sidebarToggle.innerHTML = "×";
        }
    });
</script>

</body>
</html>