// src/services/teamService.ts

const API_BASE_URL = process.env.NEXT_PUBLIC_API_BASE_URL || 'http://localhost:8080';

// Interface für die Daten, die zum Erstellen eines Teams gesendet werden
export interface TeamCreationData {
    name: string;
    description?: string;
    memberEmails?: string[];
    creatorEmail: string; // E-Mail des Erstellers
}

// Interface für die Antwort beim Erstellen eines Teams
export interface CreateTeamResponse {
    message: string;
    teamId: number;
    error?: string;
}

// Interface für ein User-Objekt (vereinfacht)
export interface UserSummary {
    id: number;
    email: string;
    firstName?: string;
    lastName?: string;
}

// Interface für ein Team-Objekt (basierend auf deinem Backend-Modell)
export interface Team {
    id: number;
    name: string;
    description?: string;
    members?: UserSummary[];
    creator?: UserSummary;
    // Füge hier weitere Felder hinzu, die dein Backend-Team-Modell hat
}

/**
 * Ruft alle Teams im System ab.
 * Entspricht GET /api/teams/all
 */
export async function getAllTeams(): Promise<Team[]> {
    try {
        const response = await fetch(`${API_BASE_URL}/api/teams/all`, {
            method: 'GET',
            headers: {
                // Füge hier ggf. Authentifizierungs-Header hinzu
            },
        });

        if (!response.ok) {
            const errorText = await response.text();
            throw new Error(`HTTP-Fehler beim Abrufen aller Teams! Status: ${response.status}, Nachricht: ${errorText}`);
        }
        return await response.json() as Team[];
    } catch (error) {
        console.error('Fehler beim Abrufen aller Teams:', error);
        throw error;
    }
}

/**
 * Ruft ein bestimmtes Team anhand seiner ID ab.
 * Entspricht GET /api/teams?id={id}
 */
export async function getTeamById(teamId: number): Promise<Team> {
    try {
        const response = await fetch(`${API_BASE_URL}/api/teams?id=${teamId}`, {
            method: 'GET',
            headers: {
                // Füge hier ggf. Authentifizierungs-Header hinzu
            },
        });

        if (!response.ok) {
            if (response.status === 404) {
                throw new Error(`Team mit ID ${teamId} nicht gefunden.`);
            }
            const errorText = await response.text();
            throw new Error(`HTTP-Fehler beim Abrufen des Teams ID ${teamId}! Status: ${response.status}, Nachricht: ${errorText}`);
        }
        return await response.json() as Team;
    } catch (error) {
        console.error(`Fehler beim Abrufen des Teams mit ID ${teamId}:`, error);
        throw error;
    }
}

/**
 * Erstellt ein neues Team.
 * Entspricht POST /api/teams/create
 */
export async function createTeam(teamData: TeamCreationData): Promise<CreateTeamResponse> {
    try {
        const response = await fetch(`${API_BASE_URL}/api/teams/create`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
                // Füge hier ggf. Authentifizierungs-Header hinzu
            },
            body: JSON.stringify(teamData),
        });

        const responseBody = await response.json();

        if (!response.ok) {
            const errorMessage = typeof responseBody === 'string' ? responseBody : responseBody.error || responseBody.message || `HTTP-Fehler! Status: ${response.status}`;
            throw new Error(errorMessage);
        }
        return responseBody as CreateTeamResponse;
    } catch (error) {
        console.error('Fehler beim Erstellen des Teams:', error);
        throw error;
    }
}

// Du kannst hier noch getTeamsForUser hinzufügen, falls benötigt,
// basierend auf dem vorherigen Beispiel.
// export async function getTeamsForUser(userId: number): Promise<Team[]> { ... }