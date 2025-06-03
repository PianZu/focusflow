// src/services/teamService.ts (Beispiel)

const API_BASE_URL = process.env.NEXT_PUBLIC_API_BASE_URL || 'http://localhost:8080';

// Interface für die Daten, die zum Erstellen eines Teams gesendet werden
interface TeamCreationData {
    name: string;
    description?: string;
    memberEmails?: string[];
    creatorEmail: string; // E-Mail des Erstellers
}

// Interface für die Antwort beim Erstellen eines Teams
interface CreateTeamResponse {
    message: string;
    teamId: number;
    error?: string; // Falls ein Fehler aufgetreten ist
}

// Interface für ein Team-Objekt (basierend auf deinem Backend-Modell)
// Du müsstest dieses Interface an dein `Team`-Modell im Backend anpassen
interface Team {
    id: number;
    name: string;
    description?: string;
    // members?: UserSummary[]; // Falls Mitgliederinformationen enthalten sind
    // creator?: UserSummary;
    // ... weitere Felder deines Team-Modells
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
            console.error('Fehler beim Abrufen aller Teams:', errorText);
            throw new Error(`HTTP-Fehler! Status: ${response.status}, Nachricht: ${errorText}`);
        }
        return await response.json() as Team[];
    } catch (error) {
        console.error('Netzwerk- oder Parser-Fehler beim Abrufen aller Teams:', error);
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
            console.error(`Fehler beim Abrufen des Teams mit ID ${teamId}:`, errorText);
            throw new Error(`HTTP-Fehler! Status: ${response.status}, Nachricht: ${errorText}`);
        }
        return await response.json() as Team;
    } catch (error) {
        console.error(`Netzwerk- oder Parser-Fehler beim Abrufen des Teams mit ID ${teamId}:`, error);
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
            // responseBody könnte eine Fehlermeldung vom Controller enthalten
            // (z.B. e.getMessage() bei IllegalArgumentException oder RuntimeException)
            console.error('Fehler beim Erstellen des Teams:', responseBody);
            const errorMessage = typeof responseBody === 'string' ? responseBody : responseBody.error || responseBody.message || `HTTP-Fehler! Status: ${response.status}`;
            throw new Error(errorMessage);
        }
        return responseBody as CreateTeamResponse;
    } catch (error) {
        console.error('Netzwerk- oder Parser-Fehler beim Erstellen des Teams:', error);
        throw error;
    }
}

/**
 * Ruft alle Teams für einen bestimmten Benutzer ab.
 * Entspricht GET /api/teams/user?userId={userId}
 */
export async function getTeamsForUser(userId: number): Promise<Team[]> {
    try {
        const response = await fetch(`${API_BASE_URL}/api/teams/user?userId=${userId}`, {
            method: 'GET',
            headers: {
                // Füge hier ggf. Authentifizierungs-Header hinzu
            },
        });

        if (!response.ok) {
            // Dein Backend könnte hier bei Fehlern (z.B. User nicht gefunden)
            // einen entsprechenden Statuscode und eine Nachricht senden.
            const errorText = await response.text();
            console.error('Fehler beim Abrufen der Teams für Benutzer:', errorText);
            throw new Error(`HTTP-Fehler! Status: ${response.status}, Nachricht: ${errorText}`);
        }
        return await response.json() as Team[];
    } catch (error) {
        console.error('Netzwerk- oder Parser-Fehler beim Abrufen der Teams für Benutzer:', error);
        throw error;
    }
}

