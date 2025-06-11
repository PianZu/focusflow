// src/app/tasks/page.tsx
"use client";

import { useEffect, useState } from 'react';
import {
  createTask,
  getAllTasks,
  updateTask,
  deleteTask,
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

  const [showCreateForm, setShowCreateForm] = useState(false);
  const [newTask, setNewTask] = useState<TaskCreationData>({
    title: '',
    description: undefined,
    longDescription: undefined,
    dueDate: '',
    assigneeId: undefined,
    teamId: undefined,
    creatorId: 1,
    priority: undefined,
    status: TaskStatus.OPEN,
    simulateNotificationFailure: false
  });

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
      setNewTask({
        ...newTask,
        title: '',
        description: undefined,
        longDescription: undefined,
        dueDate: '',
        assigneeId: undefined,
        teamId: undefined,
        priority: undefined,
        status: TaskStatus.OPEN
      });
      setShowCreateForm(false);
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
      await fetchTasks();
      cancelEdit();
    } catch (e: any) {
      setError(e.message);
    } finally {
      setIsLoading(false);
    }
  };

  const handleDelete = async (id: number) => {
    if (!confirm('Task wirklich löschen?')) return;
    setIsLoading(true);
    setError(null);
    try {
      await deleteTask(id);
      await fetchTasks();
    } catch (e: any) {
      setError(e.message);
    } finally {
      setIsLoading(false);
    }
  };

  return (
    <div className="container">
      <h1 className="title">Tasks verwalten</h1>
      {error && <p className="error">{error}</p>}

      <button
        className={`btn toggle ${showCreateForm ? 'btn-secondary' : 'btn-primary'}`}
        onClick={() => setShowCreateForm(v => !v)}
      >
        {showCreateForm ? 'Abbrechen' : 'Neuen Task erstellen'}
      </button>

      {showCreateForm && (
        <section className="create-form">
          <h2>Neuen Task anlegen</h2>
          <div className="form-grid">
            <div className="form-group">
              <label>Titel *</label>
              <input
                type="text"
                value={newTask.title}
                onChange={e => setNewTask({ ...newTask, title: e.target.value })}
                disabled={isLoading}
              />
            </div>

            <div className="form-group">
              <label>Assignee ID *</label>
              <input
                type="number"
                value={newTask.assigneeId || ''}
                onChange={e => setNewTask({ ...newTask, assigneeId: e.target.value ? +e.target.value : undefined })}
                disabled={isLoading}
              />
            </div>

            <div className="form-group">
              <label>Fälligkeitsdatum *</label>
              <input
                type="date"
                value={newTask.dueDate}
                onChange={e => setNewTask({ ...newTask, dueDate: e.target.value })}
                disabled={isLoading}
              />
            </div>

            <div className="form-group">
              <label>Team ID</label>
              <input
                type="number"
                value={newTask.teamId || ''}
                onChange={e => setNewTask({ ...newTask, teamId: e.target.value ? +e.target.value : undefined })}
                disabled={isLoading}
              />
            </div>

            <div className="form-group">
              <label>Priorität</label>
              <select
                value={newTask.priority || ''}
                onChange={e => setNewTask({ ...newTask, priority: e.target.value as TaskPriority })}
                disabled={isLoading}
              >
                <option value="">–</option>
                {Object.values(TaskPriority).map(p => <option key={p} value={p}>{p}</option>)}
              </select>
            </div>

            <div className="form-group">
              <label>Status</label>
              <select
                value={newTask.status}
                onChange={e => setNewTask({ ...newTask, status: e.target.value as TaskStatus })}
                disabled={isLoading}
              >
                {Object.values(TaskStatus).map(s => (
                  <option key={s} value={s}>{s.replace('_', ' ')}</option>
                ))}
              </select>
            </div>

            <div className="form-group full">
              <label>Kurzbeschreibung</label>
              <input
                type="text"
                value={newTask.description || ''}
                onChange={e => setNewTask({ ...newTask, description: e.target.value })}
                disabled={isLoading}
              />
            </div>

            <div className="form-group full">
              <label>Langbeschreibung</label>
              <textarea
                rows={3}
                value={newTask.longDescription || ''}
                onChange={e => setNewTask({ ...newTask, longDescription: e.target.value })}
                disabled={isLoading}
              />
            </div>
          </div>

          <button
            className="btn btn-primary full"
            onClick={handleCreate}
            disabled={isLoading}
          >
            {isLoading ? 'Speichern...' : 'Task erstellen'}
          </button>
        </section>
      )}

      <ul className="task-list">
        {isLoading && tasks.length === 0 && <li>Lade Tasks…</li>}
        {tasks.map(task => {
          const statusClass = task.status?.toLowerCase().replace('_', '-');
          return (
            <li key={task.id} className="task-card">
              {editingId === task.id ? (
                <div className="edit-form">
                  <input
                    type="text"
                    value={editData.title || ''}
                    onChange={e => setEditData({ ...editData, title: e.target.value })}
                    placeholder="Titel"
                  />
                  <input
                    type="text"
                    value={editData.description || ''}
                    onChange={e => setEditData({ ...editData, description: e.target.value })}
                    placeholder="Kurzbeschreibung"
                  />
                  <textarea
                    rows={2}
                    value={editData.longDescription || ''}
                    onChange={e => setEditData({ ...editData, longDescription: e.target.value })}
                    placeholder="Langbeschreibung"
                  />
                  <input
                    type="date"
                    value={editData.dueDate || ''}
                    onChange={e => setEditData({ ...editData, dueDate: e.target.value })}
                  />

                  <select
                    value={editData.priority || ''}
                    onChange={e => setEditData({ ...editData, priority: e.target.value as TaskPriority })}
                  >
                    <option value="">Priorität</option>
                    {Object.values(TaskPriority).map(p => (
                      <option key={p} value={p}>{p}</option>
                    ))}
                  </select>

                  <select
                    value={editData.status || ''}
                    onChange={e => setEditData({ ...editData, status: e.target.value as TaskStatus })}
                  >
                    <option value="">Status</option>
                    {Object.values(TaskStatus).map(s => (
                      <option key={s} value={s}>{s.replace('_', ' ')}</option>
                    ))}
                  </select>

                  <div className="task-actions">
                    <button className="btn btn-primary" onClick={saveEdit}>Speichern</button>
                    <button className="btn btn-secondary" onClick={cancelEdit}>Abbrechen</button>
                  </div>
                </div>
              ) : (
                <>
                  <div className="task-info">
                    <div className="task-header">
                      <h3>{task.title}</h3>
                      <span className={`badge badge-${statusClass}`}>
                        {task.status?.replace('_', ' ')}
                      </span>
                    </div>
                    {task.description && <p className="desc">{task.description}</p>}
                    {task.longDescription && <p className="desc small">{task.longDescription}</p>}
                    {task.dueDate && <p className="due">Fällig: {task.dueDate.split('T')[0]}</p>}
                  </div>
                  <div className="task-actions">
                    <button className="btn btn-secondary" onClick={() => startEdit(task)}>Bearbeiten</button>
                    <button className="btn btn-danger" onClick={() => handleDelete(task.id)}>Löschen</button>
                  </div>
                </>
              )}
            </li>
          );
        })}
      </ul>

      <style jsx>{`
        .container {
          max-width: 800px;
          margin: 0 auto;
          padding: 20px;
          font-family: sans-serif;
          color: #222;              /* dunkler Grundtext */
        }
        .title {
          text-align: center;
          margin-bottom: 20px;
          color: #111;
        }
        .error {
          color: #b00020;
          text-align: center;
        }
        .btn {
          border: none;
          border-radius: 4px;
          cursor: pointer;
          padding: 8px 12px;
          font-size: 0.9rem;
        }
        .btn-primary { background: #007bff; color: #fff; }
        .btn-secondary { background: #6c757d; color: #fff; }
        .btn-danger { background: #dc3545; color: #fff; }
        .toggle { margin-bottom: 20px; }
        .full { width: 100%; }

        .create-form {
          background: #f9f9f9;
          padding: 20px;
          border-radius: 8px;
          margin-bottom: 40px;
        }
        .form-grid {
          display: grid;
          grid-template-columns: repeat(auto-fit, minmax(240px, 1fr));
          gap: 16px;
          margin-bottom: 16px;
        }
        .form-group label {
          display: block;
          margin-bottom: 4px;
          font-weight: 600;
          color: #222;
        }
        .form-group input,
        .form-group textarea,
        .form-group select {
          width: 100%;
          padding: 8px;
          border: 1px solid #ccc;
          border-radius: 4px;
          color: #222;            /* dunklere Eingabetexte */
        }

        .task-list {
          list-style: none;
          padding: 0;
        }
        .task-card {
          background: #fff;
          border: 1px solid #e0e0e0;
          border-radius: 8px;
          padding: 16px;
          margin-bottom: 16px;
          display: flex;
          justify-content: space-between;
          align-items: flex-start;
          box-shadow: 0 2px 4px rgba(0,0,0,0.05);
        }
        .task-info {
          max-width: 70%;
        }
        .task-header {
          display: flex;
          align-items: center;
          gap: 8px;
          margin-bottom: 8px;
        }
        .task-header h3 {
          margin: 0;
          font-size: 1.1rem;
          color: #111;
        }
        .badge {
          font-size: 0.75rem;
          padding: 2px 6px;
          border-radius: 4px;
          color: #fff;
        }
        .badge-open    { background: #28a745; }
        .badge-pending { background: #ffc107; color: #212529; }
        .badge-in-review { background: #17a2b8; }
        .badge-closed  { background: #6c757d; }

        .desc {
          margin: 4px 0;
          color: #333;             /* dunklerer Absatztext */
        }
        .desc.small {
          font-size: 0.85rem;
          color: #444;            /* etwas dunkler als vorher */
        }
        .due {
          font-size: 0.85rem;
          color: #333;            /* gleiches Level wie desc */
        }

        .task-actions {
          display: flex;
          flex-direction: column;
          gap: 8px;
        }
        .edit-form input,
        .edit-form textarea {
          width: 100%;
          margin-bottom: 8px;
          padding: 8px;
          border: 1px solid #ccc;
          border-radius: 4px;
          color: #222;
        }
      `}</style>
    </div>
  );
}
