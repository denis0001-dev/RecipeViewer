document.addEventListener("DOMContentLoaded", () => {
    const createRecipe = document.getElementById("createRecipe");
    const viewRecipe = document.getElementById("viewRecipe");

    createRecipe.addEventListener("click", () => {
        parent.switchTab(1);
    })

    viewRecipe.addEventListener("click", () => {
        parent.switchTab(2);
    })
})