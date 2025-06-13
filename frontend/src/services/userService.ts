// src/services/userService.ts

const API_BASE_URL = process.env.NEXT_PUBLIC_API_BASE_URL || 'http://localhost:8080';

// Interface für User-Registrierungsdaten
export interface UserRegistrationData {
    email: string;
    firstName?: string;
    lastName?: string;
    password: string; // Benenne es so, dass es nicht mit 'password' aus anderen Kontexten kollidiert
    passwordConfirm: string;
}

// Interface für User-Login-Daten
export interface UserLoginData {
    email: string;
    password_P: string;
}

// Interface für User-Profildaten (kann erweitert werden)
export interface UserProfile {
    id: number;
    email: string;
    firstName?: string;
    lastName?: string;
    role?: string;
    // teams?: TeamSummary[]; // Wenn du Team-Infos hier haben willst
}

// Interface für die Antwort bei erfolgreicher Registrierung (falls spezifisch)
// Dein Backend leitet weiter, also ist die direkte Antwort weniger relevant für den Client
// als der Redirect-Status.

/**
 * Registriert einen neuen Benutzer.
 * Entspricht POST /api/user/register
 */
export async function registerUser(userData: UserRegistrationData): Promise<{ success: boolean; message: string; status: number }> {
    try {
        const response = await fetch(`${API_BASE_URL}/api/user/register`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify({
                email: userData.email,
                firstName: userData.firstName,
                lastName: userData.lastName,
                password: userData.password, // Sicherstellen, dass Backend 'password' erwartet
                passwordConfirm: userData.passwordConfirm,
            }),
        });

        // Bei Redirect (Status 302) ist response.ok false.
        if (response.status === 302 || response.status === 201 /* Falls du CREATED zurückgibst */) {
            // Der Location Header ist bei Redirects interessant
            // const location = response.headers.get('Location');
            return { success: true, message: 'Registrierung erfolgreich. Weiterleitung zum Login...', status: response.status };
        }

        const responseBody = await response.json(); // Nur wenn kein Redirect und Body erwartet wird
        return { success: false, message: responseBody || 'Registrierung fehlgeschlagen', status: response.status };

    } catch (error: any) {
        console.error('Fehler bei der Benutzerregistrierung:', error);
        return { success: false, message: error.message || 'Netzwerkfehler oder Server nicht erreichbar.', status: 0 };
    }
}

/**
 * Loggt einen Benutzer ein.
 * Entspricht POST /api/user/login
 * ACHTUNG: Dein Backend erwartet RequestParams. Fetch mit POST sendet typischerweise einen Body.
 * Dieses Beispiel geht davon aus, dass du das Backend anpasst oder Form-Daten sendest.
 * Hier ein Beispiel mit Form-Daten:
 */
export interface UserLoginData {
    email: string;
    password_P: string;
}

export async function loginUser(credentials: UserLoginData): Promise<{ success: boolean; message: string; token?: string }> {
    const formData = new URLSearchParams();
    formData.append('email', credentials.email);
    formData.append('password', credentials.password_P);

    try {
        const response = await fetch(`${API_BASE_URL}/api/user/login`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/x-www-form-urlencoded',
            },
            body: formData.toString(),
            credentials: 'include', // falls du Cookies/Session brauchst
        });

        const text = await response.text();
        if (response.ok) {
            // z.B. { message: "...", userId: 1, token: "..." }
            const data = JSON.parse(text);
            return { success: true, message: data.message, token: data.token };
        } else {
            return { success: false, message: text || 'Login fehlgeschlagen' };
        }
    } catch (error: any) {
        console.error('Fehler beim Benutzer-Login:', error);
        return { success: false, message: error.message || 'Netzwerkfehler.' };
    }
}


/**
 * Ruft Benutzerdetails anhand der ID ab.
 * Entspricht GET /api/user?id={id}
 */
export async function getUserById(userId: number): Promise<UserProfile> {
    try {
        const response = await fetch(`${API_BASE_URL}/api/user?id=${userId}`, {
            method: 'GET',
            headers: {
                // Ggf. Authentifizierungs-Header (z.B. für Admin-Zugriff oder eigenes Profil)
            },
        });
        if (!response.ok) {
            if (response.status === 404) throw new Error(`Benutzer mit ID ${userId} nicht gefunden.`);
            const errorText = await response.text();
            throw new Error(`HTTP-Fehler! Status: ${response.status}, Nachricht: ${errorText}`);
        }
        return await response.json() as UserProfile;
    } catch (error) {
        console.error(`Fehler beim Abrufen des Benutzers ${userId}:`, error);
        throw error;
    }
}

/**
 * Aktualisiert Benutzerprofildaten.
 * Entspricht PUT /api/user/profile?email={email}
 */
export async function updateUserProfile(email: string, updatedInfo: Partial<UserProfile>): Promise<UserProfile> {
    try {
        // Entferne die ID aus updatedInfo, falls vorhanden, da sie nicht im Body sein sollte
        const { id, ...dataToSend } = updatedInfo;

        const response = await fetch(`${API_BASE_URL}/api/user/profile?email=${encodeURIComponent(email)}`, {
            method: 'PUT',
            headers: {
                'Content-Type': 'application/json',
                // Ggf. Authentifizierungs-Header
            },
            body: JSON.stringify(dataToSend),
        });
        if (!response.ok) {
            const errorBody = await response.json();
            throw new Error(errorBody.message || `HTTP-Fehler! Status: ${response.status}`);
        }
        return await response.json() as UserProfile;
    } catch (error) {
        console.error(`Fehler beim Aktualisieren des Profils für ${email}:`, error);
        throw error;
    }
}

/**
 * Fügt einen Benutzer zu einem Team hinzu.
 * Entspricht POST /api/user/teams/add
 */
export async function addUserToTeam(userId: number, teamId: number): Promise<{ success: boolean; message: string }> {
    try {
        const response = await fetch(`${API_BASE_URL}/api/user/teams/add?userId=${userId}&teamId=${teamId}`, {
            method: 'POST',
            headers: {
                // Ggf. Authentifizierungs-Header
            },
            // Kein Body, da Parameter in URL
        });
        const responseText = await response.text();
        if (response.ok) {
            return { success: true, message: responseText };
        } else {
            return { success: false, message: responseText || 'Fehler beim Hinzufügen zum Team.' };
        }
    } catch (error: any) {
        console.error(`Fehler beim Hinzufügen von Benutzer ${userId} zu Team ${teamId}:`, error);
        return { success: false, message: error.message || 'Netzwerkfehler.' };
    }
}

/**
 * Entfernt einen Benutzer aus einem Team.
 * Entspricht DELETE /api/user/teams/delete
 */
export async function removeUserFromTeam(userId: number, teamId: number): Promise<{ success: boolean; message: string }> {
    try {
        const response = await fetch(`${API_BASE_URL}/api/user/teams/delete?userId=${userId}&teamId=${teamId}`, {
            method: 'DELETE',
            headers: {
                // Ggf. Authentifizierungs-Header
            },
        });
        const responseText = await response.text();
        if (response.ok) {
            return { success: true, message: responseText };
        } else {
            return { success: false, message: responseText || 'Fehler beim Entfernen aus Team.' };
        }
    } catch (error: any) {
        console.error(`Fehler beim Entfernen von Benutzer ${userId} aus Team ${teamId}:`, error);
        return { success: false, message: error.message || 'Netzwerkfehler.' };
    }
}

// Weitere Funktionen für z.B. updateRole könnten hier hinzugefügt werden.
