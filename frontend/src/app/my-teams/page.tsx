// src/app/my-teams/page.tsx
"use client";

import { useState, useEffect } from 'react';
import Link from 'next/link';
import { getTeamsForUser, Team } from '@/services/teamService';

export default function MyTeamsPage() {
  const [teams, setTeams] = useState<Team[]>([]);
  const [isLoading, setIsLoading] = useState(false);
  const [error, setError] = useState<string | null>(null);

  // TODO: Aus deiner Auth-Session ziehen
  const currentUserId = 123; // Beispiel-ID, hier später dynamisch setzen

  useEffect(() => {
    const fetchMyTeams = async () => {
      setIsLoading(true);
      setError(null);
      try {
        const list = await getTeamsForUser(currentUserId);
        setTeams(list);
      } catch (err: any) {
        setError(err.message || 'Fehler beim Laden deiner Teams.');
      } finally {
        setIsLoading(false);
      }
    };
    fetchMyTeams();
  }, [currentUserId]);

  return (
    <div className="max-w-3xl mx-auto p-6">
      <h1 className="text-3xl font-bold mb-6">Meine Teams</h1>

      {error && (
        <div className="mb-4 p-3 bg-red-100 text-red-700 rounded">
          {error}
        </div>
      )}

      {isLoading && <p>Lade deine Teams…</p>}

      {!isLoading && teams.length === 0 && <p>Du bist in noch keinem Team.</p>}

      {!isLoading && teams.length > 0 && (
        <ul className="space-y-4">
          {teams.map(team => (
            <li key={team.id} className="p-4 bg-white rounded-2xl shadow">
              <Link href={`/teams/${team.id}`} className="text-xl font-medium hover:underline">
                {team.name}
              </Link>
              {team.description && <p className="mt-1 text-gray-600">{team.description}</p>}
            </li>
          ))}
        </ul>
      )}
    </div>
  );
}
