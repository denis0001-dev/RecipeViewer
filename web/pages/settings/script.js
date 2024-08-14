document.addEventListener("localized", main);

function main() {
    const theme = document.getElementById("theme");
    const theme_system = theme.querySelector("md-select-option[value='system']");
    const theme_light = theme.querySelector("md-select-option[value='light']");
    const theme_dark = theme.querySelector("md-select-option[value='dark']");

    const lang = document.getElementById("language");
    const displayTips = document.getElementById("displayTips");
    const saveStates = document.getElementById("saveStates");

    let cookieTheme = top.getCookie("theme");
    let cookieLang = top.getCookie("lang");
    let cookieTips = top.getCookie("tips");
    let cookieSaveStates = top.getCookie("saveStates");

    function changeSelectValue(select, value) {
        for (let i = 0; i < select.children.length; i++) {
            const option = select.children[i];
            if (option.value === value) {
                localize(option.querySelector("div[slot='headline']"));
                option.setAttribute("selected", "");
                option.tabIndex = 0;
            } else {
                option.removeAttribute("selected");
                option.tabIndex = -1;
            }
        }
    }

    function selectedValue(select) {
        for (let i = 0; i < select.children.length; i++) {
            const option = select.children[i];
            if (option.hasAttribute("selected") && option.tabIndex === 0) {
                return option.value;
            }
        }
        return null;
    }

    function getOptionByValue(select, value) {
        return select.querySelector(`md-select-option[value="${value}"]`);
    }

    if (cookieTheme) {
        changeSelectValue(theme, cookieTheme);
    } else {
        changeSelectValue(theme, theme_system.value);
    }

    theme.addEventListener("change", () => {
        const value = selectedValue(theme);
        if (value !== "system") {
            top.setCookie("theme", value);
        } else {
            top.setCookie("theme");
        }
        document.dispatchEvent(new Event("themechange"));
        top.document.dispatchEvent(new Event("themechange"));
    });

    if (cookieLang) {
        changeSelectValue(lang, cookieLang);
    } else {
        changeSelectValue(lang, "en");
    }

    lang.addEventListener("change", () => {
        const value = selectedValue(lang);
        top.setCookie("lang", value);
        document.dispatchEvent(new Event("languagechange"));
        top.document.dispatchEvent(new Event("languagechange"));
    });

    if (cookieTips) {
        displayTips.setAttribute("selected", "");
    } else {
        displayTips.removeAttribute("selected");
    }

    displayTips.addEventListener("change", () => {
        const value = displayTips.hasAttribute("selected");
        top.setCookie("tips", value);
    });

    if (cookieSaveStates === null) {
        top.setCookie("saveStates", true);
        cookieSaveStates = true;
    }

    saveStates.selected = cookieSaveStates;

    saveStates.addEventListener("change", () => {
        const value = saveStates.hasAttribute("selected");
        top.setCookie("saveStates", value);
        top.savedViewState = {
            name: undefined,
            multiplier: 1,
            currentStep: 1,
            recipe: undefined
        };
        top.savedCreateState = {
            name: undefined,
            recipe: undefined
        };
    });

    document.body.querySelectorAll("md-outlined-select:not(#language)").forEach(item => {
        const label = item.shadowRoot.children[0].querySelector("md-outlined-field > div#label");
        label.dataset.lkey = getOptionByValue(item, selectedValue(item)).querySelector("div[slot='headline']").dataset.lkey;
        localize(label);
    });
}