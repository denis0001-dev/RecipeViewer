if (parent === window) {
    location.href = `../index.html?page=${document.head.querySelector("title").innerHTML.toLowerCase()}`;
}