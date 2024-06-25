document.onreadystatechange = () => {
    if (document.readyState === 'complete') {
        main();
    }
}

function main() {
    document.getElementById("test").addEventListener("click", () => {
        console.log("test clicked");
    })
}