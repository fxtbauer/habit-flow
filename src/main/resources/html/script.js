const habitList = document.getElementById("habitList");

        function addHabit() {
            const name = document.getElementById("habitName").value;

            if (name.trim().length === 0) {
                alert("Digite um hábito antes de adicionar.");
                return;
            }

            const li = document.createElement("li");
            li.textContent = name;
            habitList.appendChild(li);

            document.getElementById("habitName").value = "";
        }
