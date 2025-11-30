const repo = new HabitRepository();

function renderHabits() {
    const list = document.getElementById("habitList");
    list.innerHTML = "";

    repo.getAll().forEach(habit => {
        const li = document.createElement("li");
        li.textContent = habit.name;

        // Adicionando botão de remoção opcional
        const removeBtn = document.createElement("button");
        removeBtn.textContent = "x";
        removeBtn.onclick = () => {
            repo.remove(habit.id);
            renderHabits();
        };

        li.appendChild(removeBtn);
        list.appendChild(li);
    });
}

function addHabit() {
    const name = document.getElementById("habitName").value.trim();
    if (!name) return alert("Digite um nome válido.");

    repo.add(name);
    document.getElementById("habitName").value = "";
    renderHabits();
}

document.addEventListener("DOMContentLoaded", renderHabits);
