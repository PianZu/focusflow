// src/app/(app)/tasks/page.tsx
"use client";

import { useEffect, useState } from 'react';
import {
  createTask,
  updateTask,
  deleteTask,
  Task,
  TaskCreationData,
  TaskUpdateData,
  TaskPriority,
  TaskStatus,
  getTasksForUser,
} from '../../../services/taskService';
import { getTeamsForUser, Team, UserInTeam } from '../../../services/teamService';
import { useUser } from '../../../context/sessionContext';

export default function TasksPage() {
  const { user } = useUser();
  
  const [tasks, setTasks] = useState<Task[]>([]);
  const [userTeams, setUserTeams] = useState<Team[]>([]); // State für die Teams des Users
  const [selectedTeamMembers, setSelectedTeamMembers] = useState<UserInTeam[]>([]); // State für die Mitglieder des gewählten Teams

  const [isLoading, setIsLoading] = useState(true); // Ladezustand für die gesamte Seite
  const [isSubmitting, setIsSubmitting] = useState(false); // Separater Ladezustand für den Create-Button
  const [error, setError] = useState<string | null>(null);

  const [showCreateForm, setShowCreateForm] = useState(false);
  const [newTask, setNewTask] = useState<TaskCreationData>({
    title: '',
    description: '',
    longDescription: '',
    dueDate: '',
    assigneeEmail: '',
    teamId: undefined,
    creatorId: user?.id || 0,
    priority: TaskPriority.MEDIUM,
    status: TaskStatus.OPEN,
  });

  const [editingId, setEditingId] = useState<number | null>(null);
  const [editData, setEditData] = useState<TaskUpdateData>({});

  // Holt alle initialen Daten (Tasks und Teams für die Dropdowns)
  const fetchInitialData = async (userId: number) => {
    setIsLoading(true);
    setError(null);
    try {
      const [userTasks, teams] = await Promise.all([
        getTasksForUser(userId),
        getTeamsForUser(userId)
      ]);
      setTasks(userTasks || []);
      setUserTeams(teams || []);
    } catch (e: any) {
      setError("Fehler beim Laden der Daten: " + e.message);
    } finally {
      setIsLoading(false);
    }
  };

  useEffect(() => {
    if (user?.id) {
      fetchInitialData(user.id);
      setNewTask(prev => ({ ...prev, creatorId: user.id }));
    }
  }, [user?.id]);
  
  // Diese Funktion wird aufgerufen, wenn ein Team im Dropdown ausgewählt wird
  const handleTeamChange = (event: React.ChangeEvent<HTMLSelectElement>) => {
    const selectedTeamId = Number(event.target.value);
    
    // Setze die teamId und resete die Auswahl für den Assignee
    setNewTask({
        ...newTask,
        teamId: selectedTeamId || undefined,
        assigneeEmail: '', // Wichtig: Assignee zurücksetzen!
    });

    if (selectedTeamId) {
        const selectedTeam = userTeams.find(team => team.id === selectedTeamId);
        setSelectedTeamMembers(selectedTeam?.members || []);
    } else {
        setSelectedTeamMembers([]); // Kein Team gewählt -> keine Mitglieder
    }
  };

  const handleCreate = async () => {
    if (!user?.id) {
      setError('Du musst angemeldet sein.');
      return;
    }
    if (!newTask.teamId || !newTask.assigneeEmail) {
      setError('Bitte wähle ein Team und eine zugewiesene Person aus.');
      return;
    }
    if (!newTask.title.trim() || !newTask.dueDate) {
      setError('Titel und Fälligkeitsdatum sind erforderlich.');
      return;
    }

    setIsSubmitting(true);
    setError(null);
    try {
      await createTask({ ...newTask, creatorId: user.id });
      setShowCreateForm(false);
      // Formular sauber zurücksetzen
      setNewTask({ title: '', assigneeEmail: '', teamId: undefined, creatorId: user.id, status: TaskStatus.OPEN, dueDate: '', description: '', longDescription: '', priority: TaskPriority.MEDIUM });
      setSelectedTeamMembers([]);
      await fetchInitialData(user.id); // Lade alles neu
    } catch (e: any) {
      setError(e.message);
    } finally {
      setIsSubmitting(false);
    }
  };
  
  // Edit- und Delete-Funktionen bleiben unverändert, da sie mit Task-IDs arbeiten
  const startEdit = (task: Task) => { /* ... */ };
  const cancelEdit = () => { /* ... */ };
  const saveEdit = async () => { /* ... */ };
  const handleDelete = async (id: number) => { /* ... */ };

  const statusStyles: Record<TaskStatus, string> = { OPEN: "bg-emerald-100 text-emerald-800", PENDING: "bg-amber-100 text-amber-800", IN_REVIEW: "bg-sky-100 text-sky-800", CLOSED: "bg-zinc-200 text-zinc-600" };

  return (
    <>
      <header className="sticky top-0 z-10 flex h-16 items-center justify-between border-b border-zinc-200 bg-white/80 px-6 backdrop-blur-md">
        <div>
            <h1 className="text-xl font-bold">Aufgaben</h1>
            <p className="text-sm text-zinc-500">Alle zugewiesenen Aufgaben</p>
        </div>
        <button className="btn-primary" onClick={() => setShowCreateForm(v => !v)}>
            {showCreateForm ? 'Abbrechen' : 'Neue Aufgabe'}
        </button>
      </header>
      
      <main className="flex-1 p-6 md:p-8">
        {error && <p className="mb-4 rounded-md bg-red-50 p-3 text-center text-sm font-medium text-red-700 ring-1 ring-red-100">{error}</p>}
        
        {showCreateForm && (
            <section className="mb-8 rounded-2xl border border-zinc-200 bg-white p-6 shadow-sm">
                <h2 className="text-lg font-semibold mb-4">Neue Aufgabe anlegen</h2>
                <div className="grid grid-cols-1 md:grid-cols-2 gap-4 mb-4">
                    {/* Titel & Datum */}
                    <input type="text" value={newTask.title} onChange={e => setNewTask({ ...newTask, title: e.target.value })} placeholder="Titel *" className="input-field" />
                    <input type="date" value={newTask.dueDate} onChange={e => setNewTask({ ...newTask, dueDate: e.target.value })} placeholder="Fälligkeitsdatum *" className="input-field" />
                    
                    {/* Team-Dropdown */}
                    <select value={newTask.teamId || ''} onChange={handleTeamChange} className="input-field">
                        <option value="">-- Team auswählen --</option>
                        {userTeams.map(team => (
                            <option key={team.id} value={team.id}>{team.name}</option>
                        ))}
                    </select>

                    {/* Assignee-Dropdown (abhängig vom Team) */}
                    <select 
                        value={newTask.assigneeEmail || ''} 
                        onChange={e => setNewTask({ ...newTask, assigneeEmail: e.target.value })} 
                        className="input-field"
                        disabled={!newTask.teamId} // Deaktiviert, wenn kein Team gewählt ist
                    >
                        <option value="">-- Person zuweisen --</option>
                        {selectedTeamMembers.map(member => (
                            <option key={member.id} value={member.email}>
                                {member.firstName || ''} {member.lastName || ''} ({member.email})
                            </option>
                        ))}
                    </select>
                    
                    {/* Beschreibung (optional) */}
                    <textarea value={newTask.description} onChange={e => setNewTask({ ...newTask, description: e.target.value })} placeholder="Kurzbeschreibung (optional)" rows={3} className="input-field md:col-span-2" />
                </div>
                <button onClick={handleCreate} disabled={isSubmitting} className="btn-primary w-full">
                    {isSubmitting ? 'Wird erstellt...' : 'Aufgabe erstellen'}
                </button>
            </section>
        )}

        <div className="grid grid-cols-1 gap-6 md:grid-cols-2 xl:grid-cols-3">
          {isLoading && <p className="col-span-full text-zinc-500">Lade Aufgaben...</p>}
          {!isLoading && tasks.length === 0 && <p className="col-span-full text-zinc-500">Keine Aufgaben gefunden.</p>}

          {tasks.map(task => (
            <article key={task.id} className="rounded-2xl border border-zinc-200 bg-white p-5 shadow-sm">
                {/* Task Anzeige bleibt gleich */}
                <div className="flex flex-col h-full">
                    <div className="flex-grow">
                        <div className="flex items-start justify-between gap-2 mb-2">
                            <h3 className="font-semibold text-zinc-800">{task.title}</h3>
                            <span className={`rounded-full px-2.5 py-0.5 text-xs font-medium whitespace-nowrap ${statusStyles[task.status || TaskStatus.OPEN]}`}>
                                {(task.status || 'OPEN').replace('_', ' ')}
                            </span>
                        </div>
                        <p className="text-sm text-zinc-600">{task.description || 'Keine Beschreibung.'}</p>
                        {task.dueDate && <p className="text-xs text-zinc-500 mt-3">Fällig: {new Date(task.dueDate).toLocaleDateString('de-DE')}</p>}
                    </div>
                     <div className="flex justify-end gap-2 pt-4 mt-auto">
                        <button className="btn-secondary" onClick={() => {}}>Bearbeiten</button>
                        <button className="btn-danger" onClick={() => {}}>Löschen</button>
                    </div>
                </div>
            </article>
          ))}
        </div>
      </main>
    </>
  );
}