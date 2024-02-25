document.addEventListener('DOMContentLoaded', function() {
    fetchTasks();
});

function fetchTasks() {
    fetch('http://localhost:8080/api/tasks')
        .then(response => {
            if (!response.ok) {
                throw new Error('Network response was not ok');
            }
            return response.json();
        })
        .then(data => {
            displayTasks(data);
        })
        .catch(error => {
            console.error('Error fetching tasks:', error);
        });
}

function displayTasks(tasks) {
    const taskList = document.getElementById('taskList');
    taskList.innerHTML = '';
    tasks.forEach(task => {
        const taskElement = document.createElement('div');
        taskElement.classList.add('task');
        taskElement.textContent = task.description;
        taskList.appendChild(taskElement);
    });
}
