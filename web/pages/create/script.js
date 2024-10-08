document.addEventListener("localized", main);

function main() {
    const load = document.getElementById("load");
    const clear = document.getElementById("clear");
    const recipeName = document.getElementById("recipeName");

    const ingredients = document.getElementById("ingredients");
    const ingScroll = ingredients.getElementsByClassName("scroll")[0];
    const addIng = document.getElementById("addIng");

    const steps = document.getElementById("steps");
    const stepScroll = steps.getElementsByClassName("scroll")[0];
    const addStep = document.getElementById("addStep");

    const create = document.getElementById("create");

    const finishedDialog = document.getElementById("createFinished");
    const finishedClose = document.querySelectorAll(".finishedClose");
    const finishedCopy = document.querySelectorAll(".finishedCopy");
    const finishedDownload = document.querySelectorAll(".finishedDownload");
    const finishedCode = document.getElementById("finishedRecipeCode");

    const createFailDialog = document.getElementById("createFail");
    const failClose = document.getElementById("failClose");

    const invalidJSONDialog = document.getElementById("invalidJSON");
    const invalidJSONCode = document.getElementById("invalidJSONError");
    const invalidJSONClose = document.getElementById("invalidJSONClose");

    let recipeJSON = {
        ingredients: [],
        steps: []
    };
    const ingredientsList = [];
    const stepsList = [];

    document.body.onbeforeunload = () => {
        if (getCookie("saveStates") === false) return undefined;
        recipeJSON = createJSON(false);
        parent.savedCreateState = {
            name: recipeName.value,
            recipe: recipeJSON
        };
        if (parent.savedCreateState !== {
            name: "",
            recipe: {
                ingredients: [],
                steps: []
            }
        } && !parent.switching) {
            return "";
        }
        return undefined;
    };

    function inputListener(element, func, ...params) {
        if (!element instanceof HTMLElement) {
            throw new TypeError("Element must be a valid DOM element");
        } else if (!(func instanceof Function || typeof func === "function")) {
            throw new TypeError("Function must be valid");
        }

        params = params === undefined ? [] : params;

        function callback() {
            func(...params);
        }

        element.addEventListener("input", callback);
        element.addEventListener("change", callback);
        element.addEventListener("click", callback);
    }

    if (parent.savedCreateState.recipe !== undefined) {
        let name = parent.savedCreateState.name;
        if (name === undefined) {
            name = "";
        }
        recipeName.value = name;
        recipeJSON = parent.savedCreateState.recipe;
        parseJSON(recipeJSON);
    }

    function checkRecipeName() {
        const regex = /^(?!CON|PRN|AUX|NUL|(COM|LPT)([0123456789¹²³]))[^/\\><|:&?*\u2400]{1,255}$/i;
        if ((regex.exec(recipeName.value)) === null) {
            recipeName.error = true;
            recipeName.errorText = translate("dontUseRestrictedChars") || "Don't use restricted characters";

            if (recipeName.value === "") {
                recipeName.errorText = translate("shouldntBeEmpty") || "Recipe name shouldn't be empty";
            }
        } else {
            recipeName.error = false;
            recipeName.errorText = undefined;
        }
    }

    recipeName.label = translate("recipeName") || recipeName.label;

    inputListener(recipeName, checkRecipeName);

    invalidJSONClose.addEventListener("click", () => {
        invalidJSONDialog.open = false;
    });

    load.addEventListener("click", () => {
        const input = document.createElement("input");
        input.type = "file";

        input.onchange = e => {
            const file = e.target.files[0];
            const name = file.name.toString();
            const type = file.type;

            const reader = new FileReader();
            reader.readAsDataURL(file);

            reader.onload = async e => {
                const content = e.target.result;
                const json = await processJSONFile(file, content);
                if (json == null) return;

                loadJSON(name, json);
            }
        }
        input.click();
    });

    function loadJSON(name, json) {
        recipeName.value = name.substring(0, name.lastIndexOf("."));
        try {
            recipeJSON = JSON.parse(json);
        } catch (e) {
            recipeName.value = "";
            invalidJSONDialog.open = true;
            invalidJSONCode.textContent = e.toString();
            return;
        }
        parseJSON(recipeJSON);
    }

    function parseJSON(recipeJSON) {
        ingredientsList.forEach(item => {
            item.remove();
        });
        stepsList.forEach(item => {
            item.remove();
        })

        // Parse ingredients

        // noinspection JSUnresolvedReference
        const ings = recipeJSON.ingredients;

        ings.forEach(ing => {
            const count = ing.count;
            const name = ing.name;
            // noinspection JSUnresolvedReference
            const unit = ing.count2;

            addIngredient(name, count, unit);
        });
        // Parse steps

        // noinspection JSUnresolvedReference
        const steps = recipeJSON.steps;

        steps.forEach(step => {
            const desc = step[`step${steps.indexOf(step) + 1}`];

            if (desc === undefined) {
                console.warn("No step description found for step "+(steps.indexOf(step) + 1));
                return;
            }
            addStepFunc(desc);
        })
    }

    function createJSON(errCheck = true) {
        const recipe = {};

        const ingredients = [];
        const steps = [];

        let error = false;

        recipeName.dispatchEvent(new Event("input"));

        if (recipeName.error) {
            error = true;
        }

        ingredientsList.forEach(ing => {
            const ingredient = {};

            for (let i = 0; i < ing.element.children.length; i++) {
                const item = ing.element.children[i];
                item.dispatchEvent(new Event("input"));
                if (item.error !== undefined && !error) {
                    error = item.error;
                }
            }

            ingredient.name = ing.name;
            ingredient.count = ing.count;
            ingredient.count2 = ing.unit;

            ingredients.push(ingredient);
        });

        stepsList.forEach(stepItem => {
            const step = {};
            for (let i = 0; i < stepItem.element.children.length; i++) {
                const item = stepItem.element.children[i];
                item.dispatchEvent(new Event("input"));
                if (item.error !== undefined && !error) {
                    error = item.error;
                }
            }

            step[`step${stepsList.indexOf(stepItem) + 1}`] = stepItem.desc;

            steps.push(step);
        });

        if (error && errCheck) {
            /* createFailDialog.open = true;
            failClose.addEventListener("click", () => {
                createFailDialog.close();
            }) */
            return null;
        }

        recipe.ingredients = ingredients;
        recipe.steps = steps;

        return recipe;
    }

    clear.addEventListener("click", () => {
        recipeName.value = "";
        ingredientsList.forEach(item => {
            item.remove();
        });
        stepsList.forEach(item => {
            item.remove();
        });
    })

    function adjustIngAddButtonPos() {
        let offset = (10 + (ingScroll.offsetWidth - ingScroll.clientWidth)) + "px";
        console.log(offset);

        addIng.style.right = offset;
    }

    function adjustStepAddButtonPos() {
        let offset = (10 + (stepScroll.offsetWidth - stepScroll.clientWidth)) + "px";
        console.log(offset);

        addStep.style.right = offset;
    }

    /* ingScroll.addEventListener("DOMNodeInserted", updateAddIngPos);
    ingScroll.addEventListener("DOMNodeRemoved", updateAddIngPos); */
    /* new MutationObserver((_, __) => {
        console.log("Repositioning the add button")
        console.log([_, __]);
        let offset = (10 + (ingScroll.offsetWidth - ingScroll.clientWidth)) + "px";
        console.log(offset);

        addIng.style.right = offset;
    }).observe(ingScroll, {attributes: false, childList: true, subtree: true}); */
    adjustIngAddButtonPos();
    adjustStepAddButtonPos();

    function isNotEmpty(field) {
        field.error = field.value === "";
    }

    async function addIngredient(name, count, unit) {
        const list = ingScroll.getElementsByTagName("md-list")[0];

        const item = document.createElement("md-list-item");

        const number = document.createElement("span");
        number.slot = "start";
        const ingNumber = ingredientsList.length + 1;
        number.innerHTML = "#" + ingNumber;
        item.appendChild(number);

        const root = document.createElement("div");
        root.classList.add("root");
        root.dataset.nolocalize = "";

        const ingName = document.createElement("md-filled-text-field");
        ingName.id = `ing${ingNumber}_name`;
        ingName.classList.add("name");
        ingName.value = name;
        ingName.placeholder = translate("name") || "Name";
        inputListener(ingName, isNotEmpty, ingName);
        root.appendChild(ingName);

        const ingCount = document.createElement("md-filled-text-field");
        ingCount.id = `ing${ingNumber}_count`;
        ingCount.classList.add("count");
        ingCount.value = count;
        ingCount.placeholder = translate("count") || "Count";

        function validateNumber() {
            // const regex = /^[123456789]+$/m
            if (isNaN(Number(ingCount.value)) || Number(ingCount.value) === 0) {
                ingCount.error = true;
                ingCount.errorText = "Must be a number";

                if (ingCount.value === "") {
                    ingCount.errorText = "Mustn't be empty";
                } else if (ingCount.value === "0") {
                    ingCount.errorText = "Mustn't be 0";
                }
            } else {
                ingCount.error = false;
                ingCount.errorText = undefined;
            }
        }
        inputListener(ingCount, validateNumber);

        root.appendChild(ingCount);

        const ingUnit = document.createElement("md-filled-text-field");
        ingUnit.id = `ing${ingNumber}_unit`;
        ingUnit.classList.add("unit");
        ingUnit.value = unit;
        ingUnit.placeholder = translate("unit") || "Unit";
        inputListener(ingUnit, isNotEmpty, ingUnit);
        root.appendChild(ingUnit);

        const remove = document.createElement("md-filled-icon-button");
        remove.id = `ing${ingNumber}_remove`;
        remove.classList.add("remove");
        const remove_icon = document.createElement("i");
        remove_icon.classList.add("material-icons");
        remove_icon.innerHTML = "delete";
        remove.appendChild(remove_icon);

        root.appendChild(remove);

        const ing = new Ingredient(root);
        ing.remove = async () => {
            item.style.animation = "itemDisappear 0.5s ease-in-out";
            await delay(500);
            ingredientsList.splice(ingredientsList.indexOf(ing), 1);
            list.removeChild(item);
            updateIngredientNumbers();
        };
        ingredientsList.push(ing);
        remove.addEventListener("click", ing.remove);

        item.appendChild(root);
        list.appendChild(item);
        item.style.animation = "itemAppear 0.5s ease-in-out";
        await delay(500);
        item.style.animation = "none";
    }

    async function addStepFunc(desc) {
        const list = stepScroll.querySelector("md-list");

        const item = document.createElement("md-list-item");

        const number = document.createElement("span");
        number.slot = "start";
        const stepNumber = stepsList.length + 1;
        number.innerHTML = "#" + stepNumber;
        item.appendChild(number);

        const root = document.createElement("div");
        root.classList.add("root");

        const stepDesc = document.createElement("md-filled-text-field");
        stepDesc.id = `step${stepNumber}_desc`;
        stepDesc.classList.add("desc");
        stepDesc.value = desc;
        stepDesc.placeholder = translate("stepDesc") || "Describe your step";
        inputListener(stepDesc, isNotEmpty, stepDesc);
        root.appendChild(stepDesc);

        const remove = document.createElement("md-filled-icon-button");
        remove.id = `step${stepNumber}_remove`;
        remove.classList.add("remove");
        const remove_icon = document.createElement("i");
        remove_icon.classList.add("material-icons");
        remove_icon.innerHTML = "delete";
        remove.appendChild(remove_icon);
        const step = new Step(root);
        step.remove = async () => {
            item.style.animation = "itemDisappear 0.5s ease-in-out";
            await delay(500);
            stepsList.splice(stepsList.indexOf(step), 1);
            list.removeChild(item);
            updateStepNumbers();
        }
        stepsList.push(step);
        remove.addEventListener("click", step.remove);
        root.appendChild(remove);

        item.appendChild(root);

        list.appendChild(item);
        item.style.animation = "itemAppear 0.5s ease-in-out";
        await delay(500);
        item.style.animation = "none";
    }

    function updateIngredientNumbers() {
        ingredientsList.forEach(ing => {
            ing.number = ingredientsList.indexOf(ing) + 1;
        })
    }

    function updateStepNumbers() {
        stepsList.forEach(step => {
            step.number = stepsList.indexOf(step) + 1;
        })
    }

    addIng.addEventListener("click", () => {
        addIngredient("", 0, "");
    });

    addStep.addEventListener("click", () => {
        addStepFunc("");
    });

    create.addEventListener("click", () => {
        const recipe = createJSON();
        if (recipe === null || ingredientsList.length === 0 || stepsList.length === 0) {
            createFailDialog.open = true;
            failClose.addEventListener("click", () => {
                createFailDialog.close();
            }, {once: true});
            return;
        }

        const recipeJson = JSON.stringify(recipe, null, 2);

        // Dialog
        finishedDialog.open = true;
        // Parse the code
        finishedCode.textContent = recipeJson;
        parseCodeSnippet(finishedCode);

        finishedDialog.addEventListener("close", async () => {
            if (finishedDialog.returnValue === "close") {
            } else if (finishedDialog.returnValue === "copy") {
                await navigator.clipboard.writeText(recipeJson);
            } else if (finishedDialog.returnValue === "download") {
                let a = document.createElement("a");
                let file = new Blob([recipeJson], {type: "text/json"});

                let url = URL.createObjectURL(file);
                a.href = url;
                a.download = `${recipeName.value}.json`;
                a.style.display = "none";
                document.body.appendChild(a);
                a.click();
                setTimeout(() => {
                    document.body.removeChild(a);
                    URL.revokeObjectURL(url);
                }, 0);
            }
            finishedDialog.returnValue = undefined;
            /* finishedClose.click(undefined);
            finishedCopy.click(undefined);
            finishedDownload.click(undefined); */
        });

        finishedClose.forEach(item => {
            item.addEventListener("click", async () => {
                await finishedDialog.close();
            }, {once: true});
        });
        finishedCopy.forEach(item => {
            item.addEventListener("click", async () => {
                finishedDialog.returnValue = "copy";
                await finishedDialog.close();
            }, {once: true});
        });
        finishedDownload.forEach(item => {
            item.addEventListener("click", async () => {
                finishedDialog.returnValue = "download";
                await finishedDialog.close();
            }, {once: true});
        });
    });
}

// noinspection JSUnusedGlobalSymbols
class Ingredient {
    element;

    constructor(root) {
        if (!(root instanceof HTMLElement)) {
            throw new TypeError("root must be an element");
        }

        this.element = root;
    }

    get name() {
        return this.#getProperty("name").value;
    }

    set name(value) {
        this.#getProperty("name").value = value;
    }

    get count() {
        return this.#getProperty("count").value;
    }

    set count(value) {
        if (!(typeof value === "number" || value instanceof Number)) {
            throw new TypeError("count must be a number");
        }

        this.#getProperty("count").value = value;
    }

    get unit() {
        return this.#getProperty("unit").value;
    }

    set unit(value) {
        this.#getProperty("unit").value = value;
    }

    #getProperty(propertyName) {
        return this.element.querySelector(`md-filled-text-field.${propertyName}`);
    }

    get remove_btn() {
        return this.element.querySelector("md-filled-icon-button.remove");
    }

    get listItem() {
        const result = this.element.parentElement;

        if (result === null) {
            throw new Error("Ingredient is not a child of a list item");
        } else if (!result instanceof HTMLElement) {
            throw new TypeError("Ingredient's parent must be an element");
        } else if (!(result.tagName.toLowerCase() === "md-list-item")) {
            throw new Error("Ingredient's parent must be a valid list item");
        }

        return result;
    }

    get number() {
        return Number(this.listItem.querySelector("span[slot='start']").innerHTML.replace("#", ""));
    }

    set number(value) {
        if (!(typeof value === "number" || value instanceof Number)) {
            throw new TypeError("value must be a number");
        }

        this.listItem.querySelector("span[slot='start']").innerHTML = `#${value}`;
    }

    async remove() {}
}

// noinspection JSUnusedGlobalSymbols
class Step {
    element;

    constructor(root) {
        this.element = root;
    }

    get listItem() {
        const result = this.element.parentElement;

        if (result === null) {
            throw new Error("Step is not a child of a list item");
        } else if (!result instanceof HTMLElement) {
            throw new TypeError("Step's parent must be an element");
        } else if (!(result.tagName.toLowerCase() === "md-list-item")) {
            throw new Error("Step's parent must be a valid list item");
        }

        return result;
    }

    get number() {
        return Number(this.listItem.querySelector("span[slot='start']").innerHTML.replace("#", ""));
    }

    set number(value) {
        if (!(typeof value === "number" || value instanceof Number)) {
            throw new TypeError("value must be a number");
        }

        this.listItem.querySelector("span[slot='start']").innerHTML = `#${value}`;
    }

    get desc() {
        return this.element.querySelector("md-filled-text-field.desc").value;
    }

    set desc(value) {
        this.element.querySelector("md-filled-text-field.desc").value = value;
    }
}
