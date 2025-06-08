// src/services/taskService.ts

// Basis-URL für deine API (aus .env oder Fallback)
const API_BASE_URL = process.env.NEXT_PUBLIC_API_BASE_URL || 'http://localhost:8080';

// Enums für Task Priority und Status (entsprechend dem Backend)
export enum TaskPriority {
  LOW = "LOW",
  MEDIUM = "MEDIUM",
  HIGH = "HIGH",
}

export enum TaskStatus {
  OPEN = "OPEN",
  PENDING = "PENDING",
  IN_REVIEW = "IN_REVIEW",
  CLOSED = "CLOSED",
}

// --- Typen ---

/** Payload zum Erstellen eines Tasks */
export interface TaskCreationData {
  title: string;
  description?: string;
  longDescription?: string;
  dueDate?: string;       // ISO-String YYYY-MM-DD
  assigneeId?: number;
  teamId?: number;
  creatorId: number;
  priority?: TaskPriority;
  status?: TaskStatus;
  simulateNotificationFailure?: boolean;
}

/** Payload zum Aktualisieren eines Tasks */
export interface TaskUpdateData {
  title?: string;
  description?: string;
  longDescription?: string;
  dueDate?: string;
  assigneeId?: number;
  teamId?: number;
  priority?: TaskPriority;
  status?: TaskStatus;
}

/** Antwort nach Task-Erstellung */
export interface CreateTaskResponse {
  taskId: number;
  message: string;
  warning?: string;
  error?: string;
}

/** Minimaler Task-Typ für das Frontend */
export interface Task {
  id: number;
  title: string;
  description?: string;
  longDescription?: string;
  dueDate?: string;
  creationDate?: string;
  lastModifiedDate?: string;
  priority?: TaskPriority;
  status?: TaskStatus;
  creator?: { id: number; email: string };
  assignee?: { id: number; email: string };
  team?: { id: number; name: string };
}

// --- Helper für fetch (DRY) ---
async function apiFetch<T>(
  path: string,
  options: RequestInit = {}
): Promise<T> {
  const res = await fetch(`${API_BASE_URL}${path}`, {
    headers: { 'Content-Type': 'application/json' },
    ...options,
  });
  const text = await res.text();
  const data = text ? JSON.parse(text) : null;
  if (!res.ok) {
    const msg = (data && (data.error || data.message)) || res.statusText;
    throw new Error(`HTTP-Fehler ${res.status}: ${msg}`);
  }
  return data as T;
}

// --- Service-Funktionen ---

/** Erstelle einen neuen Task */
export function createTask(
  taskData: TaskCreationData
): Promise<CreateTaskResponse> {
  return apiFetch<CreateTaskResponse>(`/api/tasks`, {
    method: 'POST',
    body: JSON.stringify(taskData),
  });
}

/** Lade Tasks für einen bestimmten User */
export function getTasksForUser(
  userId: number
): Promise<Task[]> {
  return apiFetch<Task[]>(`/api/tasks/user?userId=${userId}`);
}

/** Lade alle Tasks */
export function getAllTasks(): Promise<Task[]> {
  return apiFetch<Task[]>(`/api/tasks/all`);
}

/** Lade einen Task anhand der ID */
export function getTaskById(
  taskId: number
): Promise<Task> {
  return apiFetch<Task>(`/api/tasks/${taskId}`);
}

/** Aktualisiere einen bestehenden Task */
export function updateTask(
  taskId: number,
  data: TaskUpdateData
): Promise<void> {
  return apiFetch<void>(`/api/tasks/${taskId}`, {
    method: 'PUT',
    body: JSON.stringify(data),
  });
}

/** Lade Tasks, denen der User assigned ist (falls benötigt) */
export function getAssignedTasks(
  userId: number
): Promise<Task[]> {
  return apiFetch<Task[]>(`/api/tasks?assigneeId=${userId}`);
}
