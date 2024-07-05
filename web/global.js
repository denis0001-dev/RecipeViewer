document.addEventListener("DOMContentLoaded", () => {
    const iframes = document.querySelectorAll("iframe");
    iframes.forEach(iframe => {
        // noinspection JSValidateTypes
        iframe.sandbox = "allow-forms \
        allow-modals \
        allow-pointer-lock \
        allow-popups \
        allow-popups-to-escape-sandbox \
        allow-same-origin \
        allow-scripts \
        allow-top-navigation \
        allow-downloads";
    });


});

document.html = document.querySelector("html");

// Color scheme
function updateTheme() {
    if (matchMedia("(prefers-color-scheme: dark)").matches) {
        document.html.dataset.theme = "dark";
    } else {
        document.html.dataset.theme = "light";
    }
    let theme = top.getCookie("theme");
    if (theme) {
        document.html.dataset.theme = theme;
    }
}
updateTheme();

document.addEventListener("themechange", updateTheme);


function delay(millis) {
    return new Promise(resolve => setTimeout(resolve, millis));
}

async function dataUrlToString(dataURL) {
    const res = await fetch(dataURL);
    return new TextDecoder().decode(new Uint8Array(await res.arrayBuffer()));
}

/* async function loadFile() {
    const input = document.createElement('input');
    input.type = 'file';

    let ret;

    input.onchange = () => {

    }

    return new Promise(resolve => {ret = resolve});
} */

/**
 * @deprecated
 * @returns {Promise<unknown>}
 */
async function loadJSONFile() {
    const input = document.createElement("input");
    input.type = "file";

    let ret;

    input.onchange = e => {
        const file = e.target.files[0];
        const name = file.name.toString();
        const type = file.type;

        const reader = new FileReader();
        reader.readAsDataURL(file);

        reader.onload = async e => {
            const content = e.target.result;
            console.log("Reading file: "+file.name);
            const json = await dataUrlToString(content);
            console.log(json);
            // Parse
            if (!(type === "application/json" || name.endsWith(".json"))) {
                alert("Invalid file type. Please upload a JSON file.");
                return null;
            }

            ret(json);
        }
    }

    return new Promise(resolve => {ret = resolve});
}

async function processJSONFile(file, content) {
    console.log("Reading file: "+file.name);
    const json = await dataUrlToString(content);
    console.log(json);
    // Parse
    if (!(file.type === "application/json" || name.endsWith(".json"))) {
        alert("Invalid file type. Please upload a JSON file.");
        return null;
    }

    return json;
}

/**
 * Returns an HTML-safe version of the provided string.
 * @param str The source string.
 * @returns {string} The string, with <code>&</code>,
 * <code><</code>,
 * <code>&gt;</code>
 * and <code>"</code>
 * escaped.
 */
function safeHTMLString(str) {
    return String(str)
        .replace(/&/g, '&amp;')
        .replace(/</g, '&lt;')
        .replace(/>/g, '&gt;')
        .replace(/"/g, '&quot;');
}

function getCookie(name) {
    const parts = document.cookie.split(";");

    for (let part of parts) {
        const trimmedPart = part.trim();
        if (trimmedPart.startsWith(name + "=")) {
            const value = decodeURIComponent(trimmedPart.substring(name.length + 1));
            // Boolean
            if (value === "true") {
                return true;
            } else if (value === "false") {
                return false;
            }
            // Numbers
            const number = Number(value);
            if (!isNaN(number)) {
                return number;
            }
            // JSON/array
            if (
                (value.startsWith("{") && value.endsWith("}"))
                ||
                (value.startsWith("[") && value.endsWith("]"))
            ) {
                try {
                    return JSON.parse(value);
                } catch (e) {
                    console.warn("Error parsing JSON: ",e);
                }
            }
            // HTML
            const regex = /<[\w-]+( [\w-]+=".*")*(\s*\/>|\s*>(.|\n)*<\/[\w-]+>)/gm;
            if (regex.exec(value)) {
                const parse = Range.prototype.createContextualFragment.bind(document.createRange());

                return parse(value).children[0];
            }
            // String fallback
            return value;
        }
    }
    return null;
}

/**
 * Sets a cookie to the given value.
 * If the value is <code>undefined</code> or <code>null</code>,
 * the cookie will be erased.
 * @param name The cookie name.
 * @param value The value. Can be anything.
 */
function setCookie(name, value) {
    let val = value;

    if ([undefined, null].includes(val)) {
        document.cookie = `${name}=; Max-Age=-99999999;`
        return;
    }

    if (val instanceof HTMLElement) {
        val = val.outerHTML;
    }

    if (typeof val === "object") {
        val = JSON.stringify(val);
    }

    document.cookie = `${name}=${encodeURIComponent(val)}`;
}