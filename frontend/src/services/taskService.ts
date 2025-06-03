// Definiere eine Basis-URL für deine API.
// In einem echten Projekt würdest du dies wahrscheinlich aus Umgebungsvariablen beziehen.
const API_BASE_URL = process.env.NEXT_PUBLIC_API_BASE_URL || 'http://localhost:8080';

// Interface für die Daten, die zum Erstellen eines Tasks gesendet werden.
// Dies sollte die Struktur deines `TaskCreationRequestDTO` im Backend widerspiegeln.
export interface TaskCreationData {
    title: string;
    description?: string;
    dueDate?: string; // z.B. "2024-12-31T23:59:59"
    assigneeEmail?: string; // E-Mail des zugewiesenen Benutzers
    creatorId: number; // ID des Erstellers
    priority?: string; // z.B. 'HIGH', 'MEDIUM', 'LOW'
    status?: string; // z.B. 'TODO', 'IN_PROGRESS', 'DONE'
    simulateNotificationFailure?: boolean;
    // Füge hier weitere Felder hinzu, die dein DTO erwartet
}

// Interface für die Antwort beim Erstellen eines Tasks
export interface CreateTaskResponse {
    taskId: number;
    message: string;
    warning?: string; // Falls die Benachrichtigung fehlschlägt
    error?: string;   // Falls ein Fehler aufgetreten ist (vom Backend gesendet)
}

// Interface für ein User-Zusammenfassungsobjekt
export interface UserSummary {
    id: number;
    email: string;
    firstName?: string;
    lastName?: string;
    // ... weitere User-Felder bei Bedarf
}

// Interface für ein Task-Objekt (basierend auf deinem Backend-Modell)
export interface Task {
    id: number;
    title: string;
    description?: string;
    dueDate?: string;
    creationDate?: string;
    lastModifiedDate?: string;
    priority?: string;
    status?: string;
    creator?: UserSummary;
    assignee?: UserSummary;
    // ... weitere Felder deines Task-Modells
}

/**
 * Erstellt einen neuen Task.
 * Entspricht POST /api/tasks
 */
export async function createTask(taskData: TaskCreationData): Promise<CreateTaskResponse> {
    try {
        const response = await fetch(`${API_BASE_URL}/api/tasks`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
                // Füge hier ggf. Authentifizierungs-Header hinzu (z.B. Authorization: `Bearer ${token}`)
            },
            body: JSON.stringify(taskData),
        });

        const responseBody = await response.json();

        if (!response.ok) {
            // responseBody könnte ein "error"-Feld vom Controller enthalten
            console.error('Fehler beim Erstellen des Tasks:', responseBody);
            throw new Error(responseBody.error || `HTTP-Fehler! Status: ${response.status}`);
        }

        return responseBody as CreateTaskResponse;
    } catch (error) {
        console.error('Netzwerk- oder Parser-Fehler beim Erstellen des Tasks:', error);
        throw error; // Wirf den Fehler weiter, damit die aufrufende Komponente ihn behandeln kann
    }
}

/**
 * Ruft alle Tasks für einen bestimmten Benutzer ab.
 * Entspricht GET /api/tasks/user?userId={userId}
 */
export async function getTasksForUser(userId: number): Promise<Task[]> {
    try {
        const response = await fetch(`${API_BASE_URL}/api/tasks/user?userId=${userId}`, {
            method: 'GET',
            headers: {
                // Füge hier ggf. Authentifizierungs-Header hinzu
            },
        });

        if (!response.ok) {
            // Dein Backend wirft bei "User not found" eine NoSuchElementException,
            // was serverseitig zu einem 500er-Fehler führen kann.
            // Idealerweise würde das Backend 404 zurückgeben.
            const errorText = await response.text();
            console.error('Fehler beim Abrufen der Tasks für Benutzer:', errorText);
            throw new Error(`HTTP-Fehler! Status: ${response.status}, Nachricht: ${errorText}`);
        }

        return await response.json() as Task[];
    } catch (error) {
        console.error('Netzwerk- oder Parser-Fehler beim Abrufen der Tasks für Benutzer:', error);
        throw error;
    }
}

/**
 * Ruft alle Tasks im System ab.
 * Entspricht GET /api/tasks/all
 */
export async function getAllTasks(): Promise<Task[]> {
    try {
        const response = await fetch(`${API_BASE_URL}/api/tasks/all`, {
            method: 'GET',
            headers: {
                // Füge hier ggf. Authentifizierungs-Header hinzu
            },
        });

        if (!response.ok) {
            const errorText = await response.text();
            console.error('Fehler beim Abrufen aller Tasks:', errorText);
            throw new Error(`HTTP-Fehler! Status: ${response.status}, Nachricht: ${errorText}`);
        }

        return await response.json() as Task[];
    } catch (error) {
        console.error('Netzwerk- oder Parser-Fehler beim Abrufen aller Tasks:', error);
        throw error;
    }
}

/**
 * Ruft einen bestimmten Task anhand seiner ID ab.
 * Entspricht GET /api/tasks?id={id}
 */
export async function getTaskById(taskId: number): Promise<Task> {
    try {
        const response = await fetch(`${API_BASE_URL}/api/tasks?id=${taskId}`, {
            method: 'GET',
            headers: {
                // Füge hier ggf. Authentifizierungs-Header hinzu
            },
        });

        if (!response.ok) {
            if (response.status === 404) {
                throw new Error(`Task mit ID ${taskId} nicht gefunden.`);
            }
            const errorText = await response.text();
            console.error(`Fehler beim Abrufen des Tasks mit ID ${taskId}:`, errorText);
            throw new Error(`HTTP-Fehler! Status: ${response.status}, Nachricht: ${errorText}`);
        }

        return await response.json() as Task;
    } catch (error) {
        console.error(`Netzwerk- oder Parser-Fehler beim Abrufen des Tasks mit ID ${taskId}:`, error);
        throw error;
    }
}