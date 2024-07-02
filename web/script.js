// noinspection ES6MissingAwait

document.addEventListener("DOMContentLoaded", root_page);

var savedCreateState = {
    name: undefined,
    recipe: undefined
}

var unsavedChanges = false;

document.onbeforeunload = () => {
    if (unsavedChanges) {
        return "";
    } else {
        return undefined;
    }
}

var switching = false;

function root_page() {
    const tabs = document.getElementById("tabs");

    let iframe_left = document.getElementById("page-left");
    let iframe = document.getElementById("page");
    let iframe_right = document.getElementById("page-right");

    const iframes = document.getElementById("iframes");

    let prevIndex = -1;
    let currentIndex = -1;

    async function setPage(page) {
        switching = true;
        /* iframe.src = `pages/${page}/index.html`; */
        const url = `pages/${page}/index.html`;

        async function animateChange() {
            iframe.src = url;
            iframes.style.animation = "none";
            iframe.style.animation = "appear 1s ease-in-out";
            await delay(1000);
            iframe.style.opacity = '1';
            iframe.style.animation = "none";
        }

        function clearURLs() {
            iframe_left.src = "";
            iframe_right.src = "";
        }

        // Right
        if (currentIndex > prevIndex) {
            iframe_right.style.opacity = '0';
            iframes.style.animation = "slide_right 0.5s ease-in-out";
            await delay(500);
            iframe_left.insertAdjacentElement('afterend', iframe_right);
            const iframe_r = document.getElementById("page-right");
            iframe_r.id = "_page";
            iframe.id = "page-right";
            iframe_r.id = "page";

            iframe = document.getElementById("page");
            iframe_right = document.getElementById("page-right");
            await animateChange();
        }
        // Left
        else if (currentIndex < prevIndex) {
            iframe_left.style.opacity = '0';
            iframes.style.animation = "slide_left 0.5s ease-in-out";
            await delay(500);
            iframe_right.insertAdjacentElement("beforebegin", iframe_left);
            const iframe_l = document.getElementById("page-left");
            iframe_l.id = "_page";
            iframe.id = "page-left";
            iframe_l.id = "page";

            iframe = document.getElementById("page");
            iframe_left = document.getElementById("page-left");
            await animateChange();
        }
        // Current
        else if (iframe.src !== url) {
            iframe.src = url;
        }
        clearURLs();
        switching = false;
    }

    async function switchTab(tabIndex) {
        const tab = tabs.children[tabIndex];
        const button = tab.shadowRoot.children[0];
        const ripple = button.querySelector("md-ripple");

        // Disable the ripple
        const prevDisplay = ripple.style.display;
        ripple.style.display = "none";

        button.dispatchEvent(new Event("click"));
        await delay(500);
        // Enable the ripple again
        ripple.style.display = prevDisplay;
    }

    window.switchTab = switchTab;

    tabs.addEventListener("change", (event) => {
        // noinspection JSUnresolvedReference
        currentIndex = event.target.activeTabIndex;
        if (currentIndex === 0) {
            setPage("home");
        } else if (currentIndex === 1) {
            setPage("create");
        } else if (currentIndex === 2) {
            setPage("view");
        } else if (currentIndex === 3) {
            setPage("settings");
        } else if (currentIndex === 4) {
            setPage("about");
        }

        // noinspection JSUnresolvedReference
        prevIndex = event.target.activeTabIndex;
    })

    // Parse URL params
    const params = new URLSearchParams(location.search);
    for (const [key, value] of params) {
        if (key === "page") {
            const page = value.toLowerCase();
            setTimeout(() => {
                if (page === "home") {
                    switchTab(0);
                } else if (page === "create") {
                    switchTab(1);
                } else if (page === "view") {
                    switchTab(2);
                } else if (page === "settings") {
                    switchTab(3);
                } else if (page === "about") {
                    switchTab(4);
                }
            }, 500);

        }
    }
}