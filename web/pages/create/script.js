document.addEventListener("DOMContentLoaded", main)

function main() {
    const load = document.getElementById("load");
    const clear = document.getElementById("clear");
    const recipeName = document.getElementById("recipeName");

    load.addEventListener("click", () => {
        const input = document.createElement("input");
        input.type = "file";

        input.onchange = e => {
            const file = e.target.files[0];

            const reader = new FileReader();
            reader.readAsDataURL(file);

            reader.onload = async e => {
                const content = e.target.result;
                console.log("Reading file: "+file.name)
                console.log(await dataUrlToString(content));
                // Parse
                if (!(file.type === "application/json" || file.name.toString().endsWith(".json"))) {
                    alert("Invalid file type. Please upload a JSON file.");
                    return;
                }
                recipeName.value = file.name.toString().substring(0, file.name.toString().lastIndexOf("."))

            }
        }
        input.click();
    })

    clear.addEventListener("click", () => {
        recipeName.value = "";
    })


}

