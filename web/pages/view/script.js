document.addEventListener("localized", main);

function main() {
    const recipeName = document.getElementById("recipeName");
    const multiplier = document.getElementById("multiplier");
    const load = document.getElementById("load");

    const ingredients = document.querySelector("#ingredients > .scroll > md-list");

    const steps = document.querySelector("#steps-wrapper");
    const step_left = document.querySelector("#step_left");
    const step_center = document.querySelector("#step");
    const step_right = document.querySelector("#step_right");

    const prev = document.getElementById("prev");
    const next = document.getElementById("next");

    const invalidJSONDialog = document.getElementById("invalidJSON");
    const invalidJSONCode = document.getElementById("invalidJSONError");
    const invalidJSONClose = document.getElementById("invalidJSONClose");

    invalidJSONClose.addEventListener("click", () => {
        invalidJSONDialog.open = false;
    });

    let currentStep = 1;
    let recipe;

    let multiply = 1;

    document.body.onbeforeunload = () => {
        if (getCookie("saveStates") === false) return undefined;
        parent.savedViewState = {
            name: recipeName.value,
            multiplier: multiply,
            currentStep: currentStep,
            recipe: recipe
        }

        if (!parent.switching) return "";
        return undefined;
    }

    if (parent.savedViewState.recipe !== undefined) {
        recipeName.value = parent.savedViewState.name;
        multiply, multiplier.value = parent.savedViewState.multiplier;
        multiplier.value = parent.savedViewState.multiplier;
        currentStep = parent.savedViewState.currentStep;
        recipe = parent.savedViewState.recipe;
        loadRecipe();
        step_center.querySelector("p").textContent = safeHTMLString(processRecipeText(recipe.steps[currentStep - 1]['step'+currentStep]));
        step_center.querySelector("h1").textContent = (translate("step") || "Step #")+currentStep;
        next.disabled = recipe.steps[currentStep] === undefined;
        prev.disabled = recipe.steps[currentStep - 2] === undefined;
    }

    if (parent.savedViewState.multiplier !== undefined) {
        multiplier.value = parent.savedViewState.multiplier;
        checkMultiplier();
    }

    multiplier.label = translate("multiplier") || multiplier.label;
    recipeName.label = translate("recipeName") || recipeName.label;

    function checkMultiplier() {
        if (isNaN(Number(multiplier.value)) || multiplier.value <= 0 || multiplier.value === "") {
            multiplier.error = true;
            console.warn("Invalid number");
        } else {
            multiply = Number(multiplier.value);
            multiplier.error = false;
            // console.log(step_center.querySelector("p").textContent);
            let processedText;
            try {
                processedText = processRecipeText(recipe.steps[currentStep - 1]['step'+currentStep]);
            } catch (_) {
                // we don't need to process the error
                processedText = step_center.querySelector("p").textContent;
            }
            // console.log(processedText);
            step_center.querySelector("p").textContent = processedText;
            // console.log(step_center.querySelector("p").textContent);
            updateIngredients();
        }
    }

    function updateIngredients() {
        Array.from(ingredients.children).forEach(item => {
            if (item instanceof HTMLParagraphElement) return;
            const count = item.querySelector(".root md-filled-text-field.count");
            const number = Number(item.querySelector("span[slot='start']").textContent.replace("#", ""));

            const originalCount = recipe.ingredients[number - 1].count;

            count.value = originalCount * multiply;
        });
    }

    multiplier.addEventListener("input", checkMultiplier);
    multiplier.addEventListener("change", checkMultiplier);
    multiplier.addEventListener("click", checkMultiplier);

    async function clear() {
        const items = ingredients.children;

        for (let i = 0; i < items.length; i++) {
            const item = items[i];
            const func = async () => {
                item.style.animation = "itemDisappear 0.5s ease-in-out";
                await delay(500);
                item.remove();
            }
            func();
        }
    }

    function loadRecipe() {
        const ings = recipe.ingredients;

        clear();

        ings.forEach(async ing => {
            let i = ings.indexOf(ing);
            const ingredient = document.createElement("md-list-item");

            const number = document.createElement("span");
            number.slot = "start";
            number.innerHTML = "#" + (i + 1);
            ingredient.appendChild(number);

            const root = document.createElement("div");
            root.classList.add("root");

            const ingName = document.createElement("md-filled-text-field");
            ingName.id = `ing${i + 1}_name`;
            ingName.classList.add("name");
            ingName.value = ing.name.replaceAll("_", " ");
            ingName.readOnly = true;
            root.appendChild(ingName);

            const ingCount = document.createElement("md-filled-text-field");
            ingCount.id = `ing${i + 1}_count`;
            ingCount.classList.add("count");
            ingCount.value = ing.count;
            ingCount.readOnly = true;
            root.appendChild(ingCount);

            const ingUnit = document.createElement("md-filled-text-field");
            ingUnit.id = `ing${i + 1}_unit`;
            ingUnit.classList.add("unit");
            ingUnit.value = ing.count2;
            ingUnit.readOnly = true;
            root.appendChild(ingUnit);

            ingredient.appendChild(root);
            ingredient.style.animation = "itemAppear 0.5s ease-in-out";
            ingredients.appendChild(ingredient);
            await delay(500);
            ingredient.style.animation = "none";
        });
        step_center.querySelector("p").textContent = safeHTMLString(processRecipeText(recipe.steps[0]['step1']));
        step_center.querySelector("h1").textContent = (translate("step") || "Step #") + 1;
        next.disabled = recipe.steps[1] === undefined;
    }

    load.addEventListener("click", () => {
        const input = document.createElement("input");
        input.type = "file";

        input.onchange = e => {
            const file = e.target.files[0];
            const name = file.name.toString();
            // noinspection JSUnusedLocalSymbols
            const type = file.type;

            const reader = new FileReader();
            reader.readAsDataURL(file);

            reader.onload = async e => {
                recipeName.value = name.substring(0, name.lastIndexOf("."));
                const json = await processJSONFile(file, e.target.result);

                try {
                    recipe = JSON.parse(json);
                } catch (e) {
                    recipeName.value = "";
                    invalidJSONDialog.open = true;
                    invalidJSONCode.textContent = e.toString();
                    return;
                }

                loadRecipe();
            };
        }
        input.click();
    });

    function processRecipeText(text) {
        let words = text.split(" ");
        for (let i = 0; i < words.length; i++) {
            let word = words[i];
            if (word.startsWith("*")) {
                word = word.replace("*", "");
                let number = Number(word);
                if (!isNaN(number)) {
                    word = number * multiply;
                }
            }
            words[i] = word;
        }

        return words.join(" ");
    }

    prev.addEventListener("click", async () => {
        if (recipe.steps[(currentStep - 1) - 1] !== undefined) {
            currentStep--;
            const text = safeHTMLString(processRecipeText(recipe.steps[currentStep - 1]['step' + currentStep]));
            const number = (translate("step") || "Step #") + currentStep;

            const nextState = next.disabled;

            step_left.querySelector("p").textContent = text;
            step_left.querySelector("h1").textContent = number;
            steps.style.animation = "stepSlideLeft 0.5s ease-in-out";
            next.disabled = true;
            prev.disabled = true;
            await delay(500);
            step_center.querySelector("p").textContent = text;
            step_center.querySelector("h1").textContent = number;
            steps.style.animation = "none";
            next.disabled = nextState;
            prev.disabled = recipe.steps[currentStep - 2] === undefined;
        } else {
            prev.disabled = true;
        }
        next.disabled = recipe.steps[currentStep] === undefined;
    });

    next.addEventListener("click", async () => {
        if (recipe.steps[currentStep] !== undefined) {
            currentStep++;
            const text = safeHTMLString(processRecipeText(recipe.steps[currentStep - 1]['step' + currentStep]));
            const number = (translate("step") || "Step #") + currentStep;

            const prevState = prev.disabled;

            step_right.querySelector("p").textContent = text;
            step_right.querySelector("h1").textContent = number;
            steps.style.animation = "stepSlideRight 0.5s ease-in-out";
            prev.disabled = true;
            next.disabled = true;
            await delay(500);
            step_center.querySelector("p").textContent = text;
            step_center.querySelector("h1").textContent = number;
            steps.style.animation = "none";
            prev.disabled = prevState;
            next.disabled = recipe.steps[currentStep] === undefined;
        } else {
            next.disabled = true;
        }
        prev.disabled = recipe.steps[currentStep - 2] === undefined;
    });
}