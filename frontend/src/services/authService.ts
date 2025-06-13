// src/services/authService.ts

const API_BASE_URL = process.env.NEXT_PUBLIC_API_BASE_URL || 'http://localhost:8080';

/**
 * Daten, die für den Login benötigt werden.
 */
export interface UserLoginData {
  email: string;
  password: string;
}

/**
 * Mögliche Antwort vom Backend nach Login-Versuch.
 * Dein Backend liefert aktuell nur Text, z.B. "Login successful!" oder "Invalid credentials".
 * Wenn du später auf JSON umstellst, erweiterbar auf { message, token, userId, ... }
 */
export interface LoginResponse {
  success: boolean;
  message: string;
  token?: string;
  userId?: number;
}

/**
 * Profildaten des angemeldeten Nutzers.
 * Kann vom Backend per separatem GET-/Profile-Endpoint geliefert werden.
 */
export interface UserProfile {
  id: number;
  email: string;
  firstName?: string;
  lastName?: string;
  role?: string;
}

/**
 * Führt einen Login durch.
 * Sendet Form-Daten (x-www-form-urlencoded), da dein Backend @RequestParam erwartet.
 */
export async function loginUser(
  credentials: UserLoginData
): Promise<LoginResponse> {
  // Form-Daten aufbauen
  const form = new URLSearchParams();
  form.append('email', credentials.email);
  form.append('password', credentials.password);

  const response = await fetch(`${API_BASE_URL}/api/user/login`, {
    method: 'POST',
    headers: {
      'Content-Type': 'application/x-www-form-urlencoded',
    },
    body: form.toString(),
    credentials: 'include', // falls Cookies/Session
  });

  const text = await response.text();
  if (response.ok) {
    // Backend liefert aktuell nur Text
    return { success: true, message: text };
  } else {
    return { success: false, message: text };
  }
}

/**
 * Holt das Benutzerprofil (wenn dein Backend z.B. einen /api/user/profile Endpoint hat).
 */
export async function fetchUserProfile(
  email: string
): Promise<UserProfile> {
  const res = await fetch(
    `${API_BASE_URL}/api/user/profile?email=${encodeURIComponent(email)}`
  );
  if (!res.ok) {
    throw new Error(`Profil konnte nicht geladen werden: ${res.status}`);
  }
  return res.json();
}
