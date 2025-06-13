// src/app/teams/[id]/page.tsx
"use client";

import { useState, useEffect } from "react";
import { useRouter, useParams } from "next/navigation";
import {
  getTeamById,
  addMembersToTeam,
  deleteTeam,
  Team
} from "@/services/teamService";
import Link from "next/link";

export default function TeamDetailPage() {
  const router = useRouter();
  const params = useParams();
  const teamId = Number(params.id);

  const [team, setTeam] = useState<Team | null>(null);
  const [emails, setEmails] = useState("");
  const [error, setError] = useState<string | null>(null);
  const [isLoading, setIsLoading] = useState(false);
  const [isDeleting, setIsDeleting] = useState(false);

  // Lade das Team beim Mounten bzw. wenn teamId sich ändert
  useEffect(() => {
    if (!teamId) return;
    (async () => {
      try {
        const t = await getTeamById(teamId);
        setTeam(t);
      } catch (e: any) {
        setError(e.message);
      }
    })();
  }, [teamId]);

  const handleAddMembers = async () => {
    setError(null);
    const list = emails
      .split(",")
      .map((e) => e.trim())
      .filter(Boolean);
    if (list.length === 0) {
      setError("Bitte mindestens eine E-Mail angeben.");
      return;
    }

    setIsLoading(true);
    try {
      await addMembersToTeam(teamId, list);
      const updated = await getTeamById(teamId);
      setTeam(updated);
      setEmails("");
    } catch (e: any) {
      setError(e.message);
    } finally {
      setIsLoading(false);
    }
  };

  const handleDeleteTeam = async () => {
    if (!confirm(`Team "${team?.name}" wirklich löschen?`)) return;
    setIsDeleting(true);
    setError(null);
    try {
      await deleteTeam(teamId);
      router.push("/teams");
    } catch (e: any) {
      setError(e.message);
      setIsDeleting(false);
    }
  };

  if (!team) {
    return (
      <div className="p-6">
        <p>Lade Team…</p>
        <Link href="/teams" className="text-blue-600 hover:underline">
          ← Zurück zu allen Teams
        </Link>
      </div>
    );
  }

  return (
    <div className="max-w-2xl mx-auto p-6 space-y-6">
      <div className="flex justify-between items-center">
        <Link href="/teams" className="text-blue-600 hover:underline">
          ← Zurück zu allen Teams
        </Link>
        <button
          onClick={handleDeleteTeam}
          disabled={isDeleting}
          className="px-3 py-1 bg-red-600 hover:bg-red-700 text-white rounded disabled:opacity-50"
        >
          {isDeleting ? "Lösche…" : "Team löschen"}
        </button>
      </div>

      {/* Team-Name in einem Input-Feld */}
      <div>
        <label htmlFor="team-name" className="sr-only">Teamname</label>
        <input
          id="team-name"
          type="text"
          value={team.name}
          readOnly
          className="w-full text-3xl font-bold mb-2 bg-transparent border-b border-gray-300 focus:outline-none focus:border-blue-500 text-white-900"
        />
      </div>

      {team.description && (
        <p className="text-gray-700">{team.description}</p>
      )}

      <section>
        <h2 className="text-2xl font-semibold mb-2">Mitglieder</h2>
        {team.members && team.members.length > 0 ? (
          <ul className="list-disc list-inside space-y-1">
            {team.members.map((u) => (
              <ul key={u.id}>{u.email}</ul>
            ))}
          </ul>
        ) : (
          <p className="text-gray-600">Noch keine Mitglieder.</p>
        )}
      </section>

      <section className="space-y-2">
        <h2 className="text-2xl font-semibold">Neue Mitglieder hinzufügen</h2>
        {error && <p className="text-red-600">{error}</p>}
        <input
          type="text"
          value={emails}
          onChange={(e) => setEmails(e.target.value)}
          placeholder="a@example.com, b@example.com"
          disabled={isLoading || isDeleting}
          className="w-full p-2 border rounded"
        />
        <button
          onClick={handleAddMembers}
          disabled={isLoading || isDeleting}
          className="px-4 py-2 bg-green-600 text-white rounded hover:bg-green-700 disabled:opacity-50"
        >
          {isLoading ? "Speichern…" : "Mitglieder hinzufügen"}
        </button>
      </section>
    </div>
  );
}
