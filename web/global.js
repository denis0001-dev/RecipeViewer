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
    })
});

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

function safeHTMLString(str) {
    return String(str)
        .replace(/&/g, '&amp;')
        .replace(/</g, '&lt;')
        .replace(/>/g, '&gt;')
        .replace(/"/g, '&quot;');
}