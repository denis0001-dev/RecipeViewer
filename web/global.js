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
})

function delay(millis) {
    return new Promise(resolve => setTimeout(resolve, millis));
}

async function dataUrlToString(dataURL) {
    const res = await fetch(dataURL);
    return new TextDecoder().decode(new Uint8Array(await res.arrayBuffer()));
}