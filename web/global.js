document.addEventListener("DOMContentLoaded", () => {
    // Allow everything to all iframes
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

/**
 * The root element (<code>html</code>).
 * @type {HTMLHtmlElement}
 */
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

// Display tips
let tips = getCookie("tips");

if (tips === null) {
    setCookie("tips", true);
    tips = true;
}

if (tips) {
    document.html.dataset.tips = "true";
} else {
    document.html.dataset.tips = "false";
}

/**
 * A convenience function for delaying execution.
 * This function neeeds to be awaited to have effect.
 * @example
 * async function test() {
 *  console.log("Another message will be displayed in 5 seconds...");
 *  await delay(5000);
 *  console.log("Here's the message!");
 * }
 * test();
 * @param millis The milliseconds to delay the execution for.
 * @returns {Promise<unknown>} it will be resolved after the delay has elapsed.
 */
function delay(millis) {
    return new Promise(resolve => setTimeout(resolve, millis));
}

/**
 * Converts a data URL into a string with its contents.
 * @param dataURL The data URL.
 * @returns {Promise<string>} the contents.
 */
async function dataUrlToString(dataURL) {
    const res = await fetch(dataURL);
    return new TextDecoder().decode(new Uint8Array(await res.arrayBuffer()));
}

/**
 * Processes a JSON file.
 *
 * @param file The file that was selected by the user.
 * @param content The data URL containing the JSON contents.
 * @returns {Promise<string|null>} the JSON data (unparsed). If the file is not JSON returns <code>null</code>.
 */
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

/**
 * Gets the value stored in document cookies.
 * Returns null if the cookie doesn't exist.
 * @param name The cookie name.
 * @returns {boolean|number|any|Element|null}
 */
function getCookie(name) {
    const parts = document.cookie.split(";");

    for (let part of parts) {
        const trimmedPart = part.trim();
        if (trimmedPart.startsWith(name + "=")) {
            const value = decodeURIComponent(trimmedPart.substring(name.length + 1));
            return parseString(value);
        }
    }
    return null;
}

/**
 * Parses a string to an object type.
 * <br/>
 * <code>"true"</code> or <code>"false"</code> - boolean value
 * <br/>
 * Any number that is not <code>NaN</code> - number
 * <br/>
 * Starts and ends with <code>{}</code> or <code>[]</code> - JSON parsed into an object
 * <br/>
 * Any valid HTML string - parsed HTML node
 * <br/>
 * If any of the conditions above don't match, returns <code>value</code>.
 * @param value The string to parse
 * @returns {Element|*|number|boolean}
 */
function parseString(value) {
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

/**
 * Returns an object containing all cookies present in the document.
 * The properties names are the cookie names.
 * @returns {{}}
 */
function getCookies() {
    const cookies = {};
    const parts = document.cookie.split(";");

    for (let part of parts) {
        const trimmedPart = part.trim();
        if (trimmedPart) {
            const [name, value] = trimmedPart.split("=");

            cookies[name] = parseString(decodeURIComponent(value));
        }
    }

    return cookies;
}

/**
 * Clears all cookies present in the document.
 */
function clearCookies() {
    document.cookie.split(";")
        .forEach(function(c) {
            document.cookie = c
                .replace(/^ +/, "")
                .replace(/=.*/, "=;expires=" +
                    new Date().toUTCString()
                    + ";path=/");
        });
}