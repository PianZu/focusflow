// src/services/taskService.ts

const API_BASE_URL = process.env.NEXT_PUBLIC_API_BASE_URL || 'http://localhost:8080';

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

export interface TaskCreationData {
  title: string;
  description?: string;
  longDescription?: string;
  dueDate?: string;
  assigneeId?: number;
  teamId?: number;
  creatorId: number;
  priority?: TaskPriority;
  status?: TaskStatus;
  simulateNotificationFailure?: boolean;
}

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

export interface CreateTaskResponse {
  taskId: number;
  message: string;
  warning?: string;
  error?: string;
}

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

export function createTask(
  taskData: TaskCreationData
): Promise<CreateTaskResponse> {
  return apiFetch<CreateTaskResponse>(`/api/tasks`, {
    method: 'POST',
    body: JSON.stringify(taskData),
  });
}

export function getAllTasks(): Promise<Task[]> {
  return apiFetch<Task[]>(`/api/tasks/all`, {
    // Cache ausschalten, damit wir immer frische Daten bekommen
    cache: 'no-cache',
  });
}

export function updateTask(
  taskId: number,
  data: TaskUpdateData
): Promise<void> {
  // bleibt void, wir machen das UI-Merge im Frontend
  return apiFetch<void>(`/api/tasks/${taskId}`, {
    method: 'PUT',
    body: JSON.stringify(data),
  });
}

export function getTaskById(
  taskId: number
): Promise<Task> {
  return apiFetch<Task>(`/api/tasks/${taskId}`);
}

export function getTasksForUser(
  userId: number
): Promise<Task[]> {
  return apiFetch<Task[]>(`/api/tasks/user?userId=${userId}`);
}

export function getAssignedTasks(
  userId: number
): Promise<Task[]> {
  return apiFetch<Task[]>(`/api/tasks?assigneeId=${userId}`);
}

/** Löscht einen Task per ID */
export function deleteTask(taskId: number): Promise<void> {
  return apiFetch<void>(`/api/tasks/${taskId}`, {
    method: 'DELETE'
  });
}
