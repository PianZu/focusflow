// src/app/teams/page.tsx
"use client";

import { useEffect, useState } from 'react';
import Link from 'next/link'; // Für die Navigation
import {
    getAllTeams,
    createTeam,
    Team,
    TeamCreationData,
    CreateTeamResponse
} from '@/services/teamService'; // Pfad anpassen, falls nötig

export default function TeamsPage() {
    const [teams, setTeams] = useState<Team[]>([]);
    const [newTeamName, setNewTeamName] = useState('');
    const [newTeamDescription, setNewTeamDescription] = useState('');
    const [isLoading, setIsLoading] = useState(false);
    const [error, setError] = useState<string | null>(null);

    // WICHTIG: Ersetze dies durch die E-Mail des tatsächlich eingeloggten Benutzers!
    const creatorEmailForNewTeam = "user@example.com"; // Platzhalter!

    useEffect(() => {
        async function fetchTeams() {
            setIsLoading(true);
            setError(null);
            try {
                const allTeams = await getAllTeams();
                setTeams(allTeams);
            } catch (err: any) {
                setError(err.message || 'Fehler beim Laden der Teams.');
            } finally {
                setIsLoading(false);
            }
        }
        fetchTeams();
    }, []);

    const handleCreateTeam = async () => {
        if (!newTeamName.trim()) {
            setError("Teamname darf nicht leer sein.");
            return;
        }
        if (!creatorEmailForNewTeam) {
            setError("Ersteller-E-Mail ist nicht verfügbar. Bitte sicherstellen, dass der Benutzer eingeloggt ist.");
            return;
        }

        setIsLoading(true);
        setError(null);
        const teamData: TeamCreationData = {
            name: newTeamName,
            description: newTeamDescription,
            creatorEmail: creatorEmailForNewTeam,
        };

        try {
            const result: CreateTeamResponse = await createTeam(teamData);
            console.log('Team erstellt:', result);
            setNewTeamName('');
            setNewTeamDescription('');
            // Teams neu laden, um die Liste zu aktualisieren
            const allTeams = await getAllTeams();
            setTeams(allTeams);
            alert(`Team "${result.message}" erfolgreich erstellt mit ID: ${result.teamId}`);
        } catch (err: any) {
            setError(err.message || 'Fehler beim Erstellen des Teams.');
        } finally {
            setIsLoading(false);
        }
    };

    return (
        <div>
            <h1>Teams</h1>

            {error && <p style={{ color: 'red' }}>{error}</p>}

            <div style={{ margin: '20px 0', padding: '20px', border: '1px solid #ccc' }}>
                <h2>Neues Team erstellen</h2>
                <input
                    type="text"
                    value={newTeamName}
                    onChange={(e) => setNewTeamName(e.target.value)}
                    placeholder="Teamname"
                    disabled={isLoading}
                    style={{ marginRight: '10px', marginBottom: '10px', display: 'block' }}
                />
                <input
                    type="text"
                    value={newTeamDescription}
                    onChange={(e) => setNewTeamDescription(e.target.value)}
                    placeholder="Beschreibung (optional)"
                    disabled={isLoading}
                    style={{ marginRight: '10px', marginBottom: '10px', display: 'block' }}
                />
                <button onClick={handleCreateTeam} disabled={isLoading}>
                    {isLoading ? 'Speichern...' : 'Team erstellen'}
                </button>
            </div>

            {isLoading && teams.length === 0 && <p>Lade Teams...</p>}

            <h2>Alle Teams</h2>
            {teams.length === 0 && !isLoading ? (
                <p>Noch keine Teams vorhanden.</p>
            ) : (
                <ul>
                    {teams.map(team => (
                        <li key={team.id}>
                            <Link href={`/teams/${team.id}`}><strong>{team.name}</strong></Link>
                            {team.description && <p>{team.description}</p>}
                        </li>
                    ))}
                </ul>
            )}
        </div>
    );
}