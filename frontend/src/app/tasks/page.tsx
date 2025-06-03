// src/app/tasks/page.tsx

"use client"; // Da wir fetch im Client verwenden und State managen

import { useEffect, useState } from 'react';
import {
    createTask,
    getTasksForUser,
    Task,
    TaskCreationData,
    CreateTaskResponse
} from '@/services/taskService'; // Passe den Pfad an

export default function TasksPage() {
    const [tasks, setTasks] = useState<Task[]>([]);
    const [newTaskTitle, setNewTaskTitle] = useState('');
    const [error, setError] = useState<string | null>(null);
    const [isLoading, setIsLoading] = useState(false);

    // WICHTIG: Dies ist ein Platzhalter!
    // In einer echten Anwendung muss currentUserId vom tatsächlich eingeloggten Benutzer kommen.
    // Dies erfordert eine Authentifizierungslösung (z.B. React Context, Zustand).
    const currentUserId = 1; // Ersetze dies durch die tatsächliche User-ID des eingeloggten Benutzers

    useEffect(() => {
        // Nur Tasks laden, wenn eine gültige (wenn auch hier noch hartcodierte) User-ID vorhanden ist.
        if (currentUserId) {
            async function fetchTasks() {
                setIsLoading(true);
                setError(null);
                try {
                    const userTasks = await getTasksForUser(currentUserId);
                    setTasks(userTasks);
                } catch (err: any) {
                    setError(err.message || 'Fehler beim Laden der Tasks.');
                } finally {
                    setIsLoading(false);
                }
            }
            fetchTasks();
        }
    }, [currentUserId]);

    const handleCreateTask = async () => {
        if (!currentUserId) {
            setError("Benutzer nicht identifiziert. Bitte einloggen.");
            return;
        }
        if (!newTaskTitle.trim()) {
            setError("Titel darf nicht leer sein.");
            return;
        }
        setIsLoading(true);
        setError(null);
        const taskData: TaskCreationData = {
            title: newTaskTitle,
            creatorId: currentUserId, // Wichtig: creatorId muss gesetzt sein
        };

        try {
            const result: CreateTaskResponse = await createTask(taskData);
            console.log('Task erstellt:', result);
            setNewTaskTitle(''); // Formular zurücksetzen
            // Tasks neu laden, um die Liste zu aktualisieren
            const userTasks = await getTasksForUser(currentUserId);
            setTasks(userTasks);
            if (result.warning) {
                alert(`Warnung: ${result.warning}`);
            }
        } catch (err: any) {
            setError(err.message || 'Fehler beim Erstellen des Tasks.');
        } finally {
            setIsLoading(false);
        }
    };

    return (
        <div>
            <h1>Meine Tasks</h1>
            {error && <p style={{ color: 'red' }}>{error}</p>}

            <div style={{ marginBottom: '20px' }}>
                <input
                    type="text"
                    value={newTaskTitle}
                    onChange={(e) => setNewTaskTitle(e.target.value)}
                    placeholder="Neuer Task Titel"
                    disabled={isLoading}
                    style={{ padding: '8px', marginRight: '10px', borderRadius: '4px', border: '1px solid #ccc' }}
                />
                <button onClick={handleCreateTask} disabled={isLoading} style={{ padding: '8px 15px', backgroundColor: '#007bff', color: 'white', border: 'none', borderRadius: '4px', cursor: 'pointer' }}>
                    {isLoading ? 'Speichern...' : 'Task erstellen'}
                </button>
            </div>

            {isLoading && tasks.length === 0 && <p>Lade Tasks...</p>}

            <ul>
                {tasks.map(task => (
                    <li key={task.id} style={{ marginBottom: '10px', padding: '10px', border: '1px solid #eee', borderRadius: '4px' }}>
                        <strong>{task.title}</strong>
                        {task.description && <p style={{ fontSize: '0.9em', color: '#555', margin: '5px 0 0 0' }}>{task.description}</p>}
                    </li>
                ))}
            </ul>
        </div>
    );
}