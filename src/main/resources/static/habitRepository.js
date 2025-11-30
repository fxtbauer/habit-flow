class HabitRepository {
    constructor(storageKey = "habits") {
        this.storageKey = storageKey;
    }

    _load() {
        return JSON.parse(localStorage.getItem(this.storageKey)) || [];
    }

    _save(data) {
        localStorage.setItem(this.storageKey, JSON.stringify(data));
    }

    getAll() {
        return this._load();
    }

    add(habitName) {
        const habits = this._load();
        const newHabit = {
            id: Date.now(),
            name: habitName
        };

        habits.push(newHabit);
        this._save(habits);
        return newHabit;
    }

    remove(id) {
        const habits = this._load().filter(h => h.id !== id);
        this._save(habits);
    }

    clear() {
        localStorage.removeItem(this.storageKey);
    }
}
