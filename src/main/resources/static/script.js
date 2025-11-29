function initHabitAddPage() {

    const habitList = document.getElementById("habitList");
    const habitNameInput = document.getElementById("habitName");

    if (!habitList || !habitNameInput) {
        console.warn("Elementos não encontrados — a página ADD ainda não está visível.");
        return;
    }

    window.addHabit = function () {
        const name = habitNameInput.value;

        if (name.trim().length === 0) {
            alert("Digite um hábito antes de adicionar.");
            return;
        }

        const li = document.createElement("li");
        li.textContent = name;
        habitList.appendChild(li);

        habitNameInput.value = "";
    };
}
    document.addEventListener("DOMContentLoaded", () => {
    initHabitAddPage();
});

