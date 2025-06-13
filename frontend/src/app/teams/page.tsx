// src/app/teams/page.tsx
"use client";

import { useEffect, useState } from 'react';
import Link from 'next/link';
import {
  getAllTeams,
  createTeam,
  Team,
  TeamCreationData,
  CreateTeamResponse
} from '@/services/teamService';

export default function TeamsPage() {
  const [teams, setTeams] = useState<Team[]>([]);
  const [newTeamName, setNewTeamName] = useState('');
  const [newTeamDescription, setNewTeamDescription] = useState('');
  const [isLoading, setIsLoading] = useState(false);
  const [error, setError] = useState<string | null>(null);

  const creatorEmailForNewTeam = "test@example.com";

  useEffect(() => {
    const fetchTeams = async () => {
      setIsLoading(true);
      setError(null);
      try {
        const all = await getAllTeams();
        setTeams(all);
      } catch (err: any) {
        setError(err.message || 'Fehler beim Laden der Teams.');
      } finally {
        setIsLoading(false);
      }
    };
    fetchTeams();
  }, []);

  const handleCreateTeam = async () => {
    if (!newTeamName.trim()) {
      setError("Teamname darf nicht leer sein.");
      return;
    }
    if (!creatorEmailForNewTeam) {
      setError("Ersteller-E-Mail fehlt. Bitte einloggen.");
      return;
    }

    setIsLoading(true);
    setError(null);
    const payload: TeamCreationData = {
      name: newTeamName,
      description: newTeamDescription || undefined,
      creatorEmail: creatorEmailForNewTeam,
    };

    try {
      const result: CreateTeamResponse = await createTeam(payload);
      setNewTeamName('');
      setNewTeamDescription('');
      const all = await getAllTeams();
      setTeams(all);
      alert(`✅ ${result.message} (ID: ${result.teamId})`);
    } catch (err: any) {
      setError(err.message || 'Fehler beim Erstellen des Teams.');
    } finally {
      setIsLoading(false);
    }
  };

  return (
    <div className="max-w-3xl mx-auto p-6">
      <h1 className="text-3xl font-bold mb-6 text-white-900">Teams</h1>

      {error && (
        <div className="mb-4 p-3 bg-red-100 text-red-700 rounded">
          {error}
        </div>
      )}

      <section className="mb-8 p-6 bg-white rounded-2xl shadow">
        <h2 className="text-2xl font-semibold mb-4 text-gray-900">Neues Team erstellen</h2>
        <div className="space-y-4">
          <input
            type="text"
            value={newTeamName}
            onChange={e => setNewTeamName(e.target.value)}
            placeholder="Teamname"
            disabled={isLoading}
            className="w-full p-2 border rounded text-gray-900 placeholder-gray-500"
          />
          <input
            type="text"
            value={newTeamDescription}
            onChange={e => setNewTeamDescription(e.target.value)}
            placeholder="Beschreibung (optional)"
            disabled={isLoading}
            className="w-full p-2 border rounded text-gray-900 placeholder-gray-500"
          />
          <button
            onClick={handleCreateTeam}
            disabled={isLoading}
            className="px-4 py-2 bg-blue-600 text-white rounded hover:bg-blue-700 disabled:opacity-50"
          >
            {isLoading ? 'Speichern…' : 'Team erstellen'}
          </button>
        </div>
      </section>

      <section>
        <h2 className="text-2xl font-semibold mb-4 text-white-900">Alle Teams</h2>
        {isLoading && teams.length === 0 ? (
          <p className="text-gray-900">Lade Teams…</p>
        ) : teams.length === 0 ? (
          <p className="text-gray-900">Keine Teams vorhanden.</p>
        ) : (
          <ul className="space-y-4">
            {teams.map(team => (
              <li
                key={team.id}
                className="p-4 bg-white rounded-2xl shadow hover:shadow-md transition"
              >
                <Link href={`/teams/${team.id}`} className="text-xl font-medium text-blue-800 hover:underline">
                  {team.name}
                </Link>
                {team.description && (
                  <p className="mt-1 text-gray-800">{team.description}</p>
                )}
              </li>
            ))}
          </ul>
        )}
      </section>
    </div>
  );
}
