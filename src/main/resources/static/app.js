let isProcessingTurn = false;
let currentRun = null;
let selectedMoves = [];

async function startRun() {

    const response = await fetch("/api/runs", {
        method: "POST"
    });

    currentRun = await response.json();

    renderRun(currentRun);

    showRunView();
}

function showRunView() {
    hideAllViews();

    if (currentRun !== null) {
        renderRun(currentRun);
    }

    document.getElementById("run-view")
        .classList.remove("hidden");
}

function renderRun(run) {

    renderHeroSummary(run.hero);

    renderEncounters(run.encounters);
}

function renderHeroSummary(hero) {

    const heroSummary = document.getElementById("hero-summary");

    heroSummary.innerHTML = `
        <h3>${hero.name}</h3>
        <p>Level: ${hero.level}</p>
        <p>XP: ${hero.experience}/${hero.experienceToNextLevel}</p>
        <p>HP: ${hero.stats.currentHealth}/${hero.stats.maxHealth}</p>
    `;
}

function renderEncounters(encounters) {

    const encountersContainer =
        document.getElementById("encounters");

    encountersContainer.innerHTML = "";

    const mapDiv = document.createElement("div");

    mapDiv.className = "encounter-map";

    encounters.forEach((encounter, index) => {

        const nodeDiv = document.createElement("div");

        nodeDiv.className =
            `encounter-node ${encounter.status.toLowerCase()}`;

        nodeDiv.innerHTML = `
            <div class="node-title">
                Encounter ${encounter.order}
            </div>

            <div class="node-monster">
                ${encounter.monster.name}
            </div>

            <span class="status-badge status-${encounter.status}">
                ${encounter.status}
            </span>
        `;

        if (encounter.status === "AVAILABLE"
            && currentRun.status === "IN_PROGRESS") {

            const startButton =
                document.createElement("button");

            startButton.textContent = "Battle";

            startButton.onclick = () => startBattle();

            nodeDiv.appendChild(startButton);
        }

        mapDiv.appendChild(nodeDiv);

        if (index < encounters.length - 1) {

            const connector =
                document.createElement("div");

            connector.className =
                "encounter-connector";

            mapDiv.appendChild(connector);
        }
    });

    encountersContainer.appendChild(mapDiv);
}

function showMoveManagement() {

    hideAllViews();

    document.getElementById("move-management")
        .classList.remove("hidden");

    renderLearnedMoves();
}

function hideAllViews() {

    document.getElementById("main-menu")
        .classList.add("hidden");

    document.getElementById("run-view")
        .classList.add("hidden");

    document.getElementById("move-management")
        .classList.add("hidden");

    document.getElementById("battle-view")
        .classList.add("hidden");
}

function renderLearnedMoves() {

    const learnedMovesContainer =
        document.getElementById("learned-moves");

    learnedMovesContainer.innerHTML = "";

    currentRun.hero.learnedMoves.forEach(move => {

        const moveDiv = document.createElement("div");

        moveDiv.className = "move-option";

        moveDiv.innerHTML = `
            <label>
                <input type="checkbox"
                       value="${move.id}"
                       ${currentRun.hero.equippedMoves.some(
            equippedMove => equippedMove.id === move.id
        ) ? "checked" : ""}
                >
                ${move.name}
            </label>
        `;

        learnedMovesContainer.appendChild(moveDiv);
    });
}

async function equipSelectedMoves() {

    const checkedBoxes = document.querySelectorAll(
        "#learned-moves input:checked"
    );

    const moveIds = Array.from(checkedBoxes)
        .map(checkbox => checkbox.value);

    if (moveIds.length > 4) {
        alert("You can equip at most 4 moves.");
        return;
    }

    const response = await fetch(
        `/api/runs/${currentRun.runId}/equip-moves`,
        {
            method: "PUT",
            headers: {
                "Content-Type": "application/json"
            },
            body: JSON.stringify({
                moveIds: moveIds
            })
        }
    );

    currentRun.hero = await response.json();

    alert("Moves equipped successfully.");

    showRunView();

    renderRun(currentRun);
}

async function startBattle() {

    const response = await fetch(
        `/api/battles/${currentRun.runId}/start`,
        {
            method: "POST"
        }
    );

    const battle = await response.json();

    await new Promise(resolve => setTimeout(resolve, 500));


    document.getElementById("battle-result-panel")
        .className = "result-panel hidden";

    document.getElementById("turn-indicator")
        .textContent = "Your Turn";

    renderBattle(battle);



    triggerBattleAnimations(battle);

    hideAllViews();

    document.getElementById("battle-view")
        .classList.remove("hidden");
}
async function playMove(moveId) {

    if (isProcessingTurn) {
        return;
    }

    isProcessingTurn = true;

    document.getElementById("turn-indicator")
        .textContent = "Enemy Turn...";

    const response = await fetch(
        `/api/battles/${currentRun.runId}/turn?moveId=${moveId}`,
        {
            method: "POST"
        }
    );

    const battle = await response.json();

    await new Promise(resolve => setTimeout(resolve, 600));

    renderBattle(battle);

    if (battle.status === "IN_PROGRESS") {
        document.getElementById("turn-indicator")
            .textContent = "Your Turn";
    }

    if (battle.status !== "IN_PROGRESS") {

        showBattleResult(battle);

        document.getElementById("back-to-run-button")
            .classList.remove("hidden");



        const runResponse = await fetch(
            `/api/runs/${currentRun.runId}`
        );

        currentRun = await runResponse.json();


    }
    isProcessingTurn = false;
}

function renderBattle(battle) {

    renderBattleCharacters(battle);

    renderBattleMoves(battle);

    renderBattleLog(battle);
}

function renderBattleCharacters(battle) {

    const hero = battle.hero;
    const monster = battle.monster;

    document.getElementById("hero-portrait")
        .textContent = "🛡️";

    document.getElementById("monster-portrait")
        .textContent = getMonsterPortrait(monster.id);

    document.getElementById("hero-name")
        .textContent = hero.name;

    document.getElementById("monster-name")
        .textContent = monster.name;

    document.getElementById("hero-hp")
        .textContent =
        `${hero.stats.currentHealth}/${hero.stats.maxHealth} HP`;

    document.getElementById("monster-hp")
        .textContent =
        `${monster.stats.currentHealth}/${monster.stats.maxHealth} HP`;

    const heroHealthPercent =
        (hero.stats.currentHealth / hero.stats.maxHealth) * 100;

    const monsterHealthPercent =
        (monster.stats.currentHealth / monster.stats.maxHealth) * 100;

    const heroHealthBar =
        document.getElementById("hero-health-fill");

    heroHealthBar.style.width =
        `${heroHealthPercent}%`;

    updateHealthBarColor(
        heroHealthBar,
        heroHealthPercent
    );

    const monsterHealthBar =
        document.getElementById("monster-health-fill");

    monsterHealthBar.style.width =
        `${monsterHealthPercent}%`;

    updateHealthBarColor(
        monsterHealthBar,
        monsterHealthPercent
    );
}

function renderBattleMoves(battle) {

    const moveButtons =
        document.getElementById("move-buttons");

    moveButtons.innerHTML = "";

    if (battle.status !== "IN_PROGRESS") {
        return;
    }

    battle.hero.equippedMoves.forEach(move => {

        const button = document.createElement("button");

        button.className = "move-button";


        button.setAttribute("data-description", move.description);

        button.innerHTML = `
            <span class="move-icon">${getMoveIcon(move)}</span>
            ${move.name}
            <small>${move.effectType} • ${move.type}</small>
        `;

        button.onclick = () => playMove(move.id);

        moveButtons.appendChild(button);
    });
}

function renderBattleLog(battle) {

    const battleLog =
        document.getElementById("battle-log");

    battleLog.innerHTML = "";

    battle.events.forEach(event => {

        const logEntry =
            document.createElement("div");

        logEntry.className = `log-entry log-${event.type}`;

        logEntry.textContent =
            `${getEventIcon(event.type)} ${event.message}`;

        battleLog.appendChild(logEntry);
    });

    battleLog.scrollTop = battleLog.scrollHeight;
}
function showBattleResult(battle) {

    const resultPanel =
        document.getElementById("battle-result-panel");

    resultPanel.classList.remove("hidden");

    if (battle.status === "HERO_WON") {

        resultPanel.className =
            "result-panel victory";

        let learnedMoveText = "";

        if (battle.learnedMove !== null) {
            learnedMoveText =
                `<p>Learned Move: ${battle.learnedMove.name}</p>`;
        }

        resultPanel.innerHTML = `
            <h3>Victory!</h3>
            <p>You defeated ${battle.monster.name}.</p>
            ${learnedMoveText}
        `;
    }

    if (battle.status === "HERO_LOST") {

        resultPanel.className =
            "result-panel defeat";

        resultPanel.innerHTML = `
            <h3>Game Over</h3>
            <p>Your hero was defeated.</p>
        `;
    }
}
function getMonsterPortrait(monsterId) {
    const portraits = {
        "goblin-warrior": "👺",
        "giant-spider": "🕷️",
        "witch": "🧙",
        "goblin-mage": "🔮",
        "dragon": "🐉"
    };

    return portraits[monsterId] || "👹";
}

function getMoveIcon(move) {
    if (move.effectType === "HEAL") return "💚";
    if (move.effectType === "DRAIN_LIFE") return "🩸";
    if (move.effectType.startsWith("BUFF")) return "⬆️";
    if (move.effectType.startsWith("DEBUFF")) return "⬇️";
    if (move.type === "MAGIC") return "✨";
    if (move.type === "PHYSICAL") return "⚔️";

    return "❔";
}
function getEventIcon(type) {
    if (type === "MOVE_USED") return "🎯";
    if (type === "DAMAGE_DEALT") return "⚔️";
    if (type === "HEAL_APPLIED") return "💚";
    if (type === "EFFECT_APPLIED") return "✨";
    if (type === "EFFECT_EXPIRED") return "⌛";
    if (type === "CHARACTER_DEFEATED") return "☠️";

    return "•";
}
function triggerBattleAnimations(battle) {
    const heroCard = document.getElementById("hero-card");
    const monsterCard = document.getElementById("monster-card");

    const lastEvents = battle.events.slice(-3);

    lastEvents.forEach(event => {
        if (event.type === "DAMAGE_DEALT") {
            if (event.message.includes(battle.hero.name)) {
                heroCard.classList.add("hit");
                setTimeout(() => heroCard.classList.remove("hit"), 350);
            }

            if (event.message.includes(battle.monster.name)) {
                monsterCard.classList.add("hit");
                setTimeout(() => monsterCard.classList.remove("hit"), 350);
            }
        }

        if (event.type === "HEAL_APPLIED") {
            if (event.message.includes(battle.hero.name)) {
                heroCard.classList.add("heal");
                setTimeout(() => heroCard.classList.remove("heal"), 450);
            }

            if (event.message.includes(battle.monster.name)) {
                monsterCard.classList.add("heal");
                setTimeout(() => monsterCard.classList.remove("heal"), 450);
            }
        }
    });
}
function updateHealthBarColor(barElement, percentage) {

    if (percentage > 60) {
        barElement.style.backgroundColor = "#2ecc71";
        return;
    }

    if (percentage > 30) {
        barElement.style.backgroundColor = "#f1c40f";
        return;
    }

    barElement.style.backgroundColor = "#e74c3c";
}