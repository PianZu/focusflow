// src/app/tasks/page.tsx

"use client";

import { useEffect, useState } from 'react';
import {
  createTask,
  getAllTasks,
  updateTask,
  Task,
  TaskCreationData,
  TaskUpdateData,
  CreateTaskResponse,
  TaskPriority,
  TaskStatus
} from '@/services/taskService';

export default function TasksPage() {
  const [tasks, setTasks] = useState<Task[]>([]);
  const [isLoading, setIsLoading] = useState(false);
  const [error, setError] = useState<string | null>(null);

  // Create form state
  const [newTask, setNewTask] = useState<TaskCreationData>({
    title: '',
    description: undefined,
    longDescription: undefined,
    dueDate: '',
    assigneeId: undefined,
    teamId: undefined,
    creatorId: 1,        // TODO: replace with real auth
    priority: undefined,
    status: TaskStatus.OPEN,
    simulateNotificationFailure: false
  });

  // Edit state
  const [editingId, setEditingId] = useState<number | null>(null);
  const [editData, setEditData] = useState<TaskUpdateData>({});

  const fetchTasks = async () => {
    setIsLoading(true);
    setError(null);
    try {
      const all = await getAllTasks();
      setTasks(all);
    } catch (e: any) {
      setError(e.message);
    } finally {
      setIsLoading(false);
    }
  };

  useEffect(() => {
    fetchTasks();
  }, []);

  const handleCreate = async () => {
    if (!newTask.title.trim() || !newTask.dueDate || newTask.assigneeId == null) {
      setError('Titel, Fälligkeitsdatum und Assignee-ID sind erforderlich.');
      return;
    }
    setIsLoading(true);
    setError(null);
    try {
      const res: CreateTaskResponse = await createTask(newTask);
      if (res.warning) alert(`Warnung: ${res.warning}`);
      setNewTask({ ...newTask, title: '', description: undefined, longDescription: undefined, dueDate: '', assigneeId: undefined, teamId: undefined, priority: undefined, status: TaskStatus.OPEN });
      await fetchTasks();
    } catch (e: any) {
      setError(e.message);
    } finally {
      setIsLoading(false);
    }
  };

  const startEdit = (task: Task) => {
    setEditingId(task.id);
    setEditData({
      title: task.title,
      description: task.description,
      longDescription: task.longDescription,
      dueDate: task.dueDate?.split('T')[0],
      assigneeId: task.assignee?.id,
      teamId: task.team?.id,
      priority: task.priority,
      status: task.status
    });
  };

  const cancelEdit = () => {
    setEditingId(null);
    setEditData({});
  };

  const saveEdit = async () => {
    if (editingId == null) return;
    setIsLoading(true);
    setError(null);
    try {
      await updateTask(editingId, editData);
      cancelEdit();
      await fetchTasks();
    } catch (e: any) {
      setError(e.message);
    } finally {
      setIsLoading(false);
    }
  };

  return (
    <div style={{ maxWidth: 600, margin: '0 auto', padding: 20 }}>
      <h1>Tasks verwalten</h1>
      {error && <p style={{ color: 'red' }}>{error}</p>}

      {/* Create Section */}
      <section style={{ margin: '20px 0', padding: 20, border: '1px solid #ccc', borderRadius: 8 }}>
        <h2>Neuen Task erstellen</h2>
        <input
          type="text"
          placeholder="Titel *"
          value={newTask.title}
          onChange={e => setNewTask({ ...newTask, title: e.target.value })}
          disabled={isLoading}
          style={{ width: '100%', marginBottom: 10, padding: 8 }}
        />
        <input
          type="text"
          placeholder="Kurzbeschreibung"
          value={newTask.description || ''}
          onChange={e => setNewTask({ ...newTask, description: e.target.value })}
          disabled={isLoading}
          style={{ width: '100%', marginBottom: 10, padding: 8 }}
        />
        <textarea
          placeholder="Langbeschreibung"
          value={newTask.longDescription || ''}
          onChange={e => setNewTask({ ...newTask, longDescription: e.target.value })}
          disabled={isLoading}
          rows={3}
          style={{ width: '100%', marginBottom: 10, padding: 8 }}
        />
        <input
          type="date"
          value={newTask.dueDate || ''}
          onChange={e => setNewTask({ ...newTask, dueDate: e.target.value })}
          disabled={isLoading}
          style={{ width: '100%', marginBottom: 10, padding: 8 }}
        />
        <input
          type="number"
          placeholder="Assignee ID *"
          value={newTask.assigneeId || ''}
          onChange={e => setNewTask({ ...newTask, assigneeId: e.target.value ? +e.target.value : undefined })}
          disabled={isLoading}
          style={{ width: '100%', marginBottom: 10, padding: 8 }}
        />
        <input
          type="number"
          placeholder="Team ID"
          value={newTask.teamId || ''}
          onChange={e => setNewTask({ ...newTask, teamId: e.target.value ? +e.target.value : undefined })}
          disabled={isLoading}
          style={{ width: '100%', marginBottom: 10, padding: 8 }}
        />
        <div style={{ display: 'flex', gap: 10, marginBottom: 20 }}>
          <select
            value={newTask.priority || ''}
            onChange={e => setNewTask({ ...newTask, priority: e.target.value as TaskPriority })}
            disabled={isLoading}
            style={{ flex: 1, padding: 8 }}
          >
            <option value="">Priorität</option>
            {Object.values(TaskPriority).map(p => <option key={p} value={p}>{p}</option>)}
          </select>
          <select
            value={newTask.status}
            onChange={e => setNewTask({ ...newTask, status: e.target.value as TaskStatus })}
            disabled={isLoading}
            style={{ flex: 1, padding: 8 }}
          >
            {Object.values(TaskStatus).map(s => <option key={s} value={s}>{s}</option>)}
          </select>
        </div>
        <button onClick={handleCreate} disabled={isLoading} style={{ padding: '10px', width: '100%', backgroundColor: '#007bff', color: 'white', border: 'none', borderRadius: 4, cursor: 'pointer' }}>
          {isLoading ? 'Speichern...' : 'Task erstellen'}
        </button>
      </section>

      {/* List & Edit */}
      <ul style={{ listStyle: 'none', padding: 0 }}>
        {isLoading && tasks.length === 0 && <li>Lade Tasks...</li>}
        {tasks.map(task => (
          <li key={task.id} style={{ marginBottom: 10, padding: 10, border: '1px solid #eee', borderRadius: 4 }}>
            {editingId === task.id ? (
              <>  {/* Edit form */}
                <input
                  type="text"
                  value={editData.title || ''}
                  onChange={e => setEditData({ ...editData, title: e.target.value })}
                  style={{ width: '100%', marginBottom: 6, padding: 6 }}
                />
                <textarea
                  rows={2}
                  value={editData.longDescription || ''}
                  onChange={e => setEditData({ ...editData, longDescription: e.target.value })}
                  style={{ width: '100%', marginBottom: 6, padding: 6 }}
                />
                <input
                  type="date"
                  value={editData.dueDate || ''}
                  onChange={e => setEditData({ ...editData, dueDate: e.target.value })}
                  style={{ width: '100%', marginBottom: 6, padding: 6 }}
                />
                <div style={{ display: 'flex', gap: 6 }}>
                  <button onClick={saveEdit} disabled={isLoading}>Speichern</button>
                  <button onClick={cancelEdit} disabled={isLoading}>Abbrechen</button>
                </div>
              </>
            ) : (
              <>  {/* Display view */}
                <strong>{task.title}</strong><br/>
                {task.description && <small>{task.description}</small>}<br/>
                {task.dueDate && <small>Fällig: {task.dueDate.split('T')[0]}</small>}<br/>
                <button onClick={() => startEdit(task)}>Bearbeiten</button>
              </>
            )}
          </li>
        ))}
      </ul>
    </div>
  );
}
